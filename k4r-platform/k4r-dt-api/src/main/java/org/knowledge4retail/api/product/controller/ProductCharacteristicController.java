package org.knowledge4retail.api.product.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.knowledge4retail.api.product.dto.ProductCharacteristicDto;
import org.knowledge4retail.api.product.exception.ProductCharacteristicNotFoundException;
import org.knowledge4retail.api.product.service.ProductCharacteristicService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
public class ProductCharacteristicController {

    private final ProductCharacteristicService productCharacteristicService;

    @Operation(
            summary = "Returns all product characteristics",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Product Characteristics successfully returned", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ProductCharacteristicDto.class)))),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    @GetMapping("/api/v0/productcharacteristics")
    public ResponseEntity<List<ProductCharacteristicDto>> getAllProductCharacteristics(HttpServletRequest request){

        log.debug(String.format("ProductCharacteristicController getAllCharacteristics at %1$s called",
                request.getRequestURL()));
        return new ResponseEntity<>(productCharacteristicService.readAll(), HttpStatus.OK);
    }

    @Operation(
            summary = "Creates a new product characteristic",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Product Characteristics successfully created", content = @Content(schema = @Schema(implementation = ProductCharacteristicDto.class))),
                    @ApiResponse(responseCode = "400", description = "Request Body validation error"),
                    @ApiResponse(responseCode = "500", description = "Internal server error"),
            }
    )
    @PostMapping("/api/v0/productcharacteristics")
    public ResponseEntity<ProductCharacteristicDto> createProductCharacteristic(@Valid @RequestBody ProductCharacteristicDto productCharacteristicDto, HttpServletRequest request){

        log.debug(String.format("ProductCharacteristicController createCharacteristic at %1$s called with payload %2$s",
                request.getRequestURL(), productCharacteristicDto.toString()));
        return new ResponseEntity<>(productCharacteristicService.create(productCharacteristicDto), HttpStatus.OK);
    }

    @Operation(
            summary = "Deletes a product characteristic",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Product characteristic successfully deleted", content = @Content(schema = @Schema(implementation = ProductCharacteristicDto.class))),
                    @ApiResponse(responseCode = "404", description = "Product characteristic with id does not exist"),
                    @ApiResponse(responseCode = "500", description = "Internal server error"),
            }
    )
    @DeleteMapping("/api/v0/productcharacteristics/{productcharacteristicId}")
    public ResponseEntity deleteProductCharacteristic(@PathVariable("productcharacteristicId") Integer id, HttpServletRequest request){

        log.debug(String.format("ProductCharacteristicController deleteCharacteristic at %1$s called",
                request.getRequestURL()));
        validateProductCharacteristicId(id);
        productCharacteristicService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private void validateProductCharacteristicId(Integer id) {

        if (!productCharacteristicService.exists(id)){

            log.warn(String.format("characteristic with Id %d was not found", id));
            throw new ProductCharacteristicNotFoundException();
        }
    }
}
