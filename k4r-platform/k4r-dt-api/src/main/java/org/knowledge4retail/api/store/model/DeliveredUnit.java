package org.knowledge4retail.api.store.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.OffsetDateTime;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "delivered_unit")
public class DeliveredUnit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "trolley_id")
    private Integer trolleyId;

    @NotNull
    @Column(name = "product_unit_id")
    private Integer productUnitId;

    @NotNull
    @Column(name = "product_gtin_id")
    private Integer productGtinId;

    @Column(name = "trolley_layer")
    private Integer trolleyLayer;

    @NotNull
    @Column(name = "pallet_id")
    private String palletId;

    @Column(name = "sorting_state")
    private String sortingState;

    @Column(name = "sorting_date")
    private OffsetDateTime sortingDate;

    @NotNull
    @Column(name = "amount_unit")
    private Integer amountUnit;

    @NotNull
    @Column(name = "amount_items")
    private Integer amountItems;

    @NotNull
    @Column(name = "width")
    private Double width;

    @NotNull
    @Column(name = "height")
    private Double height;

    @NotNull
    @Column(name = "depth")
    private Double depth;
}
