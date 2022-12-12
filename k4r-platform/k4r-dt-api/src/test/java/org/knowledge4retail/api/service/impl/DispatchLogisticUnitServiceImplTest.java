package org.knowledge4retail.api.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.knowledge4retail.api.product.service.ProductService;
import org.knowledge4retail.api.shared.producer.kafka.DefaultProducer;
import org.knowledge4retail.api.store.dto.DespatchLogisticUnitDto;
import org.knowledge4retail.api.store.model.DespatchLogisticUnit;
import org.knowledge4retail.api.store.repository.DespatchLogisticUnitRepository;
import org.knowledge4retail.api.store.service.DespatchAdviceService;
import org.knowledge4retail.api.store.service.impl.DespatchLogisticUnitServiceImpl;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DispatchLogisticUnitServiceImplTest {

    private DespatchLogisticUnitServiceImpl service;
    @Mock
    private DespatchLogisticUnitRepository despatchLogisticUnitRepository;
    @Mock
    private DefaultProducer producer;
    @Mock
    private DespatchLogisticUnit despatchLogisticUnit;
    @Mock
    private DespatchAdviceService despatchAdviceService;
    @Mock
    private ProductService productService;

    @BeforeEach
    public void setup() {

        service = new DespatchLogisticUnitServiceImpl(despatchLogisticUnitRepository, producer, despatchAdviceService, productService);
    }

    @Test
    public void createSendsMessageToKafka(){

        service.create(new DespatchLogisticUnitDto());
        verify(producer).publishCreate(any(), any());
    }

    @Test
    public void updateSendsMessageToKafka() {

        when(despatchLogisticUnitRepository.getReferenceById(any())).thenReturn(any());
        service.update(1234, new DespatchLogisticUnitDto());
        verify(producer).publishUpdate(any(), any(), any());
    }

    @Test
    public void deleteSendsMessageToKafka(){

        when(despatchLogisticUnitRepository.getReferenceById(any())).thenReturn(despatchLogisticUnit);
        service.delete(any());
        verify(producer).publishDelete(any(), any());
    }
}
