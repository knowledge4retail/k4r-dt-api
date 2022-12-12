package org.knowledge4retail.api.product.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.knowledge4retail.api.product.dto.ProductDescriptionDto;
import org.knowledge4retail.api.product.exception.ProductDescriptionNotFoundException;
import org.knowledge4retail.api.product.exception.ProductNotFoundException;
import org.knowledge4retail.api.product.service.ProductDescriptionService;
import org.knowledge4retail.api.product.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
public class ProductDescriptionController {

    private final ProductDescriptionService productDescriptionService;
    private final ProductService productService;

    @Operation(
            summary = "get all product description",
            responses = {
                    @ApiResponse(responseCode = "200", description = "product descriptions successfully returned", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ProductDescriptionDto.class)))),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    @GetMapping("/api/v0/productdescriptions")
    public ResponseEntity<List<ProductDescriptionDto>> getAllProductDescriptions(HttpServletRequest request) {

        log.debug(String.format("ProductDescriptionController getAllProductDescriptions at %1$s called",
                request.getRequestURL()));
        return new ResponseEntity<>(productDescriptionService.readAll(), HttpStatus.OK);
    }

    @Operation(
            summary = "Returns product description defined with the id",
            responses = {
                    @ApiResponse(responseCode = "200", description = "ProductDescription with the given Id was successfully returned", content = @Content(schema = @Schema(implementation = ProductDescriptionDto.class))),
                    @ApiResponse(responseCode = "404", description = "ProductDescription with the given Id was not found"),
                    @ApiResponse(responseCode = "500", description = "Internal server error"),
            }
    )
    @GetMapping("api/v0/productdescriptions/{productDescriptionId}")
    public ResponseEntity<ProductDescriptionDto> getProductDescription(@PathVariable("productDescriptionId") Integer id) throws ProductDescriptionNotFoundException {

        log.debug(String.format("Received a request to retrieve the productDescription with the Id %1$s", id));
        validateProductDescriptionId(id);
        return new ResponseEntity<>(productDescriptionService.read(id), HttpStatus.OK);
    }

    @Operation(
            summary = "create a new product Description",
            responses = {
                    @ApiResponse(responseCode = "200", description = "product Description successfully created", content = @Content(schema = @Schema(implementation = ProductDescriptionDto.class))),
                    @ApiResponse(responseCode = "400", description = "Request Body validation error"),
                    @ApiResponse(responseCode = "500", description = "Internal server error"),
            }
    )
    @PostMapping("/api/v0/productdescriptions")
    public ResponseEntity<ProductDescriptionDto> createProductDescription(@Valid @RequestBody ProductDescriptionDto productDescriptionDto, HttpServletRequest request) {

        log.debug(String.format("ProductDescriptionController createProductDescription at %1$s called with payload %2$s",
                request.getRequestURL(), productDescriptionDto.toString()));
        if(productDescriptionDto.getProductId() != null){

            validateProductId(productDescriptionDto.getProductId());
        }
        return new ResponseEntity<>(productDescriptionService.create(productDescriptionDto), HttpStatus.OK);
    }

    @Operation(
            summary = "Updates an product Description defined by a given Id",
            responses = {
                    @ApiResponse(responseCode = "200", description = "ProductDescription successfully updated", content = @Content(schema = @Schema(implementation = ProductDescriptionDto.class))),
                    @ApiResponse(responseCode = "400", description = "Request Body validation error"),
                    @ApiResponse(responseCode = "404", description = "ProductDescription wth the given Id was not Found"),
                    @ApiResponse(responseCode = "500", description = "Internal server error"),
            }
    )
    @PutMapping("api/v0/productdescriptions/{productDescriptionId}")
    public ResponseEntity<ProductDescriptionDto> updateProductDescription(@PathVariable("productDescriptionId") Integer id, @Valid @RequestBody ProductDescriptionDto productDescriptionDto) throws ProductDescriptionNotFoundException {

        productDescriptionDto.setId(id);
        log.debug(String.format("Received a request to update the product Description with the Id %1$s with the given details %2$s", id, productDescriptionDto));
        validateProductDescriptionId(id);
        if(productDescriptionDto.getProductId() != null){

            validateProductId(productDescriptionDto.getProductId());
        }
        return new ResponseEntity<>(productDescriptionService.update(id, productDescriptionDto), HttpStatus.OK);
    }

    @Operation(
            summary = "Delete a product Description",
            responses = {
                    @ApiResponse(responseCode = "200", description = "product Description successfully deleted", content = @Content(schema = @Schema(implementation = ProductDescriptionDto.class))),
                    @ApiResponse(responseCode = "404", description = "product Description with given Id does not exist"),
                    @ApiResponse(responseCode = "500", description = "Internal server error"),
            }
    )
    @DeleteMapping("/api/v0/productdescriptions/{productDescriptionId}")
    public ResponseEntity deleteProductDescription(@PathVariable("productDescriptionId") Integer id, HttpServletRequest request) {

        log.debug(String.format("ProductDescriptionController deleteProductDescription at %1$s called",
                request.getRequestURL()));
        validateProductDescriptionId(id);
        productDescriptionService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private void validateProductDescriptionId(Integer id) {

        if(!productDescriptionService.exists(id)) {

            log.warn(String.format("ProductDescription with Id %1$s was not found", id));
            throw new ProductDescriptionNotFoundException();
        }
    }

    private void validateProductId(String id) {

        if(!productService.exists(id)) {

            log.warn(String.format("Product with Id %1$s was not found", id));
            throw new ProductNotFoundException();
        }
    }
}
