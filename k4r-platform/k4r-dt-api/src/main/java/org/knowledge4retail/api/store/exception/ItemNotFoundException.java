package org.knowledge4retail.api.store.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Item with the given Id was not found")
public class ItemNotFoundException extends RuntimeException {
}
