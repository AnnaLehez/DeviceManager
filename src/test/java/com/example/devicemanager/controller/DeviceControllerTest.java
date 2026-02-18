package com.example.devicemanager.controller;

import com.example.devicemanager.domain.DeviceState;
import com.example.devicemanager.dto.DeviceCreateDTO;
import com.example.devicemanager.dto.DeviceUpdateDTO;
import com.example.devicemanager.repository.DeviceRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private DeviceRepository deviceRepository;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        deviceRepository.deleteAll();
    }

    @Test
    @DisplayName("Should create a new device successfully")
    void shouldCreateDevice() {
        DeviceCreateDTO request = new DeviceCreateDTO("Test Device", "Test Brand", DeviceState.INACTIVE);

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
                .body("state", equalTo("INACTIVE"));
    }

    @Test
    @DisplayName("Should get all devices")
    void shouldGetAllDevices() {
        createDevice("Device 1", "Brand A", DeviceState.AVAILABLE);
        createDevice("Device 2", "Brand B", DeviceState.IN_USE);

        given()
                .contentType(ContentType.JSON)
        .when()
                .get("/api/v1/devices")
        .then()
                .statusCode(200)
                .body(".", hasSize(2))
                .body("name", hasItems("Device 1", "Device 2"))
                .body("brand", hasItems("Brand A", "Brand B"));
    }

    @Test
    @DisplayName("Should get devices by brand")
    void shouldGetDevicesByBrand() {
        createDevice("Device 1", "Brand A", DeviceState.AVAILABLE);
        createDevice("Device 2", "Brand B", DeviceState.IN_USE);
        createDevice("Device 3", "Brand A", DeviceState.INACTIVE);

        given()
                .contentType(ContentType.JSON)
                .queryParam("brand", "Brand A")
        .when()
                .get("/api/v1/devices")
        .then()
                .statusCode(200)
                .body(".", hasSize(2))
                .body("name", hasItems("Device 1", "Device 3"))
                .body("brand", everyItem(equalTo("Brand A")));
    }

    @Test
    @DisplayName("Should get devices by state")
    void shouldGetDevicesByState() {
        createDevice("Device 1", "Brand A", DeviceState.AVAILABLE);
        createDevice("Device 2", "Brand B", DeviceState.IN_USE);
        createDevice("Device 3", "Brand A", DeviceState.AVAILABLE);

        given()
                .contentType(ContentType.JSON)
                .queryParam("state", "AVAILABLE")
        .when()
                .get("/api/v1/devices")
        .then()
                .statusCode(200)
                .body(".", hasSize(2))
                .body("name", hasItems("Device 1", "Device 3"))
                .body("state", everyItem(equalTo("AVAILABLE")));
    }

    @Test
    @DisplayName("Should get device by ID")
    void shouldGetDeviceById() {
        String id = createDevice("Device to Find", "Brand X", DeviceState.AVAILABLE);

        given()
                .contentType(ContentType.JSON)
        .when()
                .get("/api/v1/devices/" + id)
        .then()
                .statusCode(200)
                .body("id", equalTo(id))
                .body("name", equalTo("Device to Find"));
    }

    @Test
    @DisplayName("Should return 404 when getting non-existent device")
    void shouldReturn404WhenGettingNonExistentDevice() {
        given()
                .contentType(ContentType.JSON)
        .when()
                .get("/api/v1/devices/00000000-0000-0000-0000-000000000000")
        .then()
                .statusCode(404);
    }

    @Test
    @DisplayName("Should update device using DeviceUpdateDTO")
    void shouldUpdateDevice() {
        String id = createDevice("Old Name", "Old Brand", DeviceState.AVAILABLE);

        DeviceUpdateDTO updateRequest = new DeviceUpdateDTO("New Name", null, null);

        given()
                .contentType(ContentType.JSON)
                .body(updateRequest)
        .when()
                .patch("/api/v1/devices/" + id)
        .then()
                .statusCode(200)
                .body("name", equalTo("New Name"))
                .body("brand", equalTo("Old Brand"));
    }

    @Test
    @DisplayName("Should delete device")
    void shouldDeleteDevice() {
        String id = createDevice("Device to Delete", "Brand Y", DeviceState.AVAILABLE);

        given()
                .contentType(ContentType.JSON)
        .when()
                .delete("/api/v1/devices/" + id)
        .then()
                .statusCode(204);

        given()
                .contentType(ContentType.JSON)
        .when()
                .get("/api/v1/devices/" + id)
        .then()
                .statusCode(404);
    }

    private String createDevice(String name, String brand, DeviceState state) {
        DeviceCreateDTO request = new DeviceCreateDTO(name, brand, state);
        return given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/api/v1/devices")
                .then()
                .statusCode(201)
                .extract()
                .path("id");
    }
}
