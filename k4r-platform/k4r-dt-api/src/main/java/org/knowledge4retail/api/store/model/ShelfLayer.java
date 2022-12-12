package org.knowledge4retail.api.store.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name="shelf_layer")
public class ShelfLayer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "shelf_id")
    private Integer shelfId;

    @Column(name = "level")
    private Integer level;

    @Column(name = "type")
    private String type;

    @Column(name = "position_z")
    private Double positionZ;

    @NotNull
    @Column(name = "width")
    private Double width;

    @NotNull
    @Column(name = "height")
    private Double height;

    @NotNull
    @Column(name = "depth")
    private Double depth;

    @NotNull
    @Column(name = "length_unit_id")
    private Integer lengthUnitId;

    @Column(name = "external_reference_id")
    private String externalReferenceId;
}
