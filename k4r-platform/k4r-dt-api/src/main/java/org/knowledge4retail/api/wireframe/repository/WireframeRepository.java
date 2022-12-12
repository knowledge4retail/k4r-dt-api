package org.knowledge4retail.api.wireframe.repository;

import org.knowledge4retail.api.wireframe.model.Wireframe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import java.util.Optional;



@Repository
public interface WireframeRepository extends JpaRepository<Wireframe, Integer>, JpaSpecificationExecutor<Wireframe> {
    Optional<Wireframe> findBygTIN(String gTIN);
}
