package com.example.coffeeshop.service;

import com.example.coffeeshop.model.DetailedSyncReport;
import com.example.coffeeshop.model.ProductData;
import com.example.coffeeshop.model.SyncReport;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

public interface SyncService {
    SyncReport syncWithPriceList(MultipartFile file) throws IOException;
    SyncReport syncStatusOnly(MultipartFile file) throws IOException;
    //DetailedSyncReport syncWithDetailedReport(MultipartFile file) throws IOException;
    Map<String, ProductData> lookMissingProducts(MultipartFile file) throws IOException;
}
