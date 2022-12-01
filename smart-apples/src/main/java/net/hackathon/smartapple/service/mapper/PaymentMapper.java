package net.hackathon.smartapple.service.mapper;

import net.hackathon.smartapple.domain.LoanSubscription;
import net.hackathon.smartapple.domain.Payment;
import net.hackathon.smartapple.service.dto.LoanSubscriptionDTO;
import net.hackathon.smartapple.service.dto.PaymentDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Payment} and its DTO {@link PaymentDTO}.
 */
@Mapper(componentModel = "spring")
public interface PaymentMapper extends EntityMapper<PaymentDTO, Payment> {
    @Mapping(target = "subscription", source = "subscription", qualifiedByName = "loanSubscriptionId")
    PaymentDTO toDto(Payment s);

    @Named("loanSubscriptionId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    LoanSubscriptionDTO toDtoLoanSubscriptionId(LoanSubscription loanSubscription);
}
