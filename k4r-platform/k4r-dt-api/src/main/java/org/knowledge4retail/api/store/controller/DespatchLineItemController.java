package org.knowledge4retail.api.store.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.knowledge4retail.api.product.service.ProductService;
import org.knowledge4retail.api.store.dto.DespatchLineItemDto;
import org.knowledge4retail.api.store.exception.DespatchLineItemNotFoundException;
import org.knowledge4retail.api.store.exception.DespatchLogisticUnitNotFoundException;
import org.knowledge4retail.api.store.exception.ProductNotFoundException;
import org.knowledge4retail.api.store.exception.RequestedProductNotFoundException;
import org.knowledge4retail.api.store.service.DespatchLineItemService;
import org.knowledge4retail.api.store.service.DespatchLogisticUnitService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
public class DespatchLineItemController {

    private final DespatchLineItemService despatchLineItemService;
    private final DespatchLogisticUnitService despatchLogisticUnitService;
    private final ProductService productService;

    @Operation(
            summary = "Get all despatchLineItem for the given despatchLogisticUnitId",
            responses = {
                    @ApiResponse(responseCode = "200", description = "DespatchLineItems successfully returned", content = @Content(array = @ArraySchema(schema = @Schema(implementation = DespatchLineItemDto.class)))),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    @GetMapping("/api/v0/despatchlogisticunits/{despatchLogisticUnitId}/despatchlineitems")
    public ResponseEntity<List<DespatchLineItemDto>> getAllDespatchLineItemsByDespatchLogisticUnits(@PathVariable("despatchLogisticUnitId") Integer despatchLogisticUnitId, HttpServletRequest request) {

        validateDespatchLogisticUnitId(despatchLogisticUnitId);

        log.debug(String.format("DespatchLineItemController getAllDespatchLineItems at %1$s called for despatchLogisticUnitId %2$s",
                request.getRequestURL(), despatchLogisticUnitId));
        return new ResponseEntity<>(despatchLineItemService.readByDespatchLogisticUnitId(despatchLogisticUnitId), HttpStatus.OK);
    }

    @Operation(
            summary = "Returns Despatch LineItem defined with the id",
            responses = {
                    @ApiResponse(responseCode = "200", description = "DespatchLineItem with the given Id was successfully returned", content = @Content(schema = @Schema(implementation = DespatchLineItemDto.class))),
                    @ApiResponse(responseCode = "404", description = "DespatchLineItem with the given Id was not found")
            }
    )
    @GetMapping("api/v0/despatchlineitems/{despatchLineItemId}")
    public ResponseEntity<DespatchLineItemDto> getDespatchLineItem(@PathVariable("despatchLineItemId") Integer id) throws DespatchLineItemNotFoundException {

        log.debug(String.format("Received a request to retrieve the Despatch LineItem with the Id %d", id));
        validateDespatchLineItemId(id);
        return new ResponseEntity<>(despatchLineItemService.read(id), HttpStatus.OK);
    }

    @Operation(
            summary = "Create a new despatch LineItem",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Despatch LineItem successfully created", content = @Content(schema = @Schema(implementation = DespatchLineItemDto.class))),
                    @ApiResponse(responseCode = "400", description = "Request Body validation error"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    @PostMapping("/api/v0/despatchlogisticunits/{despatchLogisticUnitId}/despatchlineitems")
    public ResponseEntity<DespatchLineItemDto> createDespatchLineItem(@PathVariable("despatchLogisticUnitId") Integer despatchLogisticUnitId, @Valid @RequestBody DespatchLineItemDto despatchLineItemDto, HttpServletRequest request) {

        despatchLineItemDto.setDespatchLogisticUnitId(despatchLogisticUnitId);

        log.debug(String.format("DespatchLineItemController create DespatchLineItem at %1$s called for despatchLogisticUnitId %2$s with payload %3$s",
                request.getRequestURL(), despatchLogisticUnitId, despatchLineItemDto));
        if(despatchLineItemDto.getDespatchLogisticUnitId() != null) {

            validateDespatchLogisticUnitId(despatchLineItemDto.getDespatchLogisticUnitId());
        }
        if(despatchLineItemDto.getProductId() != null) {

            validateProductId(despatchLineItemDto.getProductId());
        }
        if(despatchLineItemDto.getRequestedProductId() != null) {

            validateRequestedProductId(despatchLineItemDto.getRequestedProductId());
        }
        return new ResponseEntity<>(despatchLineItemService.create(despatchLineItemDto), HttpStatus.OK);
    }

    @Operation(
            summary = "Updates an existing despatch LineItem defined by a given Id",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Despatch LineItem successfully updated", content = @Content(schema = @Schema(implementation = DespatchLineItemDto.class))),
                    @ApiResponse(responseCode = "400", description = "Request Body validation error"),
                    @ApiResponse(responseCode = "404", description = "Despatch LineItem wth the given Id was not found")
            }
    )
    @PutMapping("api/v0/despatchlineitems/{despatchLineItemId}")
    public ResponseEntity<DespatchLineItemDto> updateCustomer(@PathVariable("despatchLineItemId") Integer id, @Valid @RequestBody DespatchLineItemDto despatchLineItemDto) throws DespatchLineItemNotFoundException {

        despatchLineItemDto.setId(id);
        log.debug(String.format("Received a request to update the despatch LineItem with the Id %d with the given details %s", id, despatchLineItemDto));
        validateDespatchLineItemId(id);
        if(despatchLineItemDto.getDespatchLogisticUnitId() != null) {

            validateDespatchLogisticUnitId(despatchLineItemDto.getDespatchLogisticUnitId());
        }
        if(despatchLineItemDto.getProductId() != null) {

            validateProductId(despatchLineItemDto.getProductId());
        }
        if(despatchLineItemDto.getRequestedProductId() != null) {

            validateRequestedProductId(despatchLineItemDto.getRequestedProductId());
        }
        return new ResponseEntity<>(despatchLineItemService.update(id, despatchLineItemDto), HttpStatus.OK);
    }

    @SuppressWarnings("rawtypes")
    @Operation(
            summary = "Delete a Despatch LineItem",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Despatch LineItem successfully deleted", content = @Content(schema = @Schema(implementation = DespatchLineItemDto.class))),
                    @ApiResponse(responseCode = "404", description = "Despatch LineItem with given Id does not exist"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    @DeleteMapping("/api/v0/despatchlineitems/{despatchLineItemId}")
    public ResponseEntity deleteDespatchLineItem(@PathVariable("despatchLineItemId") Integer id, HttpServletRequest request) {

        log.debug(String.format("DespatchLineItemController delete Despatch LineItem at %1$s called",
                request.getRequestURL()));
        validateDespatchLineItemId(id);
        despatchLineItemService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private void validateDespatchLineItemId(Integer id) {

        if(!despatchLineItemService.exists(id)) {

            log.warn(String.format("DespatchLineItem with Id %d was not found", id));
            throw new DespatchLineItemNotFoundException();
        }
    }

    private void validateDespatchLogisticUnitId(Integer id) {

        if(!despatchLogisticUnitService.exists(id)) {

            log.warn(String.format("DespatchLogisticUnit with Id %d was not found", id));
            throw new DespatchLogisticUnitNotFoundException();
        }
    }

    private void validateProductId(String id) {

        if(!productService.exists(id)) {

            log.warn(String.format("Product with Id %s was not found", id));
            throw new ProductNotFoundException();
        }
    }

    private void validateRequestedProductId(String id) {

        if(!productService.exists(id)) {

            log.warn(String.format("Requested Product with Id %s was not found", id));
            throw new RequestedProductNotFoundException();
        }
    }
}
