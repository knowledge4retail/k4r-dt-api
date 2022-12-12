package org.knowledge4retail.api.store.filter;

import lombok.Data;
import org.knowledge4retail.api.shared.filter.FilterField;

@Data
public class DespatchAdviceFilter {

    private FilterField id;
    private FilterField storeId;
    private FilterField shipTime;
    private FilterField creationTime;
    private FilterField senderQualifier;
    private FilterField senderId;
    private FilterField documentNumber;
}
