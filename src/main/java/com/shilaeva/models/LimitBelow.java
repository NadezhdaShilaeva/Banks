package com.shilaeva.models;

import com.shilaeva.exceptions.BankException;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.NonNull;

/**
 * Class describing object LimitBelow of the BankAccount.
 *
 * @version 2.0
 * @author Nadezhda Shilaeva
 */
public class LimitBelow {
    private final BigDecimal maxLimitBelow = BigDecimal.ZERO;

    @Getter
    private final BigDecimal count;

    /**
     * Constructor of the class LimitBelow.
     * @param count the count of money of the limit below, it cannot be more than 0.
     * @throws BankException if the count is more than 0.
     */
    public LimitBelow(BigDecimal count) {
        if (!isValidLimit(count)) {
            throw BankException.invalidLimit(count);
        }

        this.count = count;
    }

    private boolean isValidLimit(@NonNull BigDecimal count) {
        return count.compareTo(maxLimitBelow) <= 0;
    }
}
