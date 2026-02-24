package com.jpmc.midascore.component;

import com.jpmc.midascore.foundation.Incentive;
import com.jpmc.midascore.foundation.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class IncentiveApiClient {
    private static final Logger logger = LoggerFactory.getLogger(IncentiveApiClient.class);
    private static final String INCENTIVE_API_URL = "http://localhost:8080/incentive";
    
    private final RestTemplate restTemplate;

    public IncentiveApiClient(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    public Incentive getIncentive(Transaction transaction) {
        try {
            Incentive incentive = restTemplate.postForObject(INCENTIVE_API_URL, transaction, Incentive.class);
            logger.info("Received incentive: {} for transaction: {}", incentive, transaction);
            return incentive != null ? incentive : new Incentive(0);
        } catch (Exception e) {
            logger.warn("Failed to get incentive from API: {}", e.getMessage());
            return new Incentive(0);
        }
    }
}
