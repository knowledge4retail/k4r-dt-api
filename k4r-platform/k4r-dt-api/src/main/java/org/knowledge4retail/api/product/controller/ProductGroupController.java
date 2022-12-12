package org.knowledge4retail.api.product.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.knowledge4retail.api.product.dto.ProductGroupDto;
import org.knowledge4retail.api.product.exception.ProductGroupAlreadyExistsException;
import org.knowledge4retail.api.product.exception.ProductGroupNotFoundException;
import org.knowledge4retail.api.product.exception.ProductNotFoundException;
import org.knowledge4retail.api.product.exception.StoreNotFoundException;
import org.knowledge4retail.api.product.service.ProductGroupService;
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
public class ProductGroupController {

    private final ProductGroupService productGroupService;
    private final ProductService productService;
    private final StoreService storeService;

    @Operation(
            summary = "Saves a given product group for the given storeId. StoreId field in payload is ignored.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Product group successfully created", content = @Content(schema = @Schema(implementation = ProductGroupDto.class))),
                    @ApiResponse(responseCode = "400", description = "Request Body validation error"),
                    @ApiResponse(responseCode = "404", description = "Store with the given storeId does not exist"),
                    @ApiResponse(responseCode = "500", description = "Internal server error"),
            }
    )
    @PostMapping("api/v0/stores/{storeId}/productgroups")
    public ResponseEntity<ProductGroupDto> createProductGroup(@PathVariable("storeId") Integer storeId, @Valid @RequestBody ProductGroupDto productGroup, HttpServletRequest request) {

        productGroup.setStoreId(storeId);
        log.debug(String.format("ProductGroupController createProductGroup at %1$s called with storeId %2$s and payload %3$s",
                request.getRequestURL(), storeId, productGroup));

        validateStoreId(storeId);
        checkIfProductGroupAlreadyExists(productGroup);
        return new ResponseEntity<>(productGroupService.create(productGroup), HttpStatus.OK);
    }

    @Operation(
            summary = "Returns all product groups for the given storeId",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Product groups successfully returned, if any", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ProductGroupDto.class)))),
                    @ApiResponse(responseCode = "404", description = "Store with the given storeId does not exist"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    @GetMapping("api/v0/stores/{storeId}/productgroups")
    public ResponseEntity<List<ProductGroupDto>> retrieveProductGroups(@PathVariable("storeId") Integer storeId, HttpServletRequest request) {

        log.debug(String.format("ProductGroupController retrieveProductGroups at %1$s called with storeId %2$s",
                request.getRequestURL(), storeId));
        validateStoreId(storeId);
        return new ResponseEntity<>(productGroupService.readByStoreId(storeId), HttpStatus.OK);
    }

    @Operation(
            summary = "Returns the product group with the given id",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Product group successfully returned", content = @Content(schema = @Schema(implementation = ProductGroupDto.class))),
                    @ApiResponse(responseCode = "404", description = "Product group with the given productGroupId does not exist"),
                    @ApiResponse(responseCode = "500", description = "Internal server error"),
            }
    )
    @GetMapping("api/v0/productgroups/{productGroupId}")
    public ResponseEntity<ProductGroupDto> retrieveProductGroup(@PathVariable("productGroupId") Integer productGroupId, HttpServletRequest request) {

        log.debug(String.format("ProductGroupController getProductGroup at %1$s called with productGroupId %2$s",
                request.getRequestURL(), productGroupId));

        validateProductGroupId(productGroupId);
        return new ResponseEntity<>(productGroupService.read(productGroupId), HttpStatus.OK);
    }

    @Operation(
            summary = "Deletes the product group with the given id",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Product group successfully deleted", content = @Content(schema = @Schema(implementation = ProductGroupDto.class))),
                    @ApiResponse(responseCode = "404", description = "Product group with the given productGroupId does not exist"),
                    @ApiResponse(responseCode = "500", description = "Internal server error"),
            }
    )
    @DeleteMapping("api/v0/productgroups/{productGroupId}")
    public ResponseEntity deleteProductGroup(@PathVariable("productGroupId") Integer productGroupId, HttpServletRequest request) {

        log.debug(String.format("ProductGroupController deleteProductGroup at %1$s called with productGroupId %2$s",
                request.getRequestURL(), productGroupId));

        validateProductGroupId(productGroupId);
        productGroupService.delete(productGroupId);
        return new ResponseEntity(HttpStatus.OK);
    }

    @Operation(
            summary = "Adds the product with the given productId to group with id productGroupId",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Product successfully added to group", content = @Content(schema = @Schema(implementation = ProductGroupDto.class))),
                    @ApiResponse(responseCode = "400", description = "Product was already in group"),
                    @ApiResponse(responseCode = "404", description = "Product group with the given productGroupId does not exist"),
                    @ApiResponse(responseCode = "404", description = "Product with the given productId does not exist"),
                    @ApiResponse(responseCode = "500", description = "Internal server error"),
            }
    )
    @PostMapping("api/v0/productgroups/{productGroupId}/products/{productId}")
    public ResponseEntity<ProductGroupDto> addProductToProductGroup(@PathVariable("productGroupId") Integer productGroupId, @PathVariable("productId") String productId, HttpServletRequest request) {

        log.debug(String.format("ProductGroupController addProductToProductGroup at %1$s called with productGroupId %2$s and productId %3$s",
                request.getRequestURL(), productGroupId, productId));

        validateProductGroupId(productGroupId);
        validateProductId(productId);
        return new ResponseEntity<>(productGroupService.updateAddProductToGroup(productGroupId, productId), HttpStatus.OK);
    }

    @Operation(
            summary = "Removes the product with the given productId from group with id productGroupId",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Product successfully removed from group", content = @Content(schema = @Schema(implementation = ProductGroupDto.class))),
                    @ApiResponse(responseCode = "400", description = "Product wasn't in the group"),
                    @ApiResponse(responseCode = "404", description = "Product group with the given productGroupId does not exist"),
                    @ApiResponse(responseCode = "404", description = "Product with the given productId does not exist"),
                    @ApiResponse(responseCode = "500", description = "Internal server error"),
            }
    )
    @DeleteMapping("api/v0/productgroups/{productGroupId}/products/{productId}")
    public ResponseEntity<ProductGroupDto> removeProductFromProductGroup(@PathVariable("productGroupId") Integer productGroupId, @PathVariable("productId") String productId, HttpServletRequest request) {

        log.debug(String.format("ProductGroupController removeProductFromProductGroup at %1$s called with productGroupId %2$s and productId %3$s",
                request.getRequestURL(), productGroupId, productId));

        validateProductGroupId(productGroupId);
        validateProductId(productId);
        return new ResponseEntity<>(productGroupService.updateRemoveProductFromGroup(productGroupId, productId), HttpStatus.OK);
    }

    private void validateProductGroupId(Integer productGroupId) {

        if (!productGroupService.exists(productGroupId)) {

            log.warn("product group with id {} does not exist", productGroupId);
            throw new ProductGroupNotFoundException();
        }
    }

    private void validateProductId(String productId) {

        if (!productService.exists(productId)) {

            log.warn("product with id {} does not exist", productId);
            throw new ProductNotFoundException();
        }
    }

    private void validateStoreId(Integer storeId) {

        if (!storeService.exists(storeId)) {

            log.warn("store with id {} does not exist", storeId);
            throw new StoreNotFoundException();
        }
    }


    private void checkIfProductGroupAlreadyExists(ProductGroupDto productGroup) {
        if (productGroupService.exists(productGroup.getId())) {

            log.warn("product group with id {} already exists", productGroup.getId());
            throw new ProductGroupAlreadyExistsException();
        }
    }
}
