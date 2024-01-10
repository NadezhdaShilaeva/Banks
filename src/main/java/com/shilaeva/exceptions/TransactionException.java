package com.shilaeva.exceptions;

import com.shilaeva.interfaces.BankAccount;
import com.shilaeva.interfaces.Transaction;
import com.shilaeva.models.LimitAbove;
import com.shilaeva.models.TransactionMoney;
import java.math.BigDecimal;

public class TransactionException extends RuntimeException {
    private TransactionException(String message) {
        super(message);
    }

    public static TransactionException invalidTransactionMoney(BigDecimal money) {
        return new TransactionException("Error: the sum of transaction money " + money + " can not be less than 0.");
    }

    public static TransactionException limitExceeding(TransactionMoney money, LimitAbove limit) {
        return new TransactionException("Error: the sum of transaction money " + money.getCount()
                + " is exceed the limit " + limit.getCount() + ".");
    }

    public static TransactionException transactionNotFound(Transaction transaction, BankAccount bankAccount) {
        return new TransactionException("Error: the transaction " + transaction.getId()
                + " not found in the bank account " + bankAccount.getId() + ".");
    }
}
