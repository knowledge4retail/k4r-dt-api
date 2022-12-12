package org.knowledge4retail.api.scan.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Scan with the given Id was not found")
public class ScanNotFoundException extends RuntimeException {
}
