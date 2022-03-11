package fr.ocr.p5_safetynetalerts.rest;

import fr.ocr.p5_safetynetalerts.model.ResponseModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("flood")
public class FloodRestController {

    @GetMapping("/stations")
    public ResponseEntity<ResponseModel> getHomeDependingFromFireStation(@RequestParam List<String> stations) {
        //Person: nom, prenom, tel
        //MedicalReport: Médicament, alergies, age

        return ResponseEntity.ok(null);
    }
}
