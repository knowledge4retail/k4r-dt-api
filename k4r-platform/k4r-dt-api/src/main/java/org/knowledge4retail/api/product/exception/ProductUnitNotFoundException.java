package org.knowledge4retail.api.product.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Product Unit not found")
public class ProductUnitNotFoundException extends RuntimeException {}
