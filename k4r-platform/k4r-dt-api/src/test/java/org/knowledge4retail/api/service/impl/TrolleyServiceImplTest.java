package org.knowledge4retail.api.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.knowledge4retail.api.shared.producer.kafka.DefaultProducer;
import org.knowledge4retail.api.store.dto.TrolleyDto;
import org.knowledge4retail.api.store.model.Trolley;
import org.knowledge4retail.api.store.repository.TrolleyRepository;
import org.knowledge4retail.api.store.service.StoreService;
import org.knowledge4retail.api.store.service.impl.TrolleyServiceImpl;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TrolleyServiceImplTest {

    private TrolleyServiceImpl service;
    @Mock
    private TrolleyRepository trolleyRepository;
    @Mock
    private DefaultProducer producer;
    @Mock
    private StoreService storeService;
    @Mock
    private Trolley trolley;

    @BeforeEach
    public void setup() {

        service = new TrolleyServiceImpl(trolleyRepository, storeService, producer);
    }

    @Test
    public void createSendsMessageToKafka(){

        service.create(new TrolleyDto());
        verify(producer).publishCreate(any(), any());
    }

    @Test
    public void deleteSendsMessageToKafka(){

        when(trolleyRepository.getReferenceById(any())).thenReturn(trolley);
        service.delete(any());
        verify(producer).publishDelete(any(), any());
    }
}
