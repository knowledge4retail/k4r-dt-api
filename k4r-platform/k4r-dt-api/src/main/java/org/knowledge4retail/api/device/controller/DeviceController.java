package org.knowledge4retail.api.device.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.knowledge4retail.api.device.dto.DeviceDto;
import org.knowledge4retail.api.device.exception.DeviceNotFoundException;
import org.knowledge4retail.api.device.service.DeviceService;
import org.knowledge4retail.api.store.exception.StoreNotFoundException;
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
public class DeviceController {

    private final DeviceService deviceService;
    private final StoreService storeService;

    @Operation(
            summary = "Get all devices",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Devices successfully returned", content = @Content(array = @ArraySchema(schema = @Schema(implementation = DeviceDto.class)))),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    @GetMapping("api/v0/devices")
    public ResponseEntity<List<DeviceDto>> getAllDevices(HttpServletRequest request) {

        log.debug(String.format("DeviceController getAllDevices at %1$s called",
                request.getRequestURL()));
        return new ResponseEntity<>(deviceService.readAll(), HttpStatus.OK);
    }

    @Operation(
            summary = "Create a new device",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Device successfully created", content = @Content(schema = @Schema(implementation = DeviceDto.class))),
                    @ApiResponse(responseCode = "400", description = "Request Body validation error"),
                    @ApiResponse(responseCode = "500", description = "Internal server error"),
            }
    )
    @PostMapping("/api/v0/devices")
    public ResponseEntity<DeviceDto> createDevice(@Valid @RequestBody DeviceDto deviceDto, HttpServletRequest request) {

        log.debug(String.format("DeviceController createDevice at %1$s called with payload %2$s",
                request.getRequestURL(), deviceDto.toString()));
        if(deviceDto.getStoreId() != null) {

            validateStoreId(deviceDto.getStoreId());
        }
        return new ResponseEntity<>(deviceService.create(deviceDto), HttpStatus.OK);
    }

    @SuppressWarnings("rawtypes")
    @Operation(
            summary = "Delete a device",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Device successfully deleted", content = @Content(schema = @Schema(implementation = DeviceDto.class))),
                    @ApiResponse(responseCode = "404", description = "Device with given Id does not exist"),
                    @ApiResponse(responseCode = "500", description = "Internal server error"),
            }
    )
    @DeleteMapping("/api/v0/devices/{deviceId}")
    public ResponseEntity deleteDevice(@PathVariable("deviceId") String id, HttpServletRequest request) {

        log.debug(String.format("DeviceController deleteDevice at %1$s called",
                request.getRequestURL()));
        validateDeviceId(id);
        deviceService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private void validateDeviceId(String id) {

        if(!deviceService.exists(id)){

            log.warn("DeviceId does not exist: {}", id);
            throw new DeviceNotFoundException();
        }
    }

    private void validateStoreId(Integer storeId) {

        if (!storeService.exists(storeId)) {

            log.warn("store with id {} does not exist", storeId);
            throw new StoreNotFoundException();
        }
    }
}
