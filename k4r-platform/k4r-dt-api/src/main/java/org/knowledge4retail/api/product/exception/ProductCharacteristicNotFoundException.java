package org.knowledge4retail.api.product.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "product characteristic not found")
public class ProductCharacteristicNotFoundException extends RuntimeException {}
