package org.knowledge4retail.api.product.converter;

import org.knowledge4retail.api.product.dto.ProductDescriptionDto;
import org.knowledge4retail.api.product.model.ProductDescription;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProductDescriptionConverter {

    ProductDescriptionConverter INSTANCE = Mappers.getMapper(ProductDescriptionConverter.class);

    ProductDescription dtoToProductDescription(ProductDescriptionDto productDescriptionDto);
    ProductDescriptionDto productDescriptionToDto(ProductDescription productDescription);
    List<ProductDescriptionDto> productDescriptionsToDtos(List<ProductDescription> productDescriptions);
}
