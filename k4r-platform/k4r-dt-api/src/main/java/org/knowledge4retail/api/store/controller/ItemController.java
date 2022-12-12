package org.knowledge4retail.api.store.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.knowledge4retail.api.product.exception.ProductUnitNotFoundException;
import org.knowledge4retail.api.product.service.ProductUnitService;
import org.knowledge4retail.api.store.dto.ItemDto;
import org.knowledge4retail.api.store.exception.FacingNotFoundException;
import org.knowledge4retail.api.store.exception.ItemNotFoundException;
import org.knowledge4retail.api.store.service.FacingService;
import org.knowledge4retail.api.store.service.ItemService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
public class ItemController {

    private final ItemService itemService;
    private final FacingService facingService;
    private final ProductUnitService productUnitService;

    @Operation(
            summary = "Get all items",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Items successfully returned", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ItemDto.class)))),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    @GetMapping("api/v0/items")
    public ResponseEntity<List<ItemDto>> getAllItems(HttpServletRequest request) {

        log.debug(String.format("ItemController getAllItems at %1$s called",
                request.getRequestURL()));
        return new ResponseEntity<>(itemService.readAll(), HttpStatus.OK);
    }

    @Operation(
            summary = "Returns item defined with the id",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Item with the given Id was successfully returned", content = @Content(schema = @Schema(implementation = ItemDto.class))),
                    @ApiResponse(responseCode = "404", description = "Item with the given Id was not found")
            }
    )
    @GetMapping("api/v0/items/{itemId}")
    public ResponseEntity<ItemDto> getItem(@PathVariable("itemId") Integer id) throws ItemNotFoundException {

        log.debug(String.format("Received a request to retrieve the item with the Id %d", id));
        validateItemId(id);
        return new ResponseEntity<>(itemService.read(id), HttpStatus.OK);
    }

    @Operation(
            summary = "Create a new item",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Item successfully created", content = @Content(schema = @Schema(implementation = ItemDto.class))),
                    @ApiResponse(responseCode = "400", description = "Request Body validation error"),
                    @ApiResponse(responseCode = "404", description = "Facing wth the given Id was not Found"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    @PostMapping("/api/v0/items")
    public ResponseEntity<ItemDto> createItem(@Valid @RequestBody ItemDto itemDto, HttpServletRequest request) {

        log.debug(String.format("ItemController createItem at %1$s called with payload %2$s",
                request.getRequestURL(), itemDto.toString()));
        if(itemDto.getFacingId() != null) {

            validateFacingId(itemDto.getFacingId());
        }
        if(itemDto.getProductUnitId() != null) {

            validateProductUnitId(itemDto.getProductUnitId());
        }
        return new ResponseEntity<>(itemService.create(itemDto), HttpStatus.OK);
    }

    @Operation(
            summary = "Updates an existing item defined by a given Id",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Item successfully updated", content = @Content(schema = @Schema(implementation = ItemDto.class))),
                    @ApiResponse(responseCode = "400", description = "Request Body validation error"),
                    @ApiResponse(responseCode = "404", description = "Item wth the given Id was not Found")
            }
    )
    @PutMapping("api/v0/items/{itemId}")
    public ResponseEntity<ItemDto> updateItem(@PathVariable("itemId") Integer id, @Valid @RequestBody ItemDto itemDto) throws ItemNotFoundException {

        itemDto.setId(id);
        log.debug(String.format("Received a request to update the item with the Id %d with the given details %s", id, itemDto));
        validateItemId(id);

        if(itemDto.getFacingId() != null) {

            validateFacingId(itemDto.getFacingId());
        }
        return new ResponseEntity<>(itemService.update(id, itemDto), HttpStatus.OK);
    }

    @Operation(
            summary = "Delete a item",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Item successfully deleted", content = @Content(schema = @Schema(implementation = ItemDto.class))),
                    @ApiResponse(responseCode = "404", description = "Item with given Id does not exist"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    @DeleteMapping("/api/v0/items/{itemId}")
    public ResponseEntity deleteItem(@PathVariable("itemId") Integer id, HttpServletRequest request) {

        log.debug(String.format("ItemController deleteItem at %1$s called",
                request.getRequestURL()));
        validateItemId(id);
        itemService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    private void validateItemId(Integer id) {

        if(!itemService.exists(id)) {

            log.warn(String.format("Item with Id %d was not found", id));
            throw new ItemNotFoundException();
        }
    }


    private void validateFacingId(Integer id) {

        if(!facingService.exists(id)) {

            log.warn(String.format("Facing with Id %d was not found", id));
            throw new FacingNotFoundException();
        }
    }

    private void validateProductUnitId(Integer id) {

        if(!productUnitService.exists(id)) {

            log.warn(String.format("ProductUnit with Id %d was not found", id));
            throw new ProductUnitNotFoundException();
        }
    }
}
