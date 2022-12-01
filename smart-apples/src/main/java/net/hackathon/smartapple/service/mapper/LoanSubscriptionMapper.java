package net.hackathon.smartapple.service.mapper;

import net.hackathon.smartapple.domain.Loan;
import net.hackathon.smartapple.domain.LoanSubscription;
import net.hackathon.smartapple.domain.User;
import net.hackathon.smartapple.service.dto.LoanDTO;
import net.hackathon.smartapple.service.dto.LoanSubscriptionDTO;
import net.hackathon.smartapple.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link LoanSubscription} and its DTO {@link LoanSubscriptionDTO}.
 */
@Mapper(componentModel = "spring")
public interface LoanSubscriptionMapper extends EntityMapper<LoanSubscriptionDTO, LoanSubscription> {
    @Mapping(target = "subscriber", source = "subscriber", qualifiedByName = "userId")
    @Mapping(target = "loan", source = "loan", qualifiedByName = "loanId")
    LoanSubscriptionDTO toDto(LoanSubscription s);

    @Named("userId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserDTO toDtoUserId(User user);

    @Named("loanId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    LoanDTO toDtoLoanId(Loan loan);
}
