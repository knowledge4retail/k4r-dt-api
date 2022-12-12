package org.knowledge4retail.api.service.impl;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.knowledge4retail.api.device.model.DeviceStatus;
import org.knowledge4retail.api.device.repository.DeviceStatusRepository;
import org.knowledge4retail.api.device.service.DeviceStatusService;
import org.knowledge4retail.api.device.service.impl.DeviceStatusServiceImpl;
import org.knowledge4retail.api.shared.producer.kafka.DefaultProducer;
import org.mockito.AdditionalAnswers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class DeviceStatusServiceImplTest {

    @Mock
    private DeviceStatusRepository repository;
    @Mock
    private Validator validator;
    @Mock
    DefaultProducer producer;

    private DeviceStatusService statusService;

    private final String validRobotStatusAsJson =
            "{\"rosMetadata\":{\"deviceId\":\"mir_robot\",\"timestamp\":1233453453,\"frameId\":\"world\"}," +
                    "\"pose\":{\"position\":{\"x\":123.4,\"y\":456.7,\"z\":789}," +
                    "\"orientation\":{\"x\":1.2,\"y\":2.3,\"z\":3.4,\"w\":4.5}}}";


    @BeforeEach
    public void setUp() {

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        this.statusService = new DeviceStatusServiceImpl(repository, validator, producer);
    }

    @Test
    public void createSendsMessageToKafka() {

        statusService.create(validRobotStatusAsJson);

        verify(producer).publishCreate(any(), any());
    }


    @Test
    public void saveStatusMapsJsonToDeviceStatusObject() {

        when(repository.save(any())).thenAnswer(AdditionalAnswers.returnsFirstArg());

        DeviceStatus deviceStatus = this.statusService.create(validRobotStatusAsJson);


        verify(repository).save(deviceStatus);

        assertEquals("mir_robot", deviceStatus.getDeviceId());
        assertEquals(1233453453, deviceStatus.getTimestamp());
        assertEquals("world", deviceStatus.getFrameId());
        
        assertEquals(1.2d, deviceStatus.getOrientationX());
        assertEquals(2.3d, deviceStatus.getOrientationY());
        assertEquals(3.4d, deviceStatus.getOrientationZ());
        assertEquals(4.5d, deviceStatus.getOrientationW());

        assertEquals(123.4d, deviceStatus.getPositionX());
        assertEquals(456.7d, deviceStatus.getPositionY());
        assertEquals(789d, deviceStatus.getPositionZ());
    }


    @Test()
    public void saveStatusThrowsExceptionWhenMandatoryFieldsDoNotExist() {

        String robotStatusAsJson =
                "{\"rosMetadata\":{\"timestamp\":1233453453,\"frameId\":\"world\"}," +
                        "\"pose\":{\"position\":{\"x\":123.4,\"y\":456.7,\"z\":789}," +
                        "\"orientation\":{\"x\":1.2,\"y\":2.3,\"z\":3.4,\"w\":4.5}}}";


        Assertions.assertThrows(ConstraintViolationException.class, () -> this.statusService.create(robotStatusAsJson));
    }

    @Test
    public void saveStatusRetrunsNullWhenJsonCouldNotBeParsed()
    {
        String invalidJSON =
                "{\"deviceId\":\"mir_robot\",\"timestamp\":1233453453,\"frameId\":\"world\"}," +
                        "\"pose\":{\"position\":{\"x\":123.4,\"y\":456.7,\"z\":789}," +
                        "\"orientation\":{\"x\":1.2,\"y\":2.3,\"z\":3.4,\"w\":4.5}}}";

        DeviceStatus deviceStatus = this.statusService.create(invalidJSON);

        Assertions.assertNull(deviceStatus);
    }


}
