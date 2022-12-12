package org.knowledge4retail.api.store.service;

import org.knowledge4retail.api.store.dto.TrolleyDto;

import java.util.List;

public interface TrolleyService {

    TrolleyDto create(TrolleyDto dto);
    List<TrolleyDto> readByStoreId(Integer storeId);
    void delete(Integer id);
    boolean exists(Integer storeId);
}
