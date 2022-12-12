package org.knowledge4retail.api.store.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.knowledge4retail.api.store.dto.TrolleyRouteDto;
import org.knowledge4retail.api.store.exception.ShelfNotFoundException;
import org.knowledge4retail.api.store.exception.TrolleyNotFoundException;
import org.knowledge4retail.api.store.exception.TrolleyRouteNotFoundException;
import org.knowledge4retail.api.store.service.ShelfService;
import org.knowledge4retail.api.store.service.TrolleyRouteService;
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
public class TrolleyRouteController {

    private final TrolleyRouteService trolleyRouteService;
    private final TrolleyService trolleyService;
    private final ShelfService shelfService;

    @Operation(
            summary = "Get all trolley routes for the given trolleyId",
            responses = {
                    @ApiResponse(responseCode = "200", description = "TrolleyRoutes successfully returned", content = @Content(array = @ArraySchema(schema = @Schema(implementation = TrolleyRouteDto.class)))),
                    @ApiResponse(responseCode = "404", description = "Trolley with the given trolleyId does not exist"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    @GetMapping("api/v0/trolleys/{trolleyId}/trolleyroutes")
    public ResponseEntity<List<TrolleyRouteDto>> getAllTrolleyRoutesOfOneTrolley(@PathVariable(name = "trolleyId") Integer trolleyId, HttpServletRequest request) {

        validateTrolleyId(trolleyId);

        log.debug(String.format("TrolleyRouteController getAllTrolleyRoutesOfOneTrolley at %1$s called for trolleyId %2$s",
                request.getRequestURL(), trolleyId));
        return new ResponseEntity<>(trolleyRouteService.readByTrolleyId(trolleyId), HttpStatus.OK);
    }

    @Operation(
            summary = "Create a new trolley route",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Trolley route successfully created", content = @Content(schema = @Schema(implementation = TrolleyRouteDto.class))),
                    @ApiResponse(responseCode = "400", description = "Request Body validation error"),
                    @ApiResponse(responseCode = "404", description = "Store with the given storeId does not exist"),
                    @ApiResponse(responseCode = "500", description = "Internal server error"),
            }
    )
    @PostMapping("/api/v0/trolleys/{trolleyId}/trolleyroutes")
    public ResponseEntity<TrolleyRouteDto> createTrolleyRoute(@Valid @RequestBody TrolleyRouteDto trolleyRouteDto, @PathVariable(name = "trolleyId") Integer trolleyId, HttpServletRequest request) {

        trolleyRouteDto.setTrolleyId(trolleyId);
        validateTrolleyId(trolleyId);
        validateShelfId(trolleyRouteDto.getShelfId());

        log.debug(String.format("TrolleyRouteController createTrolleyRoute at %1$s called with payload %2$s with trolleyId %3$s",
                request.getRequestURL(), trolleyRouteDto, trolleyId));
        return new ResponseEntity<>(trolleyRouteService.create(trolleyRouteDto), HttpStatus.OK);
    }

    @Operation(
            summary = "Delete a trolley route",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Trolley route successfully deleted", content = @Content(schema = @Schema(implementation = TrolleyRouteDto.class))),
                    @ApiResponse(responseCode = "404", description = "Trolley route with given Id does not exist"),
                    @ApiResponse(responseCode = "500", description = "Internal server error"),
            }
    )
    @DeleteMapping("/api/v0/trolleyroutes/{trolleyRouteId}")
    public ResponseEntity deleteTrolleyRoute(@PathVariable("trolleyRouteId") Integer id, HttpServletRequest request) {

        log.debug(String.format("TrolleyRouteController delete TrolleyRoute at %1$s called",
                request.getRequestURL()));
        validateTrolleyRouteId(id);
        trolleyRouteService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private void validateTrolleyRouteId(Integer trolleyRouteId) {

        if (!trolleyRouteService.exists(trolleyRouteId)) {

            log.warn("trolley route with id {} does not exist", trolleyRouteId);
            throw new TrolleyRouteNotFoundException();
        }
    }

    private void validateTrolleyId(Integer id) {

        if(!trolleyService.exists(id)){

            log.warn("TrolleyId does not exist: {}", id);
            throw new TrolleyNotFoundException();
        }
    }

    private void validateShelfId(Integer id) {

        if(!shelfService.exists(id)){

            log.warn("shelfId does not exist: {}", id);
            throw new ShelfNotFoundException();
        }
    }
}
