package org.knowledge4retail.api.store.converter;

import org.knowledge4retail.api.store.dto.PlanogramDto;
import org.knowledge4retail.api.store.dto.PlanogramMessageDto;
import org.knowledge4retail.api.store.model.Planogram;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PlanogramConverter {
    PlanogramConverter INSTANCE = Mappers.getMapper(PlanogramConverter.class);

    @Mapping(target = "blobUrl", ignore = true)
    Planogram dtoToPlanogram(PlanogramDto dto);

    PlanogramMessageDto planogramToMessageDto(Planogram planogram);
}
