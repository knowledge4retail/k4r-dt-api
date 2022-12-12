package org.knowledge4retail.api.device.filter;

import lombok.Data;
import org.knowledge4retail.api.shared.filter.FilterField;

@Data
public class Map2dFilter {

    private FilterField id;
    private FilterField timestamp;
    private FilterField frameId;
    private FilterField deviceId;
    private FilterField resolution;
    private FilterField width;
    private FilterField height;
    private FilterField positionX;
    private FilterField positionY;
    private FilterField positionZ;
    private FilterField orientationX;
    private FilterField orientationY;
    private FilterField orientationZ;
    private FilterField orientationW;
    private FilterField data;
    private FilterField storeId;
    private FilterField lengthUnitId;
}
