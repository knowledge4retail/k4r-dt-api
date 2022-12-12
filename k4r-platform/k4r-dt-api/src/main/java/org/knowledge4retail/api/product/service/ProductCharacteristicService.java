package org.knowledge4retail.api.product.service;

import org.knowledge4retail.api.product.dto.ProductCharacteristicDto;

import java.util.List;

public interface ProductCharacteristicService {

    List<ProductCharacteristicDto> readAll();
    ProductCharacteristicDto create(ProductCharacteristicDto productCharacteristicDto);
    void delete(Integer id);
    boolean exists(Integer id);
}
