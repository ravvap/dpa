package gov.fdic.tip.dpa.repository;

import gov.fdic.tip.dpa.entity.DpaOperationRules;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface DpaOperationRulesRepository extends JpaRepository<DpaOperationRules, Long> {

    // 1. Fetch unique field names for description drop-down population
    @Query("SELECT DISTINCT r.fieldName FROM DpaOperationRules r WHERE r.fieldName IS NOT NULL ORDER BY r.fieldName ASC")
    List<String> findDistinctFieldNames();

    // 2. Main screen multi-mode compound searching framework (Description, Source System, or Both)
    @Query("SELECT r FROM DpaOperationRules r WHERE r.display = true " +
           "AND (:fieldName IS NULL OR r.fieldName = :fieldName) " +
           "AND (:sourceSystem IS NULL OR r.sourceSystem = :sourceSystem) " +
           "ORDER BY r.lineItem ASC")
    List<DpaOperationRules> searchActiveRules(
            @Param("fieldName") String fieldName, 
            @Param("sourceSystem") String sourceSystem);
}