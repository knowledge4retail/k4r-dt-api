package org.knowledge4retail.api.product.service;

import org.knowledge4retail.api.product.dto.ImportProductDto;
import org.knowledge4retail.api.product.dto.ProductDto;

import java.util.List;

public interface ProductService {

    List<ProductDto> readAll();

    ProductDto read(String id);

    ProductDto update(ProductDto productDto);

    ProductDto create(ProductDto productDto);

    void delete(String id);

    List<ImportProductDto> createMany(List<ImportProductDto> productDtos);

    boolean exists(String id);

}
