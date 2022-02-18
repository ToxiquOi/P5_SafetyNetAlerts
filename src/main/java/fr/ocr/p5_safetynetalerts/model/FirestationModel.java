package fr.ocr.p5_safetynetalerts.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FirestationModel extends AbstractModel {

    @JsonProperty("address")
    private String address;

    @JsonProperty("station")
    private String station;

    public FirestationModel() {
    }

    public FirestationModel(String address, String station) {
        this.address = address;
        this.station = station;
    }
}
