package org.knowledge4retail.api.store.converter;

import org.knowledge4retail.api.store.dto.DespatchLogisticUnitDto;
import org.knowledge4retail.api.store.model.DespatchLogisticUnit;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DespatchLogisticUnitConverter {

    DespatchLogisticUnitConverter INSTANCE = Mappers.getMapper(DespatchLogisticUnitConverter.class);

    DespatchLogisticUnit dtoToDespatchLogisticUnit(DespatchLogisticUnitDto despatchLogisticUnitDto);
    DespatchLogisticUnitDto despatchLogisticUnitToDto(DespatchLogisticUnit despatchLogisticUnit);
    List<DespatchLogisticUnitDto> despatchLogisticUnitToDtos(List<DespatchLogisticUnit> despatchLogisticUnits);
}
