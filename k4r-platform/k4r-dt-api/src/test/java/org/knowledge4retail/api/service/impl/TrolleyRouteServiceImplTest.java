package org.knowledge4retail.api.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.knowledge4retail.api.shared.producer.kafka.DefaultProducer;
import org.knowledge4retail.api.store.dto.TrolleyRouteDto;
import org.knowledge4retail.api.store.model.TrolleyRoute;
import org.knowledge4retail.api.store.repository.TrolleyRouteRepository;
import org.knowledge4retail.api.store.service.TrolleyService;
import org.knowledge4retail.api.store.service.impl.TrolleyRouteServiceImpl;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TrolleyRouteServiceImplTest {

    private TrolleyRouteServiceImpl service;
    @Mock
    private TrolleyRouteRepository trolleyRouteRepository;
    @Mock
    private DefaultProducer producer;
    @Mock
    private TrolleyService trolleyService;
    @Mock
    private TrolleyRoute trolleyRoute;

    @BeforeEach
    public void setup() {

        service = new TrolleyRouteServiceImpl(trolleyRouteRepository, trolleyService, producer);
    }

    @Test
    public void createSendsMessageToKafka(){

        service.create(new TrolleyRouteDto());
        verify(producer).publishCreate(any(), any());
    }

    @Test
    public void deleteSendsMessageToKafka(){

        when(trolleyRouteRepository.getReferenceById(any())).thenReturn(trolleyRoute);
        service.delete(any());
        verify(producer).publishDelete(any(), any());
    }
}
