package org.knowledge4retail.api.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.knowledge4retail.api.shared.producer.kafka.DefaultProducer;
import org.knowledge4retail.api.store.dto.DespatchAdviceDto;
import org.knowledge4retail.api.store.model.DespatchAdvice;
import org.knowledge4retail.api.store.repository.DespatchAdviceRepository;
import org.knowledge4retail.api.store.service.StoreService;
import org.knowledge4retail.api.store.service.impl.DespatchAdviceServiceImpl;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DespatchAdviceServiceImplTest {

    private DespatchAdviceServiceImpl service;
    @Mock
    private DespatchAdviceRepository despatchAdviceRepository;
    @Mock
    private DefaultProducer producer;
    @Mock
    private DespatchAdvice despatchAdvice;
    @Mock
    private StoreService storeService;

    @BeforeEach
    public void setup() {

        service = new DespatchAdviceServiceImpl(despatchAdviceRepository, producer, storeService);
    }

    @Test
    public void createSendsMessageToKafka(){

        service.create(new DespatchAdviceDto());
        verify(producer).publishCreate(any(), any());
    }

    @Test
    public void updateSendsMessageToKafka() {

        when(despatchAdviceRepository.getReferenceById(any())).thenReturn(any());
        service.update(1234, new DespatchAdviceDto());
        verify(producer).publishUpdate(any(), any(), any());
    }

    @Test
    public void deleteSendsMessageToKafka(){

        when(despatchAdviceRepository.getReferenceById(any())).thenReturn(despatchAdvice);
        service.delete(any());
        verify(producer).publishDelete(any(), any());
    }
}
