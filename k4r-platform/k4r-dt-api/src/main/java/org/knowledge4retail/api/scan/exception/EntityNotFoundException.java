package org.knowledge4retail.api.scan.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Entity of Scan was not found")
public class EntityNotFoundException extends RuntimeException {
}
