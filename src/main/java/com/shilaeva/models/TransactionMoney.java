package com.shilaeva.models;

import com.shilaeva.exceptions.TransactionException;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.NonNull;

/**
 * Class describing object TransactionMoney of the Transaction.
 *
 * @version 2.0
 * @author Nadezhda Shilaeva
 */
public class TransactionMoney {
    private final BigDecimal minCountOfTransactionMoney = BigDecimal.ZERO;

    @Getter
    public final BigDecimal count;

    /**
     * Constructor of the class TransactionMoney.
     * @param count the count of the transaction money, it should be more than 0.
     * @throws TransactionException if the count is not more than 0.
     */
    public TransactionMoney(BigDecimal count) {
        if (!isValidCountOfMoney(count)) {
            throw TransactionException.invalidTransactionMoney(count);
        }

        this.count = count;
    }

    private boolean isValidCountOfMoney(@NonNull BigDecimal money) {
        return money.compareTo(minCountOfTransactionMoney) > 0;
    }
}