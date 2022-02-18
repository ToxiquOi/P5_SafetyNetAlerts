package fr.ocr.p5_safetynetalerts.database;

import com.fasterxml.jackson.annotation.JsonProperty;
import fr.ocr.p5_safetynetalerts.model.FirestationModel;
import fr.ocr.p5_safetynetalerts.model.MedicalRecordModel;
import fr.ocr.p5_safetynetalerts.model.PersonModel;

import java.util.List;

public class ImportDataModel {

    @JsonProperty("persons")
    List<PersonModel> persons;

    @JsonProperty("firestations")
    List<FirestationModel> firestations;

    @JsonProperty("medicalrecords")
    List<MedicalRecordModel> medicalrecords;
}
