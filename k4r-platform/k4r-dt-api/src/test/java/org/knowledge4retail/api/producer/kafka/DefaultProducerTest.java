package org.knowledge4retail.api.producer.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.knowledge4retail.api.customer.dto.CustomerDto;
import org.knowledge4retail.api.shared.producer.kafka.DefaultProducer;
import org.knowledge4retail.api.shared.producer.kafka.model.CRUDAction;
import org.knowledge4retail.api.shared.producer.kafka.model.KafkaMessage;
import org.mockito.Mock;
import org.springframework.kafka.core.KafkaTemplate;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
public class DefaultProducerTest {

    @Mock
    private KafkaTemplate<String, String> kafkaTemplate;

    private DefaultProducer producer;

    CustomerDto dtoOne;
    CustomerDto dtoTwo;

    @BeforeEach
    public void setUp(){

        producer = new DefaultProducer(kafkaTemplate);
        dtoOne = new CustomerDto();
        dtoOne.setId(1);
        dtoOne.setAnonymisedName("anon");
        dtoTwo = new CustomerDto();
        dtoTwo.setId(2);
        dtoTwo.setAnonymisedName("ymous");
    }

    @Test
    public void buildMessageResultConvertsToKafkaMessageType()
    {

        String message = producer.buildMessage(CRUDAction.UPDATE, dtoOne, dtoTwo);

        KafkaMessage kafkaMessage = new KafkaMessage();

        try {
            kafkaMessage = new ObjectMapper().readValue(message, KafkaMessage.class);
        } catch (JsonProcessingException e) {
            fail();
        }

        assertEquals(CRUDAction.UPDATE, kafkaMessage.action);
    }

    @Test
    public void buildMessageDropsOldObjectFieldWhenOldObjectParameterIsNull()
    {

        String message = producer.buildMessage(CRUDAction.UPDATE, dtoOne, null);

        assertFalse(message.contains("oldObject"));
    }

    @Test
    public void buildMessageThrowsIllegalArgumentExceptionWhenObjectParameterIsNull()
    {

        assertThrows(IllegalArgumentException.class, () -> producer.buildMessage(CRUDAction.UPDATE, null, dtoOne));
    }
}
