package org.knowledge4retail.api.store.filter;

import lombok.Data;
import org.knowledge4retail.api.shared.filter.FilterField;

@Data
public class StoreObjectFilter {

    private FilterField id;
    private FilterField type;
    private FilterField description;
    private FilterField locationTimestamp;
    private FilterField positionX;
    private FilterField positionY;
    private FilterField positionZ;
    private FilterField orientationX;
    private FilterField orientationY;
    private FilterField orientationZ;
    private FilterField orientationW;
    private FilterField width;
    private FilterField height;
    private FilterField depth;
    private FilterField storeId;
    private FilterField lengthUnitId;
}
