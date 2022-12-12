package org.knowledge4retail.api.store.filter;

import lombok.Data;
import org.knowledge4retail.api.shared.filter.FilterField;

@Data
public class BarcodeFilter {

    private FilterField id;
    private FilterField shelfLayerId;
    private FilterField productGtinId;
    private FilterField positionX;
    private FilterField positionY;
    private FilterField positionZ;
    private FilterField orientationX;
    private FilterField orientationY;
    private FilterField orientationZ;
    private FilterField orientationW;
    private FilterField lengthUnitId;
}
