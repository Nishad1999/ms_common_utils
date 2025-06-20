package org.mystore.config;

 import jakarta.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Slf4j
@Profile("!local")
@Configuration
public class ApmAgent {

    @Value("${elastic.apm.enabled}")
    Boolean enabled;
    @Value("${elastic.apm.log_level}")
    String logLevel;
    @Value("${elastic.apm.application_packages}")
    String applicationPackages;
    @Value("${elastic.apm.server_url}")
    String serverUrl;
    @Value("${elastic.apm.service_name}")
    String serviceName;
    @Value("${elastic.apm.capture_body}")
    String captureBody;
    @Value("${spring.profiles.active}")
    String environment;
    @PostConstruct
    public void initMethod() {
        log.info("Starting APM Java agent with the following properties: {service_name:{}, environment:{}, " +
                        "enabled:{}, log_level:{}, application_packages:{}, server_url:{}, capture_body:{}",
                serviceName, environment, enabled, logLevel, applicationPackages, serverUrl, captureBody);
        Map<String, String> properties = new HashMap<>();
        properties.put("service_name", serviceName);
        properties.put("environment", environment);
        properties.put("enabled", String.valueOf(enabled));
        properties.put("log_level", logLevel);
        properties.put("application_packages", applicationPackages);
        properties.put("server_url", serverUrl);
        properties.put("capture_body", captureBody);
      //  ElasticApmAttacher.attach(properties);
    }
}
