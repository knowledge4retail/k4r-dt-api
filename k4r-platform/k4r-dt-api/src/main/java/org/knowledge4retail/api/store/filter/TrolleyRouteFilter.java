package org.knowledge4retail.api.store.filter;

import lombok.Data;
import org.knowledge4retail.api.shared.filter.FilterField;

@Data
public class TrolleyRouteFilter {

    private FilterField id;
    private FilterField trolleyId;
    private FilterField sortingDate;
    private FilterField routeOrder;
    private FilterField shelfId;
}
