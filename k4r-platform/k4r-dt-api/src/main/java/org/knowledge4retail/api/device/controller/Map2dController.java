package org.knowledge4retail.api.device.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.knowledge4retail.api.device.dto.Map2dDto;
import org.knowledge4retail.api.device.exception.MapNotFoundException;
import org.knowledge4retail.api.device.service.Map2dService;
import org.knowledge4retail.api.store.exception.StoreNotFoundException;
import org.knowledge4retail.api.store.service.StoreService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Slf4j
@RequiredArgsConstructor
@RestController
public class Map2dController{

    private final Map2dService map2dService;
    private final StoreService storeService;

    @Operation(
            summary = "Saves a two-dimensional map (PF-ROB-EN-Map2D) for a given Store ID. if a map for this store already exists, it will be updated.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Map successfully created or updated", content = @Content(schema = @Schema(implementation = Map2dDto.class))),
                    @ApiResponse(responseCode = "400", description = "Request Body validation error"),
                    @ApiResponse(responseCode = "500", description = "Internal server error"),
            }
    )
    @PostMapping("api/v0/stores/{storeId}/map2d")
    public ResponseEntity<Map2dDto> saveMap(@PathVariable("storeId") Integer storeId, @Valid @RequestBody Map2dDto map, HttpServletRequest request) {

        log.debug(String.format("Map2DController saveMap at %1$s called with storeId %2$d and payload %3$s"
                , request.getRequestURL()
                , storeId
                , map.toString()));

        map.setStoreId(storeId);
        validateStoreId(storeId);
        return new ResponseEntity<>(map2dService.create(storeId, map), HttpStatus.OK);
    }

    @Operation(
            summary = "Returns a two-dimensional map (PF-ROB-EN-Map2D) for a given Store ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Map successfully found and returned", content = @Content(schema = @Schema(implementation = Map2dDto.class))),
                    @ApiResponse(responseCode = "404", description = "Requested Map could not be found"),
                    @ApiResponse(responseCode = "500", description = "Internal server error"),
            }
    )
    @GetMapping("api/v0/stores/{storeId}/map2d")
    public ResponseEntity<Map2dDto> getMap(@PathVariable("storeId") Integer storeId, HttpServletRequest request) {

        log.debug(String.format("Map2DController getMap at %1s called with storeId %2$d"
                , request.getRequestURL()
                , storeId));
        validateStoreId(storeId);
        if(!map2dService.exists(storeId)){

            log.warn("Map2d for the store with id {} does not exist", storeId);
            throw new MapNotFoundException();
        }

        return new ResponseEntity<>(map2dService.readByStoreId(storeId), HttpStatus.OK);
    }

    private void validateStoreId(Integer storeId) {

        if (!storeService.exists(storeId)) {

            log.warn("store with id {} does not exist", storeId);
            throw new StoreNotFoundException();
        }
    }
}