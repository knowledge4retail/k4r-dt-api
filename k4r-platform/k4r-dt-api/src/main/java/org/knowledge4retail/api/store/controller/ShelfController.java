package org.knowledge4retail.api.store.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.knowledge4retail.api.product.exception.UnitNotFoundException;
import org.knowledge4retail.api.product.service.ProductGroupService;
import org.knowledge4retail.api.product.service.UnitService;
import org.knowledge4retail.api.store.dto.ShelfDto;
import org.knowledge4retail.api.store.exception.ProductGroupNotFoundException;
import org.knowledge4retail.api.store.exception.ShelfNotFoundException;
import org.knowledge4retail.api.store.exception.StoreNotFoundException;
import org.knowledge4retail.api.store.service.ShelfService;
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
public class ShelfController {

    private final ShelfService shelfService;
    private final StoreService storeService;
    private final UnitService unitService;
    private final ProductGroupService productGroupService;

    @Operation(
            summary = "Saves a given shelf for the given storeId. StoreId field in payload is ignored.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Shelf successfully created", content = @Content(schema = @Schema(implementation = ShelfDto.class))),
                    @ApiResponse(responseCode = "400", description = "Request Body validation error"),
                    @ApiResponse(responseCode = "500", description = "Internal server error"),
            }
    )
    @PostMapping("api/v0/stores/{storeId}/shelves")
    public ResponseEntity<ShelfDto> createShelf(@PathVariable("storeId") Integer storeId, @Valid @RequestBody ShelfDto shelf, HttpServletRequest request) {

        shelf.setStoreId(storeId);
        log.debug(String.format("ShelfController createShelf at %1$s called for storeId %2$s with payload %3$s",
                request.getRequestURL(), storeId, shelf));

        validateStoreId(storeId);

        if(shelf.getProductGroupId() != null) {

            validateProductGroupId(shelf.getProductGroupId());
        }

        validateUnitId(shelf.getLengthUnitId());

        return new ResponseEntity<>(shelfService.create(shelf), HttpStatus.OK);
    }

    @Operation(
            summary = "Returns all shelves for the given storeId. Filtered optionally by externalReferenceId",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Shelves successfully returned, if any", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ShelfDto.class)))),
                    @ApiResponse(responseCode = "404", description = "Store with the given storeId does not exist"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    @GetMapping("api/v0/stores/{storeId}/shelves")
    public ResponseEntity<List<ShelfDto>> retrieveShelves(@PathVariable("storeId") Integer storeId, @RequestParam(name = "externalReferenceId", required = false) String externalReferenceId, HttpServletRequest request) {

        log.debug(String.format("ShelfController retrieve Shelf at %1$s called for StoreId %2$s and optionally filtered by externalReferenceId %3$s",
                request.getRequestURL(), storeId, externalReferenceId));

        validateStoreId(storeId);

        if (externalReferenceId != null && !externalReferenceId.isEmpty()) {

            validateExternalReferenceId(externalReferenceId);

            return new ResponseEntity<>(shelfService.readByStoreIdAndExternalReferenceId(storeId, externalReferenceId), HttpStatus.OK);
        } else {

            return new ResponseEntity<>(shelfService.readByStoreId(storeId), HttpStatus.OK);
        }
    }

    @Operation(
            summary = "Returns the shelf with the given id",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Shelf successfully returned", content = @Content(schema = @Schema(implementation = ShelfDto.class))),
                    @ApiResponse(responseCode = "404", description = "Shelf with the given shelfId does not exist"),
                    @ApiResponse(responseCode = "500", description = "Internal server error"),
            }
    )
    @GetMapping("api/v0/shelves/{shelfId}")
    public ResponseEntity<ShelfDto> getShelf(@PathVariable("shelfId") Integer shelfId, HttpServletRequest request) {

        log.debug(String.format("ShelfController retrieveShelf at %1$s called for shelfId %2$s",
                request.getRequestURL(), shelfId));

        validateShelfId(shelfId);

        return new ResponseEntity<>(shelfService.read(shelfId), HttpStatus.OK);
    }

    @Operation(
            summary = "Updates the shelf with the given id. Id in Path is used, Id is body is ignored.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Shelf successfully updated", content = @Content(schema = @Schema(implementation = ShelfDto.class))),
                    @ApiResponse(responseCode = "404", description = "Shelf with the given shelfId does not exist"),
                    @ApiResponse(responseCode = "500", description = "Internal server error"),
            }
    )
    @PutMapping("api/v0/shelves/{shelfId}")
    public ResponseEntity<ShelfDto> updateShelf(@PathVariable("shelfId") Integer shelfId, @Valid @RequestBody ShelfDto shelf, HttpServletRequest request) {

        log.debug(String.format("ShelfController updateShelf at %1$s called for shelfId %2$s with payload %3$s",
                request.getRequestURL(), shelfId, shelf.toString()));

        shelf.setId(shelfId);
        validateShelfId(shelf.getId());

        if(shelf.getProductGroupId() != null) {

            validateProductGroupId(shelf.getProductGroupId());
        }

        validateStoreId(shelf.getStoreId());

        validateUnitId(shelf.getLengthUnitId());

        return new ResponseEntity<>(shelfService.update(shelf), HttpStatus.OK);
    }

    @Operation(
            summary = "Deletes the shelf with the given id",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Shelf successfully deleted", content = @Content(schema = @Schema(implementation = ShelfDto.class))),
                    @ApiResponse(responseCode = "404", description = "Shelf with the given shelfId does not exist"),
                    @ApiResponse(responseCode = "500", description = "Internal server error"),
            }
    )
    @DeleteMapping("api/v0/shelves/{shelfId}")
    public ResponseEntity deleteShelf(@PathVariable("shelfId") Integer shelfId, HttpServletRequest request) {

        log.debug(String.format("ShelfController deleteShelf at %1$s called for shelfId %2$s",
                request.getRequestURL(), shelfId));

        validateShelfId(shelfId);

        shelfService.delete(shelfId);

        return new ResponseEntity(HttpStatus.OK);
    }

    private void validateStoreId(Integer storeId) {

        if (!storeService.exists(storeId)) {

            log.warn("store with id {} does not exist", storeId);
            throw new StoreNotFoundException();
        }
    }

    private void validateUnitId(Integer unitId) {

        if (!unitService.exists(unitId)) {

            log.warn("unit with id {} does not exist", unitId);
            throw new UnitNotFoundException();
        }
    }

    private void validateProductGroupId(Integer productGroupId) {

        if (productGroupId == null) {
            //we DO want to allow shelves without product groups.
            return;
        }
        if (!productGroupService.exists(productGroupId)) {

            log.warn("product group with id {} does not exist", productGroupId);
            throw new ProductGroupNotFoundException();
        }
    }

    private void validateShelfId(Integer shelfId) {

        if (!shelfService.exists(shelfId)) {

            log.warn("shelf with id {} does not exist", shelfId);
            throw new ShelfNotFoundException();
        }
    }

    private void validateExternalReferenceId(String externalReferenceId) {

        if (!shelfService.existsByExternalReferenceId(externalReferenceId)) {

            log.warn("shelf with externalReferenceId {} does not exist", externalReferenceId);
            throw new ShelfNotFoundException();
        }
    }
}
