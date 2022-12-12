package org.knowledge4retail.api.store.filter;

import lombok.Data;
import org.knowledge4retail.api.shared.filter.FilterField;

@Data
public class PlanogramFilter {

    private FilterField id;
    private FilterField storeId;
    private FilterField referenceId;
    private FilterField timestamp;
    private FilterField dataFormat;
}
