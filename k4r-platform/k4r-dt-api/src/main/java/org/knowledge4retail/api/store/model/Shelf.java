package org.knowledge4retail.api.store.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name="shelf")
public class Shelf {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "position_x")
    private Double positionX;

    @Column(name = "position_y")
    private Double positionY;

    @Column(name = "position_z")
    private Double positionZ;

    @Column(name = "orientation_x")
    private Double orientationX;

    @Column(name = "orientation_y")
    private Double orientationY;

    @Column(name = "orientation_z")
    private Double orientationZ;

    @Column(name = "orientation_w")
    private Double orientationW;

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

    @Column(name = "store_id")
    private Integer storeId;

    @Column(name = "product_group_id")
    private Integer productGroupId;

    @Column(name = "cad_plan_id")
    private String cadPlanId;

    @Column(name = "block_id")
    private Integer blockId;

    @Column(name = "running_number")
    private Integer runningNumber;

    @Column(name = "external_reference_id")
    private String externalReferenceId;

    @OneToMany(mappedBy = "shelfId")
    @ToString.Exclude
    private List<ShelfLayer> shelfLayers;
}
