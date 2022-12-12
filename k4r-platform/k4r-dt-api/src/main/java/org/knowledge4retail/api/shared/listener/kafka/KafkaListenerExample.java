package org.knowledge4retail.api.shared.listener.kafka;


import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class KafkaListenerExample {

    @KafkaListener(topics = "k4r.example", autoStartup = "${org.knowledge4retail.api.listener.kafka.enabled}")
    void listener(String data) {

        log.debug(data);
    }
}