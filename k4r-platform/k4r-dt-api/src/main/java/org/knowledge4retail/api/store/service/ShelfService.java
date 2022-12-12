package org.knowledge4retail.api.store.service;

import org.knowledge4retail.api.store.dto.ShelfDto;
import org.knowledge4retail.api.store.model.Shelf;

import java.util.List;
import java.util.Map;

public interface ShelfService {

    ShelfDto create(ShelfDto dto);
    List<ShelfDto> readByStoreId(Integer storeId);
    List<ShelfDto> readByStoreIdAndExternalReferenceId(Integer storeId, String externalReferenceId);
    List<ShelfDto> readByShelfIdsAndStoreId(List<Integer> shelfIds, Integer storeId);
    ShelfDto readByExternalReferenceId(String externalReferenceId);
    ShelfDto read(Integer shelfId);
    ShelfDto update(ShelfDto dto);
    void delete(Integer shelfId);
    boolean exists(Integer shelfId);
    boolean existsByExternalReferenceId(String externalReferenceId);
    Map<Integer, List<Shelf>> filterShelf(Map<Object, Object> shelfContext);
}
