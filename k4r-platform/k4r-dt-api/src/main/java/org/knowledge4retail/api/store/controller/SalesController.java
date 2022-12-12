package org.knowledge4retail.api.store.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.knowledge4retail.api.store.dto.SalesDto;
import org.knowledge4retail.api.store.exception.SalesNotFoundException;
import org.knowledge4retail.api.store.service.SalesService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
public class SalesController {

    private final SalesService salesService;

    @Operation(
            summary = "Creates new sales data",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Sales data successfully created", content = @Content(schema = @Schema(implementation = SalesDto.class))),
                    @ApiResponse(responseCode = "400", description = "Request Body validation error")
            }
    )
    @PostMapping("api/v0/sales")
    public ResponseEntity<SalesDto> createSales(@Valid @RequestBody SalesDto salesDto) {

        log.debug(String.format("Received a request to create sales data with given details %s",
                salesDto.toString()));
        return new ResponseEntity<>(salesService.create(salesDto), HttpStatus.OK);

    }

    @Operation(
            summary = "Creates many new sales data",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Many sales data successfully created", content = @Content(array = @ArraySchema(schema = @Schema(implementation = SalesDto.class)))),
                    @ApiResponse(responseCode = "400", description = "Request Body validation error")
            }
    )
    @PostMapping("api/v0/sales/list")
    public ResponseEntity<List<SalesDto>> createManySales(@Valid @RequestBody List<SalesDto> salesDtos) {

        log.debug(String.format("Received a request to create many sales data with given details %s",
                salesDtos.toString()));
        return new ResponseEntity<>(salesService.createMany(salesDtos), HttpStatus.OK);

    }

    @Operation(
            summary = "Get all sales",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Sales successfully returned", content = @Content(array = @ArraySchema(schema = @Schema(implementation = SalesDto.class)))),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    @GetMapping("api/v0/sales")
    public ResponseEntity<List<SalesDto>> getAllSales(HttpServletRequest request) {

        log.debug(String.format("SalesController getAllSales at %1$s called",
                request.getRequestURL()));
        return new ResponseEntity<>(salesService.readAll(), HttpStatus.OK);
    }

    @Operation(
            summary = "Returns sales defined with the storeId and the gtin",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Sales with the given storeId and gtin was successfully returned", content = @Content(array = @ArraySchema(schema = @Schema(implementation = SalesDto.class)))),
                    @ApiResponse(responseCode = "404", description = "Sales with the given storeId and gtin was not found"),
                    @ApiResponse(responseCode = "500", description = "Internal server error"),
            }
    )
    @GetMapping("api/v0/sales/{storeId}/{gtin}")
    public ResponseEntity<List<SalesDto>> getSales(@PathVariable("storeId") Integer storeId, @PathVariable("gtin") String gtin) throws SalesNotFoundException {

        log.debug(String.format("Received a request to retrieve the sales with the storeId %d and gtin %s", storeId, gtin));
        if(!salesService.exists(storeId, gtin)) {

            log.warn(String.format("Sales with storeId %d and gtin %s was not found", storeId, gtin));
            throw new SalesNotFoundException();
        }
        return new ResponseEntity<>(salesService.read(storeId, gtin), HttpStatus.OK);
    }
}
