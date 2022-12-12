package org.knowledge4retail.api.product.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.knowledge4retail.api.product.dto.UnitDto;
import org.knowledge4retail.api.product.exception.UnitNotFoundException;
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
public class UnitController {

    private final UnitService unitService;

    @Operation(
            summary = "get all units",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Units successfully returned", content = @Content(array = @ArraySchema(schema = @Schema(implementation = UnitDto.class)))),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    @GetMapping("/api/v0/units")
    public ResponseEntity<List<UnitDto>> getAllUnits(HttpServletRequest request) {

        log.debug(String.format("UnitController getAllUnits at %1$s called",
                request.getRequestURL()));
        return new ResponseEntity<>(unitService.readAll(), HttpStatus.OK);
    }

    @Operation(
            summary = "create a new init",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Unit successfully created", content = @Content(schema = @Schema(implementation = UnitDto.class))),
                    @ApiResponse(responseCode = "400", description = "Request Body validation error"),
                    @ApiResponse(responseCode = "500", description = "Internal server error"),
            }
    )
    @PostMapping("/api/v0/units")
    public ResponseEntity<UnitDto> createUnit(@Valid @RequestBody UnitDto unitDto, HttpServletRequest request) {

        log.debug(String.format("UnitController createUnit at %1$s called with payload %2$s",
                request.getRequestURL(), unitDto.toString()));

        return new ResponseEntity<>(unitService.create(unitDto), HttpStatus.OK);
    }

    @Operation(
            summary = "Delete a unit",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Unit successfully deleted", content = @Content(schema = @Schema(implementation = UnitDto.class))),
                    @ApiResponse(responseCode = "404", description = "Unit with given Id does not exist"),
                    @ApiResponse(responseCode = "500", description = "Internal server error"),
            }
    )
    @DeleteMapping("/api/v0/units/{unitId}")
    public ResponseEntity deleteUnit(@PathVariable("unitId") Integer id, HttpServletRequest request) {

        log.debug(String.format("UnitController delete unit at %1$s called",
                request.getRequestURL()));
        validateUnitId(id);
        unitService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private void validateUnitId(Integer id) {

        if(!unitService.exists(id)){

            log.warn(String.format("Unit with Id %d was not found", id));
            throw new UnitNotFoundException();
        }
    }
}
