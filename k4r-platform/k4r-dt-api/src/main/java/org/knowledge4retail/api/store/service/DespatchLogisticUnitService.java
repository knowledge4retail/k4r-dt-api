package org.knowledge4retail.api.store.service;

import org.knowledge4retail.api.store.dto.DespatchLogisticUnitDto;

import java.util.List;

public interface DespatchLogisticUnitService {

    List<DespatchLogisticUnitDto> readByDespatchAdviceId(Integer despatchAdviceId);
    DespatchLogisticUnitDto read(Integer id);
    DespatchLogisticUnitDto create(DespatchLogisticUnitDto despatchLogisticUnitDto);
    DespatchLogisticUnitDto update(Integer id, DespatchLogisticUnitDto despatchLogisticUnitDto);
    void delete(Integer id);
    boolean exists(Integer id);
}
