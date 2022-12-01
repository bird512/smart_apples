package net.hackathon.smartapple.service.mapper;

import net.hackathon.smartapple.domain.Address;
import net.hackathon.smartapple.domain.User;
import net.hackathon.smartapple.service.dto.AddressDTO;
import net.hackathon.smartapple.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Address} and its DTO {@link AddressDTO}.
 */
@Mapper(componentModel = "spring")
public interface AddressMapper extends EntityMapper<AddressDTO, Address> {
    @Mapping(target = "user", source = "user", qualifiedByName = "userId")
    AddressDTO toDto(Address s);

    @Named("userId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserDTO toDtoUserId(User user);
}
