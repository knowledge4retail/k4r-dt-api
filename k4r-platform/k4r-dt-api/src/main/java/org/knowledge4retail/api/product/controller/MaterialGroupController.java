package org.knowledge4retail.api.product.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.knowledge4retail.api.product.dto.MaterialGroupDto;
import org.knowledge4retail.api.product.exception.MaterialGroupNotFoundException;
import org.knowledge4retail.api.product.exception.ParentMaterialGroupNotFoundException;
import org.knowledge4retail.api.product.service.MaterialGroupService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
public class MaterialGroupController {

    private final MaterialGroupService materialGroupService;

    @Operation(
            summary = "get all material groups",
            responses = {
                    @ApiResponse(responseCode = "200", description = "material groups successfully returned", content = @Content(array = @ArraySchema(schema = @Schema(implementation = MaterialGroupDto.class)))),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    @GetMapping("/api/v0/materialgroups")
    public ResponseEntity<List<MaterialGroupDto>> getAllMaterialGroups(HttpServletRequest request) {

        log.debug(String.format("MaterialGroupController getAllMaterialgroups at %1$s called",
                request.getRequestURL()));
        return new ResponseEntity<>(materialGroupService.readAll(), HttpStatus.OK);
    }

    @Operation(
            summary = "Returns material group defined with the id",
            responses = {
                    @ApiResponse(responseCode = "200", description = "MaterialGroup with the given Id was successfully returned", content = @Content(schema = @Schema(implementation = MaterialGroupDto.class))),
                    @ApiResponse(responseCode = "404", description = "MaterialGroup with the given Id was not found"),
                    @ApiResponse(responseCode = "500", description = "Internal server error"),
            }
    )
    @GetMapping("api/v0/materialgroups/{materialGroupId}")
    public ResponseEntity<MaterialGroupDto> getMaterialGroup(@PathVariable("materialGroupId") Integer id) throws MaterialGroupNotFoundException {

        log.debug(String.format("Received a request to retrieve the MaterialGroup with the Id %d", id));
        validateMaterialGroupId(id);
        return new ResponseEntity<>(materialGroupService.read(id), HttpStatus.OK);
    }

    @Operation(
            summary = "create a new material group",
            responses = {
                    @ApiResponse(responseCode = "200", description = "material group successfully created", content = @Content(schema = @Schema(implementation = MaterialGroupDto.class))),
                    @ApiResponse(responseCode = "400", description = "Request Body validation error"),
                    @ApiResponse(responseCode = "500", description = "Internal server error"),
            }
    )
    @PostMapping("/api/v0/materialgroups")
    public ResponseEntity<MaterialGroupDto> createMaterialGroup(@Valid @RequestBody MaterialGroupDto materialGroupDto, HttpServletRequest request) {

        log.debug(String.format("MaterialGroupController createMaterialGroup at %1$s called with payload %2$s",
                request.getRequestURL(), materialGroupDto.toString()));
        if(materialGroupDto.getParentId() != null) {

            validateParentMaterialGroupId(materialGroupDto.getParentId());
        }
        return new ResponseEntity<>(materialGroupService.create(materialGroupDto), HttpStatus.OK);
    }

    @Operation(
            summary = "Updates an existing material group defined by a given Id",
            responses = {
                    @ApiResponse(responseCode = "200", description = "MaterialGroup successfully updated", content = @Content(schema = @Schema(implementation = MaterialGroupDto.class))),
                    @ApiResponse(responseCode = "400", description = "Request Body validation error"),
                    @ApiResponse(responseCode = "404", description = "MaterialGroup wth the given Id was not Found"),
                    @ApiResponse(responseCode = "500", description = "Internal server error"),
            }
    )
    @PutMapping("api/v0/materialgroups/{materialGroupId}")
    public ResponseEntity<MaterialGroupDto> updateMaterialGroup(@PathVariable("materialGroupId") Integer id, @Valid @RequestBody MaterialGroupDto materialGroupDto) throws MaterialGroupNotFoundException {

        materialGroupDto.setId(id);
        log.debug(String.format("Received a request to update the material group with the Id %d with the given details %s", id, materialGroupDto));
        validateMaterialGroupId(id);
        if(materialGroupDto.getParentId() != null) {

            validateParentMaterialGroupId(materialGroupDto.getParentId());
        }
        return new ResponseEntity<>(materialGroupService.update(id, materialGroupDto), HttpStatus.OK);
    }

    @Operation(
            summary = "Delete a material group",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Material group successfully deleted", content = @Content(schema = @Schema(implementation = MaterialGroupDto.class))),
                    @ApiResponse(responseCode = "404", description = "Material group with given Id does not exist"),
                    @ApiResponse(responseCode = "500", description = "Internal server error"),
            }
    )
    @DeleteMapping("/api/v0/materialgroups/{materialGroupId}")
    public ResponseEntity deleteMaterialGroup(@PathVariable("materialGroupId") Integer id, HttpServletRequest request) {

        log.debug(String.format("MaterialGroupController deleteMaterialGroup at %1$s called",
                request.getRequestURL()));
        validateMaterialGroupId(id);
        materialGroupService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private void validateMaterialGroupId(Integer id) {

        if(!materialGroupService.exists(id)){

            log.warn("MaterialGroupId does not exist: {}", id);
            throw new MaterialGroupNotFoundException();
        }
    }

    private void validateParentMaterialGroupId(Integer id) {

        if(!materialGroupService.exists(id)){

            log.warn("ParentMaterialGroupId does not exist: {}", id);
            throw new ParentMaterialGroupNotFoundException();
        }
    }
}
