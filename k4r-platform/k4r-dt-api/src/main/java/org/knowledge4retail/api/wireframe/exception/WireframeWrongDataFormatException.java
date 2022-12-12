package org.knowledge4retail.api.wireframe.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, value = HttpStatus.BAD_REQUEST,
        reason = "the data format should be either xml or csv")
public class WireframeWrongDataFormatException extends RuntimeException {
}
