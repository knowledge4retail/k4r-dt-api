package org.knowledge4retail.api.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.knowledge4retail.api.shared.producer.kafka.DefaultProducer;
import org.knowledge4retail.api.store.dto.StorePropertyDto;
import org.knowledge4retail.api.store.model.StoreProperty;
import org.knowledge4retail.api.store.repository.StorePropertyRepository;
import org.knowledge4retail.api.store.service.impl.StorePropertyServiceImpl;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class StorePropertyServiceImplTest {

    private StorePropertyServiceImpl service;
    @Mock
    private StorePropertyRepository storePropertyRepository;
    @Mock
    private DefaultProducer producer;
    @Mock
    private StoreProperty storeProperty;


    @BeforeEach
    public void setUp() {

        service = new StorePropertyServiceImpl(storePropertyRepository, producer);
    }

    @Test
    public void createSendsMessageToKafka() {

        service.create(new StorePropertyDto());
        verify(producer).publishCreate(any(), any());
    }

    @Test
    public void deleteSendsMessageToKafka() {

        when(storePropertyRepository.getProperty(any(), any())).thenReturn(Optional.of(storeProperty));
        service.delete(any(),any());
        verify(producer).publishDelete(any(), any());
    }
}
