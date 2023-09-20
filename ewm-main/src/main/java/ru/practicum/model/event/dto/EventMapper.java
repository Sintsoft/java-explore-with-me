package ru.practicum.model.event.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import ru.practicum.model.category.Category;
import ru.practicum.model.category.dto.CategoryMapper;
import ru.practicum.model.event.Event;
import ru.practicum.model.user.User;
import ru.practicum.model.user.dto.UserMapper;

@Mapper(componentModel = "spring", uses = {UserMapper.class, CategoryMapper.class})
public interface EventMapper {



    @Mapping(target = "id", expression = "java(null)")
    @Mapping(source = "dto.location.lat", target = "lattitude")
    @Mapping(source = "dto.location.lon", target = "longtitude")
    @Mapping(source = "category", target = "category")
    @Mapping(source = "user", target = "initiator")
    @Mapping(source = "dto.paid", target = "paid", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "dto.requestModeration", target = "requestModeration", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "dto.participantLimit", target = "participantLimit", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Event fromDTO(EventPostRequestDTO dto, Category category, User user);

    @Mapping(source = "eventId", target = "id", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "dto.location.lat", target = "lattitude", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "dto.location.lon", target = "longtitude", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "category", target = "category", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "user", target = "initiator", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "dto.title", target = "title", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "dto.description", target = "description", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "dto.paid", target = "paid", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "dto.requestModeration", target = "requestModeration", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "dto.annotation", target = "annotation", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "dto.eventDate", target = "eventDate", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "dto.participantLimit", target = "participantLimit", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Event fromDTO(@MappingTarget Event event, Long eventId, EventPatchRequestDTO dto, Category category, User user);


    @Mapping(source = "event.lattitude", target = "location.lat")
    @Mapping(source = "event.longtitude", target = "location.lon")
    @Mapping(source = "confirmedRequests", target = "confirmedRequests")
    @Mapping(source = "views", target = "views")
    @Mapping(target = "likes", expression = "java(event.getLikers().size())")
    EventFullResponseDTO toFullDTO(Event event, Integer confirmedRequests, Integer views);

    @Mapping(source = "confirmedRequests", target = "confirmedRequests")
    @Mapping(source = "views", target = "views")
    @Mapping(target = "likes", expression = "java(event.getLikers().size())")
    EventShortResponseDTO toShortDTO(Event event, Integer confirmedRequests, Integer views);
}
