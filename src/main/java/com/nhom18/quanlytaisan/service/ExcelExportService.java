package com.nhom18.quanlytaisan.service;

import java.util.List;
import java.util.Map;

public interface ExcelExportService {
    
    /**
     * Xuất báo cáo định giá tài sản ra định dạng Excel (byte array)
     * @param valuationData Danh sách dữ liệu báo cáo định giá
     * @param sheetName Tên sheet trong file Excel
     * @return File Excel dưới dạng byte array
     */
    byte[] exportValuationReport(List<Map<String, Object>> valuationData, String sheetName);

    /**
     * Xuất báo cáo tổng hợp khấu hao theo năm ra định dạng Excel
     * @param summaryData Dữ liệu tóm tắt khấu hao
     * @param year Năm báo cáo
     * @return File Excel dưới dạng byte array
     */
    byte[] exportDepreciationSummary(Map<Integer, Map<String, Object>> summaryData, Integer year);
}
