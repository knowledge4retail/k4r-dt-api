package org.knowledge4retail.api.device.filter;

import lombok.Data;
import org.knowledge4retail.api.shared.filter.FilterField;

@Data
public class DeviceStatusFilter {

    private FilterField deviceId;
    private FilterField timestamp;
    private FilterField frameId;
    private FilterField positionX;
    private FilterField positionY;
    private FilterField positionZ;
    private FilterField orientationX;
    private FilterField orientationY;
    private FilterField orientationZ;
    private FilterField orientationW;
}
