package org.knowledge4retail.api.store.repository;

import org.knowledge4retail.api.store.model.Facing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FacingRepository extends JpaRepository<Facing, Integer>, JpaSpecificationExecutor<Facing> {
    List<Facing> findByShelfLayerId(int shelfLayerId);

    List<Facing> findByProductUnitId(Integer productUnitId);
}
