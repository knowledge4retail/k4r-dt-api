package org.knowledge4retail.api.store.repository;

import org.knowledge4retail.api.store.model.Shelf;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShelfRepository extends JpaRepository<Shelf, Integer>, JpaSpecificationExecutor<Shelf> {

    List<Shelf> findByStoreId(Integer storeId);

    List<Shelf> findByStoreIdAndExternalReferenceId(Integer storeId, String externalReferenceId);

    Shelf findByExternalReferenceId(String externalReferenceId);

    boolean existsByExternalReferenceId(@Param("externalReferenceId") String externalReferenceId);

    @Query("select s from Shelf s where s.id in (:shelfIds) and s.storeId = (:storeId)")
    List<Shelf> findByShelfIdsAndStoreId(@Param("shelfIds") List<Integer> shelfIds, @Param("storeId") Integer storeId);
}