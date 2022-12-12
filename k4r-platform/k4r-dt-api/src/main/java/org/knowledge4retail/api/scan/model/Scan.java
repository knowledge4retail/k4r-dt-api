package org.knowledge4retail.api.scan.model;

import lombok.*;

import javax.persistence.*;
import java.time.OffsetDateTime;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Builder
@AllArgsConstructor
@Table(name = "scan")
@IdClass(ScanKey.class)
public class Scan {

    @Id
    @Column(name = "entity_type")
    private String entityType;  // referenceFrame and referenceId for further steps
    @Id
    @Column(name = "timestamp")
    private OffsetDateTime timestamp;
    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "origin")
    private String origin;
    @Column(name = "position_X")
    private Double positionX;
    @Column(name = "position_Y")
    private Double positionY;
    @Column(name = "position_Z")
    private Double positionZ;
    @Column(name = "orientation_X")
    private Double orientationX;
    @Column(name = "orientation_Y")
    private Double orientationY;
    @Column(name = "orientation_Z")
    private Double orientationZ;
    @Column(name = "orientation_W")
    private Double orientationW;
    @Column(name = "height")
    private Double height;
    @Column(name = "width")
    private Double width;
    @Column(name = "depth")
    private Double depth;
    @Column(name = "length_unit_id")
    private Integer lengthUnitId;
    @Column(name = "external_reference_id")
    private String externalReferenceId;
    @Column(name = "additional_info")
    private String additionalInfo;
}
