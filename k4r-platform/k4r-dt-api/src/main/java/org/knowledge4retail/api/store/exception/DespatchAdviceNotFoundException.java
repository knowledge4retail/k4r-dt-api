package org.knowledge4retail.api.store.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "DespatchAdvice with the given Id was not found")
public class DespatchAdviceNotFoundException extends RuntimeException{}
