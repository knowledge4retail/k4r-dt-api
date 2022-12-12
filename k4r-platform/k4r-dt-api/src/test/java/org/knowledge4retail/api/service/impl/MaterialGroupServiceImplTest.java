package org.knowledge4retail.api.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.knowledge4retail.api.product.dto.MaterialGroupDto;
import org.knowledge4retail.api.product.model.MaterialGroup;
import org.knowledge4retail.api.product.repository.MaterialGroupRepository;
import org.knowledge4retail.api.product.service.impl.MaterialGroupServiceImpl;
import org.knowledge4retail.api.shared.producer.kafka.DefaultProducer;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MaterialGroupServiceImplTest {

    private MaterialGroupServiceImpl service;
    @Mock
    private MaterialGroupRepository materialGroupRepository;
    @Mock
    private DefaultProducer producer;
    @Mock
    private MaterialGroup materialGroup;

    @BeforeEach
    public void setup() {

        service = new MaterialGroupServiceImpl(materialGroupRepository, producer);
    }

    @Test
    public void createSendsMessageToKafka(){

        service.create(new MaterialGroupDto());
        verify(producer).publishCreate(any(), any());
    }

    @Test
    public void updateSendsMessageToKafka() {

        when(materialGroupRepository.getReferenceById(any())).thenReturn(any());
        service.update(1234, new MaterialGroupDto());
        verify(producer).publishUpdate(any(), any(), any());
    }

    @Test
    public void deleteSendsMessageToKafka(){

        when(materialGroupRepository.getReferenceById(any())).thenReturn(materialGroup);
        service.delete(any());
        verify(producer).publishDelete(any(), any());
    }
}
