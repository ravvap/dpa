/**
 * ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
 * System Name       : TIP
 * Subsystem         : Data Processing Admin (DPA)
 * Class Name        : DpaManagementServiceTest
 * Description       : Core business logic tests ensuring robust data mapping
 * and proper lookup fallbacks when DB results vary.
 *
 * Primary Developer : Prasad Ravva
 * Creation Date     : June 29, 2026
 * ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
 */
package gov.fdic.tip.dpa.service;

import gov.fdic.tip.dpa.dto.MainGridRowDto;
import gov.fdic.tip.dpa.entity.DpaOperationRules;
import gov.fdic.tip.dpa.repository.DmAssessmentDatesRepository;
import gov.fdic.tip.dpa.repository.DpaOperationRulesRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DpaManagementServiceTest {

    @Mock
    private DpaOperationRulesRepository rulesRepository;

    @Mock
    private DmAssessmentDatesRepository assessmentDatesRepository;

    @InjectMocks
    private DpaManagementService dpaManagementService;

    @Test
    @DisplayName("Search Grid - Maps Target Assessment Period Dynamically")
    void searchMainGrid_ShouldMapPeriodFromAssessmentDates() {
        // Arrange
        DpaOperationRules mockRule = new DpaOperationRules();
        mockRule.setLineItem(1010);
        mockRule.setFieldName("CASH_EQ");
        mockRule.setSourceSystem("SIMS");
        mockRule.setPreCutoffOp(10L);
        mockRule.setPostCutoffOp(20L);

        when(assessmentDatesRepository.findValidWorkflowAssessmentPeriods()).thenAnswer(inv -> List.of("2026Q2"));
        when(rulesRepository.searchActiveRules("CASH_EQ", "SIMS")).thenAnswer(inv -> List.of(mockRule));

        // Act
        List<MainGridRowDto> results = dpaManagementService.searchMainGrid("CASH_EQ", "SIMS");

        // Assert
        assertNotNull(results);
        assertEquals(1, results.size());
        MainGridRowDto resultRow = results.get(0);
        assertEquals("2026Q2", resultRow.getAssessmentPeriod());
        assertEquals(1010, resultRow.getLineItem());
        assertEquals("SIMS", resultRow.getSourceSystem());
    }

    @Test
    @DisplayName("Search Grid - Handles Missing Assessment Periods Gracefully")
    void searchMainGrid_WithNoAssessmentPeriods_ShouldDefaultToNA() {
        DpaOperationRules mockRule = new DpaOperationRules();
        mockRule.setLineItem(2020);
        mockRule.setFieldName("LIAB_TOTAL");

        when(assessmentDatesRepository.findValidWorkflowAssessmentPeriods()).thenAnswer(inv -> Collections.emptyList());
        when(rulesRepository.searchActiveRules(null, null)).thenAnswer(inv -> List.of(mockRule));

        List<MainGridRowDto> results = dpaManagementService.searchMainGrid("", " ");

        assertNotNull(results);
        assertEquals("N/A", results.get(0).getAssessmentPeriod());
    }
}