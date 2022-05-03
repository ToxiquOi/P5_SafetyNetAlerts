package fr.ocr.p5_safetynetalerts.service;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;

@Service
public final class CalculatorService {

    public int caculateYearsOld(String birthdate) {
        try {
            String[] splitBirthdate = birthdate.split("/");
            Period p = Period.between(LocalDate.of(Integer.parseInt(splitBirthdate[2]),
                    Integer.parseInt(splitBirthdate[1]),
                    Integer.parseInt(splitBirthdate[0])), LocalDate.now());

            return p.getYears();
        } catch (Exception e) {
            return 0;
        }
    }
}
