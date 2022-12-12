package org.knowledge4retail.api.device.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.knowledge4retail.api.device.converter.DeviceStatusConverter;
import org.knowledge4retail.api.device.dto.DeviceStatusDto;
import org.knowledge4retail.api.device.model.DeviceStatus;
import org.knowledge4retail.api.shared.producer.kafka.DefaultProducer;
import org.knowledge4retail.api.device.repository.DeviceStatusRepository;
import org.knowledge4retail.api.device.service.DeviceStatusService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.util.List;
import java.util.Set;


@Slf4j
@Service
public class DeviceStatusServiceImpl implements DeviceStatusService {


    private final DeviceStatusRepository repository;
    private final Validator validator;
    private final DefaultProducer producer;

    @Value("${org.knowledge4retail.api.listener.kafka.topics.devicestatus}")
    private String kafkaTopic;

    public DeviceStatusServiceImpl(DeviceStatusRepository repository, Validator validator, DefaultProducer producer) {

        this.repository = repository;
        this.validator = validator;
        this.producer = producer;
    }

    @Override
    public DeviceStatus create(String robotStatus) {


        try {

            DeviceStatusDto deviceStatusDto = new ObjectMapper().readValue(robotStatus, DeviceStatusDto.class);

            Set<ConstraintViolation<DeviceStatusDto>> violations = validator.validate(deviceStatusDto);
            if (!violations.isEmpty()) {
                throw new ConstraintViolationException(violations);
            }
            DeviceStatus deviceStatus = DeviceStatusConverter.INSTANCE.dtoToDeviceStatus(deviceStatusDto);

            DeviceStatus createdStatus = this.repository.save(deviceStatus);

            producer.publishCreate(kafkaTopic, DeviceStatusConverter.INSTANCE.deviceStatusToDto(createdStatus));
            return createdStatus;
        } catch (JsonProcessingException e) {
            log.warn(String.format("Could not parse the device message update %s", robotStatus), e);
        }
        return null;
    }

    @Override
    public List<DeviceStatusDto> readAll(String deviceId) {

        return DeviceStatusConverter.INSTANCE.deviceStatusesToDtos(repository.findAll());
    }
}
