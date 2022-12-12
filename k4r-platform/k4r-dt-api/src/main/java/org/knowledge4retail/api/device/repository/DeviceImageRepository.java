package org.knowledge4retail.api.device.repository;

import org.knowledge4retail.api.device.model.DeviceImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeviceImageRepository extends JpaRepository<DeviceImage, Integer>, JpaSpecificationExecutor<DeviceImage> {
    List<DeviceImage> findByLabelIdAndLabelName(String labelId, String labelName);
}
