package org.knowledge4retail.api.store.repository;

import org.knowledge4retail.api.store.model.TrolleyRoute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrolleyRouteRepository extends JpaRepository<TrolleyRoute, Integer>, JpaSpecificationExecutor<TrolleyRoute> {

    List<TrolleyRoute> findByTrolleyId(Integer trolleyId);
}
