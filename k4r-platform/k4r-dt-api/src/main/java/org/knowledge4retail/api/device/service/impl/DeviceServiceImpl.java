package org.knowledge4retail.api.device.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.knowledge4retail.api.device.converter.DeviceConverter;
import org.knowledge4retail.api.device.dto.DeviceDto;
import org.knowledge4retail.api.device.model.Device;
import org.knowledge4retail.api.device.repository.DeviceRepository;
import org.knowledge4retail.api.device.service.DeviceService;
import org.knowledge4retail.api.shared.producer.kafka.DefaultProducer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class DeviceServiceImpl implements DeviceService {

    @Value("${org.knowledge4retail.api.listener.kafka.topics.device}")
    private String kafkaTopic;

    private final DeviceRepository deviceRepository;
    private final DefaultProducer producer;

    @Override
    public List<DeviceDto> readAll() {

        return DeviceConverter.INSTANCE.devicesToDtos(deviceRepository.findAll());
    }

    @Override
    public DeviceDto create(DeviceDto deviceDto) {

        Device deviceToSave = DeviceConverter.INSTANCE.dtoToDevice(deviceDto);
        Device savedDevice = deviceRepository.save(deviceToSave);

        DeviceDto createdDto = DeviceConverter.INSTANCE.deviceToDto(savedDevice);
        producer.publishCreate(kafkaTopic, createdDto);
        return createdDto;
    }

    @Override
    public void delete(String id) {

        DeviceDto deletedDto = DeviceConverter.INSTANCE.deviceToDto(deviceRepository.getReferenceById(id));
        deviceRepository.deleteById(id);
        producer.publishDelete(kafkaTopic, deletedDto);
    }

    @Override
    public boolean exists(String id) {

        return deviceRepository.existsById(id);
    }
 
}
