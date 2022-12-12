package org.knowledge4retail.api.store.service;

import org.knowledge4retail.api.store.dto.ItemDto;

import java.util.List;

public interface ItemService {

    List<ItemDto> readAll();
    ItemDto read(Integer id);
    ItemDto create(ItemDto itemDto);
    ItemDto update(Integer id, ItemDto itemDto);
    void delete(Integer id);
    boolean exists(Integer id);
}
