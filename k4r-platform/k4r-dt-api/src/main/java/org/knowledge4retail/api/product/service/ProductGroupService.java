package org.knowledge4retail.api.product.service;

import org.knowledge4retail.api.product.dto.ProductGroupDto;

import java.util.List;

public interface ProductGroupService {

    ProductGroupDto create(ProductGroupDto dto);
    List<ProductGroupDto> readByStoreId(Integer storeId);
    ProductGroupDto read(Integer productGroupId);
    void delete(Integer productGroupId);
    ProductGroupDto updateAddProductToGroup(Integer productGroupId, String productId);
    ProductGroupDto updateRemoveProductFromGroup(Integer productGroupId, String productId);
    boolean exists(Integer shelfId);
}
