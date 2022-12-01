package net.hackathon.smartapple.repository;

import java.util.List;
import java.util.Optional;
import net.hackathon.smartapple.domain.Address;
import net.hackathon.smartapple.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Address entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
    Optional<Address> findOneByAddressIgnoreCase(String address);
}
