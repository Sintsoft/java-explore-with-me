package ru.practicum.model.compilation.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import ru.practicum.model.compilation.Compilation;
import ru.practicum.model.event.Event;
import ru.practicum.model.event.dto.EventMapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = EventMapper.class)
public interface CompilationMapper {


    @Mapping(target = "events", source = "events")
    Compilation fromDTO(CompilationPostRequestDTO dto, List<Event> events);


    @Mapping(target = "pinned", source = "dto.pinned", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "title", source = "dto.title", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "events", source = "events", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Compilation fromDTO(@MappingTarget Compilation compilation, CompilationPatchRequestDTO dto, List<Event> events);

    CompilationResponseDTO toDTO(Compilation compilation);
}
