package org.knowledge4retail.api.product.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.knowledge4retail.api.product.dto.ProductPropertyDto;
import org.knowledge4retail.api.product.exception.*;
import org.knowledge4retail.api.product.service.ProductPropertyService;
import org.knowledge4retail.api.product.service.ProductService;
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
public class ProductPropertyController {

    private final ProductPropertyService productPropertyService;
    private final StoreService storeService;
    private final ProductService productService;

    @Operation(
            summary = "Returns all product properties for the given productId and optionally a given StoreId",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Product Properties successfully returned", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ProductPropertyDto.class)))),
                    @ApiResponse(responseCode = "404", description = "store with the given storeId does not exist"),
                    @ApiResponse(responseCode = "404", description = "product with the given productId does not exist"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    @GetMapping("api/v0/products/{productId}/productproperties")
    public ResponseEntity<List<ProductPropertyDto>> getAllPropertiesForProduct(@PathVariable("productId") String productId,
                                                                               @RequestParam(required = false) Integer storeId,
                                                                               HttpServletRequest request){
        log.debug(String.format("ProductPropertyController getAllPropertiesForProduct at %1$s called for storeId %2$s and productId %3$s",
                request.getRequestURL(), storeId, productId));

        validateProductId(productId);
        if (storeId == null) {

            return new ResponseEntity<>(productPropertyService.readByStoreIdNullAndProductId(productId), HttpStatus.OK);
        }
        validateStoreId(storeId);
        return new ResponseEntity<>(productPropertyService.readByStoreIdAndProductId(storeId, productId), HttpStatus.OK);
    }

    @Operation(
            summary = "Saves a given product property for the given productId, characteristicId and optionally a storeId. Id fields in payload are ignored",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Product property successfully created", content = @Content(schema = @Schema(implementation = ProductPropertyDto.class))),
                    @ApiResponse(responseCode = "400", description = "Product property for given store, product and characteristicId already exists"),
                    @ApiResponse(responseCode = "400", description = "Request Body validation error"),
                    @ApiResponse(responseCode = "500", description = "Internal server error"),
            }
    )
    @PostMapping("api/v0/products/{productId}/productproperties/{productCharacteristicId}")
    public ResponseEntity<ProductPropertyDto> saveProductProperty(@Valid @RequestBody ProductPropertyDto productPropertyDto,
                                                           @RequestParam(required = false) Integer storeId,
                                                           @PathVariable("productId") String productId,
                                                           @PathVariable("productCharacteristicId") Integer characteristicId,
                                                           HttpServletRequest request) {

        productPropertyDto.setCharacteristicId(characteristicId);
        productPropertyDto.setProductId(productId);
        productPropertyDto.setStoreId(storeId);
        log.debug(String.format("ProductPropertyController saveProperty at %1$s called for storeId %2$s, \" +\n" +
                        "                        \"productId %3$s and characteristicId %4$s and payload %5$s",
                request.getRequestURL(),
                storeId, productId, characteristicId, productPropertyDto));

        checkIfProductPropertyAlreadyExists(storeId, productId, characteristicId);
        return new ResponseEntity<>(productPropertyService.create(productPropertyDto), HttpStatus.OK);
    }

    @Operation(
            summary = "Updates an existing product property for a given productId, productCharacteristicId and optionally a storeId.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "ProductProperty successfully updated", content = @Content(schema = @Schema(implementation = ProductPropertyDto.class))),
                    @ApiResponse(responseCode = "400", description = "Request Body validation error"),
                    @ApiResponse(responseCode = "404", description = "ProductProperty wth the given Id was not Found"),
                    @ApiResponse(responseCode = "500", description = "Internal server error"),
            }
    )
    @PutMapping("api/v0/products/{productId}/productproperties/{productCharacteristicId}")
    public ResponseEntity<ProductPropertyDto> updateProductProperty(@RequestParam(required = false) Integer storeId, @PathVariable("productId") String productId, @PathVariable("productCharacteristicId") Integer characteristicId, @Valid @RequestBody ProductPropertyDto productPropertyDto) throws MaterialGroupNotFoundException {

        productPropertyDto.setProductId(productId);
        productPropertyDto.setCharacteristicId(characteristicId);
        if (storeId != null) {

            productPropertyDto.setStoreId(storeId);
        }
        log.debug(String.format("Received a request to update the productProperty with the productId %1$s, characteristicId %2$s and storeId %3$s with the given details %4$s", productId, characteristicId, storeId, productPropertyDto));
        validateProductProperty(storeId, productId, characteristicId);
        return new ResponseEntity<>(productPropertyService.update(storeId, productId, characteristicId, productPropertyDto), HttpStatus.OK);
    }

    @Operation(
            summary = "Deletes a product property for the given productId, characteristicId and optionally a storeID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Product property successfully deleted", content = @Content(schema = @Schema(implementation = ProductPropertyDto.class))),
                    @ApiResponse(responseCode = "404", description = "Product property for given store, product and characteristicId does not exist"),
                    @ApiResponse(responseCode = "500", description = "Internal server error"),
            }
    )
    @DeleteMapping("api/v0/products/{productId}/productproperties/{productCharacteristicId}")
    public ResponseEntity deleteProductProperty(@RequestParam(required = false) Integer storeId,
                                                      @PathVariable("productId") String productId,
                                                      @PathVariable("productCharacteristicId") Integer characteristicId,
                                                      HttpServletRequest request) {

        log.debug(String.format("ProductPropertyController deleteProperty at %1$s called for storeId %2$s, " +
                        "productId %3$s and characteristicId %4$s"
                , request.getRequestURL(), storeId, productId, characteristicId));

        if (storeId == null) {

            validateProductPropertyWithStoreIdNull(productId, characteristicId);
            productPropertyService.deleteByStoreIdNullAndProductIdAndCharacteristicId(productId, characteristicId);
        } else {

            validateProductProperty(storeId, productId, characteristicId);
            productPropertyService.delete(storeId, productId, characteristicId);
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    private void validateProductProperty(Integer storeId, String productId, Integer characteristicId) {

        if ( !productPropertyService.exists(storeId, productId, characteristicId)){

            log.warn("property for storeId {}, productId {} and characteristicId {} does not exist", storeId, productId, characteristicId );
            throw new ProductPropertyNotFoundException();
        }
    }

    private void validateProductPropertyWithStoreIdNull(String productId, Integer characteristicId) {

        if ( !productPropertyService.existsByStoreIdNullAndProductIdAndCharacteristicId(productId, characteristicId)){

            log.warn("property for productId {} and characteristicId {} does not exist", productId, characteristicId );
            throw new ProductPropertyNotFoundException();
        }
    }

    private void validateStoreId(Integer storeId) {

        if (!storeService.exists(storeId)){

            log.warn("storeId {} does not exist", storeId);
            throw new StoreNotFoundException();
        }
    }

    private void validateProductId(String productId) {

        if (!productService.exists(productId)){

            log.warn("productId {} does not exist", productId);
            throw new ProductNotFoundException();
        }
    }


    private void checkIfProductPropertyAlreadyExists(Integer storeId, String productId, Integer characteristicId) {
        if (productPropertyService.exists(storeId, productId, characteristicId)){

            log.warn("property for storeId {} and productId {} and characteristicId {} already exists", storeId, productId, characteristicId);
            throw new ProductPropertyAlreadyExistsException();
        }
    }
}
