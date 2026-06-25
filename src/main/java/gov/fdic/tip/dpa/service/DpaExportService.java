package gov.fdic.tip.dpa.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import gov.fdic.tip.dpa.entity.DpaProcessingControl;
import gov.fdic.tip.dpa.repository.DpaProcessingControlRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DpaExportService {

    private final DpaProcessingControlRepository repository;

    public byte[] exportToExcel(String processingPeriod, String requestedBy) throws IOException {
        List<DpaProcessingControl> data = repository.findByProcessingPeriod(processingPeriod);

        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Data Processing Admin");

            // AC-4: Custom Header Metrics Rows
            Row headerInfo1 = sheet.createRow(0);
            headerInfo1.createCell(0).setCellValue("Report Title:");
            headerInfo1.createCell(1).setCellValue("Data Processing Admin");
            headerInfo1.createCell(3).setCellValue("Processing Period:");
            headerInfo1.createCell(4).setCellValue(processingPeriod);

            Row headerInfo2 = sheet.createRow(1);
            headerInfo2.createCell(0).setCellValue("Export Date:");
            headerInfo2.createCell(1).setCellValue(ZonedDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            headerInfo2.createCell(3).setCellValue("Exported By:");
            headerInfo2.createCell(4).setCellValue(requestedBy);

            // Spacing Row
            sheet.createRow(3);

            // AC-1 Table Headers
            Row tableHeader = sheet.createRow(4);
            String[] columns = {"Line Item", "Description", "Source System", "Pre-Cutoff Control", "Post-Cutoff Control"};
            for (int i = 0; i < columns.length; i++) {
                Cell cell = tableHeader.createCell(i);
                cell.setCellValue(columns[i]);
            }

            // AC-5: Safe mapping text translation validation
            int rowIdx = 5;
            for (DpaProcessingControl ctrl : data) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(ctrl.getLineItemNumber());
                row.createCell(1).setCellValue(ctrl.getLineItemDescription());
                row.createCell(2).setCellValue(extractSourceSystem(ctrl.getLineItemDescription()));
                row.createCell(3).setCellValue(ctrl.getPreCutoffControl()); // Internal converted string values
                row.createCell(4).setCellValue(ctrl.getPostCutoffControl());
            }

            workbook.write(out);
            return out.toByteArray();
        }
    }

    private String extractSourceSystem(String description) {
        if (description == null) return "";
        if (description.contains("CALL")) return "CALL";
        if (description.contains("RRPS")) return "RRPS";
        if (description.contains("SIMS")) return "SIMS";
        return "UNKNOWN";
    }
}