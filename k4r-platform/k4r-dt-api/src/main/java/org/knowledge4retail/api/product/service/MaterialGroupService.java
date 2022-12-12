package org.knowledge4retail.api.product.service;

import org.knowledge4retail.api.product.dto.MaterialGroupDto;

import java.util.List;

public interface MaterialGroupService {

    List<MaterialGroupDto> readAll();
    MaterialGroupDto read(Integer id);
    MaterialGroupDto create(MaterialGroupDto materialGroupDto);
    MaterialGroupDto update(Integer id, MaterialGroupDto materialGroupDto);
    void delete(Integer id);
    boolean exists(Integer id);
}
