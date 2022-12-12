package org.knowledge4retail.api.device.filter;

import lombok.Data;
import org.knowledge4retail.api.shared.filter.FilterField;

@Data
public class DeviceImageFilter {

    private FilterField id;
    private FilterField timestamp;
    private FilterField frameId;
    private FilterField deviceId;
    private FilterField cameraPositionX;
    private FilterField cameraPositionY;
    private FilterField cameraPositionZ;
    private FilterField cameraOrientationX;
    private FilterField cameraOrientationY;
    private FilterField cameraOrientationZ;
    private FilterField cameraOrientationW;
    private FilterField devicePositionX;
    private FilterField devicePositionY;
    private FilterField devicePositionZ;
    private FilterField deviceOrientationX;
    private FilterField deviceOrientationY;
    private FilterField deviceOrientationZ;
    private FilterField deviceOrientationW;
    private FilterField labelId;
    private FilterField labelName;
    private FilterField blobUrl;
}
