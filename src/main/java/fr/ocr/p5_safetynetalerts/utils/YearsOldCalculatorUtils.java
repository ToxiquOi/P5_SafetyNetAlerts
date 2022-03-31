package fr.ocr.p5_safetynetalerts.utils;

import java.time.LocalDate;
import java.time.Period;

public final class YearsOldCalculatorUtils {

    private YearsOldCalculatorUtils() {}

    public static int caculateYearsOld(String birthdate) {
        String[] splitBirthdate = birthdate.split("/");
        Period p = Period.between(LocalDate.of(Integer.parseInt(splitBirthdate[2]),
                Integer.parseInt(splitBirthdate[1]),
                Integer.parseInt(splitBirthdate[0])), LocalDate.now());

        return p.getYears();
    }
}
