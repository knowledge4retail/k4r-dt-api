package org.knowledge4retail.api.product.service;

import org.knowledge4retail.api.product.dto.ProductUnitDto;

import java.util.List;

public interface ProductUnitService {

    List<ProductUnitDto> readAll();
    ProductUnitDto read(Integer id);
    ProductUnitDto create(ProductUnitDto productUnitDto);
    ProductUnitDto update(Integer id, ProductUnitDto productUnitDto);
    void delete(Integer id);
    boolean exists(Integer id);
}
