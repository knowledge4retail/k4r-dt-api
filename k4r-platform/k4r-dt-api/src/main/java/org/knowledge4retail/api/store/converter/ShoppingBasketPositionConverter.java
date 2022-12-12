package org.knowledge4retail.api.store.converter;

import org.knowledge4retail.api.store.dto.ShoppingBasketPositionDto;
import org.knowledge4retail.api.store.model.ShoppingBasketPosition;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ShoppingBasketPositionConverter {

    ShoppingBasketPositionConverter INSTANCE = Mappers.getMapper(ShoppingBasketPositionConverter.class);

    List<ShoppingBasketPositionDto> positionsToDtos(List<ShoppingBasketPosition> positions);
    ShoppingBasketPosition dtoToPosition(ShoppingBasketPositionDto dto);
    ShoppingBasketPositionDto positionToDto(ShoppingBasketPosition position);
}
