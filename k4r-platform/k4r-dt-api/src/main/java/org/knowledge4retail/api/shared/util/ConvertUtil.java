package org.knowledge4retail.api.shared.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.knowledge4retail.api.shared.dto.Message;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.util.Set;

@Slf4j
@RequiredArgsConstructor
public class ConvertUtil {

    private final Validator validator;

    public <T> Message<T> convertMessage(String record, Class<T> dtoClass) {

        try {

            JavaType type = new ObjectMapper().getTypeFactory().constructParametricType(Message.class, dtoClass);
            Message<T> message = new ObjectMapper().readValue(record, type);

            Set<ConstraintViolation<Message<T>>> violations = validator.validate(message);
            if (!violations.isEmpty()) {
                throw new ConstraintViolationException(violations);
            }

            return message;
        } catch (JsonProcessingException e) {

            log.warn(String.format("Could not parse the message %s", record), e);
        }

        return null;
    }
}
