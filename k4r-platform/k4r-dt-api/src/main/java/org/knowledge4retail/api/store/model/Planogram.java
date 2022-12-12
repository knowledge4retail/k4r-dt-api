package org.knowledge4retail.api.store.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.OffsetDateTime;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Table(name = "planogram")
public class Planogram {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Integer id;

    @NotNull
    @Column(name="store_id")
    private Integer storeId;

    @NotBlank
    @Column(name="reference_id")
    private String referenceId;

    @Column(name = "timestamp")
    private OffsetDateTime timestamp;

    @NotBlank
    @Column(name="data_format")
    private String dataFormat;

    @Column(name="blob_url")
    private String blobUrl;
}