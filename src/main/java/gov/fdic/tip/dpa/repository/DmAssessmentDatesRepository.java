package gov.fdic.tip.dpa.repository;

import gov.fdic.tip.dpa.entity.DmAssessmentDates;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface DmAssessmentDatesRepository extends JpaRepository<DmAssessmentDates, Integer> {

    // Pull distinct assessment periods based on targeted Begin/End operational date configurations
    @Query("SELECT DISTINCT d.assessmentPeriod FROM DmAssessmentDates d " +
           "WHERE d.dateType IN ('Period Begin Date', 'Period End Date') " +
           "ORDER BY d.assessmentPeriod DESC")
    List<String> findValidWorkflowAssessmentPeriods();
}