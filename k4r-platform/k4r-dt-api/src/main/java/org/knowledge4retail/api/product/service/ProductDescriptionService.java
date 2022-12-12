package org.knowledge4retail.api.product.service;

import org.knowledge4retail.api.product.dto.ProductDescriptionDto;
import org.knowledge4retail.api.product.model.ProductDescription;
import org.knowledge4retail.api.store.model.Barcode;

import java.util.List;
import java.util.Map;

public interface ProductDescriptionService {

    List<ProductDescriptionDto> readAll();
    ProductDescriptionDto read(Integer id);
    ProductDescriptionDto create(ProductDescriptionDto productDescriptionDto);
    ProductDescriptionDto update(Integer id, ProductDescriptionDto productDescriptionDto);
    Map<String, List<ProductDescription>> filterProductDescription(Map<Object, Object> productDescriptionContext);
    void delete(Integer id);
    boolean exists(Integer id);
}
