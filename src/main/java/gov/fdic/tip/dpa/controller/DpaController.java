package gov.fdic.tip.dpa.controller;

import java.io.IOException;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import gov.fdic.tip.dpa.dto.HistorySearchResponse;
import gov.fdic.tip.dpa.service.DpaExportService;
import gov.fdic.tip.dpa.service.DpaHistoryService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/dpa")
@RequiredArgsConstructor
public class DpaController {

    private final DpaHistoryService historyService;
    private final DpaExportService exportService;

    // GET /api/v1/dpa/history
    @GetMapping("/history")
    public ResponseEntity<?> getHistory(
            @RequestParam(required = false) Integer dataElement,
            @RequestParam(required = false) String sourceSystem,
            @RequestParam(required = false) String processingPeriod,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size) {

        // Mutual Exclusivity Guard Check Rule
        if (dataElement != null && (sourceSystem != null || processingPeriod != null)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Conflicting search parameters. Supply either dataElement OR sourceSystem+processingPeriod."));
        }

        // Mode 1 Search execution
        if (dataElement != null) {
            return ResponseEntity.ok(historyService.searchByDataElement(dataElement, page, size));
        }

        // Mode 2 Search execution
        if (sourceSystem != null && processingPeriod != null) {
            HistorySearchResponse response = historyService.searchBySourceSystemAndPeriod(sourceSystem, processingPeriod);
            if (response.getRecords().isEmpty()) {
                // AC-6 Screen Empty State Message handled contextually or returns empty list payload cleanly
                return ResponseEntity.ok(response);
            }
            return ResponseEntity.ok(response);
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", "Missing required query criteria options."));
    }

    // GET /api/v1/data-processing-admin/export?format=xlsx&processingPeriod=2026Q1
    @GetMapping("/export")
    public ResponseEntity<byte[]> exportData(
            @RequestParam String format,
            @RequestParam String processingPeriod) {
        
        if (!"xlsx".equalsIgnoreCase(format)) {
            return ResponseEntity.badRequest().build();
        }

        try {
            // Simulated extraction value for "Exported By" audit requirement tracking
            String systemUser = "SYSTEM_ANALYST_AOS"; 
            byte[] excelContent = exportService.exportToExcel(processingPeriod, systemUser);

            String filename = "Data_Processing_Admin_Export_" + processingPeriod + ".xlsx";

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                    .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                    .body(excelContent);

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}