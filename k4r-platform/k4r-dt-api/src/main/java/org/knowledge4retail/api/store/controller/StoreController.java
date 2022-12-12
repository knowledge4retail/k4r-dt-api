package org.knowledge4retail.api.store.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.knowledge4retail.api.store.dto.StoreAggregateDto;
import org.knowledge4retail.api.store.dto.StoreDto;
import org.knowledge4retail.api.store.exception.StoreNotFoundException;
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
public class StoreController {

    private final StoreService storeService;

    @Operation(
            summary = "Saves a given store. ID field is ignored, a fresh ID gets assigned during save.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Store successfully created", content = @Content(schema = @Schema(implementation = StoreDto.class))),
                    @ApiResponse(responseCode = "400", description = "Request Body validation error"),
                    @ApiResponse(responseCode = "500", description = "Internal server error"),
            }
    )
    @PostMapping("api/v0/stores")
    public ResponseEntity<StoreDto> saveStore(@Valid @RequestBody StoreDto store, HttpServletRequest request) {

        log.debug(String.format("StoreController saveStore at %1$s called with payload %2$s"
                , request.getRequestURL()
                , store.toString()));

        return new ResponseEntity<>(storeService.create(store), HttpStatus.OK);

    }

    @Operation(
            summary = "Returns all stores.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Stores successfully returned", content = @Content(array = @ArraySchema(schema = @Schema(implementation = StoreDto.class)))),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    @GetMapping("api/v0/stores")
    public ResponseEntity<List<StoreDto>> getAllStores(HttpServletRequest request) {

        log.debug(String.format("StoreController getAllStores at %1$s called"
                , request.getRequestURL()));

        return new ResponseEntity<>(storeService.readAll(), HttpStatus.OK);
    }

    @Operation(
            summary = "Returns all stores and selected object counts.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Stores successfully returned", content = @Content(array = @ArraySchema(schema = @Schema(implementation = StoreAggregateDto.class)))),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    @GetMapping("api/v0/stores/aggregates")
    public ResponseEntity<List<StoreAggregateDto>> getAllStoreAggregates(HttpServletRequest request) {

        log.debug(String.format("StoreController getAllStoreAggregates at %1$s called"
                , request.getRequestURL()));

        return new ResponseEntity<>(storeService.readAllAggregates(), HttpStatus.OK);
    }

    @Operation(
            summary = "Returns store with ID given in path.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Store successfully returned", content = @Content(schema = @Schema(implementation = StoreDto.class))),
                    @ApiResponse(responseCode = "404", description = "Store with given Id does not exist"),
                    @ApiResponse(responseCode = "500", description = "Internal server error"),
            }
    )
    @GetMapping("api/v0/stores/{storeId}")
    public ResponseEntity<StoreDto> getStoreById(@PathVariable("storeId") Integer storeId, HttpServletRequest request) {

        log.debug(String.format("StoreController getStoreById at %1$s called with storeId %2$d"
                , request.getRequestURL()
                , storeId));

        failIfNotExists(storeId);
        return new ResponseEntity<>(storeService.read(storeId), HttpStatus.OK);
    }

    @Operation(
            summary = "Updates store with given ID. Returns the updated object. Always uses ID in path for reference, ID in body is discarded.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Store successfully updated", content = @Content(schema = @Schema(implementation = StoreDto.class))),
                    @ApiResponse(responseCode = "404", description = "Store with given Id does not exist"),
                    @ApiResponse(responseCode = "500", description = "Internal server error"),
            }
    )
    @PutMapping("api/v0/stores/{storeId}")
    public ResponseEntity<StoreDto> updateStore(@PathVariable("storeId") Integer storeId, @Valid @RequestBody StoreDto store, HttpServletRequest request) {

        store.setId(storeId);
        log.debug(String.format("StoreController updateStore at %1$s called with storeId %2$d and payload %3$s"
                , request.getRequestURL()
                , storeId
                , store));

        failIfNotExists(storeId);
        return new ResponseEntity<>(storeService.update(storeId, store), HttpStatus.OK);
    }

    @Operation(
            summary = "Deletes store with given ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Store successfully deleted", content = @Content(schema = @Schema(implementation = StoreDto.class))),
                    @ApiResponse(responseCode = "404", description = "Store with given Id does not exist"),
                    @ApiResponse(responseCode = "500", description = "Internal server error"),
            }
    )
    @DeleteMapping("api/v0/stores/{storeId}")
    public ResponseEntity deleteStoreById(@PathVariable("storeId") Integer storeId, HttpServletRequest request) {

        log.debug(String.format("StoreController deleteStoreById at %1$s called with storeId %2$d"
                , request.getRequestURL()
                , storeId));

        failIfNotExists(storeId);
        storeService.delete(storeId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private void failIfNotExists(Integer storeId) throws StoreNotFoundException{

        if (!storeService.exists(storeId)) {
            log.warn("StoreId does not exist: {}", storeId);
            throw new StoreNotFoundException();
        }
    }
}
