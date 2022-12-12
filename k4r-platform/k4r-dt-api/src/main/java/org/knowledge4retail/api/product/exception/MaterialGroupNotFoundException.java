package org.knowledge4retail.api.product.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Material group not found")
public class MaterialGroupNotFoundException extends RuntimeException {}
