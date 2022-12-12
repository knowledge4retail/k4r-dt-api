package org.knowledge4retail.api.wireframe.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.OffsetDateTime;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Table(name = "wireframe")
public class Wireframe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Integer id;

    @NotBlank
    @Column(name="gTIN")
    private String gTIN;

    @Column(name = "timestamp")
    private OffsetDateTime timestamp;

    @NotBlank
    @Column(name="data_format")
    private String dataFormat;

    @Column(name="blob_url")
    private String blobUrl;
}