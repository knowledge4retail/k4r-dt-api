package org.knowledge4retail.api.store.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.knowledge4retail.api.store.dto.PlanogramDto;
import org.knowledge4retail.api.store.exception.PlanogramWrongDataFormatException;
import org.knowledge4retail.api.store.exception.StoreNotFoundException;
import org.knowledge4retail.api.store.model.Planogram;
import org.knowledge4retail.api.store.service.PlanogramService;
import org.knowledge4retail.api.store.service.StoreService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@RestController
public class PlanogramController {

    private final PlanogramService planogramService;
    private final StoreService storeService;

    @Operation(
            summary = "Uploads a planogram by storeId, referenceId and the corresponding planogramFile.",
            description = "Accepted data formats are CSV or XML data",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Planogram successfully uploaded and created", content = @Content(schema = @Schema(implementation = Planogram.class))),
                    @ApiResponse(responseCode = "400", description = "Request Body validation error or invalid data format"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    @PostMapping(path = "api/v0/stores/{storeId}/planograms", consumes = "multipart/form-data")
    public ResponseEntity<Planogram> createPlanogram(@PathVariable("storeId") Integer storeId, @RequestParam("referenceId") String referenceId, @RequestParam("planogramFile") MultipartFile planogramFile, HttpServletRequest request) throws IOException {

        PlanogramDto planogramDto = new PlanogramDto();
        validateStoreId(storeId);

        String dataFormat = getDataFormat(Objects.requireNonNull(planogramFile.getOriginalFilename()));
        validateDataFormat(dataFormat);
        planogramDto.setReferenceId(referenceId);
        planogramDto.setDataFormat(dataFormat);
        planogramDto.setTimestamp(OffsetDateTime.now());

        log.debug(String.format("PlanogramController create Planogram at %1$s called with payload %2$s and a planogramData",
                request.getRequestURL(), planogramDto));
        planogramDto.setPlanogram(planogramFile.getBytes());
        planogramDto.setStoreId(storeId);

        return new ResponseEntity<>(planogramService.create(planogramDto), HttpStatus.OK);
    }

    @Operation(
            summary = "Returns a list planograms of one store",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Planograms successfully returned, if any", content = @Content(array = @ArraySchema(schema = @Schema(implementation = PlanogramDto.class)))),
                    @ApiResponse(responseCode = "404", description = "store with the id not found"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    @GetMapping("api/v0/stores/{storeId}/planograms")
    public ResponseEntity<List<Planogram>> getAllPlanograms(@PathVariable("storeId") Integer storeId, HttpServletRequest request) {

        log.debug(String.format("PlanogramController getAllPlanograms for storeId: %2$d at %1$s called",
                request.getRequestURL(), storeId));

        return new ResponseEntity<>(planogramService.readAll(storeId), HttpStatus.OK);
    }

    @Operation(
            summary = "Returns planogram defined with the id",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Planogram with the given Id was successfully returned"),
                    @ApiResponse(responseCode = "404", description = "Planogram with the given Id was not found"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    @GetMapping("api/v0/planograms/{planogramId}")
    public ResponseEntity<ByteArrayResource> getPlanogramById(@PathVariable("planogramId") Integer planogramId, HttpServletRequest request, HttpServletResponse response) {

        log.debug(String.format("PlanogramController getPlanogramById for planogramId: %2$d at %1$s called",
                request.getRequestURL(), planogramId));

        Planogram planogram = planogramService.readById(planogramId);
        String blobUrl = planogram.getBlobUrl();

        HttpHeaders headers = setFileNameAndContentType(blobUrl);

        ByteArrayResource resource = new ByteArrayResource(planogramService.getPlanogramFile(blobUrl));

        return ResponseEntity.ok()
                .headers(headers)
                .body(resource);
    }

    @NotNull
    private HttpHeaders setFileNameAndContentType(String blobUrl) {

        String fileName = blobUrl.substring(blobUrl.lastIndexOf("/")+1);
        ContentDisposition contentDisposition = ContentDisposition.builder("inline").filename(fileName).build();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentDisposition(contentDisposition);
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

        return headers;
    }


    private void validateStoreId(Integer storeId) {

        if (!storeService.exists(storeId)) {

            log.warn("store with id {} does not exist", storeId);
            throw new StoreNotFoundException();
        }
    }

    private String getDataFormat(String fileName) {

        return fileName.substring(fileName.lastIndexOf(".")+1);
    }

    private void validateDataFormat(String dataFormat) {

        if(!dataFormat.equals("csv") && !dataFormat.equals("xml")){

            log.warn("incorrect data format");
            throw new PlanogramWrongDataFormatException();
        }
    }
}
