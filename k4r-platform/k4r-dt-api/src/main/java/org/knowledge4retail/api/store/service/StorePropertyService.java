package org.knowledge4retail.api.store.service;

import org.knowledge4retail.api.store.dto.StorePropertyDto;

import java.util.List;

public interface StorePropertyService {

    List<StorePropertyDto> readByStoreId(Integer storeId);
    StorePropertyDto create(StorePropertyDto storePropertyDto);
    void delete(Integer storeId, Integer characteristicId);
    boolean exists(Integer storeId, Integer characteristicId);
}
