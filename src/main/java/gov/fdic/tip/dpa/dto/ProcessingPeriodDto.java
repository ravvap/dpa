package gov.fdic.tip.dpa.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProcessingPeriodDto {
    private String periodCode;
    private String displayLabel;
}