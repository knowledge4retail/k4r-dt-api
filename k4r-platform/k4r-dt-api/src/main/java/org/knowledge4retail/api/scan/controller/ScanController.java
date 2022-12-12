package org.knowledge4retail.api.scan.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.knowledge4retail.api.scan.dto.ScanDto;
import org.knowledge4retail.api.scan.exception.ScanNotFoundException;
import org.knowledge4retail.api.scan.service.ScanService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
public class ScanController {

    private final ScanService scanService;

    @Operation(
            summary = "Creates a new Scan and update corresponding dt entity",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Scan successfully created", content = @Content(schema = @Schema(implementation = ScanDto.class))),
                    @ApiResponse(responseCode = "400", description = "Request Body validation error")
            }
    )
    @PostMapping("api/v0/scans")
    public ResponseEntity<ScanDto> createScan(@Valid @RequestBody ScanDto scanDto) {

        log.debug(String.format("Received a request to create a Scan and update corresponding dt entity with the given details %s",
                scanDto.toString()));
        return new ResponseEntity<>(this.scanService.create(scanDto), HttpStatus.OK);

    }

    @Operation(
            summary = "Get all scans",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Scans successfully returned", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ScanDto.class)))),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    @GetMapping("api/v0/scans")
    public ResponseEntity<List<ScanDto>> getAllScans(HttpServletRequest request) {

        log.debug(String.format("ScanController getAllScans at %1$s called",
                request.getRequestURL()));
        return new ResponseEntity<>(scanService.readAll(), HttpStatus.OK);
    }

    @Operation(
            summary = "Returns scan defined with the entityType and the id",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Scan with the given entityType and the Id was successfully returned", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ScanDto.class)))),
                    @ApiResponse(responseCode = "404", description = "Scan with the given entityType and the Id was not found"),
                    @ApiResponse(responseCode = "500", description = "Internal server error"),
            }
    )
    @GetMapping("api/v0/scans/{entityType}/{id}")
    public ResponseEntity<List<ScanDto>> getScan(@PathVariable("entityType") String entityType, @PathVariable("id") String id) throws ScanNotFoundException {

        log.debug(String.format("Received a request to retrieve the scan with of %s the Id %s", entityType, id));
        if(!scanService.exists(entityType, id)) {

            log.warn(String.format("Scan of %s with Id %s was not found", entityType, id));
            throw new ScanNotFoundException();
        }
        return new ResponseEntity<>(scanService.read(entityType, id), HttpStatus.OK);
    }
}
