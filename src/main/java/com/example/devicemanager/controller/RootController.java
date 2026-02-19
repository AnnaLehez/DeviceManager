package com.example.devicemanager.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
public class RootController {

    @GetMapping("/")
    public ResponseEntity<Map<String, Object>> apiRoot() {
        // Using LinkedHashMap to preserve the exact order of the keys in the JSON response
        Map<String, Object> metadata = new LinkedHashMap<>();
        String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();

        metadata.put("application", "Device Manager API");
        metadata.put("status", "Running 🟢");
        metadata.put("timestamp", Instant.now().toString());

        // API Documentation
        metadata.put("api_documentation", baseUrl + "/swagger-ui.html");
        metadata.put("openapi_spec", baseUrl + "/v3/api-docs");
        metadata.put("base_path", baseUrl + "/api/v1/devices");

        // Observability & Monitoring
        metadata.put("health_check", baseUrl + "/actuator/health");
        metadata.put("liveness_probe", baseUrl + "/actuator/health/liveness");
        metadata.put("readiness_probe", baseUrl + "/actuator/health/readiness");
        metadata.put("prometheus_scrape_endpoint", baseUrl + "/actuator/prometheus");

        return ResponseEntity.ok(metadata);
    }
}
