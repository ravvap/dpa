/**
 * ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
 * System Name       : TIP (Technology Integration Platform)
 * Subsystem         : Data Processing Admin (DPA)
 * Class Name        : DpaManagementServiceTest
 * Description       : Core service tests checking parameter omission guards, 
 * array parsing logic, and dynamic period token allocations.
 *
 * Primary Developer : Prasad Ravva
 * Creation Date     : June 29, 2026
 * ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
 */
package gov.fdic.tip.dpa.service;

import gov.fdic.tip.dpa.constants.DpaConstants;
import gov.fdic.tip.dpa.dto.MainGridRowDto;
import gov.fdic.tip.dpa.entity.DpaOperationRules;
import gov.fdic.tip.dpa.repository.DpaOperationRulesRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.ZonedDateTime;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@ExtendWith(MockitoExtension.class)
class DpaManagementServiceTest {

    @Mock
    private DpaOperationRulesRepository rulesRepository;

    @InjectMocks
    private DpaManagementService managementService;

    @Test
    @DisplayName("Validation Guard - Fails if both fields are null or empty strings")
    void searchMainGridPaginated_WithBothParamsMissing_ShouldThrowIllegalArgumentException() {
        Pageable pageable = PageRequest.of(0, 25);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            managementService.searchMainGridPaginated("   ", "", pageable);
        });

        assertEquals(DpaConstants.Messages.ERROR_MISSING_CRITERIA, exception.getMessage());
    }

    @Test
    @DisplayName("Dynamic Row Mapping - Successfully converts Object array payload into DTO structure")
    void searchMainGridPaginated_WithValidQuery_ShouldUnboxObjectArrayAndBuildDto() {
        Pageable pageable = PageRequest.of(0, 25);

        // Mock entity database state setup
        DpaOperationRules mockRule = new DpaOperationRules();
        mockRule.setLineItem(7000);
        mockRule.setFieldName("LIAB");
        mockRule.setDescription("Total Liabilities");
        mockRule.setSourceSystem("CALL");
        mockRule.setPreCutoffOp(2L);
        mockRule.setPostCutoffOp(1L);
        mockRule.setCreatedAt(ZonedDateTime.now());
        mockRule.setCreatedBy("SYSTEM_USER");

        // Prepare the Object array simulation matching native relational join configurations
        Object[] mockRow = new Object[] { mockRule, "FZ1 1Q-2026" };
        Page<Object[]> mockDatabasePage = new PageImpl<>(Collections.singletonList(mockRow), pageable, 1);

        Mockito.when(rulesRepository.searchRulesWithDynamicAssessmentPeriod(eq("Total Liabilities"), eq("CALL"), any(Pageable.class)))
               .thenReturn(mockDatabasePage);

        // Execute target action
        Page<MainGridRowDto> result = managementService.searchMainGridPaginated("Total Liabilities", "CALL", pageable);

        // Assert structural integrity
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        
        MainGridRowDto resolvedRow = result.getContent().get(0);
        assertEquals(7000, resolvedRow.getLineItem());
        assertEquals("FZ1 1Q-2026", resolvedRow.getAssessmentPeriod());
        assertEquals("LIAB", resolvedRow.getFieldName());
    }
}