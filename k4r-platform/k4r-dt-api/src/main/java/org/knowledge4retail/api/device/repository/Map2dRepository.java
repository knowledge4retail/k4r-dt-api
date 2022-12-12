package org.knowledge4retail.api.device.repository;

import org.knowledge4retail.api.device.model.Map2d;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface Map2dRepository extends JpaRepository<Map2d, Integer>, JpaSpecificationExecutor<Map2d> {

    Map2d findByStoreId(@Param("store_id") Integer storeId);

    boolean existsByStoreId(@Param("store_id") Integer storeId);
}
