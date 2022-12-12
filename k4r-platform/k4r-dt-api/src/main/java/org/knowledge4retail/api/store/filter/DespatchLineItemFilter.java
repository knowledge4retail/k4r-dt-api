package org.knowledge4retail.api.store.filter;

import lombok.Data;
import org.knowledge4retail.api.shared.filter.FilterField;

@Data
public class DespatchLineItemFilter {

    private FilterField id;
    private FilterField despatchLogisticUnitId;
    private FilterField productId;
    private FilterField requestedProductId;
    private FilterField lineItemNumber;
    private FilterField despatchQuantity;
}
