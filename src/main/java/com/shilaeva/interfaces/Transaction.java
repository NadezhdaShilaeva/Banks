package com.shilaeva.interfaces;

import com.shilaeva.models.Commission;
import com.shilaeva.models.TransactionMoney;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Class describing object Transaction.
 *
 * @version 2.0
 * @author Nadezhda Shilaeva
 */
public interface Transaction {
    /**
     * Method to get id of the Transaction.
     * @return id of the Transaction.
     */
    UUID getId();

    /**
     * Method to get id of the Transaction.
     * @return id of the Transaction.
     */
    TransactionMoney getMoney();

    /**
     * Method to get dateTime of the Transaction.
     * @return dateTime of the Transaction.
     */
    LocalDateTime getDateTime();

    /**
     * Method to get fromAccount of the Transaction.
     * @return fromAccount of the Transaction.
     */
    BankAccount getFromAccount();

    /**
     * Method to get toAccount of the Transaction.
     * @return toAccount of the Transaction.
     */
    BankAccount getToAccount();

    /**
     * Method to get commission of the Transaction.
     * @return commission of the Transaction.
     */
    Commission getCommission();

    /**
     * Method to execute the Transaction.
     */
    void execute();

    /**
     * Method to cancel the Transaction.
     */
    void cancel();
}
