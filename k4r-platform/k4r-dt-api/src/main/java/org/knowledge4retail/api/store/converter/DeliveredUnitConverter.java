package org.knowledge4retail.api.store.converter;

import org.knowledge4retail.api.store.dto.DeliveredUnitDto;
import org.knowledge4retail.api.store.model.DeliveredUnit;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DeliveredUnitConverter {

    DeliveredUnitConverter INSTANCE = Mappers.getMapper(DeliveredUnitConverter.class);

    DeliveredUnit dtoToDeliveredUnit(DeliveredUnitDto deliveredUnitDto);
    DeliveredUnitDto deliveredUnitToDto(DeliveredUnit deliveredUnit);
    List<DeliveredUnitDto> deliveredUnitsToDtos(List<DeliveredUnit> deliveredUnits);
}
