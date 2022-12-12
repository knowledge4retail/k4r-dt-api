package org.knowledge4retail.api.shared.producer.kafka;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.knowledge4retail.api.shared.dto.BasicDto;
import org.knowledge4retail.api.shared.producer.kafka.model.CRUDAction;
import org.knowledge4retail.api.shared.producer.kafka.model.KafkaMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DefaultProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    @Value("${org.knowledge4retail.api.producer.kafka.enabled}")
    private boolean kafkaEnabled;

    public DefaultProducer(KafkaTemplate<String, String> kafkaTemplate)
    {

        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishCreate(String topic, BasicDto dto)
    {

        String message = buildMessage(CRUDAction.CREATE, dto, null);
        sendMessage(topic, message);
    }

    public void publishUpdate(String topic, BasicDto dto, BasicDto oldDto)
    {

        String message = buildMessage(CRUDAction.UPDATE, dto, oldDto);
        sendMessage(topic, message);
    }

    public void publishDelete(String topic, BasicDto dto)
    {

        String message = buildMessage(CRUDAction.DELETE, dto, null);
        sendMessage(topic, message);
    }

    private void sendMessage(String topic, String message)
    {

        if (topic == null || topic.isEmpty()) {

            IllegalArgumentException e = new IllegalArgumentException("no topics provided");
            log.error(e.toString());
            throw e;
        }

        if (shouldSend()) {

            log.debug(String.format("KafkaProducer: message %1$s sent to topic %2$s", message, topic));
            kafkaTemplate.send(topic, message);
        }
    }

    private boolean shouldSend()
    {

        if (!kafkaEnabled) {

            log.debug("Skipping Kafka send operation. Disabled in ${org.knowledge4retail.api.producer.kafka.enabled}");
            return false;
        }

        return true;
    }

    public String buildMessage(CRUDAction action, BasicDto dto, BasicDto oldDto)
    {

        if (dto == null) {

            IllegalArgumentException e = new IllegalArgumentException("dto must not be null");
            log.error(e.toString());
            throw e;
        }

        String result = null;

        KafkaMessage message = KafkaMessage.builder().
                action(action).
                object(dto).
                oldObject(oldDto).
                build();

        try {

            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL); // do not include oldObject if it's null
            result = mapper.writeValueAsString(message);
        } catch (JsonProcessingException e) {

            log.error(String.format("Error creating KafkaMessage from payload %s", message.toString()), e);
        }

        return result;
    }
}
