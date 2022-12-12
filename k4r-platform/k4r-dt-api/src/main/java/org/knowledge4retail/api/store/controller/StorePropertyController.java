package org.knowledge4retail.api.store.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.knowledge4retail.api.store.dto.StorePropertyDto;
import org.knowledge4retail.api.store.exception.StoreCharacteristicNotFoundException;
import org.knowledge4retail.api.store.exception.StoreNotFoundException;
import org.knowledge4retail.api.store.exception.StorePropertyAlreadyExistsException;
import org.knowledge4retail.api.store.exception.StorePropertyNotFoundException;
import org.knowledge4retail.api.store.service.StoreCharacteristicService;
import org.knowledge4retail.api.store.service.StorePropertyService;
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
public class StorePropertyController {

    private final StorePropertyService storePropertyService;
    private final StoreService storeService;
    private final StoreCharacteristicService storeCharacteristicService;

    @Operation(
            summary = "Returns all store properties for the given storeId",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Store Properties successfully returned", content = @Content(array = @ArraySchema(schema = @Schema(implementation = StorePropertyDto.class)))),
                    @ApiResponse(responseCode = "404", description = "store with the given storeId does not exist"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    @GetMapping("api/v0/stores/{storeId}/storeproperties")
    public ResponseEntity<List<StorePropertyDto>> getAllPropertiesForStore(@PathVariable("storeId") Integer storeId,
                                                                               HttpServletRequest request){
        log.debug(String.format("StorePropertyController getAllProperties at %1$s called for storeId %2$s",
                request.getRequestURL(), storeId));
        validateStoreId(storeId);
        return new ResponseEntity<>(storePropertyService.readByStoreId(storeId), HttpStatus.OK);
    }

    @Operation(
            summary = "Saves a given store property for the given storeId and characteristicId. Id fields in payload are ignored",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Store property successfully created", content = @Content(schema = @Schema(implementation = StorePropertyDto.class))),
                    @ApiResponse(responseCode = "400", description = "Store property for given store and characteristicId already exists"),
                    @ApiResponse(responseCode = "400", description = "Request Body validation error"),
                    @ApiResponse(responseCode = "500", description = "Internal server error"),
            }
    )
    @PostMapping("api/v0/stores/{storeId}/storeproperties/{storeCharacteristicId}")
    public ResponseEntity<StorePropertyDto> saveStoreProperty(@Valid @RequestBody StorePropertyDto storePropertyDto,
                                                              @PathVariable("storeId") Integer storeId,
                                                              @PathVariable("storeCharacteristicId") Integer characteristicId,
                                                              HttpServletRequest request) {

        storePropertyDto.setCharacteristicId(characteristicId);
        storePropertyDto.setStoreId(storeId);

        log.debug(String.format("StorePropertyController saveProperty at %1$s called for storeId %2$s \" +\n" +
                        "                        \" and characteristicId %3$s and payload %4$s",
                request.getRequestURL(),
                storeId, characteristicId, storePropertyDto));

        validateStoreId(storeId);
        validateCharacteristicId(characteristicId);
        checkIfStorePropertyAlreadyExist(storeId, characteristicId);

        return new ResponseEntity<>(storePropertyService.create(storePropertyDto), HttpStatus.OK);

    }

    @Operation(
            summary = "Deletes a store property for the given storeID and characteristicId.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Store property successfully deleted", content = @Content(schema = @Schema(implementation = StorePropertyDto.class))),
                    @ApiResponse(responseCode = "404", description = "Store property for given store and characteristicId does not exist"),
                    @ApiResponse(responseCode = "500", description = "Internal server error"),
            }
    )
    @DeleteMapping("api/v0/stores/{storeId}/storeproperties/{storeCharacteristicId}")
    public ResponseEntity deleteStoreProperty(@PathVariable("storeId") Integer storeId,
                                                      @PathVariable("storeCharacteristicId") Integer characteristicId,
                                                      HttpServletRequest request) {

        log.debug(String.format("StorePropertyController deleteProperty at %1$s called for storeId %2$s and characteristicId %3$s"
                , request.getRequestURL(), storeId, characteristicId));
        checkIfStorePropertyExist(storeId, characteristicId);
        storePropertyService.delete(storeId, characteristicId);
        return new ResponseEntity(HttpStatus.OK);
    }

    private void validateStoreId(Integer storeId) {

        if (!storeService.exists(storeId)){

            log.warn("store with id {} does not exist", storeId);
            throw new StoreNotFoundException();
        }
    }

    private void validateCharacteristicId(Integer id) {

        if (!storeCharacteristicService.exists(id)){

            log.warn("CharacteristicId with id {} does not exist", id);
            throw new StoreCharacteristicNotFoundException();
        }
    }

    private void checkIfStorePropertyExist(Integer storeId, Integer characteristicId) {

        if ( !storePropertyService.exists(storeId, characteristicId)){

            log.warn("property for storeId {} and characteristicId {} does not exist", storeId, characteristicId );
            throw new StorePropertyNotFoundException();
        }
    }

    private void checkIfStorePropertyAlreadyExist(Integer storeId, Integer characteristicId) {

        if ( storePropertyService.exists(storeId, characteristicId)){

            log.warn("property for storeId {} and characteristicId {} already exist", storeId, characteristicId );
            throw new StorePropertyAlreadyExistsException();
        }
    }
}
