package gov.fdic.tip.dpa.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import gov.fdic.tip.dpa.constants.DpaConstants;
import gov.fdic.tip.dpa.dto.MainGridRowDto;
import gov.fdic.tip.dpa.service.DpaManagementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

/**
 * <h1>DpaGridController</h1>
 * Provide REST endpoints for managing and retrieving Data Processing Admin (DPA) 
 * operational rules grid data, description criteria, and source system lookups.
 *
 * <p><b>Technical Specification:</b> Fulfills requirements under DPA-US-004/5/6.</p>
 *
 * @author Prasad Ravva
 * @version 1.0
 * @since 2026-06-29
 */

@RestController
@RequestMapping("/api/v1/dpa")
@RequiredArgsConstructor
@Tag(name = "DPA Grid Management", description = "Endpoints processing core layout mutations, dynamic table filters, and query configurations.")
public class DpaGridController {

    private final DpaManagementService managementService;
 
    // GET /api/v1/data-processing-admin/lookup/descriptions
    @GetMapping("/lookup/descriptions")
    @PreAuthorize(DpaConstants.Permissions.HAS_VIEW_AUTHORITY)
    public ResponseEntity<List<String>> getDescriptionFilterDropdown() {
        return ResponseEntity.ok(managementService.getDescriptionDropdownValues());
    }

 // GET /api/v1/data-processing-admin/lookup/source-systems
    @GetMapping("/lookup/source-systems")
    @PreAuthorize(DpaConstants.Permissions.HAS_VIEW_AUTHORITY)
    public ResponseEntity<List<String>> getSourceSystemsFilter() {
        List<String> sourceSystems = managementService.getSourceSystemDropdownData();
        return ResponseEntity.ok(sourceSystems);
    }
    
    @Operation(
            summary = "Query Main Operational Control Grid",
            description = "Filters and builds active control data matrix using compound conditions. Maps assessment periods dynamically from core calendars.",
            responses = {
                @ApiResponse(responseCode = "200", description = "Grid array payload collected successfully."),
                @ApiResponse(responseCode = "403", description = "Access Denied - Insufficient credentials (Requires DPA_LINE_ITEM_VIEW).")
            }
        )   
    // GET /api/v1/data-processing-admin/grid-search?description=AVE_MTH_USED&sourceSystem=CALL
    @GetMapping("/grid-search")
    @PreAuthorize(DpaConstants.Permissions.HAS_VIEW_AUTHORITY)
    public ResponseEntity<List<MainGridRowDto>> fetchActiveGrid(
            @RequestParam(required = false) String description,
            @RequestParam(required = false) String sourceSystem) {
        
        List<MainGridRowDto> gridPayload = managementService.searchMainGrid(description, sourceSystem);
        return ResponseEntity.ok(gridPayload);
    }
}