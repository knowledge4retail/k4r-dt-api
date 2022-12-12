package org.knowledge4retail.api.store.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.knowledge4retail.api.product.exception.ProductGtinNotFoundException;
import org.knowledge4retail.api.product.exception.UnitNotFoundException;
import org.knowledge4retail.api.product.service.ProductGtinService;
import org.knowledge4retail.api.product.service.UnitService;
import org.knowledge4retail.api.store.dto.BarcodeDto;
import org.knowledge4retail.api.store.exception.BarcodeNotFoundException;
import org.knowledge4retail.api.store.exception.ShelfLayerNotFoundException;
import org.knowledge4retail.api.store.service.BarcodeService;
import org.knowledge4retail.api.store.service.ShelfLayerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
public class BarcodeController {

    private final BarcodeService barcodeService;
    private final ShelfLayerService shelfLayerService;
    private final ProductGtinService productGtinService;
    private final UnitService unitService;

    @Operation(
            summary = "Saves a given barcode for the given shelfLayerId. ShelfLayerId field in payload is ignored.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Barcode successfully created", content = @Content(schema = @Schema(implementation = BarcodeDto.class))),
                    @ApiResponse(responseCode = "400", description = "Request Body validation error"),
                    @ApiResponse(responseCode = "404", description = "ShelfLayerId not found"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    @PostMapping("api/v0/shelflayers/{shelfLayerId}/barcodes")
    public ResponseEntity<BarcodeDto> createBarcode(@PathVariable Integer shelfLayerId, @Valid @RequestBody BarcodeDto barcode, HttpServletRequest request) {

        barcode.setShelfLayerId(shelfLayerId);
        log.debug(String.format("BarcodeController createBarcode at %1$s called for shelfLayerId %2$s with payload %3$s",
                request.getRequestURL(), shelfLayerId, barcode));

        validateShelfLayerId(shelfLayerId);
        barcode.setShelfLayerId(shelfLayerId);
        validateUnitId(barcode.getLengthUnitId());

        validateProductGtinId(barcode.getProductGtinId());

        return new ResponseEntity<>(barcodeService.create(barcode), HttpStatus.OK);
    }

    @Operation(
            summary = "Returns all Barcodes for the given shelfLayerId",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Barcodes successfully returned", content = @Content(array = @ArraySchema(schema = @Schema(implementation = BarcodeDto.class)))),
                    @ApiResponse(responseCode = "404", description = "ShelfLayer with the given shelfLayerId does not exist"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    @GetMapping("api/v0/shelflayers/{shelfLayerId}/barcodes")
    public ResponseEntity<List<BarcodeDto>> retrieveBarcodes(@PathVariable Integer shelfLayerId, HttpServletRequest request) {

        log.debug(String.format("BarcodeController retrieveBarcodes at %1$s called for shelfLayerId %2$s",
                request.getRequestURL(), shelfLayerId));

        validateShelfLayerId(shelfLayerId);

        return new ResponseEntity<>(barcodeService.readByShelfLayerId(shelfLayerId), HttpStatus.OK);
    }

    @Operation(
            summary = "Returns the Barcode with the given id",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Barcode successfully returned", content = @Content(schema = @Schema(implementation = BarcodeDto.class))),
                    @ApiResponse(responseCode = "404", description = "ShelfLayer with the given shelfLayerId does not exist"),
                    @ApiResponse(responseCode = "404", description = "Barcode with the given BarcodeId does not exist"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    @GetMapping("api/v0/barcodes/{barcodeId}")
    public ResponseEntity<BarcodeDto> retrieveBarcode(@PathVariable Integer barcodeId, HttpServletRequest request) {

        log.debug(String.format("BarcodeController retrieveBarcode at %1$s called for BarcodeId %2$s",
                request.getRequestURL(), barcodeId));

        validateBarcodeId(barcodeId);

        return new ResponseEntity<>(barcodeService.read(barcodeId), HttpStatus.OK);
    }

    @Operation(
            summary = "Updates an existing Barcode defined by the given barcodeId",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Barcode successfully updated", content = @Content(schema = @Schema(implementation = BarcodeDto.class))),
                    @ApiResponse(responseCode = "400", description = "Request Body validation error"),
                    @ApiResponse(responseCode = "404", description = "Barcode wth the given Id was not Found")
            }
    )
    @PutMapping("api/v0/barcodes/{barcodeId}")
    public ResponseEntity<BarcodeDto> updateBarcode(@PathVariable Integer barcodeId,
                                                      @Valid @RequestBody BarcodeDto barcodeDto) {

        barcodeDto.setId(barcodeId);
        log.debug(String.format("Received a request to update the Barcode with the Id %d with the given details %s",
                barcodeId, barcodeDto));

        validateBarcodeId(barcodeId);

        validateShelfLayerId(barcodeDto.getShelfLayerId());

        if(barcodeDto.getLengthUnitId() != null) {

            validateProductGtinId(barcodeDto.getProductGtinId());
        }

        return new ResponseEntity<>(this.barcodeService.update(barcodeId, barcodeDto), HttpStatus.OK);

    }

    @SuppressWarnings("rawtypes")
    @Operation(
            summary = "Deletes the Barcode with the given id",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Barcode successfully deleted", content = @Content(schema = @Schema(implementation = BarcodeDto.class))),
                    @ApiResponse(responseCode = "404", description = "ShelfLayer with the given shelfLayerId does not exist"),
                    @ApiResponse(responseCode = "404", description = "Barcode with the given BarcodeId does not exist"),
                    @ApiResponse(responseCode = "500", description = "Internal server error"),
            }
    )
    @DeleteMapping("api/v0/barcodes/{barcodeId}")
    public ResponseEntity deleteBarcode(@PathVariable Integer barcodeId, HttpServletRequest request) {

        log.debug(String.format("BarcodeController deleteBarcode at %1$s called for BarcodeId %2$s",
                request.getRequestURL(), barcodeId));

        validateBarcodeId(barcodeId);
        barcodeService.delete(barcodeId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    private void validateBarcodeId(@PathVariable Integer barcodeId) {

        if (!barcodeService.exists(barcodeId)) {
            log.warn("Barcode with id {} does not exist", barcodeId);
            throw new BarcodeNotFoundException();
        }
    }

    private void validateShelfLayerId(@PathVariable Integer shelfLayerId) {

        if (!shelfLayerService.exists(shelfLayerId)) {

            log.warn("shelf layer with id {} does not exist", shelfLayerId);
            throw new ShelfLayerNotFoundException();
        }
    }

    private void validateProductGtinId(Integer productGtinId) {

        if (!productGtinService.exists(productGtinId)) {
            log.warn("ProductGtin with id {} does not exist", productGtinId);
            throw new ProductGtinNotFoundException();
        }
    }

    private void validateUnitId(Integer lengthUnitId) {

        if (!unitService.exists(lengthUnitId)) {
            log.warn("Unit with id {} does not exist", lengthUnitId);
            throw new UnitNotFoundException();
        }
    }
}
