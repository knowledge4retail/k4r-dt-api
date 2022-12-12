package org.knowledge4retail.api.wireframe.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Wireframe not found")
public class WireframeNotFoundException extends RuntimeException {
}
