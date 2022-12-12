package org.knowledge4retail.api.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.knowledge4retail.api.device.dto.DeviceDto;
import org.knowledge4retail.api.device.model.Device;
import org.knowledge4retail.api.device.repository.DeviceRepository;
import org.knowledge4retail.api.device.service.impl.DeviceServiceImpl;
import org.knowledge4retail.api.shared.producer.kafka.DefaultProducer;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class DeviceServiceImplTest {

    private DeviceServiceImpl service;
    @Mock
    private DeviceRepository deviceRepository;
    @Mock
    private DefaultProducer producer;
    @Mock
    private Device device;


    @BeforeEach
    public void setup() {

        service = new DeviceServiceImpl(deviceRepository, producer);
    }

    @Test
    public void createSendsMessageToKafka(){

        service.create(new DeviceDto());
        verify(producer).publishCreate(any(), any());
    }

    @Test
    public void deleteSendsMessageToKafka(){

        when(deviceRepository.getReferenceById(any())).thenReturn(device);
        service.delete(any());
        verify(producer).publishDelete(any(), any());
    }
}
