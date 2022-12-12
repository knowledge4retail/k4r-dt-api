package org.knowledge4retail.api.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.knowledge4retail.api.shared.producer.kafka.DefaultProducer;
import org.knowledge4retail.api.store.dto.BarcodeDto;
import org.knowledge4retail.api.store.model.Barcode;
import org.knowledge4retail.api.store.repository.BarcodeRepository;
import org.knowledge4retail.api.store.service.impl.BarcodeServiceImpl;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BarcodeServiceImplTest {

    private BarcodeServiceImpl service;
    @Mock
    private BarcodeRepository barcodeRepository;
    @Mock
    private DefaultProducer producer;
    @Mock
    private Barcode barcode;

    @BeforeEach
    public void setup() {

        service = new BarcodeServiceImpl(barcodeRepository, producer);
    }

    @Test
    public void createSendsMessageToKafka(){

        service.create(new BarcodeDto());
        verify(producer).publishCreate(any(), any());
    }

    @Test
    public void updateSendsMessageToKafka() {

        when(barcodeRepository.getReferenceById(any())).thenReturn(any());
        service.update(1234, new BarcodeDto());
        verify(producer).publishUpdate(any(), any(), any());
    }

    @Test
    public void deleteSendsMessageToKafka(){

        when(barcodeRepository.getReferenceById(any())).thenReturn(barcode);
        service.delete(any());
        verify(producer).publishDelete(any(), any());
    }
}
