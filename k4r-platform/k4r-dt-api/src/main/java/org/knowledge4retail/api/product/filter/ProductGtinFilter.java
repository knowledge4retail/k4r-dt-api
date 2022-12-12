package org.knowledge4retail.api.product.filter;

import lombok.Data;
import org.knowledge4retail.api.shared.filter.FilterField;

@Data
public class ProductGtinFilter {

    private FilterField id;
    private FilterField gtin;
    private FilterField productUnitId;
    private FilterField gtinType;
    private FilterField mainGtinFlag;
    private FilterField externalReferenceId;
}
