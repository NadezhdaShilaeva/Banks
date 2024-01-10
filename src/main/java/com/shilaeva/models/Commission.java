package com.shilaeva.models;

import com.shilaeva.exceptions.BankException;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.NonNull;

/**
 * Class describing object Commission of the Transaction.
 *
 * @version 2.0
 * @author Nadezhda Shilaeva
 */
public class Commission {
    private final BigDecimal minCommissionCount = BigDecimal.ZERO;

    @Getter
    private final BigDecimal count;

    /**
     * Constructor of the class Commission.
     * @param count the count of the commission, it cannot be less than 0.
     * @throws BankException if the count is less than 0.
     */
    public Commission(BigDecimal count) {
        if (!isValidCount(count)) {
            throw BankException.invalidCommission(count);
        }

        this.count = count;
    }

    private boolean isValidCount(@NonNull BigDecimal count) {
        return count.compareTo(minCommissionCount) >= 0;
    }
}