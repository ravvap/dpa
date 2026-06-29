/**
 * ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
 * System Name       : TIP
 * Subsystem         : Data Processing Admin (DPA)
 * Class Name        : MainGridRowDto
 * Description       : Data Transfer Object representing a single consolidated row 
 * inside the primary Angular Enterprise Grid view layout.
 *
 * Primary Developer : Prasad Ravva
 * Creation Date     : June 29, 2026
 * ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
 */
package gov.fdic.tip.dpa.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MainGridRowDto {

    private Integer lineItem;          // The uniquely indexed structural code identifier
    private String fieldName;          // Populates the dynamic criteria dropdown filter
    private String description;        // Full structural business description mapping 
    private String sourceSystem;       // Contextual source routing domain (CALL, RRPS, SIMS)
    
    // Extracted dynamically from dm_assessment_dates mapping contexts
    private String assessmentPeriod;   
    
    private Long preCutoffOp;          // Maps to primary database rule operator code
    private Long postCutoffOp;         // Maps to primary database rule operator code
    
    // UI formatted date timestamp ("MM/dd/yyyy HH:mm:ss")
    private String changeDate;         
    
    // User principal identity responsible for the latest modification
    private String changedBy;          
}