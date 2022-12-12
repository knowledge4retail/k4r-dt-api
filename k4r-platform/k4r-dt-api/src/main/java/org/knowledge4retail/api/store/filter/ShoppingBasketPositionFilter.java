package org.knowledge4retail.api.store.filter;

import lombok.Data;
import org.knowledge4retail.api.shared.filter.FilterField;

@Data
public class ShoppingBasketPositionFilter {

    private FilterField id;
    private FilterField productId;
    private FilterField storeId;
    private FilterField customerId;
    private FilterField sellingPrice;
    private FilterField quantity;
    private FilterField currency;
}
