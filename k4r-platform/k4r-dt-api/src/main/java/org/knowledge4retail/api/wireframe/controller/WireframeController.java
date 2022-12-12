package org.knowledge4retail.api.wireframe.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.knowledge4retail.api.wireframe.dto.WireframeDto;
import org.knowledge4retail.api.wireframe.model.Wireframe;
import org.knowledge4retail.api.wireframe.service.WireframeService;
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
public class WireframeController {

    private final WireframeService WireframeService;

    @Operation(
            summary = "Uploads a Wireframe by id (GTIN) and the corresponding WireframeFile.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Wireframe successfully uploaded and created", content = @Content(schema = @Schema(implementation = Wireframe.class))),
                    @ApiResponse(responseCode = "400", description = "Request Body validation error or invalid data format"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    @PostMapping(path = "api/v0/wireframes", consumes = "multipart/form-data")
    public ResponseEntity<Wireframe> createWireframe(@RequestParam("gTIN") String gTIN, @RequestParam("wireframeFile") MultipartFile WireframeFile, HttpServletRequest request) throws IOException {

        WireframeDto WireframeDto = new WireframeDto();

        String dataFormat = getDataFormat(Objects.requireNonNull(WireframeFile.getOriginalFilename()));
        WireframeDto.setGTIN(gTIN);
        WireframeDto.setDataFormat(dataFormat);
        WireframeDto.setTimestamp(OffsetDateTime.now());

        log.debug(String.format("WireframeController create Wireframe at %1$s called with payload %2$s and a WireframeData",
                request.getRequestURL(), WireframeDto));
        WireframeDto.setWireframe(WireframeFile.getBytes());

        return new ResponseEntity<>(WireframeService.create(WireframeDto), HttpStatus.OK);
    }

    @Operation(
            summary = "Returns a list all Wireframes",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Wireframes successfully returned, if any", content = @Content(array = @ArraySchema(schema = @Schema(implementation = WireframeDto.class)))),
                    @ApiResponse(responseCode = "404", description = "No Wireframes found"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    @GetMapping("api/v0/wireframes")
    public ResponseEntity<List<Wireframe>> getAllWireframes(HttpServletRequest request) {

        log.debug(String.format("WireframeController getAllWireframes at %1$s called",
                request.getRequestURL()));

        return new ResponseEntity<>(WireframeService.readAll(), HttpStatus.OK);
    }

    @Operation(
            summary = "Returns Wireframe defined with the id (GTIN)",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Wireframe with the given Id was successfully returned"),
                    @ApiResponse(responseCode = "404", description = "Wireframe with the given Id was not found"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    @GetMapping("api/v0/wireframes/{GTIN}")
    public ResponseEntity<ByteArrayResource> getWireframeById(@PathVariable("GTIN") String gTIN, HttpServletRequest request, HttpServletResponse response) {

        log.debug(String.format("WireframeController getWireframeById for GTIN: %2$s at %1$s called",
                request.getRequestURL(), gTIN));

        Wireframe Wireframe = WireframeService.readById(gTIN);
        String blobUrl = Wireframe.getBlobUrl();

        HttpHeaders headers = setFileNameAndContentType(blobUrl);

        ByteArrayResource resource = new ByteArrayResource(WireframeService.getWireframeFile(blobUrl));

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

    private String getDataFormat(String fileName) {

        return fileName.substring(fileName.lastIndexOf(".")+1);
    }

}
