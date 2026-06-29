package gov.fdic.tip.dpa.service;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import gov.fdic.tip.dpa.constants.DpaConstants;
import gov.fdic.tip.dpa.dto.MainGridRowDto;
import gov.fdic.tip.dpa.entity.DpaOperationRules;
import gov.fdic.tip.dpa.repository.DmAssessmentDatesRepository;
import gov.fdic.tip.dpa.repository.DpaOperationRulesRepository;
import gov.fdic.tip.dpa.repository.DpaSourceSystemRepository;
import lombok.RequiredArgsConstructor;

/**
 * ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
 * System Name       : TIP  
 * Subsystem         : Data Processing Admin (DPA)
 * Class Name        : DpaManagementService
 * Description       : Handles business rule validations, composite grid queries,
 * and assessment period lookup assignments.
 *
 * Developer 		 : Prasad Ravva
 * Creation Date     : June 29, 2026
 * ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
 * Modification History:
 * Date         Author           Version    Description
 * 2026-06-29   Prasad Ravva     1.0        Initial compilation and package setup.
 * ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
 */

@Service
@RequiredArgsConstructor
public class DpaManagementService {

    private final DpaOperationRulesRepository rulesRepository;
    private final DmAssessmentDatesRepository assessmentDatesRepository;
    private final DpaSourceSystemRepository sourceSystemRepository;
    
    private static final DateTimeFormatter UI_DATE_FORMATTER = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss");
    // Orchestrates metadata dropdown arrays
    public List<String> getDescriptionDropdownValues() {
        return rulesRepository.findDistinctFieldNames();
    }

    public List<String> getSourceSystemDropdownData() {
        return sourceSystemRepository.findAllSourceSystemNames();
    }
    
    
    // Executes composite grid view rendering based on user query configurations
    /**
     * Executes composite main grid view search logic matching UI layouts,
     * returning a list of mapped MainGridRowDto records.
     */
    public List<MainGridRowDto> searchMainGrid(String description, String sourceSystem) {
        String searchField = (description != null && !description.trim().isEmpty()) ? description.trim() : null;
        String searchSystem = (sourceSystem != null && !sourceSystem.trim().isEmpty()) ? sourceSystem.trim() : null;

        List<String> activePeriods = assessmentDatesRepository.findValidWorkflowAssessmentPeriods();
        String targetPeriod = activePeriods.isEmpty() ? "N/A" : activePeriods.get(0);

        List<DpaOperationRules> records = rulesRepository.searchActiveRules(searchField, searchSystem);

        return records.stream()
                .map(rule -> MainGridRowDto.builder()
                        .lineItem(rule.getLineItem()) // Matches 'lineItem' field in MainGridRowDto
                        .fieldName(rule.getFieldName())
                        .description(rule.getDescription())
                        .sourceSystem(rule.getSourceSystem())
                        .assessmentPeriod(targetPeriod)
                        .preCutoffOp(rule.getPreCutoffOp())
                        .postCutoffOp(rule.getPostCutoffOp())
                        .changeDate(rule.getCreatedAt() != null ? rule.getCreatedAt().format(UI_DATE_FORMATTER) : "")
                        .changedBy(rule.getCreatedBy() != null ? rule.getCreatedBy() : DpaConstants.Messages.DEFAULT_USER)
                        .build())
                .collect(Collectors.toList());
    }
    
   
    
    
}