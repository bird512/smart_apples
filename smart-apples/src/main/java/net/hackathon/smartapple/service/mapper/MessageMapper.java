package net.hackathon.smartapple.service.mapper;

import net.hackathon.smartapple.domain.Message;
import net.hackathon.smartapple.service.dto.MessageDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Message} and its DTO {@link MessageDTO}.
 */
@Mapper(componentModel = "spring")
public interface MessageMapper extends EntityMapper<MessageDTO, Message> {}
