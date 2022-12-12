package org.knowledge4retail.api.store.filter;

import lombok.Data;
import org.knowledge4retail.api.shared.filter.FilterField;

@Data
public class DespatchLogisticUnitFilter {

    private FilterField id;
    private FilterField parentId;
    private FilterField despatchAdviceId;
    private FilterField packageTypeCode;
    private FilterField estimatedDelivery;
    private FilterField logisticUnitId;
}
