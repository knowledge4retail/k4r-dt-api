package org.knowledge4retail.api.store.filter;

import lombok.Data;
import org.knowledge4retail.api.shared.filter.FilterField;

@Data
public class ShelfFilter {

    private FilterField id;
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
    private FilterField productGroupId;
    private FilterField cadPlanId;
    private FilterField blockId;
    private FilterField runningNumber;
    private FilterField externalReferenceId;
    private FilterField lengthUnitId;
}
