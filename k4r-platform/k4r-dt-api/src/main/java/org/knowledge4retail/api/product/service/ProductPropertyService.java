package org.knowledge4retail.api.product.service;

import org.knowledge4retail.api.product.dto.ProductPropertyDto;

import java.util.List;

public interface ProductPropertyService {

    List<ProductPropertyDto> readByStoreIdAndProductId(Integer storeId, String productId);
    List<ProductPropertyDto> readByStoreIdNullAndProductId(String productId);
    ProductPropertyDto create(ProductPropertyDto productPropertyDto);
    void delete(Integer storeId, String productId, Integer characteristicId);
    void deleteByStoreIdNullAndProductIdAndCharacteristicId(String productId, Integer characteristicId);
    boolean exists(Integer storeId, String productId, Integer characteristicId);
    boolean existsByStoreIdNullAndProductIdAndCharacteristicId(String productId, Integer characteristicId);
    ProductPropertyDto update(Integer storeId, String productId, Integer characteristicId, ProductPropertyDto productPropertyDto);
}
