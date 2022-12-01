package net.hackathon.smartapple.service.impl;

import java.time.Instant;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import net.hackathon.smartapple.domain.Loan;
import net.hackathon.smartapple.repository.LoanRepository;
import net.hackathon.smartapple.service.LoanService;
import net.hackathon.smartapple.service.dto.LoanDTO;
import net.hackathon.smartapple.service.mapper.LoanMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Loan}.
 */
@Service
@Transactional
public class LoanServiceImpl implements LoanService {

    private final Logger log = LoggerFactory.getLogger(LoanServiceImpl.class);

    private final LoanRepository loanRepository;

    private final LoanMapper loanMapper;

    public LoanServiceImpl(LoanRepository loanRepository, LoanMapper loanMapper) {
        this.loanRepository = loanRepository;
        this.loanMapper = loanMapper;
    }

    @Override
    public LoanDTO save(LoanDTO loanDTO) {
        log.debug("Request to save Loan : {}", loanDTO);
        Loan loan = loanMapper.toEntity(loanDTO);
        if (loan.getCreateAt() == null) {
            loan.setCreateAt(Instant.now());
        }
        loan.setUpdateAt(Instant.now());
        loan = loanRepository.save(loan);
        return loanMapper.toDto(loan);
    }

    @Override
    public LoanDTO update(LoanDTO loanDTO) {
        log.debug("Request to update Loan : {}", loanDTO);
        Loan loan = loanMapper.toEntity(loanDTO);
        loan.setUpdateAt(Instant.now());
        loan = loanRepository.save(loan);
        return loanMapper.toDto(loan);
    }

    @Override
    public Optional<LoanDTO> partialUpdate(LoanDTO loanDTO) {
        log.debug("Request to partially update Loan : {}", loanDTO);

        return loanRepository
            .findById(loanDTO.getId())
            .map(existingLoan -> {
                loanMapper.partialUpdate(existingLoan, loanDTO);

                return existingLoan;
            })
            .map(loanRepository::save)
            .map(loanMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LoanDTO> findAll() {
        log.debug("Request to get all Loans");
        return loanRepository.findAll().stream().map(loanMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<LoanDTO> findOne(Long id) {
        log.debug("Request to get Loan : {}", id);
        return loanRepository.findById(id).map(loanMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Loan : {}", id);
        loanRepository.deleteById(id);
    }
}
