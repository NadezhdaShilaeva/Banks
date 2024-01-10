package com.shilaeva.models;

import com.shilaeva.exceptions.BankException;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.NonNull;

/**
 * Class describing object LimitAbove of the BankAccount.
 *
 * @version 2.0
 * @author Nadezhda Shilaeva
 */
public class LimitAbove {
    private final BigDecimal minLimitAbove = BigDecimal.ZERO;

    @Getter
    private final BigDecimal count;

    /**
     * Constructor of the class LimitAbove.
     * @param count the count of money of the limit above, it cannot be less than 0.
     * @throws BankException if the count is less than 0.
     */
    public LimitAbove(BigDecimal count) {
        if (!isValidLimit(count)) {
            throw BankException.invalidLimit(count);
        }

        this.count = count;
    }

    private boolean isValidLimit(@NonNull BigDecimal count) {
        return count.compareTo(minLimitAbove) >= 0;
    }
}
