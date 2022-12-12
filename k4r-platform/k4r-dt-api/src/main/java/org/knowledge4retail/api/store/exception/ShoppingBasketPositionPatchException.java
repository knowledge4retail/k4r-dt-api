package org.knowledge4retail.api.store.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "ShoppingBasketPosition could not be updated")
public class ShoppingBasketPositionPatchException extends RuntimeException {
}
