package org.knowledge4retail.api.store.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.knowledge4retail.api.store.dto.StoreCharacteristicDto;
import org.knowledge4retail.api.store.exception.StoreCharacteristicNotFoundException;
import org.knowledge4retail.api.store.service.StoreCharacteristicService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
public class StoreCharacteristicController {

    private final StoreCharacteristicService storeCharacteristicService;

    @Operation(
            summary = "Creates a new store characteristic",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Store Characteristics successfully created", content = @Content(schema = @Schema(implementation = StoreCharacteristicDto.class))),
                    @ApiResponse(responseCode = "400", description = "Request Body validation error"),
                    @ApiResponse(responseCode = "500", description = "Internal server error"),
            }
    )
    @PostMapping("/api/v0/storecharacteristics")
    public ResponseEntity<StoreCharacteristicDto> createStoreCharacteristic(@Valid @RequestBody StoreCharacteristicDto storeCharacteristicDto, HttpServletRequest request){

        log.debug(String.format("StoreCharacteristicController createCharacteristic at %1$s called with payload %2$s",
                request.getRequestURL(), storeCharacteristicDto.toString()));
        return new ResponseEntity<>(storeCharacteristicService.create(storeCharacteristicDto), HttpStatus.OK);
    }

    @Operation(
            summary = "Returns all store characteristics",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Store Characteristics successfully returned", content = @Content(array = @ArraySchema(schema = @Schema(implementation = StoreCharacteristicDto.class)))),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    @GetMapping("/api/v0/storecharacteristics")
    public ResponseEntity<List<StoreCharacteristicDto>> getAllStoreCharacteristics(HttpServletRequest request){

        log.debug(String.format("StoreCharacteristicController getAllCharacteristics at %1$s called",
                request.getRequestURL()));
        return new ResponseEntity<>(storeCharacteristicService.readAll(), HttpStatus.OK);
    }

    @Operation(
            summary = "Deletes a store characteristic",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Store characteristic successfully deleted", content = @Content(schema = @Schema(implementation = StoreCharacteristicDto.class))),
                    @ApiResponse(responseCode = "404", description = "Store characteristic with id does not exist"),
                    @ApiResponse(responseCode = "500", description = "Internal server error"),
            }
    )
    @DeleteMapping("/api/v0/storecharacteristics/{storecharacteristicId}")
    public ResponseEntity deleteStoreCharacteristic(@PathVariable("storecharacteristicId") Integer id, HttpServletRequest request){

        log.debug(String.format("StoreCharacteristicController deleteCharacteristic at %1$s called",
                request.getRequestURL()));
        validatestoreCharacteristicId(id);
        storeCharacteristicService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private void validatestoreCharacteristicId(Integer id) {

        if (!storeCharacteristicService.exists(id)){

            log.warn("characteristicId does not exist: {}", id);
            throw new StoreCharacteristicNotFoundException();
        }
    }
}
