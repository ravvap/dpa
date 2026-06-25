package gov.fdic.tip.dpa.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class HistoryRecordDto {
    private Integer lineItemNumber;
    private String description;
    private String processingPeriod;
    private String preCutoffControl;
    private String postCutoffControl;
    private String changeDate;
    private String changedBy;
}