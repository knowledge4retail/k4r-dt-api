package org.knowledge4retail.api.position.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.knowledge4retail.api.position.dto.PositionDto;
import org.knowledge4retail.api.position.service.PositionService;
import org.knowledge4retail.api.product.exception.ProductGtinNotFoundException;
import org.knowledge4retail.api.product.service.ProductGtinService;
import org.knowledge4retail.api.store.dto.FacingDto;
import org.knowledge4retail.api.store.exception.ShelfLayerNotFoundException;
import org.knowledge4retail.api.store.exception.StoreNotFoundException;
import org.knowledge4retail.api.store.service.ShelfLayerService;
import org.knowledge4retail.api.store.service.StoreService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
public class PositionController {

    private final PositionService positionService;
    private final ShelfLayerService shelfLayerService;
    private final ProductGtinService productGtinService;
    private final StoreService storeService;

    @Operation(
            summary = "Return Facing for the given shelfLayerId and productGtin",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Facings successfully returned", content = @Content(schema = @Schema(implementation = FacingDto.class))),
                    @ApiResponse(responseCode = "400", description = "ShelfLayer with the given shelfLayerId or productGtin with the GTIN does not exist"),
                    @ApiResponse(responseCode = "500", description = "Internal server error"),
            }
    )
    @GetMapping("api/v0/shelflayers/{externalReferenceId}/productgtins/{gtin}/facing")
    public ResponseEntity<FacingDto> getFacingOfGtin(@PathVariable String externalReferenceId, @PathVariable String gtin, HttpServletRequest request) {

        log.debug(String.format("FacingController getFacingOfGtin at %1$s called for shelfLayerId %2$s and gtin %3$s",
                request.getRequestURL(), externalReferenceId, gtin));

        if (!shelfLayerService.existsByExternalReferenceId(externalReferenceId)) {

            log.warn("shelf layer with id {} does not exist", externalReferenceId);
            throw new ShelfLayerNotFoundException();
        }

        if(!productGtinService.existsByGtin(gtin)) {

            log.warn("productGtin with gtin {} does not exist", gtin);
            throw new ProductGtinNotFoundException();
        }

        return new ResponseEntity<>(positionService.getFacingByExternalReferenceIdAndGtin(externalReferenceId, gtin), HttpStatus.OK);
    }

    @Operation(
            summary = "Return Facing for the given shelfLayerId and productGtin",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Positions successfully returned", content = @Content(array = @ArraySchema(schema = @Schema(implementation = PositionDto.class)))),
                    @ApiResponse(responseCode = "404", description = "Store with the given storeId or productGtin with the GTIN does not exist"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    @GetMapping("api/v0/stores/{storeId}/productgtins/{gtin}/positions")
    public ResponseEntity<List<PositionDto>> getAllPositionsOfGtinInStore(@PathVariable("storeId") Integer storeId, @PathVariable("gtin") String gtin, HttpServletRequest request) {

        log.debug(String.format("PositionController getAllPositionOfGtinInStore at %1$s called for gtin %2$s and stireId %3$d",
                request.getRequestURL(), gtin, storeId));

        if(!productGtinService.existsByGtin(gtin)) {

            log.warn("productGtin with gtin {} does not exist", gtin);
            throw new ProductGtinNotFoundException();
        }

        if(!storeService.exists(storeId)) {

            log.warn("store with Id {} does not exist", storeId);
            throw new StoreNotFoundException();
        }

        return new ResponseEntity<>(positionService.getAllPositionsByStoreIdAndGtin(storeId, gtin), HttpStatus.OK);
    }
}
