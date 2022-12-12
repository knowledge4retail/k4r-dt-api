package org.knowledge4retail.api.product.converter;

import org.knowledge4retail.api.product.dto.ProductGtinDto;
import org.knowledge4retail.api.product.model.ProductGtin;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProductGtinConverter {

    ProductGtinConverter INSTANCE = Mappers.getMapper(ProductGtinConverter.class);

    ProductGtin dtoToProductGtin(ProductGtinDto productGtinDto);
    ProductGtinDto productGtinToDto(ProductGtin productGtin);
    List<ProductGtinDto> productGtinsToDtos(List<ProductGtin> productGtins);
}
