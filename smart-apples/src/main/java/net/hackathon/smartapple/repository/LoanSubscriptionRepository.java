package net.hackathon.smartapple.repository;

import java.util.List;
import net.hackathon.smartapple.domain.Loan;
import net.hackathon.smartapple.domain.LoanSubscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the LoanSubscription entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LoanSubscriptionRepository extends JpaRepository<LoanSubscription, Long> {
    @Query(
        "select loanSubscription from LoanSubscription loanSubscription where loanSubscription.subscriber.login = ?#{principal.username}"
    )
    List<LoanSubscription> findBySubscriberIsCurrentUser();

    List<LoanSubscription> findByLoan(Loan loan);
}
