package org.knowledge4retail.api.store.filter;

import lombok.Data;
import org.knowledge4retail.api.shared.filter.FilterField;

@Data
public class FacingFilter {

    private FilterField id;
    private FilterField shelfLayerId;
    private FilterField layerRelativePosition;
    private FilterField noOfItemsWidth;
    private FilterField noOfItemsDepth;
    private FilterField noOfItemsHeight;
    private FilterField minStock;
    private FilterField stock;
    private FilterField misplacedStock;
    private FilterField productUnitId;
    private FilterField externalReferenceId;
}
