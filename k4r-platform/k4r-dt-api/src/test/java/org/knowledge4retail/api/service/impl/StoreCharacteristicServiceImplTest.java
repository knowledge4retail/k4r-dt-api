package org.knowledge4retail.api.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.knowledge4retail.api.shared.producer.kafka.DefaultProducer;
import org.knowledge4retail.api.store.dto.StoreCharacteristicDto;
import org.knowledge4retail.api.store.model.StoreCharacteristic;
import org.knowledge4retail.api.store.repository.StoreCharacteristicRepository;
import org.knowledge4retail.api.store.service.impl.StoreCharacteristicServiceImpl;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class StoreCharacteristicServiceImplTest {

    private StoreCharacteristicServiceImpl service;
    @Mock
    private StoreCharacteristicRepository storeCharacteristicRepository;
    @Mock
    private DefaultProducer producer;
    @Mock
    private StoreCharacteristic storeCharacteristic;


    @BeforeEach
    public void setUp() {

        service = new StoreCharacteristicServiceImpl(storeCharacteristicRepository, producer);
    }

    @Test
    public void createSendsMessageToKafka() {

        service.create(new StoreCharacteristicDto());
        verify(producer).publishCreate(any(), any());
    }

    @Test
    public void deleteSendsMessageToKafka() {

        when(storeCharacteristicRepository.getReferenceById(any())).thenReturn(storeCharacteristic);
        service.delete(any());
        verify(producer).publishDelete(any(), any());
    }
}