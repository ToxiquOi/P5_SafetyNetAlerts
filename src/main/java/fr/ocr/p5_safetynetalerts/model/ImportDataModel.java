package fr.ocr.p5_safetynetalerts.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import fr.ocr.p5_safetynetalerts.model.FirestationModel;
import fr.ocr.p5_safetynetalerts.model.MedicalRecordModel;
import fr.ocr.p5_safetynetalerts.model.PersonModel;

import java.util.List;

public class ImportDataModel {

    @JsonProperty("persons")
    public List<PersonModel> persons;

    @JsonProperty("firestations")
    public List<FirestationModel> firestations;

    @JsonProperty("medicalrecords")
    public List<MedicalRecordModel> medicalrecords;
}
