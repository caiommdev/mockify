package org.example.mockify.transaction.infrastructure.config;

import org.example.mockify.transaction.domain.rule.FraudRule;
import org.example.mockify.transaction.domain.rule.FraudRuleChain;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Configuration
public class FraudRuleChainConfig {

    @Bean
    public FraudRuleChain fraudRuleChain(List<FraudRule> rules) {
        return new FraudRuleChain(rules);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
