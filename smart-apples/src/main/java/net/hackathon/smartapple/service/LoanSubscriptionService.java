package net.hackathon.smartapple.service;

import java.util.List;
import java.util.Optional;
import net.hackathon.smartapple.domain.LoanSubscription;
import net.hackathon.smartapple.service.dto.LoanSubscriptionDTO;

/**
 * Service Interface for managing {@link net.hackathon.smartapple.domain.LoanSubscription}.
 */
public interface LoanSubscriptionService {
    /**
     * Save a loanSubscription.
     *
     * @param loanSubscriptionDTO the entity to save.
     * @return the persisted entity.
     */
    LoanSubscriptionDTO save(LoanSubscriptionDTO loanSubscriptionDTO);

    /**
     * Updates a loanSubscription.
     *
     * @param loanSubscriptionDTO the entity to update.
     * @return the persisted entity.
     */
    LoanSubscriptionDTO update(LoanSubscriptionDTO loanSubscriptionDTO);

    /**
     * Partially updates a loanSubscription.
     *
     * @param loanSubscriptionDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<LoanSubscriptionDTO> partialUpdate(LoanSubscriptionDTO loanSubscriptionDTO);

    /**
     * Get all the loanSubscriptions.
     *
     * @return the list of entities.
     */
    List<LoanSubscriptionDTO> findAll();

    /**
     * Get the "id" loanSubscription.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<LoanSubscriptionDTO> findOne(Long id);

    /**
     * Delete the "id" loanSubscription.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    LoanSubscription findOneByLoanId(Long loanId);
}
