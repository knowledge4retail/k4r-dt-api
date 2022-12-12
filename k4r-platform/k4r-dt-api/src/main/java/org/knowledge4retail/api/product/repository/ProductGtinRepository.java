package org.knowledge4retail.api.product.repository;

import org.knowledge4retail.api.product.model.ProductGtin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductGtinRepository extends JpaRepository<ProductGtin, Integer>, JpaSpecificationExecutor<ProductGtin> {

    @Query("select case when count(p)> 0 then true else false end from ProductGtin p where p.gtin = (:gtin)")
    boolean existsByGtin(@Param("gtin") String gtin);

    @Query("select p from ProductGtin p where p.gtin = (:gtin)")
    ProductGtin getByGtin(@Param("gtin") String gtin);

    @Query("select p from ProductGtin p where p.externalReferenceId = (:externalReferenceId)")
    ProductGtin getByExternalReferenceId(@Param("externalReferenceId") String externalReferenceId);

    @Query("select case when count(p)> 0 then true else false end from ProductGtin p where p.externalReferenceId = (:externalReferenceId)")
    boolean existsByExternalReferenceId(@Param("externalReferenceId") String externalReferenceId);
}
