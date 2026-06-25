package gov.fdic.tip.dpa.controller;

import gov.fdic.tip.dpa.dto.DpaDropdownMetadataResponse;
import gov.fdic.tip.dpa.service.DpaMetadataService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/dpa")
@RequiredArgsConstructor
public class DpaLookupController {

    private final DpaMetadataService metadataService;

    @GetMapping("/metadata")
    @PreAuthorize("hasAuthority('DPA_LINE_ITEM_VIEW')")
    public ResponseEntity<DpaDropdownMetadataResponse> getControlScreenDropdowns() {
        return ResponseEntity.ok(metadataService.getDropdownMetadata());
    }

    @GetMapping("/processing-periods")
    @PreAuthorize("hasAuthority('DPA_LINE_ITEM_VIEW')")
    public ResponseEntity<?> getStandalonePeriods() {
        return ResponseEntity.ok(metadataService.getDropdownMetadata().getProcessingPeriods());
    }
}