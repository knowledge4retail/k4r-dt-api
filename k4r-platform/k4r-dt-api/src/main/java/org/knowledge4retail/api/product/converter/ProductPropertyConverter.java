package org.knowledge4retail.api.product.converter;

import org.knowledge4retail.api.product.dto.ProductPropertyDto;
import org.knowledge4retail.api.product.model.ProductProperty;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProductPropertyConverter {

    ProductPropertyConverter INSTANCE = Mappers.getMapper(ProductPropertyConverter.class);

    ProductProperty dtoToProperty(ProductPropertyDto property);
    ProductPropertyDto propertyToDto(ProductProperty productProperty);
    List<ProductProperty> dtosToProperties(List<ProductPropertyDto> productPropertyDtos);
    List<ProductPropertyDto> propertiesToDtos(List<ProductProperty>properties);
}
