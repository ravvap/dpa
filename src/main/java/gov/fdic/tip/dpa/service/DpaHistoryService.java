package gov.fdic.tip.dpa.service;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import gov.fdic.tip.dpa.dto.HistoryRecordDto;
import gov.fdic.tip.dpa.dto.HistorySearchResponse;
import gov.fdic.tip.dpa.entity.DpaProcessingControlHistory;
import gov.fdic.tip.dpa.repository.DpaProcessingControlHistoryRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DpaHistoryService {

    private final DpaProcessingControlHistoryRepository historyRepository;
    private static final DateTimeFormatter ISO_FORMATTER = DateTimeFormatter.ISO_OFFSET_DATE_TIME;

    // Search Mode 1: By Data Element (With Server-Side Pagination support)
    public HistorySearchResponse searchByDataElement(Integer dataElement, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("processingPeriod").descending());
        List<HistoryRecordDto> records = historyRepository.findByLineItemNumber(dataElement, pageable)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());

        return HistorySearchResponse.builder()
                .dataElement(dataElement)
                .records(records)
                .build();
    }

    // Search Mode 2: By Source System and Processing Period
    public HistorySearchResponse searchBySourceSystemAndPeriod(String sourceSystem, String processingPeriod) {
        List<HistoryRecordDto> records = historyRepository
                .findByLineItemDescriptionContainingAndProcessingPeriodOrderByLineItemNumberAsc(sourceSystem, processingPeriod)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());

        return HistorySearchResponse.builder()
                .sourceSystem(sourceSystem)
                .processingPeriod(processingPeriod)
                .records(records)
                .build();
    }

    private HistoryRecordDto mapToDto(DpaProcessingControlHistory entity) {
        return HistoryRecordDto.builder()
                .lineItemNumber(entity.getLineItemNumber())
                .description(entity.getLineItemDescription())
                .processingPeriod(entity.getProcessingPeriod())
                .preCutoffControl(entity.getPreCutoffControl())
                .postCutoffControl(entity.getPostCutoffControl())
                .changeDate(entity.getChangeDate().format(ISO_FORMATTER))
                .changedBy(entity.getChangedBy())
                .build();
    }
}