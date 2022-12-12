package org.knowledge4retail.api.store.controller;

import com.github.fge.jsonpatch.JsonPatch;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.knowledge4retail.api.customer.service.CustomerService;
import org.knowledge4retail.api.product.service.ProductService;
import org.knowledge4retail.api.store.dto.ShoppingBasketPositionDto;
import org.knowledge4retail.api.store.exception.CustomerNotFoundException;
import org.knowledge4retail.api.store.exception.ProductNotFoundException;
import org.knowledge4retail.api.store.exception.ShoppingBasketPositionNotFoundException;
import org.knowledge4retail.api.store.exception.StoreNotFoundException;
import org.knowledge4retail.api.store.service.ShoppingBasketService;
import org.knowledge4retail.api.store.service.StoreService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
public class ShoppingBasketController {

    private final ShoppingBasketService shoppingBasketService;
    private final StoreService storeService;
    private final CustomerService customerService;
    private final ProductService productService;

    @Operation(
            summary = "Creates a new Shopping Basket Position, identified by storeId, customerId and productId",
            description = "storeId and customerId may be omitted in the request body. The path parameters will be used instead.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Position successfully created", content = @Content(schema = @Schema(implementation = ShoppingBasketPositionDto.class))),
                    @ApiResponse(responseCode = "400", description = "Parameter mismatch"),
                    @ApiResponse(responseCode = "404", description = "Store, customer or product does not exist"),
                    @ApiResponse(responseCode = "500", description = "Internal server error"),
            }
    )
    @PostMapping("api/v0/stores/{storeId}/customers/{customerId}/shoppingbasketpositions")
    public ResponseEntity<ShoppingBasketPositionDto> createPosition (
            @PathVariable("storeId") Integer storeId,
            @PathVariable("customerId") Integer customerId,
            @Valid @RequestBody ShoppingBasketPositionDto positionDto,
            HttpServletRequest request) {

        positionDto.setStoreId(storeId);
        positionDto.setCustomerId(customerId);
        log.debug(String.format("ShoppingBasketController createPosition at %1$s called with payload %2$s"
                , request.getRequestURL()
                , positionDto));

        validateStoreId(storeId);
        validateCustomerId(customerId);
        validateProductId(positionDto.getProductId());

        return new ResponseEntity<>(shoppingBasketService.create(positionDto), HttpStatus.OK);
    }

    @Operation(
            summary = "Returns all Shopping Basket Positions for the given store and customer combination",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Positions successfully returned", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ShoppingBasketPositionDto.class)))),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    @GetMapping("api/v0/stores/{storeId}/customers/{customerId}/shoppingbasketpositions")
    public ResponseEntity<List<ShoppingBasketPositionDto>> getPositions (
            @PathVariable("storeId") Integer storeId,
            @PathVariable("customerId") Integer customerId,
            HttpServletRequest request) {

        validateStoreId(storeId);
        validateCustomerId(customerId);
        log.debug(String.format("ShoppingBasketController getPositions at %1$s called"
                , request.getRequestURL()));

        return new ResponseEntity<>(shoppingBasketService.readByStoreIdAndCustomerId(storeId, customerId), HttpStatus.OK);
    }

    @Operation(
            summary = "Returns the Shopping Basket Position with the given Id",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Position successfully returned", content = @Content(schema = @Schema(implementation = ShoppingBasketPositionDto.class))),
                    @ApiResponse(responseCode = "404", description = "Position does not exist"),
                    @ApiResponse(responseCode = "500", description = "Internal server error"),
            }
    )
    @GetMapping("api/v0/shoppingbasketpositions/{positionId}")
    public ResponseEntity<ShoppingBasketPositionDto> getPosition (
            @PathVariable("positionId") Integer positionId,
            HttpServletRequest request) {

        log.debug(String.format("ShoppingBasketController getPosition at %1$s called"
                , request.getRequestURL()));

        validatePositionId(positionId);

        return new ResponseEntity<>(shoppingBasketService.read(positionId), HttpStatus.OK);
    }

    @Operation(
            summary = "Deletes all Shopping Basket Positions for the given store and customer combination",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Positions successfully deleted", content = @Content(schema = @Schema(implementation = ShoppingBasketPositionDto.class))),
                    @ApiResponse(responseCode = "500", description = "Internal server error"),
            }
    )
    @DeleteMapping("api/v0/stores/{storeId}/customers/{customerId}/shoppingbasketpositions")
    public ResponseEntity deletePositions (
            @PathVariable("storeId") Integer storeId,
            @PathVariable("customerId") Integer customerId,
            HttpServletRequest request) {

        log.debug(String.format("ShoppingBasketController deletePositions at %1$s called"
                , request.getRequestURL()));
        validateCustomerId(customerId);
        validateStoreId(storeId);
        shoppingBasketService.deleteByStoreIdAndCustomerId(storeId, customerId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(
            summary = "Deletes the Shopping Basket Position with the given Id",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Position successfully deleted", content = @Content(schema = @Schema(implementation = ShoppingBasketPositionDto.class))),
                    @ApiResponse(responseCode = "404", description = "Position successfully deleted"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    @DeleteMapping("api/v0/shoppingbasketpositions/{positionId}")
    public ResponseEntity deletePosition (
            @PathVariable("positionId") Integer positionId,
            HttpServletRequest request) {

        log.debug(String.format("ShoppingBasketController deletePosition at %1$s called"
                , request.getRequestURL()));

        validatePositionId(positionId);
        shoppingBasketService.delete(positionId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(
            summary = "Updates the Shopping Basket Position with the given Id",
            description = "accepts one or multiple JSON-PATCH replace operations for path /sellingPrice, /quantity and /currency. See http://jsonpatch.com/",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Position successfully updated", content = @Content(schema = @Schema(implementation = ShoppingBasketPositionDto.class))),
                    @ApiResponse(responseCode = "404", description = "Position does not exist"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    @PatchMapping(path = "api/v0/shoppingbasketpositions/{positionId}")
    public ResponseEntity<ShoppingBasketPositionDto> patchPosition (
            @PathVariable("positionId") Integer positionId,
            @RequestBody JsonPatch patch,
            HttpServletRequest request) {

        log.debug(String.format("ShoppingBasketController patchPosition at %1$s called with payload %2$s"
                , request.getRequestURL()
                , patch.toString()));
        validatePositionId(positionId);

        return new ResponseEntity<>(shoppingBasketService.update(positionId, patch), HttpStatus.OK);
    }

    private void validateStoreId(Integer storeId) {

        if (!storeService.exists(storeId)) {

            log.warn("store with id {} does not exist", storeId);
            throw new StoreNotFoundException();
        }
    }

    private void validateCustomerId(Integer customerId) {

        if (!customerService.exists(customerId)) {

            log.warn("customer with id {} does not exist", customerId);
            throw new CustomerNotFoundException();
        }
    }

    private void validateProductId(String productId) {

        if (!productService.exists(productId)) {

            log.warn("product with id {} does not exist", productId);
            throw new ProductNotFoundException();
        }
    }

    private void validatePositionId(Integer positionId) {

        if (!shoppingBasketService.exists(positionId)) {
            log.warn("ShoppingBasketPosition does not exist");
            throw new ShoppingBasketPositionNotFoundException();
        }
    }
}
