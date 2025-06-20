package org.mystore.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.jakarta.xmlbind.JakartaXmlBindAnnotationModule;
import io.swagger.v3.core.jackson.ModelResolver;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class JacksonConfiguration {

    @Value("${airretailer.common.simpleDateFormat}")
    private String simpleDateFormat;

    @Bean
    @Primary
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        JakartaXmlBindAnnotationModule module = new JakartaXmlBindAnnotationModule();
        mapper.registerModule(module);
        mapper.registerModule(new JavaTimeModule());
        mapper.enable(SerializationFeature.WRAP_ROOT_VALUE);
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        mapper.setPropertyNamingStrategy(PropertyNamingStrategies.UPPER_CAMEL_CASE);
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        mapper.enable(DeserializationFeature.UNWRAP_ROOT_VALUE);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.disable(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS).enable(JsonGenerator.Feature.WRITE_BIGDECIMAL_AS_PLAIN);
        DateFormat dateFormat = new SimpleDateFormat(simpleDateFormat);
        mapper.setDateFormat(dateFormat);
        mapper.setSerializationInclusion(Include.NON_NULL);
        mapper.setDefaultPropertyInclusion(JsonInclude.Value.construct(Include.NON_EMPTY,
                Include.NON_EMPTY));
        mapper.enable(JsonParser.Feature.STRICT_DUPLICATE_DETECTION);
        mapper.registerModule(new SimpleModule() {
            @Override
            public void setupModule(SetupContext context) {
                super.setupModule(context);
                context.addBeanSerializerModifier(new CustomBeanSerializerModifier());
            }
        });
        mapper.getSerializerProvider().setNullValueSerializer(new NullSerializer());
        return mapper;
    }


    @Bean
    ModelResolver modelResolver() {
        return new ModelResolver(objectMapper());
    }

//    @Bean
//    public OpenApiCustomiser addCustomOpenAPI() {
//        return (OpenAPI openApi) -> {
//            Map<String, Schema> definitions = openApi.getComponents().getSchemas();
//            Json.mapper().registerModule(new SimpleModule().addSerializer(new JsonNodeExampleSerializer()));
//            definitions.entrySet().stream().forEach(e -> {
//                Schema schema = e.getValue();
//                if (ObjectUtils.notEqual(JsonNode.class.getSimpleName(), schema.getName())) {
//                    Example example = ExampleBuilder.fromSchema(schema, definitions);
//                    Map<String, Object> finalExample = new HashMap<>();
//                    finalExample.put(e.getKey(), example);
//                    schema.setExample(Json.pretty(finalExample));
//                } else {
//                    schema.setProperties(new HashMap<>());
//                }
//                openApi.getComponents().getSchemas().put(e.getKey(), schema);
//            });
//        };
//    }
}
