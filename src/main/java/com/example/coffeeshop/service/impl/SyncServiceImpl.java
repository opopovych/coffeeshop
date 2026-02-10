package com.example.coffeeshop.service.impl;

import com.example.coffeeshop.model.*;
import com.example.coffeeshop.repository.CoffeeBeanRepository;
import com.example.coffeeshop.service.SyncService;
import jakarta.transaction.Transactional;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Service
public class SyncServiceImpl implements SyncService {

    @Autowired
    private CoffeeBeanRepository coffeeBeanRepository;

    @Transactional
    @Override
    public SyncReport syncWithPriceList(MultipartFile file,Double percent) throws IOException {
        Map<String, Double> priceMapFromFile = new HashMap<>();

        try (InputStream is = file.getInputStream();
             Workbook workbook = WorkbookFactory.create(is)) {

            Sheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue; // Пропуск заголовка

                // Використовуємо індекси 1 та 3 згідно з твоїм файлом
                Cell skuCell = row.getCell(0);
                Cell priceCell = row.getCell(2);

                if (skuCell != null && priceCell != null) {
                    String sku = skuCell.toString().trim();

                    try {
                        // РЕДАГУВАННЯ ЦІНИ (очищення від ком та пробілів)
                        String rawPrice = priceCell.toString();
                        String cleanPrice = rawPrice
                                .replace("\"", "")
                                .replace(",", ".")
                                .replaceAll("\\s", "")
                                .trim();

                        double price = Double.parseDouble(cleanPrice);
                        priceMapFromFile.put(sku, price);
                    } catch (Exception e) {
                        continue; // Пропускаємо помилкові рядки
                    }
                }
            }
        }

        List<CoffeeBean> allProducts = coffeeBeanRepository.findAll();
        int updatedCount = 0;
        int deactivatedCount = 0;

        for (CoffeeBean product : allProducts) {
            String productSku = product.getSku();

            if (productSku != null && priceMapFromFile.containsKey(productSku)) {
                double newPrice = priceMapFromFile.get(productSku)*(1+(percent/100));
                product.setPrice((double) Math.round(newPrice));
                product.setActive(true);
                updatedCount++;
            } else {
                product.setActive(false);
                deactivatedCount++;
            }
        }

        coffeeBeanRepository.saveAll(allProducts);

        return new SyncReport(updatedCount, deactivatedCount);
    }
    @Transactional
    @Override
    public SyncReport syncStatusOnly(MultipartFile file) throws IOException {
        Map<String, Double> priceMapFromFile = new HashMap<>();
        Map<String, ProductData> listFromFile = new HashMap<>();

        try (InputStream is = file.getInputStream();
             Workbook workbook = WorkbookFactory.create(is)) {

            Sheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue; // Пропуск заголовка

                // Використовуємо індекси 1 та 3 згідно з твоїм файлом
                Cell skuCell = row.getCell(0);
                Cell nameCell = row.getCell(1);
                Cell priceCell = row.getCell(2);

                if (skuCell != null && priceCell != null) {
                    String sku = skuCell.toString().trim();
                    String name = nameCell.toString().trim();
                    try {
                        // РЕДАГУВАННЯ ЦІНИ (очищення від ком та пробілів)
                        String rawPrice = priceCell.toString();
                        String cleanPrice = rawPrice
                                .replace("\"", "")
                                .replace(",", ".")
                                .replaceAll("\\s", "")
                                .trim();

                        double price = Double.parseDouble(cleanPrice);
                        priceMapFromFile.put(sku, price);
                        listFromFile.put(sku,new ProductData(name,price));
                    } catch (Exception e) {
                        continue; // Пропускаємо помилкові рядки
                    }
                }
            }
        }

        List<CoffeeBean> allProducts = coffeeBeanRepository.findAll();
        int updatedCount = 0;
        int deactivatedCount = 0;

        for (CoffeeBean product : allProducts) {
            String productSku = product.getSku();

            if (productSku != null && priceMapFromFile.containsKey(productSku)) {
                //product.setPrice(priceMapFromFile.get(productSku));
                product.setActive(true);
                updatedCount++;
            } else {
                product.setActive(false);
                deactivatedCount++;
            }
        }
        for (CoffeeBean product : allProducts) {
            String productSku = product.getSku();

            if (productSku != null && listFromFile.containsKey(productSku)) {
                //product.setPrice(priceMapFromFile.get(productSku));
                listFromFile.remove(productSku);
            }
        }
        listFromFile.forEach((sku, data) -> {
            System.out.println("SKU: " + sku + " -> Назва: " + data.getName() + ", Ціна: " + data.getPrice());
        });

        coffeeBeanRepository.saveAll(allProducts);

        return new SyncReport(updatedCount, deactivatedCount);
    }

    @Transactional
    @Override
    public Map<String, ProductData> lookMissingProducts(MultipartFile file) throws IOException {
        Map<String, ProductData> listFromFile = new HashMap<>();
        try (InputStream is = file.getInputStream();
             Workbook workbook = WorkbookFactory.create(is)) {

            Sheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue; // Пропуск заголовка

                // Використовуємо індекси 1 та 3 згідно з твоїм файлом
                Cell skuCell = row.getCell(0);
                Cell nameCell = row.getCell(1);
                Cell priceCell = row.getCell(2);

                if (skuCell != null && priceCell != null) {
                    String sku = skuCell.toString().trim();
                    String name = nameCell.toString().trim();
                    try {
                        // РЕДАГУВАННЯ ЦІНИ (очищення від ком та пробілів)
                        String rawPrice = priceCell.toString();
                        String cleanPrice = rawPrice
                                .replace("\"", "")
                                .replace(",", ".")
                                .replaceAll("\\s", "")
                                .trim();

                        double price = Double.parseDouble(cleanPrice);
                        listFromFile.put(sku,new ProductData(name,price));
                    } catch (Exception e) {
                        continue; // Пропускаємо помилкові рядки
                    }
                }
            }
        }

        List<CoffeeBean> allProducts = coffeeBeanRepository.findAll();

        for (CoffeeBean product : allProducts) {
            String productSku = product.getSku();

            if (productSku != null && listFromFile.containsKey(productSku)) {
                listFromFile.remove(productSku);
            }
        }

        return listFromFile;
    }



//    @Transactional
//    @Override
//    public DetailedSyncReport syncWithDetailedReport(MultipartFile file) throws IOException {
//        DetailedSyncReport report = new DetailedSyncReport();
//        // Мапа: SKU -> [Назва, Ціна]
//        Map<String, ProductData> fileData = new HashMap<>();
//
//        try (InputStream is = file.getInputStream(); Workbook workbook = WorkbookFactory.create(is)) {
//            Sheet sheet = workbook.getSheetAt(0);
//            for (Row row : sheet) {
//                if (row.getRowNum() == 0) continue;
//
//                Cell nameCell = row.getCell(0); // Припустимо, назва в колонці A
//                Cell skuCell = row.getCell(1);  // SKU в колонці B
//                Cell priceCell = row.getCell(3); // Ціна в колонці D
//
//                if (skuCell != null && nameCell != null) {
//                    String sku = skuCell.toString().trim();
//                    String name = nameCell.toString().trim();
//                    if (sku.isEmpty()) continue;
//
//                    double price = 0;
//                    try {
//                        price = Double.parseDouble(priceCell.toString().replace(",", ".").replaceAll("\\s", ""));
//                    } catch (Exception e) {}
//
//                    fileData.put(sku, new ProductData(name, price));
//                }
//            }
//        }
//
//        List<CoffeeBean> allDbProducts = coffeeBeanRepository.findAll();
//        Set<String> processedSkus = new HashSet<>();
//
//        // 1. Проходимо по базі
//        for (CoffeeBean product : allDbProducts) {
//            String sku = product.getSku();
//            if (fileData.containsKey(sku)) {
//                product.setPrice(fileData.get(sku).getPrice());
//                product.setActive(true);
//                report.getUpdated().add(new ProductInfo(sku, product.getName()));
//                processedSkus.add(sku);
//            } else {
//                product.setActive(false);
//                report.getMissingInFile().add(new ProductInfo(sku, product.getName()));
//            }
//        }
//        coffeeBeanRepository.saveAll(allDbProducts);
//
//        // 2. Знаходимо те, чого в базі взагалі немає
//        fileData.forEach((sku, data) -> {
//            if (!processedSkus.contains(sku)) {
//                report.getMissingInDatabase().add(new ProductInfo(sku, data.getName()));
//            }
//        });
//
//        return report;
//    }

}
