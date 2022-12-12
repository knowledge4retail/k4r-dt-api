package org.knowledge4retail.api.store.filter;

import lombok.Data;
import org.knowledge4retail.api.shared.filter.FilterField;

@Data
public class StoreCharacteristicFilter {

    private FilterField id;
    private FilterField name;
    private FilterField code;
}
