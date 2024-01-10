package com.shilaeva.models;

import com.shilaeva.exceptions.BankException;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.NonNull;

/**
 * Class describing object Percent.
 *
 * @version 2.0
 * @author Nadezhda Shilaeva
 */
public class Percent {
    private final BigDecimal minPercentNumber = BigDecimal.ZERO;
    private final BigDecimal maxPercentNumber = BigDecimal.valueOf(100);

    @Getter
    private final BigDecimal number;

    /**
     * Constructor of the class Percent.
     * @param number the number of the percent, it cannot be less than 0.
     * @throws BankException if the number is less than 0.
     */
    public Percent(BigDecimal number) {
        if (!isValidPercent(number)) {
            throw BankException.invalidPercent(number);
        }

        this.number = number;
    }

    private boolean isValidPercent(@NonNull BigDecimal number) {
        return (number.compareTo(minPercentNumber) >= 0) && (number.compareTo(maxPercentNumber) <= 0);
    }
}
