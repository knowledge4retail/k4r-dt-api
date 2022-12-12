package org.knowledge4retail.api.store.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.knowledge4retail.api.shared.producer.kafka.DefaultProducer;
import org.knowledge4retail.api.store.converter.ItemConverter;
import org.knowledge4retail.api.store.dto.ItemDto;
import org.knowledge4retail.api.store.model.Item;
import org.knowledge4retail.api.store.repository.ItemRepository;
import org.knowledge4retail.api.store.service.ItemService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class ItemServiceImpl implements ItemService {

    @Value("${org.knowledge4retail.api.listener.kafka.topics.item}")
    private String kafkaTopic;

    private final ItemRepository itemRepository;
    private final DefaultProducer producer;
    @Override
    public List<ItemDto> readAll() {

        return ItemConverter.INSTANCE.itemsToDtos(itemRepository.findAll());
    }

    @Override
    public ItemDto read(Integer id) {

        Item item = itemRepository.getReferenceById(id);
        return ItemConverter.INSTANCE.itemToDto(item);
    }

    @Override
    public ItemDto create(ItemDto itemDto) {

        itemDto.setId(null);
        Item itemToSave = ItemConverter.INSTANCE.dtoToItem(itemDto);
        Item savedItem = itemRepository.save(itemToSave);

        ItemDto createdDto = ItemConverter.INSTANCE.itemToDto(savedItem);
        producer.publishCreate(kafkaTopic, createdDto);
        return createdDto;
    }

    @Override
    public ItemDto update(Integer id, ItemDto itemDto) {

        ItemDto oldDto = read(id);
        itemDto.setId(id);

        Item item = ItemConverter.INSTANCE.dtoToItem(itemDto);
        ItemDto createdDto = ItemConverter.INSTANCE.itemToDto(itemRepository.save(item));

        producer.publishUpdate(kafkaTopic, createdDto, oldDto);
        return createdDto;
    }

    @Override
    public void delete(Integer id) {

        ItemDto deletedDto = ItemConverter.INSTANCE.itemToDto(itemRepository.getReferenceById(id));
        itemRepository.deleteById(id);
        producer.publishDelete(kafkaTopic, deletedDto);
    }

    @Override
    public boolean exists(Integer id) {

        return itemRepository.existsById(id);
    }

}
