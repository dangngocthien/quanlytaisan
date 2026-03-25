package com.nhom18.quanlytaisan.service;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
public class ExcelExportServiceImpl implements ExcelExportService {

    @Override
    public byte[] exportValuationReport(List<Map<String, Object>> valuationData, String sheetName) {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.createSheet(sheetName != null ? sheetName : "Báo Cáo Định Giá");

            // Tạo các styles cho Excel
            CellStyle headerStyle = createHeaderStyle(workbook);
            CellStyle dateStyle = createDateStyle(workbook);
            CellStyle currencyStyle = createCurrencyStyle(workbook);
            CellStyle percentageStyle = createPercentageStyle(workbook);

            // Row 0: Tiêu đề báo cáo
            Row titleRow = sheet.createRow(0);
            Cell titleCell = titleRow.createCell(0);
            titleCell.setCellValue("BÁO CÁO ĐỊNH GIÁ TÀI SẢN");
            CellStyle mainTitleStyle = workbook.createCellStyle();
            Font mainTitleFont = workbook.createFont();
            mainTitleFont.setBold(true);
            mainTitleFont.setFontHeightInPoints((short) 16);
            mainTitleStyle.setFont(mainTitleFont);
            titleCell.setCellStyle(mainTitleStyle);

            // Headers
            String[] headers = {
                    "STT", "Mã Tài Sản", "Tên Tài Sản", "Danh Mục", "Ngày Mua", 
                    "Giá Mua (VNĐ)", "Khấu Hao Lũy Kế (VNĐ)", "Giá Trị Sổ Sách (VNĐ)",
                    "Giá Trị Hiện Tại (VNĐ)", "Tỷ Lệ Khấu Hao", "Trạng Thái"
            };

            Row headerRow = sheet.createRow(2);
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            // Fill data
            int rowIndex = 3;
            int stt = 1;
            for (Map<String, Object> rowData : valuationData) {
                Row row = sheet.createRow(rowIndex++);

                row.createCell(0).setCellValue(stt++);
                row.createCell(1).setCellValue((String) rowData.get("assetCode"));
                row.createCell(2).setCellValue((String) rowData.get("assetName"));
                row.createCell(3).setCellValue((String) rowData.get("categoryName"));

                // Ngày mua
                Cell purchaseDateCell = row.createCell(4);
                Object purchaseDate = rowData.get("purchaseDate");
                if (purchaseDate instanceof LocalDate) {
                    purchaseDateCell.setCellValue((LocalDate) purchaseDate);
                    purchaseDateCell.setCellStyle(dateStyle);
                }

                // Giá mua
                Cell purchasePriceCell = row.createCell(5);
                setBigDecimalValue(purchasePriceCell, rowData.get("purchasePrice"), currencyStyle);

                // Khấu hao lũy kế
                Cell accDepCell = row.createCell(6);
                setBigDecimalValue(accDepCell, rowData.get("accumulatedDepreciation"), currencyStyle);

                // Giá trị sổ sách
                Cell bookValueCell = row.createCell(7);
                setBigDecimalValue(bookValueCell, rowData.get("bookValue"), currencyStyle);

                // Giá trị hiện tại (system)
                Cell currentValueCell = row.createCell(8);
                setBigDecimalValue(currentValueCell, rowData.get("currentSystemValue"), currencyStyle);

                // Tỷ lệ khấu hao
                Cell depRateCell = row.createCell(9);
                Object depRate = rowData.get("depreciationRate");
                if (depRate != null) {
                    String rateStr = depRate.toString().replace("%", "");
                    try {
                        double rateVal = Double.parseDouble(rateStr) / 100.0;
                        depRateCell.setCellValue(rateVal);
                        depRateCell.setCellStyle(percentageStyle);
                    } catch (NumberFormatException e) {
                        depRateCell.setCellValue(depRate.toString());
                    }
                }

                // Trạng thái
                String statusMsg = getStatusMessage((String) rowData.get("status"));
                row.createCell(10).setCellValue(statusMsg);
            }

            // Auto-size columns
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(out);
            return out.toByteArray();

        } catch (IOException e) {
            throw new RuntimeException("Lỗi khi tạo file Excel export", e);
        }
    }

    private void setBigDecimalValue(Cell cell, Object value, CellStyle style) {
        if (value instanceof BigDecimal) {
            cell.setCellValue(((BigDecimal) value).doubleValue());
            cell.setCellStyle(style);
        } else if (value instanceof Number) {
            cell.setCellValue(((Number) value).doubleValue());
            cell.setCellStyle(style);
        } else if (value != null) {
            try {
                cell.setCellValue(Double.parseDouble(value.toString()));
                cell.setCellStyle(style);
            } catch (NumberFormatException ignored) {
                cell.setCellValue(value.toString());
            }
        }
    }

    private String getStatusMessage(String status) {
        if (status == null) return "Trống";
        switch (status.toUpperCase()) {
            case "DANG_SUDUNG":
            case "ACTIVE": return "Đang sử dụng";
            case "BAO_TRI":
            case "MAINTENANCE": return "Bảo trì";
            case "THANH_LY":
            case "SCRAP": return "Thanh lý";
            case "TRONG_KHO":
            case "INACTIVE": return "Trong kho";
            default: return status;
        }
    }

    private CellStyle createHeaderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        font.setColor(IndexedColors.WHITE.getIndex());
        style.setFont(font);
        
        style.setFillForegroundColor(IndexedColors.ROYAL_BLUE.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setAlignment(HorizontalAlignment.CENTER);
        return style;
    }

    private CellStyle createDateStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        CreationHelper createHelper = workbook.getCreationHelper();
        style.setDataFormat(createHelper.createDataFormat().getFormat("dd/mm/yyyy"));
        return style;
    }

    private CellStyle createCurrencyStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        DataFormat format = workbook.createDataFormat();
        style.setDataFormat(format.getFormat("#,##0"));
        return style;
    }

    private CellStyle createPercentageStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setDataFormat(workbook.createDataFormat().getFormat("0.00%"));
        return style;
    }

    @Override
    public byte[] exportDepreciationSummary(Map<Integer, Map<String, Object>> summaryData, Integer year) {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.createSheet("Tóm Tắt Khấu Hao " + year);

            // Tạo style
            CellStyle headerStyle = createHeaderStyle(workbook);
            CellStyle currencyStyle = createCurrencyStyle(workbook);
            
            // Row 0: Tiêu đề
            Row titleRow = sheet.createRow(0);
            Cell titleCell = titleRow.createCell(0);
            titleCell.setCellValue("BÁO CÁO TỔNG HỢP KHẤU HAO TÀI SẢN NĂM " + year);
            CellStyle mainTitleStyle = workbook.createCellStyle();
            Font mainTitleFont = workbook.createFont();
            mainTitleFont.setBold(true);
            mainTitleFont.setFontHeightInPoints((short) 16);
            mainTitleStyle.setFont(mainTitleFont);
            titleCell.setCellStyle(mainTitleStyle);

            // Headers
            String[] headers = {
                    "Tháng", "Tổng Số TS Khấu Hao", "Tổng Khấu Hao Trong Tháng (VNĐ)", "Tổng Giá Trị Còn Lại (VNĐ)"
            };

            Row headerRow = sheet.createRow(2);
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            // Fill data
            int rowIndex = 3;
            // Iterate from 1 to 12 logically
            for (int month = 1; month <= 12; month++) {
                if (summaryData.containsKey(month)) {
                    Map<String, Object> monthData = summaryData.get(month);
                    Row row = sheet.createRow(rowIndex++);
                    
                    row.createCell(0).setCellValue("Tháng " + month);
                    
                    Cell countCell = row.createCell(1);
                    setBigDecimalValue(countCell, monthData.get("recordCount"), workbook.createCellStyle()); // Normal format for count
                    
                    Cell depCell = row.createCell(2);
                    setBigDecimalValue(depCell, monthData.get("totalDepreciation"), currencyStyle);
                    
                    Cell remainingCell = row.createCell(3);
                    setBigDecimalValue(remainingCell, monthData.get("totalRemainingValue"), currencyStyle);
                } else {
                    // Nếu không có dữ liệu tháng đó thì in ra 0
                    Row row = sheet.createRow(rowIndex++);
                    row.createCell(0).setCellValue("Tháng " + month);
                    row.createCell(1).setCellValue(0);
                    row.createCell(2).setCellValue(0);
                    row.createCell(3).setCellValue(0);
                }
            }

            // Report info (Tổng cộng trong năm)
            BigDecimal totalYearDep = BigDecimal.ZERO;
            for (Map<String, Object> monthData : summaryData.values()) {
                Object dep = monthData.get("totalDepreciation");
                if (dep instanceof BigDecimal) {
                    totalYearDep = totalYearDep.add((BigDecimal) dep);
                }
            }
            
            rowIndex++; // Bỏ cách 1 dòng
            Row totalRow = sheet.createRow(rowIndex);
            Cell totalLabelCell = totalRow.createCell(1);
            totalLabelCell.setCellValue("Tổng Khấu Hao Cả Năm:");
            CellStyle boldStyle = workbook.createCellStyle();
            Font boldFont = workbook.createFont();
            boldFont.setBold(true);
            boldStyle.setFont(boldFont);
            totalLabelCell.setCellStyle(boldStyle);

            Cell totalValueCell = totalRow.createCell(2);
            setBigDecimalValue(totalValueCell, totalYearDep, currencyStyle);
            totalValueCell.setCellStyle(currencyStyle); // Ensure currency style and bold
            
            // Auto size columns
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(out);
            return out.toByteArray();

        } catch (IOException e) {
            throw new RuntimeException("Lỗi khi tạo file Excel export", e);
        }
    }
}