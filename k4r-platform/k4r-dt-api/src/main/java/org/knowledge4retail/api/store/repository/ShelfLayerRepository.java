package org.knowledge4retail.api.store.repository;

import org.knowledge4retail.api.store.model.ShelfLayer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShelfLayerRepository extends JpaRepository<ShelfLayer, Integer>, JpaSpecificationExecutor<ShelfLayer> {

    List<ShelfLayer> findByShelfId(Integer shelfId);

    ShelfLayer findByShelfIdAndExternalReferenceId(Integer shelfId, String externalReferenceId);

    ShelfLayer findByShelfIdAndExternalReferenceIdAndLevel(Integer shelfId, String externalReferenceId, Integer level);

    boolean existsByExternalReferenceId(@Param("externalReferenceId") String externalReferenceId);

    @Query("select s from ShelfLayer s where s.id in (:shelfLayerIds)")
    List<ShelfLayer> findByShelfLayerIds(@Param("shelfLayerIds") List<Integer> shelfLayerIds);

    boolean existsByExternalReferenceIdAndLevel(@Param("externalReferenceId") String externalReferenceId, @Param("level") Integer level);
}