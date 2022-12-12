package org.knowledge4retail.api.store.service;

import org.knowledge4retail.api.store.dto.TrolleyRouteDto;

import java.util.List;

public interface TrolleyRouteService {

    TrolleyRouteDto create(TrolleyRouteDto dto);
    List<TrolleyRouteDto> readByTrolleyId(Integer trolleyId);
    void delete(Integer id);
    boolean exists(Integer storeId);
}
