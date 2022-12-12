package org.knowledge4retail.api.store.model;

import lombok.*;

@AllArgsConstructor
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class StoreAggregate {
    private Store store;
    private Long shelfCount;
    private Long shelfLayerCount;
    private Long barcodeCount;
    private Long productCount;
}
