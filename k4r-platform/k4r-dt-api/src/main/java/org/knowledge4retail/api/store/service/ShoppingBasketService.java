package org.knowledge4retail.api.store.service;

import com.github.fge.jsonpatch.JsonPatch;
import org.knowledge4retail.api.store.dto.ShoppingBasketPositionDto;

import java.util.List;

public interface ShoppingBasketService {

    ShoppingBasketPositionDto create(ShoppingBasketPositionDto dto);
    List<ShoppingBasketPositionDto> readByStoreIdAndCustomerId(Integer storeId, Integer customerId);
    ShoppingBasketPositionDto read(Integer positionId);
    ShoppingBasketPositionDto update(Integer positionId, JsonPatch patch);
    void deleteByStoreIdAndCustomerId(Integer storeId, Integer customerId);
    void delete(Integer positionId);
    boolean exists(Integer positionId);
}
