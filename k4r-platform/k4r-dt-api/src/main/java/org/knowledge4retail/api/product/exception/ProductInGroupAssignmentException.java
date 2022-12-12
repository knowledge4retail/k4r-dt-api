package org.knowledge4retail.api.product.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class ProductInGroupAssignmentException extends RuntimeException {

    public ProductInGroupAssignmentException(String reason) {

        super(reason);
    }
}
