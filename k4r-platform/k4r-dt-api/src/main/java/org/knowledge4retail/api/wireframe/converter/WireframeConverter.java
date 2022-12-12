package org.knowledge4retail.api.wireframe.converter;

import org.knowledge4retail.api.wireframe.dto.WireframeDto;
import org.knowledge4retail.api.wireframe.dto.WireframeMessageDto;
import org.knowledge4retail.api.wireframe.model.Wireframe;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface WireframeConverter {
    WireframeConverter INSTANCE = Mappers.getMapper(WireframeConverter.class);

    @Mapping(target = "blobUrl", ignore = true)
    Wireframe dtoToWireframe(WireframeDto dto);

    WireframeMessageDto WireframeToMessageDto(Wireframe Wireframe);
}
