package org.knowledge4retail.api.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.knowledge4retail.api.product.dto.ProductCharacteristicDto;
import org.knowledge4retail.api.product.model.ProductCharacteristic;
import org.knowledge4retail.api.product.repository.ProductCharacteristicRepository;
import org.knowledge4retail.api.product.service.impl.ProductCharacteristicServiceImpl;
import org.knowledge4retail.api.shared.producer.kafka.DefaultProducer;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProductCharacteristicServiceImplTest {

    private ProductCharacteristicServiceImpl service;
    @Mock
    private ProductCharacteristicRepository productCharacteristicRepository;
    @Mock
    private DefaultProducer producer;
    @Mock
    private ProductCharacteristic productCharacteristic;


    @BeforeEach
    public void setUp() {

        service = new ProductCharacteristicServiceImpl(productCharacteristicRepository, producer);
    }

    @Test
    public void createSendsMessageToKafka() {

        service.create(new ProductCharacteristicDto());
        verify(producer).publishCreate(any(), any());
    }

    @Test
    public void deleteSendsMessageToKafka() {

        when(productCharacteristicRepository.getReferenceById(any())).thenReturn(productCharacteristic);
        service.delete(any());
        verify(producer).publishDelete(any(), any());
    }
}