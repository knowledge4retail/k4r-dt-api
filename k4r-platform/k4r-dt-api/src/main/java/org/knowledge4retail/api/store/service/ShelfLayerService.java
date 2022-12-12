package org.knowledge4retail.api.store.service;

import org.knowledge4retail.api.store.dto.ShelfLayerDto;
import org.knowledge4retail.api.store.model.ShelfLayer;

import java.util.List;
import java.util.Map;

public interface ShelfLayerService {

    ShelfLayerDto create(ShelfLayerDto dto);
    List<ShelfLayerDto> readByShelfId(Integer shelfId);
    List<ShelfLayerDto> readByShelfIdAndExternalReferenceId(Integer shelfId, String externalReferenceId);
    ShelfLayerDto readOneShelfLayerByShelfIdAndExternalReferenceId(Integer shelfId, String externalReferenceId);
    ShelfLayerDto readByShelfIdAndExternalReferenceIdAndLevel(Integer shelfId, String externalReferenceId, Integer level);
    List<ShelfLayerDto> readByShelfLayerIds(List<Integer> shelfLayerIds);
    ShelfLayerDto read(Integer shelfLayerId);
    void delete(Integer shelfLayerId);
    boolean exists(Integer shelfLayerId);
    boolean existsByExternalReferenceId(String externalReferenceId);
    boolean existsByExternalReferenceIdAndLevel(String externalReferenceId, Integer level);
    Map<Integer, List<ShelfLayer>> filterShelfLayer(Map<Object, Object> shelfContext);
    ShelfLayerDto update(ShelfLayerDto shelfLayerDto);
}
