package org.knowledge4retail.api.store.model;

import lombok.*;
import org.knowledge4retail.api.scan.model.ScanKey;

import javax.persistence.*;
import java.time.OffsetDateTime;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Builder
@AllArgsConstructor
@Table(name = "sales")
@IdClass(SalesKey.class)
public class Sales {

    @Id
    @Column(name = "gtin")
    private String gtin;
    @Id
    @Column(name = "timestamp")
    private OffsetDateTime timestamp;
    @Id
    @Column(name = "store_id")
    private Integer storeId;

    @Column(name = "sold_units")
    private Double soldUnits;
    @Column(name = "turnover")
    private Double turnover;
    @Column(name = "time_resolution")
    private String timeResolution;
    @Column(name = "type")
    private String type;
}
