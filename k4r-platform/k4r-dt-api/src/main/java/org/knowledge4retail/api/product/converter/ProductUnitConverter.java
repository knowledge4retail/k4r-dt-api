package org.knowledge4retail.api.product.converter;

import org.knowledge4retail.api.product.dto.ProductUnitDto;
import org.knowledge4retail.api.product.model.ProductUnit;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProductUnitConverter {

    ProductUnitConverter INSTANCE = Mappers.getMapper(ProductUnitConverter.class);

    ProductUnit dtoToProductUnit(ProductUnitDto productUnitDto);
    ProductUnitDto productUnitToDto(ProductUnit productUnit);
    List<ProductUnitDto> productUnitToDtos(List<ProductUnit> productUnits);
}
