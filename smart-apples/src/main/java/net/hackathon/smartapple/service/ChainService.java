package net.hackathon.smartapple.service;

import io.reactivex.schedulers.Schedulers;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import javax.annotation.Resource;
import net.hackathon.smartapple.domain.Loan;
import net.hackathon.smartapple.domain.LoanSubscription;
import net.hackathon.smartapple.domain.User;
import net.hackathon.smartapple.domain.enumeration.LoanStatus;
import net.hackathon.smartapple.repository.AddressRepository;
import net.hackathon.smartapple.repository.LoanRepository;
import net.hackathon.smartapple.repository.LoanSubscriptionRepository;
import net.hackathon.smartapple.repository.PaymentRepository;
import net.hackathon.smartapple.service.dto.LoanDTO;
import net.hackathon.smartapple.service.dto.LoanSubscriptionDTO;
import net.hackathon.smartapple.service.dto.PaymentDTO;
import net.hackathon.smartapple.service.dto.UserDTO;
import net.hackathon.smartapple.service.impl.LoanServiceImpl;
import net.hackathon.smartapple.service.mapper.LoanMapper;
import net.hackathon.smartapple.service.mapper.LoanSubscriptionMapper;
import net.hackathon.smartapple.service.mapper.PaymentMapperImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.EventValues;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.DefaultBlockParameterNumber;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.tx.Contract;

@Component
// @Slf4j
public class ChainService {

    private static final Logger log = LoggerFactory.getLogger(ChainService.class);

    @Value("${blockchain.contract-address}")
    String contractAddress;

    @Autowired
    LoanServiceImpl loanServiceImpl;

    @Resource
    private Web3j web3j;

    @Resource
    private LoanRepository loanRepository;

    @Resource
    private LoanService loanService;

    @Resource
    private LoanMapper loanMapper;

    @Resource
    AddressRepository addressRepository;

    @Resource
    LoanSubscriptionRepository loanSubscriptionRepository;

    @Resource
    LoanSubscriptionMapper loanSubscriptionMapper;

    @Resource
    LoanSubscriptionService loanSubscriptionService;

    @Resource
    PaymentService paymentService;

    @Resource
    PaymentRepository paymentRepository;

    @Resource
    PaymentMapperImpl paymentMapper;

    // default loaner adder if not found in db
    private final String issuerAddr = "0x1162A635B4fD4c28AFe9076DD4c1070CE055F887";
    // default lender adder if not found in db
    private final String lenderAddr = "0x623dDa19C26a2d4Ed5f03b5050bB23EbE47CB6fe";

    BigInteger lastRegisterBlockNum = new BigInteger("7681350");

    // BigInteger lastSubLoanBlockNum = new BigInteger("7681350");
    // BigInteger lastPaymentBlockNum = new BigInteger("7681350");
    public ChainService() {}

    // event NewLoan(uint indexed loanId,address indexed issuer, uint maxAmount,
    // uint rate, uint terms);
    Event newLoanEven = new Event(
        "NewLoan",
        Arrays.<TypeReference<?>>asList(
            new TypeReference<Uint256>(true) {},
            new TypeReference<Address>(true) {},
            new TypeReference<Uint256>(false) {},
            new TypeReference<Uint256>(false) {},
            new TypeReference<Uint256>(false) {}
        )
    );

    // event SubLoan(uint indexed loanId,address indexed lender, uint amount, uint
    // subIndex);
    Event subLoanEvent = new Event(
        "SubLoan",
        Arrays.<TypeReference<?>>asList(
            new TypeReference<Uint256>(true) {},
            new TypeReference<Address>(true) {},
            new TypeReference<Uint256>(false) {},
            new TypeReference<Uint256>(false) {}
        )
    );

    // event PaymentReceive(uint indexed loanId,address indexed to, uint interest,
    // uint principal ,uint payRound);
    Event paymentReceiveEvent = new Event(
        "PaymentReceive",
        Arrays.<TypeReference<?>>asList(
            new TypeReference<Uint256>(true) {},
            new TypeReference<Address>(true) {},
            new TypeReference<Uint256>(false) {},
            new TypeReference<Uint256>(false) {},
            new TypeReference<Uint256>(false) {}
        )
    );

    @Async
    public void scanRegister() {
        BigInteger last = getLastChainBlockNum(0);
        log.info("[OffScanTask]stakeEventScan enter. current block number is {}", last);

        EthFilter filter = new EthFilter(
            DefaultBlockParameter.valueOf(lastRegisterBlockNum),
            DefaultBlockParameterName.LATEST,
            contractAddress
        );

        String topicData = EventEncoder.encode(newLoanEven);
        filter.addSingleTopic(topicData);

        web3j
            .ethLogFlowable(filter)
            .observeOn(Schedulers.io())
            .retry()
            .doOnError(e -> {
                log.error("[newLoanEven] catched doOnError eventScan: ", e);
                TimeUnit.MINUTES.sleep(1);
                scanRegister();
            })
            .doOnComplete(this::scanRegister)
            .subscribe(
                this::consumeNewLoanEvent,
                ex -> {
                    log.error("[newLoanEven] catched error in eventScan: ", ex);
                    TimeUnit.MINUTES.sleep(1);
                    scanRegister();
                }
            );
        log.info("done");
    }

    private void consumeNewLoanEvent(Log eventLog) throws IOException {
        if (eventLog.isRemoved()) {
            return;
        }
        EventValues eventValues = Contract.staticExtractEventParameters(newLoanEven, eventLog);
        BigInteger loanId = (BigInteger) eventValues.getIndexedValues().get(0).getValue();
        String issuer = (String) eventValues.getIndexedValues().get(1).getValue();
        BigInteger rate = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
        BigInteger terms = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
        BigInteger amount = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
        EthBlock block = web3j.ethGetBlockByNumber(new DefaultBlockParameterNumber(eventLog.getBlockNumber()), false).send();
        BigInteger blockTime = block.getBlock().getTimestamp();
        if (!loanRepository.existsById(loanId.longValue())) {
            LoanDTO loan = new LoanDTO();
            loan.setId(loanId.longValue());
            loan.setInterestRate(rate.intValue());
            loan.setTerms(terms.intValue());
            if (amount.intValue() < 20000) {
                amount = BigInteger.valueOf(20000);
            }
            loan.setLoanAmt(amount.intValue());
            loan.setAvailableAmt(amount.intValue());

            loan.setStatus(LoanStatus.PENDING);
            loan.setHash(eventLog.getTransactionHash() + eventLog.getLogIndexRaw());
            Optional<net.hackathon.smartapple.domain.Address> addr = addressRepository.findOneByAddressIgnoreCase(issuer);
            if (!addr.isPresent()) {
                log.debug("failed to get User by address : {}, use default", issuer);
                issuer = issuerAddr;
                User usr = addressRepository.findOneByAddressIgnoreCase(issuer).orElseThrow().getUser();
                loan.setOwner(new UserDTO(usr));
            } else {
                User usr = addr.orElseThrow().getUser();
                loan.setOwner(new UserDTO(usr));
            }
            LoanDTO loan1 = loanService.save(loan);
            log.info("saved {}", loan1);
        }
    }

    private void consumeSubLoanEvent(Log eventLog) throws IOException {
        if (eventLog.isRemoved()) {
            return;
        }
        EventValues eventValues = Contract.staticExtractEventParameters(subLoanEvent, eventLog);
        BigInteger loanId = (BigInteger) eventValues.getIndexedValues().get(0).getValue();
        String lender = (String) eventValues.getIndexedValues().get(1).getValue();
        BigInteger amount = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
        BigInteger subIndex = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
        EthBlock block = web3j.ethGetBlockByNumber(new DefaultBlockParameterNumber(eventLog.getBlockNumber()), false).send();
        BigInteger blockTime = block.getBlock().getTimestamp();
        LoanSubscriptionDTO dto = new LoanSubscriptionDTO();
        dto.setHash(eventLog.getTransactionHash() + eventLog.getLogIndexRaw());
        LoanDTO loan = loanMapper.toDto(loanRepository.findById(loanId.longValue()).orElseThrow());
        loan.setAvailableAmt(loan.getLoanAmt() - amount.intValue());
        if (loan.getAvailableAmt() < 0) {
            loan.setAvailableAmt(0);
        }
        loan.setStatus(loan.getAvailableAmt() == 0 ? LoanStatus.ACTIVE : LoanStatus.PENDING);
        dto.setLoan(loan);
        dto.setSubAmt(amount.intValue());

        Optional<net.hackathon.smartapple.domain.Address> addr = addressRepository.findOneByAddressIgnoreCase(lender);
        if (!addr.isPresent()) {
            log.debug("failed to get User by address : {}, use default", lender);
            lender = lenderAddr;
            User usr = addressRepository.findOneByAddressIgnoreCase(lender).orElseThrow().getUser();
            dto.setSubscriber(new UserDTO(usr));
        } else {
            User usr = addr.orElseThrow().getUser();
            dto.setSubscriber(new UserDTO(usr));
        }

        try {
            loanService.update(loan);
            loanSubscriptionService.save(dto);
        } catch (org.springframework.dao.DataIntegrityViolationException ex) {
            log.warn("ignore dup exception for subsripiton: {}", ex.getMessage());
        }
    }

    private void consumePaymentReceiveEvent(Log eventLog) throws IOException {
        if (eventLog.isRemoved()) {
            return;
        }
        // event PaymentReceive(uint indexed loanId,address indexed to, uint interest,
        // uint principal ,uint payRound);
        EventValues eventValues = Contract.staticExtractEventParameters(paymentReceiveEvent, eventLog);
        BigInteger loanId = (BigInteger) eventValues.getIndexedValues().get(0).getValue();
        String lender = (String) eventValues.getIndexedValues().get(1).getValue();
        BigInteger interest = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
        BigInteger principal = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
        BigInteger payRound = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
        EthBlock block = web3j.ethGetBlockByNumber(new DefaultBlockParameterNumber(eventLog.getBlockNumber()), false).send();
        BigInteger blockTime = block.getBlock().getTimestamp();

        PaymentDTO dto = new PaymentDTO();
        dto.setPrincipal(principal.intValue());
        dto.setInterest(interest.intValue());
        dto.setHash(eventLog.getTransactionHash() + "+" + eventLog.getLogIndexRaw());
        Loan loan = loanRepository.findById(loanId.longValue()).orElseThrow();
        loan.setStatus(LoanStatus.CLOSED);
        List<LoanSubscription> subs = loanSubscriptionRepository.findByLoan(loan);

        dto.setSubscription(loanSubscriptionMapper.toDto(subs.get(0)));

        try {
            loanService.update(loanMapper.toDto(loan));
            paymentService.save(dto);
        } catch (org.springframework.dao.DataIntegrityViolationException ex) {
            log.warn("ignore dup exception for payment: {}", ex.getMessage());
        } catch (Exception e) {
            log.error("", e);
        }
    }

    @Async
    public void scanSubLoan() {
        EthFilter filter = new EthFilter(
            DefaultBlockParameter.valueOf(lastRegisterBlockNum),
            DefaultBlockParameterName.LATEST,
            contractAddress
        );

        String topicData = EventEncoder.encode(subLoanEvent);
        filter.addSingleTopic(topicData);

        web3j
            .ethLogFlowable(filter)
            .observeOn(Schedulers.io())
            .retry()
            .doOnError(e -> {
                log.error("[newLoanEven] catched doOnError eventScan: ", e);
                TimeUnit.MINUTES.sleep(1);
                scanSubLoan();
            })
            .doOnComplete(this::scanSubLoan)
            .subscribe(
                this::consumeSubLoanEvent,
                ex -> {
                    log.error("[newLoanEven] catched error in eventScan: ", ex);
                    TimeUnit.MINUTES.sleep(1);
                    scanSubLoan();
                }
            );
        log.info("done");
    }

    @Async
    public void scanPaymentReceive() {
        EthFilter filter = new EthFilter(
            DefaultBlockParameter.valueOf(lastRegisterBlockNum),
            DefaultBlockParameterName.LATEST,
            contractAddress
        );

        String topicData = EventEncoder.encode(paymentReceiveEvent);
        filter.addSingleTopic(topicData);

        web3j
            .ethLogFlowable(filter)
            .observeOn(Schedulers.io())
            .retry()
            .doOnError(e -> {
                log.error("[scanPaymentReceive] catched doOnError eventScan: ", e);
                TimeUnit.MINUTES.sleep(1);
                scanPaymentReceive();
            })
            .doOnComplete(this::scanPaymentReceive)
            .subscribe(
                this::consumePaymentReceiveEvent,
                ex -> {
                    log.error("[scanPaymentReceive] catched error in eventScan: ", ex);
                    TimeUnit.MINUTES.sleep(1);
                    scanPaymentReceive();
                }
            );
        log.info("done");
    }

    private BigInteger getLastChainBlockNum(int count) {
        if (count > 300) {
            log.error("ScanTask", "", "connect node error");
            return null;
        }
        count++;
        try {
            EthBlock.Block block = web3j.ethGetBlockByNumber(DefaultBlockParameterName.LATEST, false).send().getBlock();
            return block.getNumber();
        } catch (Exception e) {
            log.info("web3j connect error , try count: " + count);
            try {
                Thread.sleep(20 * 1000);
            } catch (Exception ignore) {
                // ignore it
            }
            return getLastChainBlockNum(count);
        }
    }
}
