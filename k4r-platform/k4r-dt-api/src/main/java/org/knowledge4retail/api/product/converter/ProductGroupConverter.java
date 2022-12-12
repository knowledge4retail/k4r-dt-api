package org.knowledge4retail.api.product.converter;

import org.knowledge4retail.api.product.dto.ProductGroupDto;
import org.knowledge4retail.api.product.model.ProductGroup;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;


@Mapper(uses = { ProductConverter.class }, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProductGroupConverter {
    ProductGroupConverter INSTANCE = Mappers.getMapper(ProductGroupConverter.class);

    ProductGroupDto productGroupToDto(ProductGroup productGroup);
    ProductGroup dtoToProductGroup(ProductGroupDto dto);
    List<ProductGroupDto> productGroupsToDtos(List<ProductGroup> productGroups);
}
