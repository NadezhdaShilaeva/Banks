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
 * Class describing object WithdrawalTransaction.
 *
 * @version 2.0
 * @author Nadezhda Shilaeva
 */
@Getter
public class WithdrawalTransaction implements Transaction {
    private UUID id;
    private TransactionMoney money;
    private LocalDateTime dateTime;
    private BankAccount fromAccount;
    private BankAccount toAccount;
    private Commission commission;

    /**
     * Constructor of the class WithdrawalTransaction.
     * @param id the id of the WithdrawalTransaction.
     * @param money the money of the WithdrawalTransaction.
     * @param dateTime the date and time of the WithdrawalTransaction.
     * @param fromAccount the account from which the money of this transaction is withdrawn.
     */
    public WithdrawalTransaction(UUID id, TransactionMoney money, LocalDateTime dateTime, BankAccount fromAccount)
    {
        this.id = id;
        this.money = money;
        this.dateTime = dateTime;
        this.fromAccount = fromAccount;

        this.commission = new Commission(BigDecimal.ZERO);
    }

    /**
     * Method to execute the Transaction.
     */
    public void execute() {
        if (fromAccount.getMoney().compareTo(BigDecimal.ZERO) < 0) {
            commission = fromAccount.getCommission();
        }

        fromAccount.withdrawMoneyFromTheAccount(this);
    }

    /**
     * Method to cancel the Transaction.
     */
    public void cancel() {
        fromAccount.cancelTransactionAndPutMoney(this);
    }
}
