package org.knowledge4retail.api.device.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.imaging.ImageFormat;
import org.apache.commons.imaging.ImageFormats;
import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.Imaging;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.knowledge4retail.api.device.dto.DeviceImageDto;
import org.knowledge4retail.api.device.exception.DeviceNotFoundException;
import org.knowledge4retail.api.device.model.DeviceImage;
import org.knowledge4retail.api.device.service.DeviceImageService;
import org.knowledge4retail.api.device.service.DeviceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import javax.validation.Validator;
import java.io.IOException;
import java.util.List;
import java.util.Set;

@Slf4j
@RequiredArgsConstructor
@RestController
public class SensorDataController {

    private final DeviceImageService imageService;
    private final DeviceService deviceService;
    private final Validator validator;

    @Operation(
            summary = "Uploads a new Image captured by a device (request part \"image\"), along with Pose Information (request part \"data\").",
            description = "Accepted Images are of type JPG or PNG, both request parts are compulsory!",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Image and Pose successfully uploaded and created", content = @Content(schema = @Schema(implementation = DeviceImage.class))),
                    @ApiResponse(responseCode = "400", description = "Missing Request Parameter or invalid image"),
                    @ApiResponse(responseCode = "500", description = "Internal server error"),
            }
    )
    @PostMapping(path = "/api/v0/devices/{deviceId}/images", consumes = "multipart/form-data")
    public ResponseEntity saveImage(@ModelAttribute @Valid @RequestParam("data") String dtoJson, @RequestParam("image") MultipartFile image, @PathVariable("deviceId") String deviceId) throws IOException {

        validateDeviceId(deviceId);
        DeviceImageDto dto;

        try {
            dto = getDeviceImageDto(dtoJson);
        } catch (JsonProcessingException | ConstraintViolationException e) {
            log.warn("JSON in (data) part is not valid", e);
            return new ResponseEntity<>(String.format("JSON in (data) part is not valid. Exception Message: %s", e.getMessage()), HttpStatus.BAD_REQUEST);
        }

        if (image.isEmpty()) {
            return new ResponseEntity<>("Image file may not be empty!", HttpStatus.BAD_REQUEST);
        }

        if (!IsValidImage(image)) {
            return new ResponseEntity<>("The uploaded file is not a valid image file!", HttpStatus.BAD_REQUEST);
        }

        dto.setImageName(image.getOriginalFilename());
        dto.setImage(image.getBytes());

        return new ResponseEntity<>(this.imageService.create(dto), HttpStatus.OK);
    }

    @Operation(
            summary = "Returns a list of all ImageDtos of a certain device, defined with device Id,  which have the matching values for the parameters labelName and labelId ",
            responses = {
                    @ApiResponse(responseCode = "200", description = "List of matching results (DeviceImages) returned", content = @Content(schema = @Schema(implementation = DeviceImage.class))),
                    @ApiResponse(responseCode = "400", description = "Missing Request Parameter"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    @GetMapping(path = "/api/v0/devices/{deviceId}/images")
    public ResponseEntity<List<DeviceImageDto>> getImageListByLabelNameAndLabelId(@PathVariable("deviceId") String deviceId, @RequestParam String labelName, @RequestParam String labelId) {

        validateDeviceId(deviceId);
        return new ResponseEntity<>(this.imageService.readByLabelTypeAndLabelId(labelName, labelId), HttpStatus.OK);
    }

    @Operation(
            summary = "Returns an Image of a certain device, defined with {deviceId},  which has the Id {imageId} ",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Image defined with the given Id from blob storage"),
                    @ApiResponse(responseCode = "400", description = "Missing Request Parameter"),
                    @ApiResponse(responseCode = "404", description = "Image with the given Id was not found"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    @GetMapping(path = "/api/v0/devices/{deviceId}/images/{imageId}")
    public void getImageById(@PathVariable("deviceId") String deviceId, @PathVariable("imageId") Integer imageId, HttpServletResponse response) throws IOException {

        validateDeviceId(deviceId);
        response.setContentType("image/*");
        IOUtils.copy(this.imageService.read(imageId), response.getOutputStream() );
    }


    private boolean IsValidImage(MultipartFile image) throws IOException {
        ImageFormat mimeType = Imaging.guessFormat(image.getBytes());
        return mimeType == ImageFormats.JPEG || mimeType == ImageFormats.PNG;
    }


    private DeviceImageDto getDeviceImageDto(String dtoJson) throws JsonProcessingException {

        DeviceImageDto dto = new ObjectMapper().readValue(dtoJson, DeviceImageDto.class);

        Set<ConstraintViolation<DeviceImageDto>> violations = validator.validate(dto);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
        return dto;
    }

    private void validateDeviceId(String deviceId) {

        if (!deviceService.exists(deviceId)) {

            log.warn("device with id {} does not exist", deviceId);
            throw new DeviceNotFoundException();
        }
    }
}
