package gov.fdic.tip.dpa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import gov.fdic.tip.dpa.entity.DpaProcessingControl;

public interface DpaProcessingControlRepository extends JpaRepository<DpaProcessingControl, Long> {
    List<DpaProcessingControl> findByProcessingPeriod(String processingPeriod);
}