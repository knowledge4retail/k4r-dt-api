package org.knowledge4retail.api.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.knowledge4retail.api.shared.producer.kafka.DefaultProducer;
import org.knowledge4retail.api.store.dto.ShelfLayerDto;
import org.knowledge4retail.api.store.repository.ShelfLayerRepository;
import org.knowledge4retail.api.store.service.impl.ShelfLayerServiceImpl;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class ShelfLayerServiceImplTest {

    @Mock
    private ShelfLayerRepository repository;
    @Mock
    private DefaultProducer producer;

    private ShelfLayerServiceImpl service;


    @BeforeEach
    public void setUp() {

        service = new ShelfLayerServiceImpl(repository, producer);
    }

    @Test
    public void createSendsMessageToKafka() {

        service.create(new ShelfLayerDto());

        verify(producer).publishCreate(any(), any());
    }

    @Test
    public void deleteSendsMessageToKafka() {
        
        service.delete(any());

        verify(producer).publishDelete(any(), any());
    }
}
