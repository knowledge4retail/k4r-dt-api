package org.knowledge4retail.api.store.service;

import org.knowledge4retail.api.store.dto.SalesDto;

import java.util.List;

public interface SalesService {

    List<SalesDto> readAll();
    List<SalesDto> read(Integer storeId, String gtin);
    SalesDto create(SalesDto salesDto);
    List<SalesDto> createMany(List<SalesDto> salesDtos);
    boolean exists(Integer storeId, String gtin);
}
