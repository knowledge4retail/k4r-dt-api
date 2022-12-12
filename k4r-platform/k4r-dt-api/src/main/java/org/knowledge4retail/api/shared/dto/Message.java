package org.knowledge4retail.api.shared.dto;

import lombok.Data;

@Data
public class Message<T> {

    private String action;
    private T object;
    private T oldObject;
}
