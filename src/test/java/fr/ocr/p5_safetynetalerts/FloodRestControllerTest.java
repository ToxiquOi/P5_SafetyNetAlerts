package fr.ocr.p5_safetynetalerts;

import fr.ocr.p5_safetynetalerts.service.Database;
import fr.ocr.p5_safetynetalerts.model.ResponseModel;
import fr.ocr.p5_safetynetalerts.controller.rest.FloodRestController;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;

@SpringBootTest
class FloodRestControllerTest {
    @Autowired
    FloodRestController floodRestController;

    @Autowired
    Database database;

    @BeforeEach
    private void BeforePerTest() {
        database.loadData(getClass().getClassLoader().getResourceAsStream("data.json"));
    }

    @AfterEach
    private void AfterPerTest() {
        database.truncate();
    }

    @Test
    void getHomeDependingFromFireStationTest() {
        String[] stations = {"1", "2"};

        ResponseEntity<ResponseModel> response = floodRestController.getHomeDependingFromFireStation(Arrays.stream(stations).toList());

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(2, response.getBody().size());
    }
}
