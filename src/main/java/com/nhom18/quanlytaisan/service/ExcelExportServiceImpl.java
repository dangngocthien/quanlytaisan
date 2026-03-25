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
}
