package org.knowledge4retail.api.store.filter;

import lombok.Data;
import org.knowledge4retail.api.shared.filter.FilterField;

@Data
public class DeliveredUnitFilter {

    private FilterField id;
    private FilterField trolleyId;
    private FilterField productUnitId;
    private FilterField productGtinId;
    private FilterField trolleyLayer;
    private FilterField palletId;
    private FilterField sortingState;
    private FilterField sortingDate;
    private FilterField amountUnit;
    private FilterField amountItems;
    private FilterField width;
    private FilterField height;
    private FilterField depth;
}
