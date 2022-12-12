package org.knowledge4retail.api.device.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, value = HttpStatus.BAD_REQUEST,
        reason = "Error occurred while uploading image to persistent storage, please check log for details")
public class ImageUploadFailedException extends RuntimeException {
}
