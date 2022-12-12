package org.knowledge4retail.api.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.knowledge4retail.api.shared.producer.kafka.DefaultProducer;
import org.knowledge4retail.api.store.dto.DeliveredUnitDto;
import org.knowledge4retail.api.store.model.DeliveredUnit;
import org.knowledge4retail.api.store.repository.DeliveredUnitRepository;
import org.knowledge4retail.api.store.service.impl.DeliveredUnitServiceImpl;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DeliveredUnitServiceImplTest {

    private DeliveredUnitServiceImpl service;
    @Mock
    private DeliveredUnitRepository deliveredUnitRepository;
    @Mock
    private DefaultProducer producer;
    @Mock
    private DeliveredUnit deliveredUnit;

    @BeforeEach
    public void setup() {

        service = new DeliveredUnitServiceImpl(deliveredUnitRepository, producer);
    }

    @Test
    public void createSendsMessageToKafka(){

        service.create(new DeliveredUnitDto());
        verify(producer).publishCreate(any(), any());
    }

    @Test
    public void updateSendsMessageToKafka() {

        when(deliveredUnitRepository.getReferenceById(any())).thenReturn(any());
        service.update(1234, new DeliveredUnitDto());
        verify(producer).publishUpdate(any(), any(), any());
    }

    @Test
    public void deleteSendsMessageToKafka(){

        when(deliveredUnitRepository.getReferenceById(any())).thenReturn(deliveredUnit);
        service.delete(any());
        verify(producer).publishDelete(any(), any());
    }
}
