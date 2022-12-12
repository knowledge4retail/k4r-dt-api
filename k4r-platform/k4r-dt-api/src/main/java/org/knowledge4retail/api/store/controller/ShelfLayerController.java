package org.knowledge4retail.api.store.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.knowledge4retail.api.product.exception.UnitNotFoundException;
import org.knowledge4retail.api.product.service.UnitService;
import org.knowledge4retail.api.store.dto.ShelfLayerDto;
import org.knowledge4retail.api.store.exception.ShelfLayerNotFoundException;
import org.knowledge4retail.api.store.exception.ShelfNotFoundException;
import org.knowledge4retail.api.store.service.ShelfLayerService;
import org.knowledge4retail.api.store.service.ShelfService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
public class ShelfLayerController {

    private final ShelfLayerService shelfLayerService;
    private final ShelfService shelfService;
    private final UnitService unitService;

    @Operation(
            summary = "Saves a given shelf layer for the given shelfId. ShelfId field in payload is ignored.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Shelf Layer successfully created", content = @Content(schema = @Schema(implementation = ShelfLayerDto.class))),
                    @ApiResponse(responseCode = "400", description = "Request Body validation error"),
                    @ApiResponse(responseCode = "500", description = "Internal server error"),
            }
    )
    @PostMapping("api/v0/shelves/{shelfId}/shelflayers")
    public ResponseEntity<ShelfLayerDto> createShelfLayer(@PathVariable Integer shelfId, @Valid @RequestBody ShelfLayerDto shelfLayer, HttpServletRequest request) throws ShelfNotFoundException{

        shelfLayer.setShelfId(shelfId);
        log.debug(String.format("ShelfLayerController createShelfLayer at %1$s called for shelfId %2$s with payload %3$s",
                request.getRequestURL(), shelfId, shelfLayer));

        validateShelfId(shelfId);

        validateUnitId(shelfLayer.getLengthUnitId());

        return new ResponseEntity<>(shelfLayerService.create(shelfLayer), HttpStatus.OK);

    }

    @Operation(
            summary = "Returns all shelf layers for the given shelfId",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Shelf Layers successfully returned, if any", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ShelfLayerDto.class)))),
                    @ApiResponse(responseCode = "404", description = "Shelf with the given shelfId does not exist"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    @GetMapping("api/v0/shelves/{shelfId}/shelflayers")
    public ResponseEntity<List<ShelfLayerDto>> retrieveShelfLayers(@PathVariable Integer shelfId, @RequestParam(name = "externalReferenceId", required = false) String externalReferenceId, HttpServletRequest request) throws ShelfNotFoundException {

        log.debug(String.format("ShelfLayerController retrieveShelfLayer at %1$s called for shelfLayerId %2$s. Optionally Filtered by externalReferenceId for %3$s",
                request.getRequestURL(), shelfId, externalReferenceId));

        validateShelfId(shelfId);

        if (externalReferenceId != null && !externalReferenceId.isEmpty()) {

            validateExternalReferenceId(externalReferenceId);

            return new ResponseEntity<>(shelfLayerService.readByShelfIdAndExternalReferenceId(shelfId, externalReferenceId), HttpStatus.OK);
        } else {

            return new ResponseEntity<>(shelfLayerService.readByShelfId(shelfId), HttpStatus.OK);
        }
    }

    @Operation(
            summary = "Returns the shelf layer with the given id",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Shelf Layer successfully returned", content = @Content(schema = @Schema(implementation = ShelfLayerDto.class))),
                    @ApiResponse(responseCode = "404", description = "Shelf Layer with the given shelfLayerId does not exist"),
                    @ApiResponse(responseCode = "500", description = "Internal server error"),
            }
    )
    @GetMapping("api/v0/shelflayers/{shelfLayerId}")
    public ResponseEntity<ShelfLayerDto> retrieveShelfLayer(@PathVariable Integer shelfLayerId, HttpServletRequest request) throws ShelfLayerNotFoundException {

        log.debug(String.format("ShelfLayerController retrieveShelfLayer at %1$s called for shelfLayerId %2$s",
                request.getRequestURL(), shelfLayerId));

        validateShelfLayerId(shelfLayerId);

        return new ResponseEntity<>(shelfLayerService.read(shelfLayerId), HttpStatus.OK);
    }

    @Operation(
            summary = "Deletes the shelf layer with the given id",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Shelf Layer successfully deleted", content = @Content(schema = @Schema(implementation = ShelfLayerDto.class))),
                    @ApiResponse(responseCode = "404", description = "Shelf with the given shelfId does not exist"),
                    @ApiResponse(responseCode = "404", description = "Shelf Layer with the given shelfLayerId does not exist"),
                    @ApiResponse(responseCode = "500", description = "Internal server error"),
            }
    )
    @DeleteMapping("api/v0/shelflayers/{shelfLayerId}")
    public ResponseEntity deleteShelfLayer(@PathVariable Integer shelfLayerId, HttpServletRequest request) throws ShelfLayerNotFoundException {

        log.debug(String.format("ShelfLayerController deleteShelfLayer at %1$s called for shelfLayerId %2$s",
                request.getRequestURL(), shelfLayerId));

        validateShelfLayerId(shelfLayerId);

        shelfLayerService.delete(shelfLayerId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    private void validateExternalReferenceId(String externalReferenceId) {

        if (!shelfLayerService.existsByExternalReferenceId(externalReferenceId)) {

            log.warn("shelf layer with externalReferenceId {} does not exist", externalReferenceId);
            throw new ShelfNotFoundException();
        }
    }

    private void validateShelfId(Integer shelfId) {

        if (!shelfService.exists(shelfId)) {

            log.warn("shelf with id {} does not exist", shelfId);
            throw new ShelfNotFoundException();
        }
    }


    private void validateShelfLayerId(Integer shelfLayerId) {

        if (!shelfLayerService.exists(shelfLayerId)) {

            log.warn("shelf layer with id {} does not exist", shelfLayerId);
            throw new ShelfLayerNotFoundException();
        }
    }

    private void validateUnitId(Integer unitId) {

        if (!unitService.exists(unitId)) {

            log.warn("unit with id {} does not exist", unitId);
            throw new UnitNotFoundException();
        }
    }
}
