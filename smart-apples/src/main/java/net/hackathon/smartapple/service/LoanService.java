package net.hackathon.smartapple.service;

import java.util.List;
import java.util.Optional;
import net.hackathon.smartapple.service.dto.LoanDTO;

/**
 * Service Interface for managing {@link net.hackathon.smartapple.domain.Loan}.
 */
public interface LoanService {
    /**
     * Save a loan.
     *
     * @param loanDTO the entity to save.
     * @return the persisted entity.
     */
    LoanDTO save(LoanDTO loanDTO);

    /**
     * Updates a loan.
     *
     * @param loanDTO the entity to update.
     * @return the persisted entity.
     */
    LoanDTO update(LoanDTO loanDTO);

    /**
     * Partially updates a loan.
     *
     * @param loanDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<LoanDTO> partialUpdate(LoanDTO loanDTO);

    /**
     * Get all the loans.
     *
     * @return the list of entities.
     */
    List<LoanDTO> findAll();

    /**
     * Get the "id" loan.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<LoanDTO> findOne(Long id);

    /**
     * Delete the "id" loan.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
