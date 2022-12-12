package org.knowledge4retail.api.scan.listener.kafka;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.knowledge4retail.api.scan.dto.ScanDto;
import org.knowledge4retail.api.scan.exception.EntityNotFoundException;
import org.knowledge4retail.api.scan.service.ScanService;
import org.knowledge4retail.api.shared.dto.Message;
import org.knowledge4retail.api.shared.util.ConvertUtil;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolationException;
import javax.validation.Validator;

@Slf4j
@Component
@RequiredArgsConstructor
public class ScanListener {

    private final Validator validator;
    private final ScanService scanService;

    @KafkaListener(topics = "${org.knowledge4retail.api.listener.kafka.topics.ubicascan}", autoStartup = "${org.knowledge4retail.api.listener.kafka.enabled}")
    void scanListener(String record) {

        log.info(String.format("Received Scan data on topic %1s, with the content %2s",
                "${org.knowledge4retail.api.listener.kafka.topics.ubicascan}", record));
        ConvertUtil convertUtil = new ConvertUtil(validator);

        try {

            Message<ScanDto> message = convertUtil.convertMessage(record, ScanDto.class);
            ScanDto scan = message.getObject();
            scanService.create(scan);
            log.info(String.format("create new scan data from kafka message: %s", message.getObject()));

            String type = scan.getEntityType();
            switch (type) {
                case "store" -> scanService.createOrUpdateStore(scan);
                case "shelf" -> scanService.createOrUpdateShelf(scan);
                case "shelfLayer" -> scanService.createOrUpdateShelfLayer(scan);
                default -> throw new EntityNotFoundException();
            }
        }
        catch (ConstraintViolationException e) {

            log.warn(String.format("The given data is not valid %s", record), e);
        }
    }
}
