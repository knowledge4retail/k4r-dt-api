package org.knowledge4retail.api.store.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.OffsetDateTime;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "despatch_logistic_unit")
public class DespatchLogisticUnit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "parent_id")
    private Integer parentId;
    @Column(name = "despatch_advice_id")
    private Integer despatchAdviceId;
    @Column(name = "package_type_code")
    private String packageTypeCode;
    @Column(name = "logistic_unit_id")
    private String logisticUnitId;
    @Column(name = "estimated_delivery")
    private OffsetDateTime estimatedDelivery;
}