package org.knowledge4retail.api.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.knowledge4retail.api.product.dto.ProductUnitDto;
import org.knowledge4retail.api.product.model.ProductUnit;
import org.knowledge4retail.api.product.repository.ProductUnitRepository;
import org.knowledge4retail.api.product.service.ProductService;
import org.knowledge4retail.api.product.service.UnitService;
import org.knowledge4retail.api.product.service.impl.ProductUnitServiceImpl;
import org.knowledge4retail.api.shared.producer.kafka.DefaultProducer;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProductUnitServiceImplTest {

    private ProductUnitServiceImpl service;
    @Mock
    private ProductUnitRepository productUnitRepository;
    @Mock
    private DefaultProducer producer;
    @Mock
    private ProductUnit productUnit;
    @Mock
    private UnitService unitService;
    @Mock
    private ProductService productService;

    @BeforeEach
    public void setup() {

        service = new ProductUnitServiceImpl(productUnitRepository, producer, unitService, productService);
    }

    @Test
    public void createSendsMessageToKafka(){

        service.create(new ProductUnitDto());
        verify(producer).publishCreate(any(), any());
    }

    @Test
    public void updateSendsMessageToKafka() {

        when(productUnitRepository.getReferenceById(any())).thenReturn(any());
        service.update(1234, new ProductUnitDto());
        verify(producer).publishUpdate(any(), any(), any());
    }

    @Test
    public void  deleteSendsMessageToKafka(){

        when(productUnitRepository.getReferenceById(any())).thenReturn(productUnit);
        service.delete(any());
        verify(producer).publishDelete(any(), any());
    }
}
