package net.hackathon.smartapple.service.mapper;

import net.hackathon.smartapple.domain.Loan;
import net.hackathon.smartapple.domain.User;
import net.hackathon.smartapple.service.dto.LoanDTO;
import net.hackathon.smartapple.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Loan} and its DTO {@link LoanDTO}.
 */
@Mapper(componentModel = "spring")
public interface LoanMapper extends EntityMapper<LoanDTO, Loan> {
    @Mapping(target = "owner", source = "owner", qualifiedByName = "userId")
    LoanDTO toDto(Loan s);

    @Named("userId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserDTO toDtoUserId(User user);
}
