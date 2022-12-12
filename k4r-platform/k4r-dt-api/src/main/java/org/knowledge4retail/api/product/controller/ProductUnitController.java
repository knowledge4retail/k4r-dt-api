package org.knowledge4retail.api.product.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.knowledge4retail.api.product.dto.ProductUnitDto;
import org.knowledge4retail.api.product.exception.ProductUnitNotFoundException;
import org.knowledge4retail.api.product.exception.UnitNotFoundException;
import org.knowledge4retail.api.product.service.ProductService;
import org.knowledge4retail.api.product.service.ProductUnitService;
import org.knowledge4retail.api.product.service.UnitService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
public class ProductUnitController {

    private final ProductUnitService productUnitService;
    private final ProductService productService;
    private final UnitService unitService;

    @Operation(
            summary = "get all product unit",
            responses = {
                    @ApiResponse(responseCode = "200", description = "product unit successfully returned", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ProductUnitDto.class)))),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    @GetMapping("/api/v0/productunits")
    public ResponseEntity<List<ProductUnitDto>> getAllProductUnits(HttpServletRequest request) {

        log.debug(String.format("ProductUnitController getAllProductUnits at %1$s called",
                request.getRequestURL()));
        return new ResponseEntity<>(productUnitService.readAll(), HttpStatus.OK);
    }

    @Operation(
            summary = "Returns product unit defined with the id",
            responses = {
                    @ApiResponse(responseCode = "200", description = "ProductUnit with the given Id was successfully returned", content = @Content(schema = @Schema(implementation = ProductUnitDto.class))),
                    @ApiResponse(responseCode = "404", description = "ProductUnit with the given Id was not found"),
                    @ApiResponse(responseCode = "500", description = "Internal server error"),
            }
    )
    @GetMapping("api/v0/productunits/{productUnitId}")
    public ResponseEntity<ProductUnitDto> getProductUnit(@PathVariable("productUnitId") Integer id) throws ProductUnitNotFoundException {

        log.debug(String.format("Received a request to retrieve the productUnit with the Id %d", id));
        validateProductUnitId(id);
        return new ResponseEntity<>(productUnitService.read(id), HttpStatus.OK);
    }

    @Operation(
            summary = "create a new product unit",
            responses = {
                    @ApiResponse(responseCode = "200", description = "product unit successfully created", content = @Content(schema = @Schema(implementation = ProductUnitDto.class))),
                    @ApiResponse(responseCode = "400", description = "Request Body validation error"),
                    @ApiResponse(responseCode = "500", description = "Internal server error"),
            }
    )
    @PostMapping("/api/v0/productunits")
    public ResponseEntity<ProductUnitDto> createProductUnit(@Valid @RequestBody ProductUnitDto productUnitDto, HttpServletRequest request) {

        log.debug(String.format("ProductUnitController createProductUnit at %1$s called with payload %2$s",
                request.getRequestURL(), productUnitDto.toString()));
        validateProductUnitDto(productUnitDto);
        return new ResponseEntity<>(productUnitService.create(productUnitDto), HttpStatus.OK);
    }

    @Operation(
            summary = "Updates an existing product unit defined by a given Id",
            responses = {
                    @ApiResponse(responseCode = "200", description = "ProductUnit successfully updated", content = @Content(schema = @Schema(implementation = ProductUnitDto.class))),
                    @ApiResponse(responseCode = "400", description = "Request Body validation error"),
                    @ApiResponse(responseCode = "404", description = "ProductUnit wth the given Id was not Found"),
                    @ApiResponse(responseCode = "500", description = "Internal server error"),
            }
    )
    @PutMapping("api/v0/productunits/{productUnitId}")
    public ResponseEntity<ProductUnitDto> updateProductUnit(@PathVariable("productUnitId") Integer id, @Valid @RequestBody ProductUnitDto productUnitDto) throws ProductUnitNotFoundException {

        productUnitDto.setId(id);
        log.debug(String.format("Received a request to update the product unit with the Id %d with the given details %s", id, productUnitDto));
        validateProductUnitId(id);
        validateProductUnitDto(productUnitDto);
        return new ResponseEntity<>(productUnitService.update(id, productUnitDto), HttpStatus.OK);
    }

    @Operation(
            summary = "Delete a product unit",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Product unit successfully deleted", content = @Content(schema = @Schema(implementation = ProductUnitDto.class))),
                    @ApiResponse(responseCode = "404", description = "Product unit with given Id does not exist"),
                    @ApiResponse(responseCode = "500", description = "Internal server error"),
            }
    )
    @DeleteMapping("/api/v0/productunits/{productUnitId}")
    public ResponseEntity deleteProductUnit(@PathVariable("productUnitId") Integer id, HttpServletRequest request) {

        log.debug(String.format("ProductUnitController deleteProductUnit at %1$s called",
                request.getRequestURL()));
        validateProductUnitId(id);
        productUnitService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private void validateProductUnitDto(@RequestBody @Valid ProductUnitDto productUnitDto) {
        if (productUnitDto.getProductId() != null) {

            validateProductId(productUnitDto.getProductId());
        }
        if (productUnitDto.getDimensionUnit() != null) {

            validateUnitId(productUnitDto.getDimensionUnit());
        }
        if (productUnitDto.getVolumeUnit() != null) {

            validateUnitId(productUnitDto.getVolumeUnit());
        }
        if (productUnitDto.getWeightUnit() != null) {

            validateUnitId(productUnitDto.getWeightUnit());
        }
    }

    private void validateProductUnitId(Integer id) {

        if(!productUnitService.exists(id)){

            log.warn(String.format("ProductUnit with Id %d was not found", id));
            throw new ProductUnitNotFoundException();
        }
    }

    private void validateProductId(String id) {

        if(!productService.exists(id)){

            log.warn(String.format("Product with Id %s was not found", id));
            throw new UnitNotFoundException();
        }
    }

    private void validateUnitId(Integer id) {

        if(!unitService.exists(id)){

            log.warn(String.format("Unit with Id %d was not found", id));
            throw new UnitNotFoundException();
        }
    }
}
