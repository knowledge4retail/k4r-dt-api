package org.knowledge4retail.api.shared.producer.kafka.model;

import lombok.*;

@Data
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class KafkaMessage {

    public CRUDAction action;
    public Object oldObject;
    public Object object;
}



