package org.knowledge4retail.api.product.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.knowledge4retail.api.product.dto.ProductGtinDto;
import org.knowledge4retail.api.product.exception.ProductGtinNotFoundException;
import org.knowledge4retail.api.product.exception.ProductUnitNotFoundException;
import org.knowledge4retail.api.product.service.ProductGtinService;
import org.knowledge4retail.api.product.service.ProductUnitService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
public class ProductGtinController {

    private final ProductGtinService productGtinService;
    private final ProductUnitService productUnitService;


    @Operation(
            summary = "get all product gtin",
            responses = {
                    @ApiResponse(responseCode = "200", description = "product gtins successfully returned", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ProductGtinDto.class)))),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    @GetMapping("/api/v0/productgtins")
    public ResponseEntity<List<ProductGtinDto>> getAllProductGtins(HttpServletRequest request) {

        log.debug(String.format("ProductGtinController getAllProductgtins at %1$s called",
                request.getRequestURL()));
        return new ResponseEntity<>(productGtinService.readAll(), HttpStatus.OK);
    }

    @Operation(
            summary = "Returns product gtin defined with the id",
            responses = {
                    @ApiResponse(responseCode = "200", description = "ProductGtin with the given Id was successfully returned", content = @Content(schema = @Schema(implementation = ProductGtinDto.class))),
                    @ApiResponse(responseCode = "404", description = "ProductGtin with the given Id was not found")
            }
    )
    @GetMapping("api/v0/productgtins/{productGtinId}")
    public ResponseEntity<ProductGtinDto> getProductGtin(@PathVariable("productGtinId") Integer id) throws ProductGtinNotFoundException {

        log.debug(String.format("Received a request to retrieve the productGtin with the Id %1$s", id));
        validateProductGtinId(id);
        return new ResponseEntity<>(productGtinService.read(id), HttpStatus.OK);
    }

    @Operation(
            summary = "create a new product gtin",
            responses = {
                    @ApiResponse(responseCode = "200", description = "product gtin successfully created", content = @Content(schema = @Schema(implementation = ProductGtinDto.class))),
                    @ApiResponse(responseCode = "400", description = "Request Body validation error"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    @PostMapping("/api/v0/productgtins")
    public ResponseEntity<ProductGtinDto> createProductGtin(@Valid @RequestBody ProductGtinDto productGtinDto, HttpServletRequest request) {

        log.debug(String.format("ProductGtinController createProductGtin at %1$s called with payload %2$s",
                request.getRequestURL(), productGtinDto.toString()));
        if(productGtinDto.getProductUnitId() != null){

            validateProductUnitId(productGtinDto.getProductUnitId());
        }
        return new ResponseEntity<>(productGtinService.create(productGtinDto), HttpStatus.OK);
    }

    @Operation(
            summary = "Updates an product gtin defined by a given Id",
            responses = {
                    @ApiResponse(responseCode = "200", description = "ProductGtin successfully updated", content = @Content(schema = @Schema(implementation = ProductGtinDto.class))),
                    @ApiResponse(responseCode = "400", description = "Request Body validation error"),
                    @ApiResponse(responseCode = "404", description = "ProductGtin wth the given Id was not Found")
            }
    )
    @PutMapping("api/v0/productgtins/{productGtinId}")
    public ResponseEntity<ProductGtinDto> updateProductGtin(@PathVariable("productGtinId") Integer id, @Valid @RequestBody ProductGtinDto productGtinDto) throws ProductGtinNotFoundException {

        productGtinDto.setId(id);
        log.debug(String.format("Received a request to update the product gtin with the Id %1$s with the given details %2$s", id, productGtinDto));
        validateProductGtinId(id);
        if(productGtinDto.getProductUnitId() != null){

            validateProductUnitId(productGtinDto.getProductUnitId());
        }
        return new ResponseEntity<>(productGtinService.update(id, productGtinDto), HttpStatus.OK);
    }

    @Operation(
            summary = "Delete a product gtin",
            responses = {
                    @ApiResponse(responseCode = "200", description = "product gtin successfully deleted", content = @Content(schema = @Schema(implementation = ProductGtinDto.class))),
                    @ApiResponse(responseCode = "404", description = "product gtin with given Id does not exist"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    @DeleteMapping("/api/v0/productgtins/{productGtinId}")
    public ResponseEntity deleteProductGtin(@PathVariable("productGtinId") Integer id, HttpServletRequest request) {

        log.debug(String.format("ProductGtinController deleteProductGtin at %1$s called",
                request.getRequestURL()));
        validateProductGtinId(id);
        productGtinService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private void validateProductGtinId(Integer id) {

        if(!productGtinService.exists(id)){

            log.warn(String.format("ProductGtin with Id %d was not found", id));
            throw new ProductGtinNotFoundException();
        }
    }


    private void validateProductUnitId(Integer id) {

        if(!productUnitService.exists(id)){

            log.warn(String.format("ProductUnit with Id %d was not found", id));
            throw new ProductUnitNotFoundException();
        }
    }
}
