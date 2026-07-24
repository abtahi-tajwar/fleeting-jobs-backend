package com.fleetingtrails.fleetingjobsbackend.company.seeder;

import com.fleetingtrails.fleetingjobsbackend.company.entity.CompanyEntity;
import com.fleetingtrails.fleetingjobsbackend.company.repository.CompanyRepository;
import com.fleetingtrails.fleetingjobsbackend.parser.entity.ParserTemplateEntity;
import com.fleetingtrails.fleetingjobsbackend.parser.entity.ParserTemplateType;
import com.fleetingtrails.fleetingjobsbackend.parser.repository.ParserTemplateRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

@Service
@RequiredArgsConstructor
public class CompanySeeder {
    private final CompanyRepository companyRepository;
    private final ParserTemplateRepository parserTemplateRepository;
    private final ObjectMapper objectMapper;
    ClassPathResource resource = new ClassPathResource("seeds/companies/companies_seed.csv");

    @Transactional
    public void seed() {
        try (
                Reader reader = new BufferedReader(
                        new InputStreamReader(resource.getInputStream())
                );
                CSVParser csv = CSVFormat.DEFAULT.builder()
                        .setHeader()
                        .setSkipHeaderRecord(true)
                        .build()
                        .parse(reader);
        ) {
            for (CSVRecord row : csv) {
                ParserTemplateType template = this.getParserTemplate(row.get("url_template_file_name"));

                CompanyEntity companyEntity = companyRepository
                        .findByListingUrl(row.get("listing_url"))
                        .orElseGet(CompanyEntity::new);

                companyEntity.setName(row.get("name"));
                companyEntity.setListingUrl(row.get("listing_url"));
                companyEntity.setEnabled(true);

                CompanyEntity savedCompany = companyRepository.save(companyEntity);

                ParserTemplateEntity parserTemplateEntity = parserTemplateRepository
                        .findByCompany(savedCompany)
                        .orElseGet(() -> {
                            ParserTemplateEntity newEntity = new ParserTemplateEntity();
                            newEntity.setCompany(savedCompany);
                            return newEntity;
                        });
                parserTemplateEntity.setConfig(template);
                parserTemplateRepository.save(parserTemplateEntity);
            }
        } catch (IOException exception) {
            throw new IllegalStateException(
                    "Unable to read the company seed file", exception
            );
        }
    }

    private ParserTemplateType getParserTemplate (String filename) {
        ClassPathResource resource = new ClassPathResource("seeds/companies/templates/"+filename);
        try {
                return objectMapper.readValue(
                    resource.getInputStream(),
                    new TypeReference<ParserTemplateType>() {}
            );
        } catch (Exception exception) {
            throw new IllegalStateException(
                    "Something went wrong while reading JSON file: ", exception
            );
        }
    }
}
