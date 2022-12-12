package org.knowledge4retail.api.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.knowledge4retail.api.shared.producer.kafka.DefaultProducer;
import org.knowledge4retail.api.store.dto.StoreObjectDto;
import org.knowledge4retail.api.store.model.StoreObject;
import org.knowledge4retail.api.store.repository.StoreObjectRepository;
import org.knowledge4retail.api.store.service.StoreService;
import org.knowledge4retail.api.store.service.impl.StoreObjectServiceImpl;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class StoreObjectServiceImplTest {

    @Mock
    private StoreObjectRepository repository;

    @Mock
    private DefaultProducer producer;
    @Mock
    private StoreService storeService;

    private StoreObjectServiceImpl service;
    @Mock
    StoreObject storeObject;


    @BeforeEach
    public void setUp() {

        service = new StoreObjectServiceImpl(repository, producer, storeService);
    }

    @Test
    public void createSendsMessageToKafka() {

        service.create(new StoreObjectDto());

        verify(producer).publishCreate(any(), any());
    }

    @Test
    public void deleteSendsMessageToKafka() {

        when(repository.getReferenceById(any())).thenReturn(storeObject);

        service.delete(any());

        verify(producer).publishDelete(any(), any());
    }
}
