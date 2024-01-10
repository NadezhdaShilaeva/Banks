package com.shilaeva.entities;

import com.shilaeva.interfaces.BankAccount;
import com.shilaeva.interfaces.Transaction;
import com.shilaeva.models.Commission;
import com.shilaeva.models.TransactionMoney;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Getter;

/**
 * Class describing object ReplenishmentTransaction.
 *
 * @version 2.0
 * @author Nadezhda Shilaeva
 */
@Getter
public class ReplenishmentTransaction implements Transaction {
    private UUID id;
    private TransactionMoney money;
    private LocalDateTime dateTime;
    private BankAccount fromAccount;
    private BankAccount toAccount;
    private Commission commission;

    /**
     * Constructor of the class ReplenishmentTransaction.
     * @param id the id of the ReplenishmentTransaction.
     * @param money the money of the ReplenishmentTransaction.
     * @param dateTime the date and time of the ReplenishmentTransaction.
     * @param toAccount the account that receives the money of this transaction.
     */
    public ReplenishmentTransaction(UUID id, TransactionMoney money, LocalDateTime dateTime, BankAccount toAccount) {
        this.id = id;
        this.money = money;
        this.dateTime = dateTime;
        this.toAccount = toAccount;

        this.commission = new Commission(BigDecimal.ZERO);
    }

    /**
     * Method to execute the Transaction.
     */
    public void execute() {
        if (toAccount.getMoney().compareTo(BigDecimal.ZERO) < 0) {
            commission = toAccount.getCommission();
        }

        toAccount.putMoneyInTheAccount(this);
    }

    /**
     * Method to cancel the Transaction.
     */
    public void cancel() {
        toAccount.cancelTransactionAndWithdrawMoney(this);
    }
}
