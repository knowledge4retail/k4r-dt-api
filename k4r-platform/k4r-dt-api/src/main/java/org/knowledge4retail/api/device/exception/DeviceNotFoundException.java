package org.knowledge4retail.api.device.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Device could not be found")
public class DeviceNotFoundException extends RuntimeException{}
