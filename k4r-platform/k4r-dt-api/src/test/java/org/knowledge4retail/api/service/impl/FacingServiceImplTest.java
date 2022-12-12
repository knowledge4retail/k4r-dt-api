package org.knowledge4retail.api.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.knowledge4retail.api.shared.producer.kafka.DefaultProducer;
import org.knowledge4retail.api.store.dto.FacingDto;
import org.knowledge4retail.api.store.model.Facing;
import org.knowledge4retail.api.store.repository.FacingRepository;
import org.knowledge4retail.api.store.service.impl.FacingServiceImpl;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class FacingServiceImplTest {

    @Mock
    FacingRepository facingRepository;
    @Mock
    DefaultProducer producer;

    FacingServiceImpl service;
    @Mock
    Facing facing;


    @BeforeEach
    public void setUp() {
        service = new FacingServiceImpl(facingRepository, producer);
    }

    @Test
    public void createSendsMessageToKafka() {

        service.create(new FacingDto());

        verify(producer).publishCreate(any(), any());
    }

    @Test
    public void updateSendsMessageToKafka() {

        when(facingRepository.getReferenceById(any())).thenReturn(facing);

        service.update(1, new FacingDto());

        verify(producer).publishUpdate(any(), any(), any());
    }

    @Test
    public void deleteSendsMessageToKafka() {

        when(facingRepository.getReferenceById(any())).thenReturn(facing);

        service.delete(1);

        verify(producer).publishDelete(any(), any());
    }
}
