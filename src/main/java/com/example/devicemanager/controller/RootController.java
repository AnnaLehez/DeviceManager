package com.example.devicemanager.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
public class RootController {

    @GetMapping("/")
    public Map<String, Object> apiRoot() {
        // Using LinkedHashMap to preserve the exact order of the keys in the JSON response
        Map<String, Object> metadata = new LinkedHashMap<>();

        metadata.put("application", "Device Manager API");
        metadata.put("status", "Running 🟢");
        metadata.put("timestamp", Instant.now().toString());

        // API Documentation
        metadata.put("api_documentation", "/swagger-ui.html");
        metadata.put("base_path", "/api/v1/devices");

        // Observability & Monitoring
        metadata.put("health_check", "/actuator/health");
        metadata.put("liveness_probe", "/actuator/health/liveness");
        metadata.put("readiness_probe", "/actuator/health/readiness");
        metadata.put("metrics", "/actuator/metrics");
        metadata.put("prometheus_scrape_endpoint", "/actuator/prometheus");

        return metadata;
    }
}
