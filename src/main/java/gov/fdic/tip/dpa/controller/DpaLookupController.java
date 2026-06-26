package gov.fdic.tip.dpa.controller;

import gov.fdic.tip.dpa.dto.DpaDropdownMetadataResponse;
import gov.fdic.tip.dpa.service.DpaMetadataService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import gov.fdic.tip.dpa.constants.*;

@RestController
@RequestMapping("/api/v1/dpa")
@RequiredArgsConstructor
public class DpaLookupController {

    private final DpaMetadataService metadataService;

    @GetMapping(DpaConstants.Routes.DPA_METADATA)
    @PreAuthorize(DpaConstants.Permissions.HAS_VIEW_AUTHORITY)
    public ResponseEntity<DpaDropdownMetadataResponse> getControlScreenDropdowns() {
        return ResponseEntity.ok(metadataService.getDropdownMetadata());
    }
    @GetMapping(DpaConstants.Routes.DPA_PROCESSING_PERIODS)
    @PreAuthorize(DpaConstants.Permissions.HAS_VIEW_AUTHORITY)
    public ResponseEntity<?> getStandalonePeriods() {
        return ResponseEntity.ok(metadataService.getDropdownMetadata().getProcessingPeriods());
    }
    
    @GetMapping(DpaConstants.Routes.DPA_SOURCE_SYSTEMS)
    @PreAuthorize(DpaConstants.Permissions.HAS_VIEW_AUTHORITY)
    public ResponseEntity<?> getSourceSystems() {
        return ResponseEntity.ok(metadataService.getDropdownMetadata().getSourceSystems());
    }
    
    @GetMapping(DpaConstants.Routes.DPA_DATA_ELEMENTS)
    @PreAuthorize(DpaConstants.Permissions.HAS_VIEW_AUTHORITY)
    public ResponseEntity<?> getDataElements() {
        return ResponseEntity.ok(metadataService.getDropdownMetadata().getDataElements());
    }
    
    
}