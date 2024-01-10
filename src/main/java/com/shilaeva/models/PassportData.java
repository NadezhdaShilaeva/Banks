package com.shilaeva.models;

import com.shilaeva.exceptions.ClientException;
import lombok.Getter;

/**
 * Class describing object PassportData of the Client.
 *
 * @version 2.0
 * @author Nadezhda Shilaeva
 */
public class PassportData {
    private final int minPassportNumber = 1;

    @Getter
    private final int number;

    /**
     * Constructor of the class PassportData.
     * @param number the number of the passport, it cannot be less than 1.
     * @throws ClientException if the number is less than 1.
     */
    public PassportData(int number) {
        if (!isValidNumber(number)) {
            throw ClientException.invalidPassportData(number);
        }

        this.number = number;
    }


    private boolean isValidNumber(int number) {
        return number >= minPassportNumber;
    }
}
