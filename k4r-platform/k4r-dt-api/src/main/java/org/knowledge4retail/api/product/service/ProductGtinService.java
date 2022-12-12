package org.knowledge4retail.api.product.service;

import org.knowledge4retail.api.product.dto.ProductGtinDto;

import java.util.List;

public interface ProductGtinService {

    List<ProductGtinDto> readAll();
    ProductGtinDto read(Integer id);
    ProductGtinDto readByGtin(String gtin);
    ProductGtinDto readByExternalReferenceId(String externalReferenceId);
    ProductGtinDto create(ProductGtinDto productGtinDto);
    ProductGtinDto update(Integer id, ProductGtinDto productGtinDto);
    void delete(Integer id);
    boolean exists(Integer id);
    boolean existsByGtin(String gtin);
    boolean existsByExternalReferenceId(String externalReferenceId);
}
