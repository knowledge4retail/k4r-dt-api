package org.knowledge4retail.api.store.filter;

import lombok.Data;
import org.knowledge4retail.api.shared.filter.FilterField;

@Data
public class ShelfLayerFilter {

    private FilterField id;
    private FilterField shelfId;
    private FilterField level;
    private FilterField type;
    private FilterField positionZ;
    private FilterField width;
    private FilterField height;
    private FilterField depth;
    private FilterField externalReferenceId;
    private FilterField lengthUnitId;
}
