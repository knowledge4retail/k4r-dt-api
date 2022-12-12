package org.knowledge4retail.api.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.knowledge4retail.api.product.dto.ProductDescriptionDto;
import org.knowledge4retail.api.product.model.ProductDescription;
import org.knowledge4retail.api.product.repository.ProductDescriptionRepository;
import org.knowledge4retail.api.product.service.impl.ProductDescriptionServiceImpl;
import org.knowledge4retail.api.shared.producer.kafka.DefaultProducer;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProductDescriptionServiceImplTest {

    private ProductDescriptionServiceImpl service;
    @Mock
    private ProductDescriptionRepository productDescriptionRepository;
    @Mock
    private DefaultProducer producer;
    @Mock
    private ProductDescription productDescription;

    @BeforeEach
    public void setup() {

        service = new ProductDescriptionServiceImpl(productDescriptionRepository, producer);
    }

    @Test
    public void createSendsMessageToKafka(){

        service.create(new ProductDescriptionDto());
        verify(producer).publishCreate(any(), any());
    }

    @Test
    public void updateSendsMessageToKafka() {

        when(productDescriptionRepository.getReferenceById(any())).thenReturn(any());
        service.update(1000, new ProductDescriptionDto());
        verify(producer).publishUpdate(any(), any(), any());
    }

    @Test
    public void deleteSendsMessageToKafka(){

        when(productDescriptionRepository.getReferenceById(any())).thenReturn(productDescription);
        service.delete(any());
        verify(producer).publishDelete(any(), any());
    }
}
