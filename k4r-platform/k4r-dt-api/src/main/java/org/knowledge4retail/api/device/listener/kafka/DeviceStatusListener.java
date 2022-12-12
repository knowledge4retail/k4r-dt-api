package org.knowledge4retail.api.device.listener.kafka;


import lombok.extern.slf4j.Slf4j;
import org.knowledge4retail.api.device.service.DeviceStatusService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolationException;

@Slf4j
@Component
public class DeviceStatusListener {

    private final DeviceStatusService deviceStatusService;

    public DeviceStatusListener(DeviceStatusService deviceStatusService) {
        this.deviceStatusService = deviceStatusService;
    }

    @KafkaListener(topics = "${org.knowledge4retail.api.listener.kafka.topics.devicestatus}",
            autoStartup = "${org.knowledge4retail.api.listener.kafka.enabled}")
    void saveStatusUpdate(String robotStatus) {

        log.info(String.format("Received Status Update on topic %1s, with the content %2s",
                "${org.knowledge4retail.api.listener.kafka.topics.devicestatus}", robotStatus));

        try {
            this.deviceStatusService.create(robotStatus);
        }
        catch (ConstraintViolationException e) {
            log.warn(String.format("The given robotStatus is not valid %s", robotStatus), e);
        }

    }
}
