package org.knowledge4retail.api.product.service;

import org.knowledge4retail.api.product.dto.UnitDto;

import java.util.List;

public interface UnitService {

    List<UnitDto> readAll();
    UnitDto create(UnitDto unitDto);
    void delete(Integer id);
    boolean exists(Integer id);
}
