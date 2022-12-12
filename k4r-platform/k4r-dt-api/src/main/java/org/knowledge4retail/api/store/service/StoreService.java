package org.knowledge4retail.api.store.service;

import org.knowledge4retail.api.store.dto.StoreAggregateDto;
import org.knowledge4retail.api.store.dto.StoreDto;

import java.util.List;

public interface StoreService  {

    StoreDto create(StoreDto dto);

    List<StoreDto> readAll();

    StoreDto read(Integer storeId);

    StoreDto readByExternalReferenceId(String externalReferenceId);

    StoreDto update(Integer storeId, StoreDto dto);

    void delete(Integer storeId);

    boolean exists(Integer storeId);

    boolean existsByExternalReferenceId(String externalReferenceId);

    List<StoreAggregateDto> readAllAggregates();
}
