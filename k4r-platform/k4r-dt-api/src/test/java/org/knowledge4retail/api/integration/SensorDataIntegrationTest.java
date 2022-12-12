package org.knowledge4retail.api.integration;

import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.knowledge4retail.api.device.dto.DeviceImageDto;
import org.knowledge4retail.api.test.AbstractIntegrationTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.knowledge4retail.api.test.Data.DEVICE_ID;

public class SensorDataIntegrationTest extends AbstractIntegrationTest {


    public static final String URL_TEMPLATE = "/api/v0/devices/";

    private String dtoJson;

    @BeforeEach
    @Override
    public void setUp() throws Exception {
        super.setUp();
    }

    @Test
    public void saveImageReturns400WhenNoFileWasUploaded() throws Exception {

        // Generate a request without an Image
        MockMultipartFile emptyFile = new MockMultipartFile("image", "file.jpg", MediaType.IMAGE_JPEG_VALUE, new byte[]{});

        MvcResult mvcResult = mvc.perform(
                MockMvcRequestBuilders.multipart(URL_TEMPLATE + DEVICE_ID + "/images")
                        .file(emptyFile)
                        .param("data","{}")
        ).andReturn();

        // Assert
        assertResponseCodeEquals(HttpStatus.BAD_REQUEST, mvcResult);

    }

    @Test
    public void saveImageReturns400IfNoDataPartWasPresent() throws Exception {

        MockMultipartFile image = new MockMultipartFile("image", "file.jpg", MediaType.IMAGE_JPEG_VALUE, new byte[]{1,2,3});

        MvcResult mvcResult = mvc.perform(
                MockMvcRequestBuilders.multipart(URL_TEMPLATE + DEVICE_ID + "/images")
                        .file(image)

        ).andReturn();

        // Assert
        assertResponseCodeEquals(HttpStatus.BAD_REQUEST, mvcResult);

    }




    @Test
    public void saveImageReturns400IfJsonCouldNotBeParsed() throws Exception {

        MockMultipartFile image = new MockMultipartFile("image", "file.jpg", MediaType.IMAGE_JPEG_VALUE, new byte[]{1,2,3});

        MvcResult mvcResult = mvc.perform(
                MockMvcRequestBuilders.multipart(URL_TEMPLATE + DEVICE_ID + "/images")
                        .file(image)
                        .param("data","{\"test\":}")
        ).andReturn();

        // Assert
        assertResponseCodeEquals(HttpStatus.BAD_REQUEST, mvcResult);
        MatcherAssert.assertThat(mvcResult.getResponse().getContentAsString(), CoreMatchers.containsString("JSON in (data) part is not valid"));
    }

    @Test
    public void saveImageReturns400IfJsonIsNotValidObject() throws Exception {

        MockMultipartFile image = new MockMultipartFile("image", "file.jpg", MediaType.IMAGE_JPEG_VALUE, new byte[]{1,2,3});

        dtoJson = """
                {  "rosMetadata": {
                    "deviceId": "ROB-1000",
                    "frameId": "234",
                    "timestamp": 55
                  },
                  "device_pose": {
                    "orientation": {
                      "w": 1,
                      "x": 2,
                      "y": 3,
                      "z":40
                    },
                    "position": {
                      "x": 4,
                      "y":2,
                      "z": 2
                    }
                  },
                  "camera_pose": {
                    "orientation": {
                      "w": 1,
                      "x": 2,
                      "y": 3,
                      "z":40
                    },
                    "position": {
                      "y":2,
                      "z": 2
                    }
                  }
                }""";
        MvcResult mvcResult = mvc.perform(
                MockMvcRequestBuilders.multipart(URL_TEMPLATE + DEVICE_ID +"/images")
                        .file(image)
                        .param("data", dtoJson)
        ).andReturn();

        // Assert
        assertResponseCodeEquals(HttpStatus.BAD_REQUEST, mvcResult);
        MatcherAssert.assertThat(mvcResult.getResponse().getContentAsString(), CoreMatchers.containsString("JSON in (data) part is not valid"));

    }

    @Test
    public void saveImageReturns400IfUploadedFileIsNotImage() throws Exception {

        MockMultipartFile image = new MockMultipartFile("image", "file.jpg", MediaType.IMAGE_JPEG_VALUE, new byte[]{1,2,3,4,5,6});

        dtoJson = """
                {  "rosMetadata": {
                    "deviceId": "ROB-1000",
                    "frameId": "234",
                    "timestamp": 55
                  },
                  "device_pose": {
                    "orientation": {
                      "w": 1,
                      "x": 2,
                      "y": 3,
                      "z":40
                    },
                    "position": {
                      "x": 4,
                      "y":2,
                      "z": 2
                    }
                  },
                  "camera_pose": {
                    "orientation": {
                      "w": 1,
                      "x": 2,
                      "y": 3,
                      "z":40
                    },
                    "position": {
                      "x":2,
                      "y":2,
                      "z": 2
                    }
                  },
                "label_name": "shelf",
                "label_id":"5"}""";
        MvcResult mvcResult = mvc.perform(
                MockMvcRequestBuilders.multipart(URL_TEMPLATE + DEVICE_ID + "/images")
                        .file(image)
                        .param("data", dtoJson)
        ).andReturn();

        // Assert
        assertResponseCodeEquals(HttpStatus.BAD_REQUEST, mvcResult);
        MatcherAssert.assertThat(mvcResult.getResponse().getContentAsString(), CoreMatchers.containsString("The uploaded file is not a valid image file!"));

    }

    @Test
    public void getImageListsReturn200WithEmptyListWhenNoMatchingImagesWereFound() throws Exception{

        MvcResult result = mvc.perform(MockMvcRequestBuilders
                .get(String.format(URL_TEMPLATE + "%s/images?labelId=%s&labelName=%s", "ROB-1000","999","shelfLayer"))
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();

        assertResponseCodeEquals(HttpStatus.OK, result);

        List<DeviceImageDto> returnedDtos = mapListFromJson(result.getResponse().getContentAsString(), DeviceImageDto.class);

        assertEquals(returnedDtos.size(), 0);

    }

    @Test
    public void getImageReturn404WhenNoMatchingImageWasFound() throws Exception{

        MvcResult result = mvc.perform(MockMvcRequestBuilders
                .get(String.format(URL_TEMPLATE + "%s/images/%d", "ROB-1000",999))
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();

        assertResponseCodeEquals(HttpStatus.NOT_FOUND, result);

    }

}