package com.accesa.pricecomparator.service;

import com.accesa.pricecomparator.model.NutritionFacts;
import com.accesa.pricecomparator.model.Product;
import com.accesa.pricecomparator.repository.NutritionFactsRepository;
import com.accesa.pricecomparator.repository.ProductRepository;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CSVNutritionReaderService {

    private final ProductRepository productRepository;
    private final NutritionFactsRepository nutritionFactsRepository;

    public void importNutritionFacts(MultipartFile file) throws Exception {
        try (CSVReader reader = new CSVReaderBuilder(
                new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))
                .withCSVParser(new CSVParserBuilder().withSeparator(';').build())
                .build()) {

            String[] line;
            reader.readNext(); // skip header

            while ((line = reader.readNext()) != null) {
                if (line.length < 4) continue;

                String externalProductId = line[0];
                Double saturatedFat = Double.parseDouble(line[1]);
                Double sugar = Double.parseDouble(line[2]);
                Double protein = Double.parseDouble(line[3]);

                Optional<Product> productOpt = productRepository.findByExternalProductId(externalProductId);
                if (productOpt.isEmpty()) continue;

                Product product = productOpt.get();

                NutritionFacts nf = nutritionFactsRepository.findByProduct(product)
                        .orElse(new NutritionFacts());

                nf.setProduct(product);
                nf.setSaturatedFatPer100g(saturatedFat);
                nf.setSugarPer100g(sugar);
                nf.setProteinPer100g(protein);

                nutritionFactsRepository.save(nf);
            }
        }
    }
}
