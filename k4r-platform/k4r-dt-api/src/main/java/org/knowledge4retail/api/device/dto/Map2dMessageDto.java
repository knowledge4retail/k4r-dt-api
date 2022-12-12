package org.knowledge4retail.api.device.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.knowledge4retail.api.device.dto.ros.RosMetadata;
import org.knowledge4retail.api.shared.dto.BasicDto;
import org.knowledge4retail.api.device.dto.ros.Pose;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Map2dMessageDto implements BasicDto {

    private Integer id;
    private RosMetadata rosMetadata;
    private Double resolution;
    private Double width;
    private Double height;
    private Integer lengthUnitId;
    private Pose pose;
    private Integer storeId;
}
