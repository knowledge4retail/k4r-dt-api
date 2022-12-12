package org.knowledge4retail.api.store.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.knowledge4retail.api.product.service.ProductService;
import org.knowledge4retail.api.store.dto.DespatchLogisticUnitDto;
import org.knowledge4retail.api.store.exception.DespatchAdviceNotFoundException;
import org.knowledge4retail.api.store.exception.DespatchLogisticUnitNotFoundException;
import org.knowledge4retail.api.store.exception.DespatchLogisticUnitParentNotFoundException;
import org.knowledge4retail.api.store.exception.ProductNotFoundException;
import org.knowledge4retail.api.store.service.DespatchAdviceService;
import org.knowledge4retail.api.store.service.DespatchLogisticUnitService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
public class DespatchLogsticUnitController {

    private final DespatchLogisticUnitService despatchLogisticUnitService;
    private final DespatchAdviceService despatchAdviceService;
    private final ProductService productsService;

    @Operation(
            summary = "Get all despatchLogisticUnit for the given despatchAdviceId",
            responses = {
                    @ApiResponse(responseCode = "200", description = "DespatchLogisticUnits successfully returned", content = @Content(array = @ArraySchema(schema = @Schema(implementation = DespatchLogisticUnitDto.class)))),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    @GetMapping("/api/v0/despatchadvices/{despatchAdviceId}/despatchlogisticunits")
    public ResponseEntity<List<DespatchLogisticUnitDto>> getAllDespatchLogisticUnitsByAdviceId(@PathVariable ("despatchAdviceId") Integer despatchAdviceId, HttpServletRequest request) {

        validateDespatchAdviceId(despatchAdviceId);

        log.debug(String.format("DespatchLogisticUnitController getAllDespatchLogisticUnits at %1$s called for despatchAdviceId %2$s",
                request.getRequestURL(), despatchAdviceId));
        return new ResponseEntity<>(despatchLogisticUnitService.readByDespatchAdviceId(despatchAdviceId), HttpStatus.OK);
    }

    @Operation(
            summary = "Returns Despatch Logistic Unit defined with the id",
            responses = {
                    @ApiResponse(responseCode = "200", description = "DespatchLogisticUnit with the given Id was successfully returned", content = @Content(schema = @Schema(implementation = DespatchLogisticUnitDto.class))),
                    @ApiResponse(responseCode = "404", description = "DespatchLogisticUnit with the given Id was not found")
            }
    )
    @GetMapping("api/v0/despatchlogisticunits/{despatchLogisticUnitId}")
    public ResponseEntity<DespatchLogisticUnitDto> getDespatchLogisticUnit(@PathVariable("despatchLogisticUnitId") Integer id) throws DespatchLogisticUnitNotFoundException {

        log.debug(String.format("Received a request to retrieve the Despatch LogisticUnit with the Id %d", id));
        validateDespatchLogisticUnitId(id);
        return new ResponseEntity<>(despatchLogisticUnitService.read(id), HttpStatus.OK);
    }

    @Operation(
            summary = "Create a new despatch Logistic Unit",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Despatch Logistic Unit successfully created", content = @Content(schema = @Schema(implementation = DespatchLogisticUnitDto.class))),
                    @ApiResponse(responseCode = "400", description = "Request Body validation error"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    @PostMapping("/api/v0/despatchadvices/{despatchAdviceId}/despatchlogisticunits")
    public ResponseEntity<DespatchLogisticUnitDto> createDespatchLogisticUnit(@PathVariable ("despatchAdviceId") Integer despatchAdviceId, @Valid @RequestBody DespatchLogisticUnitDto despatchLogisticUnitDto, HttpServletRequest request) {

        despatchLogisticUnitDto.setDespatchAdviceId(despatchAdviceId);

        log.debug(String.format("DespatchLogisticUnitController create DespatchLogisticUnit at %1$s called for despatchAdviceId %2$s with payload %3$s",
                request.getRequestURL(), despatchAdviceId, despatchLogisticUnitDto));
        if(despatchLogisticUnitDto.getParentId() != null) {

            validateParentId(despatchLogisticUnitDto.getParentId());
        }
        if(despatchLogisticUnitDto.getDespatchAdviceId() != null) {

            validateDespatchAdviceId(despatchLogisticUnitDto.getDespatchAdviceId());
        }
        return new ResponseEntity<>(despatchLogisticUnitService.create(despatchLogisticUnitDto), HttpStatus.OK);
    }

    @Operation(
            summary = "Updates an existing despatch Logistic Unit defined by a given Id",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Despatch Logistic Unit successfully updated", content = @Content(schema = @Schema(implementation = DespatchLogisticUnitDto.class))),
                    @ApiResponse(responseCode = "400", description = "Request Body validation error"),
                    @ApiResponse(responseCode = "404", description = "Despatch Logistic Unit wth the given Id was not found")
            }
    )
    @PutMapping("api/v0/despatchlogisticunits/{despatchLogisticUnitId}")
    public ResponseEntity<DespatchLogisticUnitDto> updateCustomer(@PathVariable("despatchLogisticUnitId") Integer id, @Valid @RequestBody DespatchLogisticUnitDto despatchLogisticUnitDto) throws DespatchLogisticUnitNotFoundException {

        despatchLogisticUnitDto.setId(id);
        log.debug(String.format("Received a request to update the despatch Logistic Unit with the Id %d with the given details %s", id, despatchLogisticUnitDto));
        validateDespatchLogisticUnitId(id);
        if(despatchLogisticUnitDto.getParentId() != null) {

            validateParentId(despatchLogisticUnitDto.getParentId());
        }
        if(despatchLogisticUnitDto.getDespatchAdviceId() != null) {

            validateDespatchAdviceId(despatchLogisticUnitDto.getDespatchAdviceId());
        }
        return new ResponseEntity<>(despatchLogisticUnitService.update(id, despatchLogisticUnitDto), HttpStatus.OK);
    }

    @SuppressWarnings("rawtypes")
    @Operation(
            summary = "Delete a Despatch Logistic Unit",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Despatch Logistic Unit successfully deleted", content = @Content(schema = @Schema(implementation = DespatchLogisticUnitDto.class))),
                    @ApiResponse(responseCode = "404", description = "Despatch Logistic Unit with given Id does not exist"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    @DeleteMapping("/api/v0/despatchlogisticunits/{despatchLogisticUnitId}")
    public ResponseEntity deleteDespatchLogisticUnit(@PathVariable("despatchLogisticUnitId") Integer id, HttpServletRequest request) {

        log.debug(String.format("DespatchLogisticUnitController delete Despatch LogisticUnit at %1$s called",
                request.getRequestURL()));
        validateDespatchLogisticUnitId(id);
        despatchLogisticUnitService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private void validateDespatchLogisticUnitId(Integer id) {

        if(!despatchLogisticUnitService.exists(id)) {

            log.warn(String.format("DespatchLogisticUnit with Id %d was not found", id));
            throw new DespatchLogisticUnitNotFoundException();
        }
    }

    private void validateDespatchAdviceId(Integer id) {

        if(!despatchAdviceService.exists(id)) {

            log.warn(String.format("DespatchAdvice with Id %d was not found", id));
            throw new DespatchAdviceNotFoundException();
        }
    }

    private void validateParentId(Integer id) {

        if(!despatchLogisticUnitService.exists(id)) {

            log.warn(String.format("ParentId with Id %d was not found", id));
            throw new DespatchLogisticUnitParentNotFoundException();
        }
    }

    private void validateProductId(String id) {

        if(!productsService.exists(id)) {

            log.warn(String.format("Product with Id %s was not found", id));
            throw new ProductNotFoundException();
        }
    }
}
