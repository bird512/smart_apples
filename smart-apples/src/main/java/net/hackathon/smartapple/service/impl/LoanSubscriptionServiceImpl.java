package net.hackathon.smartapple.service.impl;

import java.time.Instant;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import net.hackathon.smartapple.domain.LoanSubscription;
import net.hackathon.smartapple.repository.LoanRepository;
import net.hackathon.smartapple.repository.LoanSubscriptionRepository;
import net.hackathon.smartapple.service.LoanSubscriptionService;
import net.hackathon.smartapple.service.dto.LoanSubscriptionDTO;
import net.hackathon.smartapple.service.mapper.LoanSubscriptionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link LoanSubscription}.
 */
@Service
@Transactional
public class LoanSubscriptionServiceImpl implements LoanSubscriptionService {

    private final Logger log = LoggerFactory.getLogger(LoanSubscriptionServiceImpl.class);

    private final LoanSubscriptionRepository loanSubscriptionRepository;

    private final LoanRepository loanRepository;

    private final LoanSubscriptionMapper loanSubscriptionMapper;

    public LoanSubscriptionServiceImpl(
        LoanSubscriptionRepository loanSubscriptionRepository,
        LoanSubscriptionMapper loanSubscriptionMapper,
        LoanRepository loanRepository
    ) {
        this.loanSubscriptionRepository = loanSubscriptionRepository;
        this.loanSubscriptionMapper = loanSubscriptionMapper;
        this.loanRepository = loanRepository;
    }

    @Override
    public LoanSubscriptionDTO save(LoanSubscriptionDTO loanSubscriptionDTO) {
        log.debug("Request to save LoanSubscription : {}", loanSubscriptionDTO);
        LoanSubscription loanSubscription = loanSubscriptionMapper.toEntity(loanSubscriptionDTO);
        if (loanSubscription.getCreateAt() == null) {
            loanSubscription.setCreateAt(Instant.now());
        }
        loanSubscription.setUpdateAt(Instant.now());
        loanSubscription = loanSubscriptionRepository.save(loanSubscription);
        return loanSubscriptionMapper.toDto(loanSubscription);
    }

    @Override
    public LoanSubscriptionDTO update(LoanSubscriptionDTO loanSubscriptionDTO) {
        log.debug("Request to update LoanSubscription : {}", loanSubscriptionDTO);
        LoanSubscription loanSubscription = loanSubscriptionMapper.toEntity(loanSubscriptionDTO);
        loanSubscription.setUpdateAt(Instant.now());
        loanSubscription = loanSubscriptionRepository.save(loanSubscription);
        return loanSubscriptionMapper.toDto(loanSubscription);
    }

    @Override
    public Optional<LoanSubscriptionDTO> partialUpdate(LoanSubscriptionDTO loanSubscriptionDTO) {
        log.debug("Request to partially update LoanSubscription : {}", loanSubscriptionDTO);

        return loanSubscriptionRepository
            .findById(loanSubscriptionDTO.getId())
            .map(existingLoanSubscription -> {
                loanSubscriptionMapper.partialUpdate(existingLoanSubscription, loanSubscriptionDTO);

                return existingLoanSubscription;
            })
            .map(loanSubscriptionRepository::save)
            .map(loanSubscriptionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LoanSubscriptionDTO> findAll() {
        log.debug("Request to get all LoanSubscriptions");
        return loanSubscriptionRepository
            .findBySubscriberIsCurrentUser()
            .stream()
            .map(loanSubscriptionMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<LoanSubscriptionDTO> findOne(Long id) {
        log.debug("Request to get LoanSubscription : {}", id);
        return loanSubscriptionRepository.findById(id).map(loanSubscriptionMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete LoanSubscription : {}", id);
        loanSubscriptionRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public LoanSubscription findOneByLoanId(Long loanId) {
        List<LoanSubscription> subs = loanSubscriptionRepository.findByLoan(loanRepository.findById(loanId).orElseThrow());
        if (!subs.isEmpty()) {
            return subs.get(0);
        }
        return null;
    }
}
