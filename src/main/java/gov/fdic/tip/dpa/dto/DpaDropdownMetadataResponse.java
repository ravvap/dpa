package gov.fdic.tip.dpa.dto;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class DpaDropdownMetadataResponse {
    private List<String> sourceSystems;
    private List<ProcessingPeriodDto> processingPeriods;
    private List<DataElementLookupDto> dataElements;
}