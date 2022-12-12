package org.knowledge4retail.api.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.knowledge4retail.api.product.dto.ProductPropertyDto;
import org.knowledge4retail.api.product.model.ProductProperty;
import org.knowledge4retail.api.product.repository.ProductPropertyRepository;
import org.knowledge4retail.api.product.service.impl.ProductPropertyServiceImpl;
import org.knowledge4retail.api.shared.producer.kafka.DefaultProducer;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductPropertyServiceImplTest {

    private ProductPropertyServiceImpl service;
    @Mock
    private ProductPropertyRepository productPropertyRepository;
    @Mock
    private DefaultProducer producer;
    @Mock
    private ProductProperty productProperty;


    @BeforeEach
    public void setUp() {

        service = new ProductPropertyServiceImpl(productPropertyRepository, producer);
    }

    @Test
    public void createSendsMessageToKafka() {

        service.create(new ProductPropertyDto());
        verify(producer).publishCreate(any(), any());
    }

    @Test
    public void updateSendsMessageToKafka() {

        ProductProperty oldDto = mock(ProductProperty.class);
        when(oldDto.getId()).thenReturn(1000);

        when(productPropertyRepository.getOneByProductIdAndCharacteristicIdAndStoreId(any(), any(), any())).thenReturn(oldDto);
        service.update(1000, "TST-1000", 1000, new ProductPropertyDto());
        verify(producer).publishUpdate(any(), any(), any());
    }

    @Test
    public void deleteSendsMessageToKafka() {

        when(productPropertyRepository.getProperty(anyInt(), any(), any())).thenReturn(Optional.of(productProperty));
        service.delete(anyInt(),any(),any());
        verify(producer).publishDelete(any(), any());
    }
}
