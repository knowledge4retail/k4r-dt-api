package org.knowledge4retail.api.store.service;

import org.knowledge4retail.api.store.dto.DeliveredUnitDto;

import java.util.List;

public interface DeliveredUnitService {

    List<DeliveredUnitDto> readAll();
    DeliveredUnitDto read(Integer id);
    DeliveredUnitDto create(DeliveredUnitDto deliveredUnitDto);
    DeliveredUnitDto update(Integer id, DeliveredUnitDto deliveredUnitDto);
    void delete(Integer id);
    boolean exists(Integer id);
}
