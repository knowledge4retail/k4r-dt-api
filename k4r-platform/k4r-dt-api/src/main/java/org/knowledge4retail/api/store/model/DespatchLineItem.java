package org.knowledge4retail.api.store.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "despatch_line_item")
public class DespatchLineItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "despatch_logistic_unit_id")
    private Integer despatchLogisticUnitId;
    @Column(name = "product_id")
    private String productId;
    @Column(name = "requested_product_id")
    private String requestedProductId;
    @Column(name = "line_item_number")
    private String lineItemNumber;
    @Column(name = "despatch_quantity")
    private Integer despatchQuantity;
}