package com.accesa.pricecomparator.service;

import com.accesa.pricecomparator.model.*;
import com.accesa.pricecomparator.repository.*;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.CSVParserBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class CsvReaderService {

    private final StoreRepository storeRepository;
    private final ProductRepository productRepository;
    private final PriceHistoryRepository priceHistoryRepository;
    private final DiscountRepository discountRepository;
    private final PriceAlertService priceAlertService;


    // functia care se ocupa cu citirea produselor / preturilor si populeaza tabelele din baza de date
    public void importProductsAndPrices(MultipartFile file) throws Exception {
        String filename = file.getOriginalFilename();
            if (filename == null || !filename.contains("_"))
                throw new RuntimeException("Filename must be in format storeName_yyyy-MM-dd.csv");

        String[] parts = filename.split("_");
        String storeName = parts[0].toLowerCase(); // normalize case

        String dateStr = parts[1].replace(".csv", "").trim();
        LocalDate date;
            try {
                date = LocalDate.parse(dateStr);
            } catch (Exception e) {
           throw new RuntimeException("Date format invalid in filename: " + dateStr);
            }

        Store store = storeRepository.findByNameIgnoreCase(storeName)
        .orElseGet(() -> storeRepository.save(new Store(null, storeName)));

        try (CSVReader reader = new CSVReaderBuilder(
                new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))
                .withCSVParser(new CSVParserBuilder().withSeparator(';').build())
                .build()) {

            String[] line;
            reader.readNext(); // skip header

            while ((line = reader.readNext()) != null) {
                if (line.length < 8) continue;  

                String externalId = line[0];
                String name = line[1];
                String category = line[2];
                String brand = line[3];
                Double quantity = Double.parseDouble(line[4]);
                String unit = line[5];
                Double price = Double.parseDouble(line[6]);
                String currency = line[7];

                Product product = productRepository.findByExternalProductId(externalId)
                        .orElseGet(() -> productRepository.save(new Product(
                                externalId, name, category, brand,
                                quantity, unit, currency
                        )));

                PriceHistory priceHistory = new PriceHistory();
                priceHistory.setProduct(product);
                priceHistory.setStore(store);
                priceHistory.setPrice(price);
                priceHistory.setDate(date);

                priceHistoryRepository.save(priceHistory);
                priceAlertService.handleImportedPrice(product, price);
            }
        }
    }

        // functia care se ocupa cu citirea discounturilor si populeaza tabelele din baza de date
    public void importDiscounts(MultipartFile file) throws Exception {
        String filename = file.getOriginalFilename();
        if (filename == null) throw new RuntimeException("Invalid file name!");

        String storeName = filename.split("_")[0];
        Store store = storeRepository.findByName(storeName)
                .orElseThrow(() -> new RuntimeException("Store not found: " + storeName));

        try (CSVReader reader = new CSVReaderBuilder(
                new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))
                .withCSVParser(new CSVParserBuilder().withSeparator(';').build())
                .build()) {

            String[] line;
            reader.readNext(); 

            while ((line = reader.readNext()) != null) {
                if (line.length < 9) continue; 

                String externalId = line[0];
                String name = line[1];
                String brand = line[2];
                Double quantity = Double.parseDouble(line[3]);
                String unit = line[4];
                String category = line[5];
                LocalDate fromDate = LocalDate.parse(line[6]);
                LocalDate toDate = LocalDate.parse(line[7]);
                Double percentage = Double.parseDouble(line[8]);

                Product product = productRepository.findByExternalProductId(externalId)
                        .orElseGet(() -> productRepository.save(new Product(
                                externalId, name, category, brand,
                                quantity, unit, "RON"
                        )));

                Discount discount = new Discount();
                discount.setProduct(product);
                discount.setStore(store);
                discount.setFromDate(fromDate);
                discount.setToDate(toDate);
                discount.setPercentage(percentage);

                discountRepository.save(discount);
            }
        }
    }




}
