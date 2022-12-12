package org.knowledge4retail.api.store.converter;

import org.knowledge4retail.api.store.dto.FacingDto;
import org.knowledge4retail.api.store.model.Facing;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface FacingConverter {
    FacingConverter INSTANCE = Mappers.getMapper(FacingConverter.class);


    Facing dtoToFacing(FacingDto dto);

    FacingDto facingToDto(Facing facing);

    List<FacingDto> facingsToDtos(List<Facing> facings);
}
