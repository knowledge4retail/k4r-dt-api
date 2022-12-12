package org.knowledge4retail.api.device.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, value = HttpStatus.NOT_FOUND, reason = "Image could not be found")
public class DeviceImageNotFoundException extends RuntimeException {
}
