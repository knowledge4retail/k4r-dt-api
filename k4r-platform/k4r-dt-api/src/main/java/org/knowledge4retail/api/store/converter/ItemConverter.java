package org.knowledge4retail.api.store.converter;

import org.knowledge4retail.api.store.dto.ItemDto;
import org.knowledge4retail.api.store.model.Item;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ItemConverter {

    ItemConverter INSTANCE = Mappers.getMapper(ItemConverter.class);

    Item dtoToItem(ItemDto itemDto);
    ItemDto itemToDto(Item item);
    List<ItemDto> itemsToDtos(List<Item> items);
}
