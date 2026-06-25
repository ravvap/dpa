package gov.fdic.tip.dpa.repository;

import gov.fdic.tip.dpa.entity.DmAssessmentDates;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface DmAssessmentDatesRepository extends JpaRepository<DmAssessmentDates, Long> {
    
    @Query("SELECT DISTINCT d.assessmentPeriod FROM DmAssessmentDates d ORDER BY d.assessmentPeriod DESC")
    List<String> findDistinctAssessmentPeriods();
}