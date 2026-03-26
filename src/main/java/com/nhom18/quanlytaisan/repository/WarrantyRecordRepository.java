package com.nhom18.quanlytaisan.repository;

import com.nhom18.quanlytaisan.entity.WarrantyRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WarrantyRecordRepository extends JpaRepository<WarrantyRecord, Long> {
    List<WarrantyRecord> findByAssetIdOrderByCreatedAtDesc(Long assetId);
}
