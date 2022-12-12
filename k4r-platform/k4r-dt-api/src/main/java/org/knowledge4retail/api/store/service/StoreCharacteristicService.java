package org.knowledge4retail.api.store.service;

import org.knowledge4retail.api.store.dto.StoreCharacteristicDto;

import java.util.List;

public interface StoreCharacteristicService {

    List<StoreCharacteristicDto> readAll();
    StoreCharacteristicDto create(StoreCharacteristicDto storeCharacteristicDto);
    void delete(Integer id);
    boolean exists(Integer id);
}
