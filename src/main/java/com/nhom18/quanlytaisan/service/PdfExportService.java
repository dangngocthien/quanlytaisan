package com.nhom18.quanlytaisan.service;

import java.util.List;
import java.util.Map;

public interface PdfExportService {
    /**
     * Xuất báo cáo định giá tài sản ra định dạng PDF (byte array)
     * @param valuationData Danh sách dữ liệu báo cáo định giá
     * @param title Tiêu đề báo cáo
     * @return File PDF dưới dạng byte array
     */
    byte[] exportValuationReport(List<Map<String, Object>> valuationData, String title);
    /**
     * Xuất báo cáo tổng hợp khấu hao theo năm ra định dạng PDF (byte array)
     * @param summaryData Dữ liệu tóm tắt khấu hao
     * @param year Năm báo cáo
     * @param title Tiêu đề báo cáo
     * @return File PDF dưới dạng byte array
     */
    byte[] exportDepreciationSummary(Map<Integer, Map<String, Object>> summaryData, Integer year, String title);
}
