package org.knowledge4retail.api.store.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.knowledge4retail.api.store.dto.FacingDto;
import org.knowledge4retail.api.store.exception.FacingNotFoundException;
import org.knowledge4retail.api.store.exception.ShelfLayerNotFoundException;
import org.knowledge4retail.api.store.service.FacingService;
import org.knowledge4retail.api.store.service.ShelfLayerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
public class FacingController {

    private final FacingService facingService;
    private final ShelfLayerService shelfLayerService;

    @Operation(
            summary = "Saves a given Facing for the given shelfLayerId. ShelfLayerId field in payload is ignored.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Facing successfully created", content = @Content(schema = @Schema(implementation = FacingDto.class))),
                    @ApiResponse(responseCode = "400", description = "Request Body validation error"),
                    @ApiResponse(responseCode = "404", description = "ShelfLayerId not found"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    @PostMapping("api/v0/shelflayers/{shelfLayerId}/facings")
    public ResponseEntity<FacingDto> createFacing(@PathVariable Integer shelfLayerId, @Valid @RequestBody FacingDto facing, HttpServletRequest request) {

        facing.setShelfLayerId(shelfLayerId);
        log.debug(String.format("FacingController createFacing at %1$s called for shelfLayerId %2$s with payload %3$s",
                request.getRequestURL(), shelfLayerId, facing));

        validateShelfLayerId(shelfLayerId);

        return new ResponseEntity<>(facingService.create(facing), HttpStatus.OK);
    }

    @Operation(
            summary = "Returns all Facings for the given shelfLayerId",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Facings successfully returned", content = @Content(array = @ArraySchema(schema = @Schema(implementation = FacingDto.class)))),
                    @ApiResponse(responseCode = "404", description = "ShelfLayer with the given shelfLayerId does not exist"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    @GetMapping("api/v0/shelflayers/{shelfLayerId}/facings")
    public ResponseEntity<List<FacingDto>> retrieveFacings(@PathVariable Integer shelfLayerId, HttpServletRequest request) {

        log.debug(String.format("FacingController retrieveFacings at %1$s called for shelfLayerId %2$s",
                request.getRequestURL(), shelfLayerId));

        validateShelfLayerId(shelfLayerId);

        return new ResponseEntity<>(facingService.readByShelfLayerId(shelfLayerId), HttpStatus.OK);
    }

    @Operation(
            summary = "Returns the Facing with the given id",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Facing successfully returned", content = @Content(schema = @Schema(implementation = FacingDto.class))),
                    @ApiResponse(responseCode = "404", description = "ShelfLayer with the given shelfLayerId does not exist"),
                    @ApiResponse(responseCode = "404", description = "Facing with the given FacingId does not exist"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    @GetMapping("api/v0/facings/{facingId}")
    public ResponseEntity<FacingDto> retrieveFacing(@PathVariable Integer facingId, HttpServletRequest request) {

        log.debug(String.format("FacingController retrieveFacing at %1$s called for FacingId %2$s",
                request.getRequestURL(), facingId));

        validateFacingId(facingId);

        return new ResponseEntity<>(facingService.read(facingId), HttpStatus.OK);
    }

    @Operation(
            summary = "Updates an existing Facing (only quantity and relative position) defined by the given facingId",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Facing successfully updated", content = @Content(schema = @Schema(implementation = FacingDto.class))),
                    @ApiResponse(responseCode = "400", description = "Request Body validation error"),
                    @ApiResponse(responseCode = "404", description = "Facing wth the given Id was not Found")
            }
    )
    @PutMapping("api/v0/facings/{facingId}")
    public ResponseEntity<FacingDto> updateFacing(@PathVariable Integer facingId,
                                                      @Valid @RequestBody FacingDto facingDto) {

        facingDto.setId(facingId);
        log.debug(String.format("Received a request to update the Facing with the Id %d with the given details %s",
                facingId, facingDto));

        validateFacingId(facingId);

        validateShelfLayerId(facingDto.getShelfLayerId());

        return new ResponseEntity<>(this.facingService.update(facingId, facingDto), HttpStatus.OK);

    }

    @SuppressWarnings("rawtypes")
    @Operation(
            summary = "Deletes the Facing with the given id",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Facing successfully deleted", content = @Content(schema = @Schema(implementation = FacingDto.class))),
                    @ApiResponse(responseCode = "404", description = "ShelfLayer with the given shelfLayerId does not exist"),
                    @ApiResponse(responseCode = "404", description = "Facing with the given FacingId does not exist"),
                    @ApiResponse(responseCode = "500", description = "Internal server error"),
            }
    )
    @DeleteMapping("api/v0/facings/{facingId}")
    public ResponseEntity deleteFacing(@PathVariable Integer facingId, HttpServletRequest request) {

        log.debug(String.format("FacingController deleteFacing at %1$s called for FacingId %2$s",
                request.getRequestURL(), facingId));

        validateFacingId(facingId);

        return new ResponseEntity<>(facingService.delete(facingId), HttpStatus.OK);
    }

    private void validateFacingId(@PathVariable Integer facingId) {

        if (!facingService.exists(facingId)) {
            log.warn("Facing with id {} does not exist", facingId);
            throw new FacingNotFoundException();
        }
    }

    private void validateShelfLayerId(Integer shelfLayerId) {

        if (!shelfLayerService.exists(shelfLayerId)) {

            log.warn("shelf layer with id {} does not exist", shelfLayerId);
            throw new ShelfLayerNotFoundException();
        }
    }
}
