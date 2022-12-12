package org.knowledge4retail.api.product.filter;

import lombok.Data;
import org.knowledge4retail.api.shared.filter.FilterField;

@Data
public class ProductPropertyFilter {

    private FilterField id;
    private FilterField productId;
    private FilterField characteristicId;
    private FilterField storeId;
    private FilterField valueLow;
    private FilterField valueHigh;
}
