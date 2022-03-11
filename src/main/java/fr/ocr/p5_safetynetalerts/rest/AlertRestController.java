package fr.ocr.p5_safetynetalerts.rest;

import fr.ocr.p5_safetynetalerts.model.ResponseModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("alert")
public class AlertRestController {

    @GetMapping("/childAlert")
    public ResponseEntity<ResponseModel> childAlert(@RequestParam String address) {
        /* TODO
           Person : address
           Filtre la liste sur age < 18
           Contient:
                Info sur l'enfant + membre du foyer
        */

        return ResponseEntity.ok(null);
    }

    @GetMapping("/phoneAlert")
    public ResponseEntity<ResponseModel> phoneAlert(@RequestParam String firestationNumber) {
        /*
            TODO
            Firestation: number -> address
            Person: address -> tel

         */

        return ResponseEntity.ok(null);
    }

    //TODO: NotFoundHandler
}
