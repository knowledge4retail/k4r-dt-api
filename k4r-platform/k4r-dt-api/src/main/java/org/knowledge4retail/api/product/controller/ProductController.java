package org.knowledge4retail.api.product.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.knowledge4retail.api.product.dto.ImportProductDto;
import org.knowledge4retail.api.product.dto.ProductDto;
import org.knowledge4retail.api.product.dto.ProductListDto;
import org.knowledge4retail.api.product.dto.XmlProductImportDto;
import org.knowledge4retail.api.product.exception.MaterialGroupNotFoundException;
import org.knowledge4retail.api.product.exception.ProductAlreadyExistsException;
import org.knowledge4retail.api.product.exception.ProductNotFoundException;
import org.knowledge4retail.api.product.exception.XMLDataNotValidException;
import org.knowledge4retail.api.product.service.MaterialGroupService;
import org.knowledge4retail.api.product.service.ProductService;
import org.knowledge4retail.api.product.service.XMLService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.xml.transform.TransformerException;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
public class ProductController {

    private final ProductService productService;
    private final XMLService xmlService;
    private final MaterialGroupService materialGroupService;

    @Operation(
            summary = "Returns all products",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Product successfully returned", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ProductDto.class)))),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    @GetMapping("api/v0/products")
    public ResponseEntity<List<ProductDto>> getAllProducts(HttpServletRequest request){

        log.debug(String.format("ProductController getAllProducts at %1$s called", request.getRequestURL()));
        return new ResponseEntity<>(productService.readAll(), HttpStatus.OK);
    }


    @Operation(
            summary = "Creates a new product",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Product successfully created", content = @Content(schema = @Schema(implementation = ProductDto.class))),
                    @ApiResponse(responseCode = "400", description = "Product already exists"),
                    @ApiResponse(responseCode = "500", description = "Internal server error"),
            }
    )
    @PostMapping("api/v0/products")
    public ResponseEntity<ProductDto> createNewProduct(@RequestBody @Valid ProductDto productDto, HttpServletRequest request){

        log.debug(String.format("ProductController createNewProduct at %1$s with id %2$s called with payload %3$s"
                , request.getRequestURL(), productDto.getId(), productDto));
        checkIfProductAlreadyExists(productDto.getId());
        if(productDto.getMaterialGroupId() != null){

            validateMaterialGroupId(productDto.getMaterialGroupId());
        }
        return new ResponseEntity<>(productService.create(productDto), HttpStatus.OK);
    }

    @Operation(
            summary = "Creates new products",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Products successfully returned", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ProductDto.class)))),
                    @ApiResponse(responseCode = "400", description = "At least one of the products already exists"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    @PostMapping("api/v0/products/list")
    public ResponseEntity<List<ImportProductDto>> createNewProducts(@RequestBody @Valid ProductListDto products, HttpServletRequest request){

        log.debug(String.format("ProductController createNewProducts at %1$s called with payload %2$s"
                , request.getRequestURL(), products.toString()));
        for (ProductDto productDto : products.getProducts()){

            checkIfProductAlreadyExists(productDto.getId());
            if(productDto.getMaterialGroupId() != null){

                validateMaterialGroupId(productDto.getMaterialGroupId());
            }
        }
        return new ResponseEntity<>(productService.createMany(products.getProducts()), HttpStatus.OK);
    }

    @Operation(
            summary = "Imports products via xml with xslt transformation",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Products successfully imported", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ProductDto.class)))),
                    @ApiResponse(responseCode = "400", description = "error during xml import: unable to deserialize transformation result"),
                    @ApiResponse(responseCode = "400", description = "error during xml import: unable to transform xml with given xslt"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    @PostMapping("api/v0/products/import")
    public ResponseEntity<List<ImportProductDto>> importViaXml(@Valid @RequestBody XmlProductImportDto xmlProductImportDto, HttpServletRequest request ){

        log.debug(String.format("ProductController importViaXml at %1$s called with payload %2$s"
                , request.getRequestURL(), xmlProductImportDto.toString()));

        try{
            return new ResponseEntity<>(xmlService.importProducts(xmlProductImportDto), HttpStatus.OK);
        }
        catch(DataIntegrityViolationException dive){
            throw new XMLDataNotValidException("error during xml import: unable to deserialize transformation result");
        }
        catch(TransformerException te){
            throw new XMLDataNotValidException("error during xml import: unable to transform xml with given xslt");
        }
        catch (Exception ex){
            throw new RuntimeException(ex);
        }
    }

    @Operation(
            summary = "Returns the product with the given id",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Products successfully returned", content = @Content(schema = @Schema(implementation = ProductDto.class))),
                    @ApiResponse(responseCode = "404", description = "No product with the given id exists"),
                    @ApiResponse(responseCode = "500", description = "Internal server error"),
            }
    )
    @GetMapping("api/v0/products/{productId}")
    public ResponseEntity<ProductDto> getProduct(@PathVariable("productId") String id, HttpServletRequest request){

        log.debug(String.format("ProductController getProduct at %1$s called for id %2$s"
                , request.getRequestURL(), id));
        validateProductId(id);
        return new ResponseEntity<>(productService.read(id), HttpStatus.OK);
    }

    @Operation(
            summary = "Updates the product with the given id. Id in the payload is ignored",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Products successfully updated", content = @Content(schema = @Schema(implementation = ProductDto.class))),
                    @ApiResponse(responseCode = "404", description = "No product with the given id exists"),
                    @ApiResponse(responseCode = "500", description = "Internal server error"),
            }
    )
    @PutMapping("api/v0/products/{productId}")
    public ResponseEntity<ProductDto> updateProduct(@PathVariable("productId") String id,
                                                    @RequestBody @Valid ProductDto productDto,
                                                    HttpServletRequest request){
        productDto.setId(id);
        log.debug(String.format("ProductController updateProduct at %1$s called for id %2$s with payload %3$s"
                , request.getRequestURL(), id, productDto));

        validateProductId(id);
        if(productDto.getMaterialGroupId() != null){

            validateMaterialGroupId(productDto.getMaterialGroupId());
        }
        return new ResponseEntity<>(productService.update(productDto), HttpStatus.OK);
    }

    @Operation(
            summary = "Deletes the product with the given id",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Products successfully deleted", content = @Content(schema = @Schema(implementation = ProductDto.class))),
                    @ApiResponse(responseCode = "404", description = "No product with the given id exists"),
                    @ApiResponse(responseCode = "500", description = "Internal server error"),
            }
    )
    @DeleteMapping("api/v0/products/{productId}")
    public ResponseEntity deleteProduct(@PathVariable("productId") String id, HttpServletRequest request){

        log.debug(String.format("ProductController deleteProduct at %1$s called for id %2$s"
                , request.getRequestURL(), id));
        validateProductId(id);
        productService.delete(id);
        return new ResponseEntity(HttpStatus.OK);
    }

    private void validateProductId(String id) {

        if (!productService.exists(id)){

            log.warn("no product with id {} exists", id);
            throw new ProductNotFoundException();
        }
    }

    private void validateMaterialGroupId(Integer id) {

        if (!materialGroupService.exists(id)){

            log.warn("MaterialGroup with id {} not found", id);
            throw new MaterialGroupNotFoundException();
        }
    }

    private void checkIfProductAlreadyExists(String id) {

        if (productService.exists(id)) {

            log.warn("product with id {} already exists", id);
            throw new ProductAlreadyExistsException();
        }
    }
}
