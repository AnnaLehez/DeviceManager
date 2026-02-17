package com.example.devicemanager.controller;

import com.example.devicemanager.domain.DeviceState;
import com.example.devicemanager.dto.DeviceRequestDTO;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class DeviceControllerTest {

    @LocalServerPort
    private Integer port;

    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "http://localhost:" + port;
    }

    @Test
    void shouldCreateDevice() {
        DeviceRequestDTO request = new DeviceRequestDTO();
        request.setName("Test Device");
        request.setBrand("Test Brand");
        request.setState(DeviceState.AVAILABLE);

        given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/api/v1/devices")
                .then()
                .statusCode(201)
                .body("id", notNullValue())
                .body("name", equalTo("Test Device"))
                .body("brand", equalTo("Test Brand"))
                .body("state", equalTo("AVAILABLE"));
    }

    @Test
    void shouldGetAllDevices() {
        given()
                .when()
                .get("/api/v1/devices")
                .then()
                .statusCode(200)
                .body(".", hasSize(greaterThanOrEqualTo(0)));
    }

    @Test
    void shouldGetDeviceById() {
        // First create a device
        DeviceRequestDTO request = new DeviceRequestDTO();
        request.setName("Device to Find");
        request.setBrand("Brand X");
        request.setState(DeviceState.AVAILABLE);

        String id = given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/api/v1/devices")
                .then()
                .statusCode(201)
                .extract()
                .path("id");

        // Then fetch it
        given()
                .when()
                .get("/api/v1/devices/" + id)
                .then()
                .statusCode(200)
                .body("id", equalTo(id))
                .body("name", equalTo("Device to Find"));
    }

    @Test
    void shouldUpdateDevice() {
        // First create a device
        DeviceRequestDTO request = new DeviceRequestDTO();
        request.setName("Old Name");
        request.setBrand("Old Brand");
        request.setState(DeviceState.AVAILABLE);

        String id = given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/api/v1/devices")
                .then()
                .statusCode(201)
                .extract()
                .path("id");

        // Update it
        DeviceRequestDTO updateRequest = new DeviceRequestDTO();
        updateRequest.setName("New Name");

        given()
                .contentType(ContentType.JSON)
                .body(updateRequest)
                .when()
                .patch("/api/v1/devices/" + id)
                .then()
                .statusCode(200)
                .body("name", equalTo("New Name"))
                .body("brand", equalTo("Old Brand")); // Brand should remain unchanged
    }

    @Test
    void shouldDeleteDevice() {
        // First create a device
        DeviceRequestDTO request = new DeviceRequestDTO();
        request.setName("Device to Delete");
        request.setBrand("Brand Y");
        request.setState(DeviceState.AVAILABLE);

        String id = given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/api/v1/devices")
                .then()
                .statusCode(201)
                .extract()
                .path("id");

        // Delete it
        given()
                .when()
                .delete("/api/v1/devices/" + id)
                .then()
                .statusCode(204);

        // Verify it's gone
        given()
                .when()
                .get("/api/v1/devices/" + id)
                .then()
                .statusCode(404);
    }
}
