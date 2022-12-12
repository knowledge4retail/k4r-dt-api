package org.knowledge4retail.api.store.service;

import org.knowledge4retail.api.store.dto.FacingDto;
import org.knowledge4retail.api.store.model.Facing;

import java.util.List;
import java.util.Map;

public interface FacingService {

    FacingDto create(FacingDto facing);
    List<FacingDto> readByShelfLayerId(int shelfLayerId);
    List<FacingDto> readByProductUnitId(Integer productUnitId);
    boolean exists(Integer facingId);
    FacingDto update(int facingId, FacingDto dto);
    FacingDto read(Integer facingId);
    int delete(int facingId);
    Map<Integer, List<Facing>> filterFacing(Map<Object, Object> shelfLayerContext);
}
