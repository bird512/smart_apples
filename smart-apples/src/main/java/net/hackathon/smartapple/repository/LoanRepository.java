package net.hackathon.smartapple.repository;

import java.util.List;
import net.hackathon.smartapple.domain.Loan;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Loan entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {
    @Query("select loan from Loan loan where loan.owner.login = ?#{principal.username}")
    List<Loan> findByOwnerIsCurrentUser();
}
