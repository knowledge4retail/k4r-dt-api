package org.knowledge4retail.api.store.filter;

import lombok.Data;
import org.knowledge4retail.api.shared.filter.FilterField;

@Data
public class TrolleyFilter {

    private FilterField id;
    private FilterField storeId;
    private FilterField layers;
    private FilterField width;
    private FilterField height;
    private FilterField depth;
}
