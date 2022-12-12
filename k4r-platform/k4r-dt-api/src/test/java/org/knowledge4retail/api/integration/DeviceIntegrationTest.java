package org.knowledge4retail.api.integration;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.knowledge4retail.api.device.dto.DeviceDto;
import org.knowledge4retail.api.test.AbstractIntegrationTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.knowledge4retail.api.test.Data.*;


public class DeviceIntegrationTest extends AbstractIntegrationTest {

    private static final String DEVICE_URI = "/api/v0/devices";

    @BeforeEach
    @Override
    public void setUp() throws Exception {

        super.setUp();
    }

    @Test
    public void getAllDevices() throws Exception {

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(DEVICE_URI)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        assertResponseCodeEquals(HttpStatus.OK, mvcResult);

        List<DeviceDto> contentList = mapListFromJson(mvcResult.getResponse().getContentAsString(), DeviceDto.class);
        Assertions.assertNotNull(contentList);
        Assertions.assertTrue(contentList.size() > 0);
        Assertions.assertNotNull(contentList.get(0));
        Assertions.assertNotNull(contentList.get(0).getId());
        Assertions.assertNotNull(contentList.get(0).getStoreId());
    }


    @Test
    public void createDeviceTest() throws Exception {

        DeviceDto requestDto = new DeviceDto();
        requestDto.setId(DEVICE_ID);
        requestDto.setStoreId(STORE_ID);
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(DEVICE_URI).contentType(JSON_CONTENT_TYPE).content(mapToJson(requestDto))
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        assertResponseCodeEquals(HttpStatus.OK, mvcResult);
    }

    @Test
    public void createDeviceMissingBody() throws Exception {

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(DEVICE_URI).contentType(JSON_CONTENT_TYPE)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        assertResponseCodeEquals(HttpStatus.BAD_REQUEST, mvcResult);
    }

    @Test
    public void createDeviceWrongBody() throws Exception {

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(DEVICE_URI).contentType(JSON_CONTENT_TYPE).content("test")
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        assertResponseCodeEquals(HttpStatus.BAD_REQUEST, mvcResult);
    }


    @Test
    public void deleteDeviceTest() throws Exception {

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.delete(DEVICE_URI + "/" + DEVICE_ID_DELETE)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        assertResponseCodeEquals(HttpStatus.OK, mvcResult);
    }

    @Test
    public void deleteDeviceMissingID() throws Exception {

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.delete(DEVICE_URI)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        assertResponseCodeEquals(HttpStatus.METHOD_NOT_ALLOWED, mvcResult);
    }

    @Test
    public void deleteDeviceNotExistingID() throws Exception {

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.delete(DEVICE_URI + "/" + DEVICE_ID_404)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        assertResponseCodeEquals(HttpStatus.NOT_FOUND, mvcResult);
    }
}
