package org.knowledge4retail.api.store.service;

import org.knowledge4retail.api.store.dto.StoreObjectDto;

import java.util.List;

public interface StoreObjectService {

    StoreObjectDto create(StoreObjectDto dto);
    List<StoreObjectDto> readByStoreId(Integer storeId);
    List<StoreObjectDto> readByStoreIdAndType(Integer storeId, String type);
    void delete(Integer storeObjectId);
    boolean exists(Integer storeObjectId);
}
