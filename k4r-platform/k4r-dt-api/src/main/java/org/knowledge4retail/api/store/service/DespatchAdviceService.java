package org.knowledge4retail.api.store.service;

import org.knowledge4retail.api.store.dto.DespatchAdviceDto;

import java.util.List;

public interface DespatchAdviceService {

    List<DespatchAdviceDto> readByStoreId(Integer storeId);
    DespatchAdviceDto read(Integer id);
    DespatchAdviceDto create(DespatchAdviceDto despatchAdviceDto);
    DespatchAdviceDto update(Integer id, DespatchAdviceDto despatchAdviceDto);
    void delete(Integer id);
    boolean exists(Integer id);
}
