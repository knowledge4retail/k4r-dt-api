package org.knowledge4retail.api.store.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.knowledge4retail.api.store.dto.TrolleyDto;
import org.knowledge4retail.api.store.exception.StoreNotFoundException;
import org.knowledge4retail.api.store.exception.TrolleyNotFoundException;
import org.knowledge4retail.api.store.service.StoreService;
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
public class TrolleyController {

    private final TrolleyService trolleyService;
    private final StoreService storeService;

    @Operation(
            summary = "Get all trolleys for the given storeId",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Trolleys successfully returned", content = @Content(array = @ArraySchema(schema = @Schema(implementation = TrolleyDto.class)))),
                    @ApiResponse(responseCode = "404", description = "Store with the given storeId does not exist"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    @GetMapping("api/v0/stores/{storeId}/trolleys")
    public ResponseEntity<List<TrolleyDto>> getAllTrolleysOfOneStore(@PathVariable(name = "storeId") Integer storeId, HttpServletRequest request) {

        validateStoreId(storeId);

        log.debug(String.format("TrolleyController getAllTrolleys at %1$s called for storeId %2$s",
                request.getRequestURL(), storeId));
        return new ResponseEntity<>(trolleyService.readByStoreId(storeId), HttpStatus.OK);
    }

    @Operation(
            summary = "Create a new trolley",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Trolley successfully created", content = @Content(schema = @Schema(implementation = TrolleyDto.class))),
                    @ApiResponse(responseCode = "400", description = "Request Body validation error"),
                    @ApiResponse(responseCode = "404", description = "Store with the given storeId does not exist"),
                    @ApiResponse(responseCode = "500", description = "Internal server error"),
            }
    )
    @PostMapping("/api/v0/stores/{storeId}/trolleys")
    public ResponseEntity<TrolleyDto> createTrolley(@Valid @RequestBody TrolleyDto trolleyDto, @PathVariable(name = "storeId") Integer storeId, HttpServletRequest request) {

        trolleyDto.setStoreId(storeId);
        validateStoreId(storeId);

        log.debug(String.format("TrolleyController createTrolley at %1$s called with payload %2$s with storeId %3$s",
                request.getRequestURL(), trolleyDto, storeId));
        return new ResponseEntity<>(trolleyService.create(trolleyDto), HttpStatus.OK);
    }

    @Operation(
            summary = "Delete a trolley",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Trolley successfully deleted", content = @Content(schema = @Schema(implementation = TrolleyDto.class))),
                    @ApiResponse(responseCode = "404", description = "Trolley with given Id does not exist"),
                    @ApiResponse(responseCode = "500", description = "Internal server error"),
            }
    )
    @DeleteMapping("/api/v0/trolleys/{trolleyId}")
    public ResponseEntity deleteTrolley(@PathVariable("trolleyId") Integer id, HttpServletRequest request) {

        log.debug(String.format("TrolleyController deleteTrolley at %1$s called",
                request.getRequestURL()));
        validateTrolleyId(id);
        trolleyService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private void validateStoreId(Integer storeId) {

        if (!storeService.exists(storeId)) {

            log.warn("store with id {} does not exist", storeId);
            throw new StoreNotFoundException();
        }
    }

    private void validateTrolleyId(Integer id) {

        if(!trolleyService.exists(id)){

            log.warn("TrolleyId does not exist: {}", id);
            throw new TrolleyNotFoundException();
        }
    }
}
