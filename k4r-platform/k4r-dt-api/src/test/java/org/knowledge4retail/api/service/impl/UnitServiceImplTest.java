package org.knowledge4retail.api.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.knowledge4retail.api.product.dto.UnitDto;
import org.knowledge4retail.api.product.model.Unit;
import org.knowledge4retail.api.product.repository.UnitRepository;
import org.knowledge4retail.api.product.service.impl.UnitServiceImpl;
import org.knowledge4retail.api.shared.producer.kafka.DefaultProducer;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UnitServiceImplTest {

    private UnitServiceImpl service;
    @Mock
    private UnitRepository unitRepository;
    @Mock
    private DefaultProducer producer;
    @Mock
    private Unit unit;

    @BeforeEach
    public void setup() {

        service = new UnitServiceImpl(unitRepository, producer);
    }

    @Test
    public void createSendsMessageToKafka(){

        service.create(new UnitDto());
        verify(producer).publishCreate(any(), any());
    }

    @Test
    public void deleteSendsMessageToKafka(){

        when(unitRepository.getReferenceById(any())).thenReturn(unit);
        service.delete(any());
        verify(producer).publishDelete(any(), any());
    }
}
