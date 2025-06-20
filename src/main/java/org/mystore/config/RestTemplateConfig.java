package org.mystore.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate(ObjectMapper objectMapper) {
        return new RestTemplateBuilder()
                .additionalMessageConverters(new MappingJackson2HttpMessageConverter(objectMapper))
                .build();
    }

}