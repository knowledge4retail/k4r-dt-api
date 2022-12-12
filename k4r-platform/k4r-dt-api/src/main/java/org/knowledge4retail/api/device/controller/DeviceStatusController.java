package org.knowledge4retail.api.device.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.knowledge4retail.api.device.dto.DeviceStatusDto;
import org.knowledge4retail.api.device.exception.DeviceNotFoundException;
import org.knowledge4retail.api.device.service.DeviceService;
import org.knowledge4retail.api.device.service.DeviceStatusService;
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
public class DeviceStatusController {

    private final DeviceStatusService deviceStatusService;
    private final DeviceService deviceService;

    @Operation(
            summary = "Get all deviceStatuses",
            responses = {
                    @ApiResponse(responseCode = "200", description = "DeviceStatuses successfully returned", content = @Content(array = @ArraySchema(schema = @Schema(implementation = DeviceStatusDto.class)))),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    @GetMapping("api/v0/devices/{deviceId}/statuses")
    public ResponseEntity<List<DeviceStatusDto>> getAllDeviceStatuses(@PathVariable("deviceId") String deviceId, HttpServletRequest request) {

        log.debug(String.format("DeviceStatusController getAllDeviceStatuses at %1$s called for deviceId %2$s",
                request.getRequestURL(), deviceId));
        validateDeviceId(deviceId);

        return new ResponseEntity<>(deviceStatusService.readAll(deviceId), HttpStatus.OK);
    }

    private void validateDeviceId(String id) {

        if(!deviceService.exists(id)){

            log.warn("DeviceId does not exist: {}", id);
            throw new DeviceNotFoundException();
        }
    }
}
