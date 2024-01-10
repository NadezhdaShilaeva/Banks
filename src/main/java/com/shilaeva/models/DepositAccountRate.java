package com.shilaeva.models;

import com.shilaeva.exceptions.BankException;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

/**
 * Class describing object DepositAccountRate.
 *
 * @version 2.0
 * @author Nadezhda Shilaeva
 */
public class DepositAccountRate {
    private final BigDecimal minCountOfMoney = BigDecimal.ZERO;

    @Getter
    @Setter
    private BigDecimal minMoneyCount;

    @Getter
    @Setter
    private Percent percent;

    /**
     * Constructor of the class DepositAccountRate.
     * @param minMoneyCount is the minimum count of money to open a deposit account at this rate.
     * @param percent is the percent of the deposit rate.
     * @throws BankException if the count of money less than 0.
     */
    public DepositAccountRate(BigDecimal minMoneyCount, BigDecimal percent) {
        if (!isValidMoney(minMoneyCount)) {
            throw BankException.invalidMoney(minMoneyCount);
        }

        this.minMoneyCount = minMoneyCount;
        this.percent = new Percent(percent);
    }

    private boolean isValidMoney(@NonNull BigDecimal money) {
        return money.compareTo(minCountOfMoney) >= 0;
    }
}
