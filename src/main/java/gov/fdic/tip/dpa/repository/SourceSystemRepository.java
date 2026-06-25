package gov.fdic.tip.dpa.repository;

import gov.fdic.tip.dpa.entity.SourceSystem;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SourceSystemRepository extends JpaRepository<SourceSystem, Long> {
    List<SourceSystem> findAllByOrderByNameAsc();
}