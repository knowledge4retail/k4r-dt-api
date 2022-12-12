package org.knowledge4retail.api.store.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.knowledge4retail.api.product.exception.ProductGtinNotFoundException;
import org.knowledge4retail.api.product.exception.ProductUnitNotFoundException;
import org.knowledge4retail.api.product.service.ProductGtinService;
import org.knowledge4retail.api.product.service.ProductUnitService;
import org.knowledge4retail.api.store.dto.DeliveredUnitDto;
import org.knowledge4retail.api.store.exception.DeliveredUnitNotFoundException;
import org.knowledge4retail.api.store.exception.TrolleyNotFoundException;
import org.knowledge4retail.api.store.service.DeliveredUnitService;
import org.knowledge4retail.api.store.service.TrolleyService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
public class DeliveredUnitController {

    private final DeliveredUnitService deliveredUnitService;
    private final ProductUnitService productUnitService;
    private final ProductGtinService productGtinService;
    private final TrolleyService trolleyService;

    @Operation(
            summary = "Get all delivered units",
            responses = {
                    @ApiResponse(responseCode = "200", description = "DeliveredUnits successfully returned", content = @Content(array = @ArraySchema(schema = @Schema(implementation = DeliveredUnitDto.class)))),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    @GetMapping("api/v0/deliveredunits")
    public ResponseEntity<List<DeliveredUnitDto>> getAllDeliveredUnits(HttpServletRequest request) {

        log.debug(String.format("DeliveredUnitController getAllDeliveredUnits at %1$s called",
                request.getRequestURL()));
        return new ResponseEntity<>(deliveredUnitService.readAll(), HttpStatus.OK);
    }

    @Operation(
            summary = "Returns delivered unit defined with the id",
            responses = {
                    @ApiResponse(responseCode = "200", description = "DeliveredUnit with the given Id was successfully returned", content = @Content(schema = @Schema(implementation = DeliveredUnitDto.class))),
                    @ApiResponse(responseCode = "404", description = "DeliveredUnitr wth the given Id was not found")
            }
    )
    @GetMapping("api/v0/deliveredunits/{deliveredUnitId}")
    public ResponseEntity<DeliveredUnitDto> getDeliveredUnit(@PathVariable("deliveredUnitId") Integer id) throws DeliveredUnitNotFoundException {

        log.debug(String.format("Received a request to retrieve the delivered unit with the Id %d", id));
        validateDeliveredUnitId(id);
        return new ResponseEntity<>(deliveredUnitService.read(id), HttpStatus.OK);
    }

    @Operation(
            summary = "Create a new delivered Unit",
            responses = {
                    @ApiResponse(responseCode = "200", description = "DeliveredUnit successfully created", content = @Content(schema = @Schema(implementation = DeliveredUnitDto.class))),
                    @ApiResponse(responseCode = "400", description = "Request Body validation error"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    @PostMapping("/api/v0/deliveredunits")
    public ResponseEntity<DeliveredUnitDto> createDeliveredUnit(@Valid @RequestBody DeliveredUnitDto deliveredUnitDto, HttpServletRequest request) {

        log.debug(String.format("DeliveredUnitController createDeliveredUnit at %1$s called with payload %2$s",
                request.getRequestURL(), deliveredUnitDto.toString()));

        if(deliveredUnitDto.getTrolleyId() != null) {
            validateTrolleyId(deliveredUnitDto.getTrolleyId());
        }
        validateProductGtinId(deliveredUnitDto.getProductGtinId());
        validateProductUnitId(deliveredUnitDto.getProductUnitId());
        return new ResponseEntity<>(deliveredUnitService.create(deliveredUnitDto), HttpStatus.OK);
    }

    @Operation(
            summary = "Updates an existing DeliveredUnit defined by a given Id",
            responses = {
                    @ApiResponse(responseCode = "200", description = "DeliveredUnit successfully updated", content = @Content(schema = @Schema(implementation = DeliveredUnitDto.class))),
                    @ApiResponse(responseCode = "400", description = "Request Body validation error"),
                    @ApiResponse(responseCode = "404", description = "DeliveredUnit wth the given Id was not found")
            }
    )
    @PutMapping("api/v0/deliveredunits/{deliveredUnitId}")
    public ResponseEntity<DeliveredUnitDto> updateDeliveredUnit(@PathVariable("deliveredUnitId") Integer id, @Valid @RequestBody DeliveredUnitDto deliveredUnitDto) throws DeliveredUnitNotFoundException {

        deliveredUnitDto.setId(id);
        log.debug(String.format("Received a request to update the delivered unit with the Id %d with the given details %s", id, deliveredUnitDto));
        validateDeliveredUnitId(id);

        if(deliveredUnitDto.getTrolleyId() != null) {
            validateTrolleyId(deliveredUnitDto.getTrolleyId());
        }
        validateProductGtinId(deliveredUnitDto.getProductGtinId());
        validateProductUnitId(deliveredUnitDto.getProductUnitId());

        return new ResponseEntity<>(deliveredUnitService.update(id, deliveredUnitDto), HttpStatus.OK);
    }

    @SuppressWarnings("rawtypes")
    @Operation(
            summary = "Get all delivered units",
            responses = {
                    @ApiResponse(responseCode = "200", description = "DeliveredUnits successfully returned", content = @Content(array = @ArraySchema(schema = @Schema(implementation = DeliveredUnitDto.class)))),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    @DeleteMapping("/api/v0/deliveredunits/{deliveredUnitId}")
    public ResponseEntity deleteDeliveredUnit(@PathVariable("deliveredUnitId") Integer id, HttpServletRequest request) {

        log.debug(String.format("DeliveredUnitController deleteDeliveredUnit at %1$s called",
                request.getRequestURL()));
        validateDeliveredUnitId(id);
        deliveredUnitService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    private void validateDeliveredUnitId(Integer id) {

        if(!deliveredUnitService.exists(id)) {

            log.warn(String.format("DeliveredUnit with Id %d was not found", id));
            throw new DeliveredUnitNotFoundException();
        }
    }

    private void validateProductUnitId(Integer id) {

        if(!productUnitService.exists(id)) {

            log.warn(String.format("ProductUnit with Id %d was not found", id));
            throw new ProductUnitNotFoundException();
        }
    }

    private void validateProductGtinId(Integer id) {

        if(!productGtinService.exists(id)) {

            log.warn(String.format("ProductGtin with Id %d was not found", id));
            throw new ProductGtinNotFoundException();
        }
    }

    private void validateTrolleyId(Integer id) {

        if(!trolleyService.exists(id)) {

            log.warn(String.format("Trolley with Id %d was not found", id));
            throw new TrolleyNotFoundException();
        }
    }
}
