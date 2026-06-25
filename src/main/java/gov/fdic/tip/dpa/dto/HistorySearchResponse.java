package gov.fdic.tip.dpa.dto;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class HistorySearchResponse {
    private String sourceSystem;
    private String processingPeriod;
    private Integer dataElement;
    private List<HistoryRecordDto> records;
}