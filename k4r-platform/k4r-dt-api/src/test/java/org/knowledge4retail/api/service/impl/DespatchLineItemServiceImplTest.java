package org.knowledge4retail.api.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.knowledge4retail.api.product.service.ProductService;
import org.knowledge4retail.api.shared.producer.kafka.DefaultProducer;
import org.knowledge4retail.api.store.dto.DespatchLineItemDto;
import org.knowledge4retail.api.store.model.DespatchLineItem;
import org.knowledge4retail.api.store.repository.DespatchLineItemRepository;
import org.knowledge4retail.api.store.service.DespatchLogisticUnitService;
import org.knowledge4retail.api.store.service.impl.DespatchLineItemServiceImpl;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DespatchLineItemServiceImplTest {

    private DespatchLineItemServiceImpl service;
    @Mock
    private DespatchLineItemRepository despatchLineItemRepository;
    @Mock
    private DefaultProducer producer;
    @Mock
    private DespatchLineItem despatchLineItem;
    @Mock
    private DespatchLogisticUnitService despatchLogisticUnitService;
    @Mock
    private ProductService productService;

    @BeforeEach
    public void setup() {

        service = new DespatchLineItemServiceImpl(despatchLineItemRepository, producer, despatchLogisticUnitService, productService);
    }

    @Test
    public void createSendsMessageToKafka(){

        service.create(new DespatchLineItemDto());
        verify(producer).publishCreate(any(), any());
    }

    @Test
    public void updateSendsMessageToKafka() {

        when(despatchLineItemRepository.getReferenceById(any())).thenReturn(any());
        service.update(1234, new DespatchLineItemDto());
        verify(producer).publishUpdate(any(), any(), any());
    }

    @Test
    public void deleteSendsMessageToKafka(){

        when(despatchLineItemRepository.getReferenceById(any())).thenReturn(despatchLineItem);
        service.delete(any());
        verify(producer).publishDelete(any(), any());
    }
}
