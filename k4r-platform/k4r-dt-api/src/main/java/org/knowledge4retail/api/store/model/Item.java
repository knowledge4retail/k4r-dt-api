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
@Table(name = "item")
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "position_in_facing_x")
    private Integer positionInFacingX;
    @Column(name = "position_in_facing_y")
    private Integer positionInFacingY;
    @Column(name = "position_in_facing_z")
    private Integer positionInFacingZ;
    @Column(name = "facing_id")
    private Integer facingId;
    @Column(name = "external_reference_id")
    private String externalReferenceId;
    @Column(name = "product_unit_id")
    private Integer productUnitId;
}
