package org.knowledge4retail.api.product.converter;

import org.knowledge4retail.api.product.dto.ImportProductDto;
import org.knowledge4retail.api.product.dto.ProductDto;
import org.knowledge4retail.api.product.model.Product;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProductConverter {
    ProductConverter INSTANCE = Mappers.getMapper(ProductConverter.class);

    @IterableMapping(qualifiedByName = "productToDto")
    List<ProductDto> productsToDtos(List<Product> products);

    Product importDtoToProduct(ImportProductDto importProductDto);

    Product dtoToProduct(ProductDto productDto);

    ImportProductDto productToImportDto(Product product);

    @Named(value = "productToDto")
    ProductDto productToDto(Product product);
}
