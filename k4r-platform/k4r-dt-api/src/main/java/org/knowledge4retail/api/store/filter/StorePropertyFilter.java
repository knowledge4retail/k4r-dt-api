package org.knowledge4retail.api.store.filter;

import lombok.Data;
import org.knowledge4retail.api.shared.filter.FilterField;

@Data
public class StorePropertyFilter {

    private FilterField id;
    private FilterField characteristicId;
    private FilterField storeId;
    private FilterField valueLow;
    private FilterField valueHigh;
}
