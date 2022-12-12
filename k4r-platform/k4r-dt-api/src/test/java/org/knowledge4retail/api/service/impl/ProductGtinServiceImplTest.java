package org.knowledge4retail.api.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.knowledge4retail.api.product.dto.ProductGtinDto;
import org.knowledge4retail.api.product.model.ProductGtin;
import org.knowledge4retail.api.product.repository.ProductGtinRepository;
import org.knowledge4retail.api.product.service.impl.ProductGtinServiceImpl;
import org.knowledge4retail.api.shared.producer.kafka.DefaultProducer;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProductGtinServiceImplTest {

    private ProductGtinServiceImpl service;
    @Mock
    private ProductGtinRepository productGtinRepository;
    @Mock
    private DefaultProducer producer;
    @Mock
    private ProductGtin productGtin;

    @BeforeEach
    public void setup() {

        service = new ProductGtinServiceImpl(productGtinRepository, producer);
    }

    @Test
    public void createSendsMessageToKafka(){

        service.create(new ProductGtinDto());
        verify(producer).publishCreate(any(), any());
    }

    @Test
    public void updateSendsMessageToKafka() {

        when(productGtinRepository.getReferenceById(any())).thenReturn(any());
        service.update(1000, new ProductGtinDto());
        verify(producer).publishUpdate(any(), any(), any());
    }

    @Test
    public void deleteSendsMessageToKafka(){

        when(productGtinRepository.getReferenceById(any())).thenReturn(productGtin);
        service.delete(any());
        verify(producer).publishDelete(any(), any());
    }
}
