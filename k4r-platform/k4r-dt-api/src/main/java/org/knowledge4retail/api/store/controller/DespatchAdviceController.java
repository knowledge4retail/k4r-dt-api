package org.knowledge4retail.api.store.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.knowledge4retail.api.store.dto.DespatchAdviceDto;
import org.knowledge4retail.api.store.exception.DespatchAdviceNotFoundException;
import org.knowledge4retail.api.store.exception.StoreNotFoundException;
import org.knowledge4retail.api.store.service.DespatchAdviceService;
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
public class DespatchAdviceController {

    private final DespatchAdviceService despatchAdviceService;
    private final StoreService storeService;

    @Operation(
            summary = "Get all despatchAdvice for the given storeId",
            responses = {
                    @ApiResponse(responseCode = "200", description = "DespatchAdvices successfully returned", content = @Content(array = @ArraySchema(schema = @Schema(implementation = DespatchAdviceDto.class)))),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    @GetMapping("/api/v0/stores/{storeId}/despatchadvices")
    public ResponseEntity<List<DespatchAdviceDto>> getAllDespatchAdvices(@PathVariable("storeId") Integer storeId, HttpServletRequest request) {

        validateStoreId(storeId);

        log.debug(String.format("DespatchAdviceController getAllDespatchAdvices at %1$s called for storeId %2$s",
                request.getRequestURL(), storeId));
        return new ResponseEntity<>(despatchAdviceService.readByStoreId(storeId), HttpStatus.OK);
    }

    @Operation(
            summary = "Returns Despatch Advice defined with the id",
            responses = {
                    @ApiResponse(responseCode = "200", description = "DespatchAdvice with the given Id was successfully returned", content = @Content(schema = @Schema(implementation = DespatchAdviceDto.class))),
                    @ApiResponse(responseCode = "404", description = "DespatchAdvice with the given Id was not found")
            }
    )
    @GetMapping("api/v0/despatchadvices/{despatchAdviceId}")
    public ResponseEntity<DespatchAdviceDto> getDespatchAdviceByStoreId(@PathVariable("despatchAdviceId") Integer id) throws DespatchAdviceNotFoundException {

        log.debug(String.format("Received a request to retrieve the Despatch Advice with the Id %d", id));
        validateDespatchAdviceId(id);
        return new ResponseEntity<>(despatchAdviceService.read(id), HttpStatus.OK);
    }

    @Operation(
            summary = "Create a new despatch advice",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Despatch Advice successfully created", content = @Content(schema = @Schema(implementation = DespatchAdviceDto.class))),
                    @ApiResponse(responseCode = "400", description = "Request Body validation error"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    @PostMapping("/api/v0/stores/{storeId}/despatchadvices")
    public ResponseEntity<DespatchAdviceDto> createDespatchAdvice(@PathVariable ("storeId") Integer storeId, @Valid @RequestBody DespatchAdviceDto despatchAdviceDto, HttpServletRequest request) {

        despatchAdviceDto.setStoreId(storeId);

        log.debug(String.format("DespatchAdviceController create DespatchAdvice at %1$s called  for storeId %2$s with payload %3$s",
                request.getRequestURL(), storeId, despatchAdviceDto));
        validateStoreId(despatchAdviceDto.getStoreId());
        return new ResponseEntity<>(despatchAdviceService.create(despatchAdviceDto), HttpStatus.OK);
    }

    @Operation(
            summary = "Updates an existing despatch Advice defined by a given Id",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Despatch Advice successfully updated", content = @Content(schema = @Schema(implementation = DespatchAdviceDto.class))),
                    @ApiResponse(responseCode = "400", description = "Request Body validation error"),
                    @ApiResponse(responseCode = "404", description = "Despatch Advice wth the given Id was not found")
            }
    )
    @PutMapping("api/v0/despatchadvices/{despatchAdviceId}")
    public ResponseEntity<DespatchAdviceDto> updateCustomer(@PathVariable("despatchAdviceId") Integer id, @Valid @RequestBody DespatchAdviceDto despatchAdviceDto) throws DespatchAdviceNotFoundException {

        despatchAdviceDto.setId(id);
        log.debug(String.format("Received a request to update the despatch advice with the Id %d with the given details %s", id, despatchAdviceDto));
        validateDespatchAdviceId(id);
        validateStoreId(despatchAdviceDto.getStoreId());
        return new ResponseEntity<>(despatchAdviceService.update(id, despatchAdviceDto), HttpStatus.OK);
    }

    @SuppressWarnings("rawtypes")
    @Operation(
            summary = "Delete a Despatch Advice",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Despatch Advice successfully deleted", content = @Content(schema = @Schema(implementation = DespatchAdviceDto.class))),
                    @ApiResponse(responseCode = "404", description = "Despatch Advice with given Id does not exist"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    @DeleteMapping("/api/v0/despatchadvices/{despatchAdviceId}")
    public ResponseEntity deleteDespatchAdvice(@PathVariable("despatchAdviceId") Integer id, HttpServletRequest request) {

        log.debug(String.format("DespatchAdviceController delete Despatch Advice at %1$s called",
                request.getRequestURL()));
        validateDespatchAdviceId(id);
        despatchAdviceService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private void validateDespatchAdviceId(Integer id) {

        if(!despatchAdviceService.exists(id)) {

            log.warn(String.format("DespatchAdvice with Id %d was not found", id));
            throw new DespatchAdviceNotFoundException();
        }
    }

    private void validateStoreId(Integer storeId) {

        if (!storeService.exists(storeId)) {

            log.warn("store with id {} does not exist", storeId);
            throw new StoreNotFoundException();
        }
    }
}
