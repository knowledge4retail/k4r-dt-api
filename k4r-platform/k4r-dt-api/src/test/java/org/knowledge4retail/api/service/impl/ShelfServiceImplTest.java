package org.knowledge4retail.api.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.knowledge4retail.api.shared.producer.kafka.DefaultProducer;
import org.knowledge4retail.api.store.dto.ShelfDto;
import org.knowledge4retail.api.store.model.Shelf;
import org.knowledge4retail.api.store.repository.ShelfLayerRepository;
import org.knowledge4retail.api.store.repository.ShelfRepository;
import org.knowledge4retail.api.store.service.impl.ShelfServiceImpl;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ShelfServiceImplTest {

    @Mock
    private ShelfRepository repository;
    @Mock
    private ShelfLayerRepository shelfLayerRepository;
    @Mock
    private DefaultProducer producer;

    private ShelfServiceImpl service;
    @Mock
    Shelf shelf;


    @BeforeEach
    public void setUp() {

        service = new ShelfServiceImpl(repository, shelfLayerRepository, producer);
    }

    @Test
    public void createSendsMessageToKafka() {

        service.create(new ShelfDto());

        verify(producer).publishCreate(any(), any());
    }

    @Test
    public void updateSendsMessageToKafka() {

        service.update(new ShelfDto());

        verify(producer).publishUpdate(any(), any(), any());
    }

    @Test
    public void deleteSendsMessageToKafka() {

        when(repository.getReferenceById(any())).thenReturn(shelf);

        service.delete(any());

        verify(producer).publishDelete(any(), any());
    }
}
