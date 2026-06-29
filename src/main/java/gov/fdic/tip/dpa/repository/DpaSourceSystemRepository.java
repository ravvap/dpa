package gov.fdic.tip.dpa.repository;

import gov.fdic.tip.dpa.entity.DpaSourceSystem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface DpaSourceSystemRepository extends JpaRepository<DpaSourceSystem, Long> {

    // Retrieves names directly as clean strings sorted alphabetically
    @Query("SELECT s.name FROM DpaSourceSystem s ORDER BY s.name ASC")
    List<String> findAllSourceSystemNames();
}