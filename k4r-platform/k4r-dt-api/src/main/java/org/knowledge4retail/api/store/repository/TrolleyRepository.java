package org.knowledge4retail.api.store.repository;

import org.knowledge4retail.api.store.model.Trolley;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrolleyRepository extends JpaRepository<Trolley, Integer>, JpaSpecificationExecutor<Trolley> {

    List<Trolley> findByStoreId(Integer storeId);
}
