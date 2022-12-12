package org.knowledge4retail.api.store.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.knowledge4retail.api.store.dto.StoreObjectDto;
import org.knowledge4retail.api.store.exception.StoreNotFoundException;
import org.knowledge4retail.api.store.exception.StoreObjectNotFoundException;
import org.knowledge4retail.api.store.service.StoreObjectService;
import org.knowledge4retail.api.store.service.StoreService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
public class StoreObjectController {

    private final StoreObjectService storeObjectService;

    private final StoreService storeService;

    public StoreObjectController(StoreObjectService storeObjectService, StoreService storeService) {

        this.storeObjectService = storeObjectService;
        this.storeService = storeService;
    }

    @Operation(
            summary = "Saves a given store object for the given storeId. StoreId field in payload is ignored.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "StoreObject successfully created", content = @Content(schema = @Schema(implementation = StoreObjectDto.class))),
                    @ApiResponse(responseCode = "400", description = "Request Body validation error"),
                    @ApiResponse(responseCode = "500", description = "Internal server error"),
            }
    )
    @PostMapping("api/v0/stores/{storeId}/storeobjects")
    public ResponseEntity<StoreObjectDto> createStoreObject(@PathVariable("storeId") Integer storeId, @Valid @RequestBody StoreObjectDto storeObject, HttpServletRequest request) {

        storeObject.setStoreId(storeId);
        log.debug(String.format("StoreObjectController createStoreObject at %1$s called for storeId %2$s with payload %3$s",
                request.getRequestURL(), storeId, storeObject));

        validateStoreId(storeId);

        return new ResponseEntity<>(storeObjectService.create(storeObject), HttpStatus.OK);
    }

    @Operation(
            summary = "Returns all store objects for the given storeId. Filters by type (cashzone, warehousentrance, obstacle etc.) if required.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Store objects successfully returned, if any", content = @Content(array = @ArraySchema(schema = @Schema(implementation = StoreObjectDto.class)))),
                    @ApiResponse(responseCode = "404", description = "Store with the given storeId does not exist"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    @GetMapping("api/v0/stores/{storeId}/storeobjects")
    public ResponseEntity<List<StoreObjectDto>> retrieveStoreObjectsByType(@PathVariable("storeId") Integer storeId, @RequestParam(value = "type", required = false) String type, HttpServletRequest request) {

        log.debug(String.format("StoreObjectController retrieveStoreObjectsByType at %1$s called for storeId %2$s and type %3$s",
                request.getRequestURL(), storeId, type));

        validateStoreId(storeId);

        return new ResponseEntity<>(storeObjectService.readByStoreIdAndType(storeId, type), HttpStatus.OK);
    }

    @Operation(
            summary = "Deletes the store object with the given id",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Store object successfully deleted", content = @Content(schema = @Schema(implementation = StoreObjectDto.class))),
                    @ApiResponse(responseCode = "404", description = "Store object with the given storeObjectId does not exist"),
                    @ApiResponse(responseCode = "500", description = "Internal server error"),
            }
    )
    @DeleteMapping("api/v0/storeobjects/{storeObjectId}")
    public ResponseEntity deleteStoreObject(@PathVariable("storeObjectId") Integer storeObjectId, HttpServletRequest request) {

        log.debug(String.format("ShelfController deleteShelf at %1$s called for shelfId %2$s",
                request.getRequestURL(), storeObjectId));

        validateStoreObjectId(storeObjectId);

        storeObjectService.delete(storeObjectId);

        return new ResponseEntity(HttpStatus.OK);
    }

    private void validateStoreId(Integer storeId) {

        if (!storeService.exists(storeId)) {

            log.warn("store with id {} does not exist", storeId);
            throw new StoreNotFoundException();
        }
    }

    private void validateStoreObjectId(Integer storeObjectId) {

        if (!storeObjectService.exists(storeObjectId)) {

            log.warn("store object with id {} does not exist", storeObjectId);
            throw new StoreObjectNotFoundException();
        }
    }

}
