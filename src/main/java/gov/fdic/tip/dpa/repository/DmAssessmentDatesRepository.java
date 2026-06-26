package gov.fdic.tip.dpa.repository;

import gov.fdic.tip.dpa.constants.DpaConstants;
import gov.fdic.tip.dpa.entity.DmAssessmentDates;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface DmAssessmentDatesRepository extends JpaRepository<DmAssessmentDates, Long> {
    
	@Query(value = DpaConstants.SqlQueries.FIND_DISTINCT_ASSESSMENT_PERIODS, nativeQuery = true)
	List<String> findDistinctAssessmentPeriods();
}