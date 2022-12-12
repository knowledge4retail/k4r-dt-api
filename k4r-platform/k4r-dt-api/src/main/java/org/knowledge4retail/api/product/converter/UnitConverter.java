package org.knowledge4retail.api.product.converter;

import org.knowledge4retail.api.product.dto.UnitDto;
import org.knowledge4retail.api.product.model.Unit;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UnitConverter {

    UnitConverter INSTANCE = Mappers.getMapper(UnitConverter.class);

    Unit dtoToUnit(UnitDto unitDto);
    UnitDto unitToDto(Unit unit);
    List<UnitDto> unitsToDtos(List<Unit> units);
}
