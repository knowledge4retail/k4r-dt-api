package org.knowledge4retail.api.product.filter;

import lombok.Data;
import org.knowledge4retail.api.shared.filter.FilterField;

@Data
public class ProductFilter {

    private FilterField id;
    private FilterField materialGroupId;
    private FilterField productType;
    private FilterField productBaseUnit;
}
