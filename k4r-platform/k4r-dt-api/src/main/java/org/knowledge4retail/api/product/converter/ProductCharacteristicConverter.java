package org.knowledge4retail.api.product.converter;

import org.knowledge4retail.api.product.dto.ProductCharacteristicDto;
import org.knowledge4retail.api.product.model.ProductCharacteristic;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProductCharacteristicConverter {

    ProductCharacteristicConverter INSTANCE = Mappers.getMapper(ProductCharacteristicConverter.class);

    ProductCharacteristic dtoToCharacteristic(ProductCharacteristicDto productCharacteristicDto);
    ProductCharacteristicDto characteristicToDto(ProductCharacteristic productCharacteristic);
    List<ProductCharacteristicDto> characteristicsToDtos(List<ProductCharacteristic> productCharacteristics);
}
