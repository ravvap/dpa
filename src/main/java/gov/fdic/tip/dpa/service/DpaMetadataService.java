package gov.fdic.tip.dpa.service;

import gov.fdic.tip.dpa.dto.DataElementLookupDto;
import gov.fdic.tip.dpa.dto.DpaDropdownMetadataResponse;
import gov.fdic.tip.dpa.dto.ProcessingPeriodDto;
import gov.fdic.tip.dpa.entity.SourceSystem;
import gov.fdic.tip.dpa.repository.DmAssessmentDatesRepository;
import gov.fdic.tip.dpa.repository.DpaProcessingControlRepository;
import gov.fdic.tip.dpa.repository.SourceSystemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DpaMetadataService {

    private final SourceSystemRepository sourceSystemRepository;
    private final DmAssessmentDatesRepository assessmentDatesRepository;
    private final DpaProcessingControlRepository processingControlRepository;

    public DpaDropdownMetadataResponse getDropdownMetadata() {
        List<String> systems = sourceSystemRepository.findAllByOrderByNameAsc().stream()
                .map(SourceSystem::getName)
                .collect(Collectors.toList());

        List<ProcessingPeriodDto> periods = assessmentDatesRepository.findDistinctAssessmentPeriods().stream()
                .map(period -> {
                    String label = period;
                    if (period.contains("Q")) {
                        String[] parts = period.split("Q");
                        label = parts + " Quarter " + parts;
                    }
                    return new ProcessingPeriodDto(period, label);
                })
                .collect(Collectors.toList());

        List<DataElementLookupDto> elements = processingControlRepository.findAll().stream()
                .map(ctrl -> new DataElementLookupDto(ctrl.getLineItemNumber(), ctrl.getLineItemDescription()))
                .distinct()
                .collect(Collectors.toList());

        return DpaDropdownMetadataResponse.builder()
                .sourceSystems(systems)
                .processingPeriods(periods)
                .dataElements(elements)
                .build();
    }
}