package org.knowledge4retail.api.store.service;

import org.knowledge4retail.api.store.dto.DespatchLineItemDto;

import java.util.List;

public interface DespatchLineItemService {

    List<DespatchLineItemDto> readByDespatchLogisticUnitId(Integer despatchLogisticUnitId);
    DespatchLineItemDto read(Integer id);
    DespatchLineItemDto create(DespatchLineItemDto despatchLineItemDto);
    DespatchLineItemDto update(Integer id, DespatchLineItemDto despatchLineItemDto);
    void delete(Integer id);
    boolean exists(Integer id);
}
