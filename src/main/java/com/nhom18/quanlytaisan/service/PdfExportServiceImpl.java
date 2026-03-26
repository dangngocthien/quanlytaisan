package com.nhom18.quanlytaisan.service;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Service
public class PdfExportServiceImpl implements PdfExportService {

    private Font getBaseFont(boolean isBold) {
        try {
            String fontPath = isBold ? "C:\\Windows\\Fonts\\timesbd.ttf" : "C:\\Windows\\Fonts\\times.ttf";
            BaseFont bf = BaseFont.createFont(fontPath, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            return new Font(bf, 13, isBold ? Font.BOLD : Font.NORMAL);
        } catch (DocumentException | IOException e) {
            e.printStackTrace();
            // Fallback font if custom font not found
            return new Font(Font.TIMES_ROMAN, 13, isBold ? Font.BOLD : Font.NORMAL);
        }
    }

    private Font getTitleFont() {
        try {
            BaseFont bf = BaseFont.createFont("C:\\Windows\\Fonts\\timesbd.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            return new Font(bf, 18, Font.BOLD);
        } catch (DocumentException | IOException e) {
            return new Font(Font.TIMES_ROMAN, 18, Font.BOLD);
        }
    }

    @Override
    public byte[] exportValuationReport(List<Map<String, Object>> valuationData, String titleText) {
        Document document = new Document(PageSize.A4.rotate());
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            // Font
            Font titleFont = getTitleFont();
            Font headerFont = getBaseFont(true);
            Font cellFont = getBaseFont(false);

            // Title
            Paragraph title = new Paragraph(titleText, titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(20);
            document.add(title);

            // Table setup
            PdfPTable table = new PdfPTable(7);
            table.setWidthPercentage(100);
            table.setSpacingBefore(10f);
            table.setWidths(new float[]{1.5f, 3.5f, 2.0f, 2.0f, 2.0f, 2.0f, 1.5f});

            // Table headers
            String[] headers = {"Mã TS", "Tên Tài Sản", "Danh Mục", "Giá Mua", "Khấu Hao LK", "Giá Trị TT", "Tình Trạng"};
            for (String header : headers) {
                PdfPCell cell = new PdfPCell(new Phrase(header, headerFont));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setPadding(8);
                table.addCell(cell);
            }

            // Number formatter
            NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));

            // Table data
            for (Map<String, Object> row : valuationData) {
                table.addCell(createCell(getStringValue(row, "assetCode"), cellFont, Element.ALIGN_CENTER));
                table.addCell(createCell(getStringValue(row, "assetName"), cellFont, Element.ALIGN_LEFT));
                table.addCell(createCell(getStringValue(row, "categoryName"), cellFont, Element.ALIGN_LEFT));
                
                table.addCell(createCell(formatCurrency(row.get("purchasePrice"), currencyFormat), cellFont, Element.ALIGN_RIGHT));
                table.addCell(createCell(formatCurrency(row.get("accumulatedDepreciation"), currencyFormat), cellFont, Element.ALIGN_RIGHT));
                table.addCell(createCell(formatCurrency(row.get("currentSystemValue"), currencyFormat), cellFont, Element.ALIGN_RIGHT));
                
                table.addCell(createCell(getStatus(getStringValue(row, "status")), cellFont, Element.ALIGN_CENTER));
            }

            document.add(table);
            document.close();

        } catch (DocumentException e) {
            e.printStackTrace();
        }

        return out.toByteArray();
    }

    private String getStatus(String st) {
        if ("DANG_SUDUNG".equalsIgnoreCase(st) || "ACTIVE".equalsIgnoreCase(st)) return "Đang sử dụng";
        if ("TRONG_KHO".equalsIgnoreCase(st)) return "Trong kho";
        if ("BAO_TRI".equalsIgnoreCase(st) || "MAINTENANCE".equalsIgnoreCase(st)) return "Bảo trì";
        if ("THANH_LY".equalsIgnoreCase(st) || "LIQUIDATED".equalsIgnoreCase(st)) return "Thanh lý";
        return st;
    }

    private PdfPCell createCell(String content, Font font, int alignment) {
        PdfPCell cell = new PdfPCell(new Phrase(content, font));
        cell.setPadding(6);
        cell.setHorizontalAlignment(alignment);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        return cell;
    }

    private String getStringValue(Map<String, Object> row, String key) {
        Object value = row.get(key);
        return value != null ? value.toString() : "";
    }

    private String formatCurrency(Object amountObj, NumberFormat format) {
        if (amountObj == null) return "0 ₫";
        try {
            double amount = 0;
            if (amountObj instanceof Number) {
                amount = ((Number) amountObj).doubleValue();
            } else {
                amount = Double.parseDouble(amountObj.toString());
            }
            return format.format(amount);
        } catch (Exception e) {
            return amountObj.toString() + " ₫";
        }
    }

    @Override
    public byte[] exportDepreciationSummary(Map<Integer, Map<String, Object>> summaryData, Integer year, String titleText) {
        Document document = new Document(PageSize.A4);
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            // Font
            Font titleFont = getTitleFont();
            Font headerFont = getBaseFont(true);
            Font cellFont = getBaseFont(false);

            // Title
            Paragraph title = new Paragraph(titleText, titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(20);
            document.add(title);

            // Table setup
            PdfPTable table = new PdfPTable(4);
            table.setWidthPercentage(100);
            table.setSpacingBefore(10f);
            table.setWidths(new float[]{1.5f, 2.5f, 3.0f, 3.0f});

            // Table headers
            String[] headers = {"Tháng", "Tổng Số TS Khấu Hao", "Tổng Khấu Hao (VNĐ)", "Tổng Giá Trị Còn Lại (VNĐ)"};
            for (String header : headers) {
                PdfPCell cell = new PdfPCell(new Phrase(header, headerFont));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setPadding(8);
                table.addCell(cell);
            }

            // Number formatter
            NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));

            // Table data for 12 months
            for (int month = 1; month <= 12; month++) {
                table.addCell(createCell("Tháng " + month, cellFont, Element.ALIGN_CENTER));

                if (summaryData.containsKey(month)) {
                    Map<String, Object> monthData = summaryData.get(month);

                    Object recordCount = monthData.get("recordCount");
                    table.addCell(createCell(recordCount != null ? recordCount.toString() : "0", cellFont, Element.ALIGN_CENTER));
                    
                    table.addCell(createCell(formatCurrency(monthData.get("totalDepreciation"), currencyFormat), cellFont, Element.ALIGN_RIGHT));
                    table.addCell(createCell(formatCurrency(monthData.get("totalRemainingValue"), currencyFormat), cellFont, Element.ALIGN_RIGHT));
                } else {
                    table.addCell(createCell("0", cellFont, Element.ALIGN_CENTER));
                    table.addCell(createCell(formatCurrency(0, currencyFormat), cellFont, Element.ALIGN_RIGHT));
                    table.addCell(createCell(formatCurrency(0, currencyFormat), cellFont, Element.ALIGN_RIGHT));
                }
            }

            document.add(table);
            document.close();

        } catch (DocumentException e) {
            e.printStackTrace();
        }

        return out.toByteArray();
    }
}