package org.knowledge4retail.api.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.knowledge4retail.api.shared.producer.kafka.DefaultProducer;
import org.knowledge4retail.api.store.dto.ItemDto;
import org.knowledge4retail.api.store.model.Item;
import org.knowledge4retail.api.store.repository.ItemRepository;
import org.knowledge4retail.api.store.service.impl.ItemServiceImpl;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ItemServiceImplTest {

    private ItemServiceImpl service;
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private DefaultProducer producer;
    @Mock
    private Item item;

    @BeforeEach
    public void setup() {

        service = new ItemServiceImpl(itemRepository, producer);
    }

    @Test
    public void createSendsMessageToKafka(){

        service.create(new ItemDto());
        verify(producer).publishCreate(any(), any());
    }

    @Test
    public void updateSendsMessageToKafka() {

        when(itemRepository.getReferenceById(any())).thenReturn(any());
        service.update(1234, new ItemDto());
        verify(producer).publishUpdate(any(), any(), any());
    }

    @Test
    public void deleteSendsMessageToKafka(){

        when(itemRepository.getReferenceById(any())).thenReturn(item);
        service.delete(any());
        verify(producer).publishDelete(any(), any());
    }
}
