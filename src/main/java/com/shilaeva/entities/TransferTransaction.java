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
 * Class describing object TransferTransaction.
 *
 * @version 2.0
 * @author Nadezhda Shilaeva
 */
@Getter
public class TransferTransaction implements Transaction {
    private UUID id;
    private TransactionMoney money;
    private LocalDateTime dateTime;
    private BankAccount fromAccount;
    private BankAccount toAccount;
    private Commission commission;

    /**
     * Constructor of the class TransferTransaction.
     * @param id the id of the TransferTransaction.
     * @param money the money of the TransferTransaction.
     * @param dateTime the date and time of the TransferTransaction.
     * @param fromAccount the account from which the money of this transaction is withdrawn.
     * @param toAccount the account that receives the money of this transaction.
     */
    public TransferTransaction(UUID id, TransactionMoney money, LocalDateTime dateTime,
                               BankAccount fromAccount, BankAccount toAccount) {
        this.id = id;
        this.money = money;
        this.dateTime = dateTime;
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;

        this.commission = new Commission(BigDecimal.ZERO);
    }

    /**
     * Method to execute the Transaction.
     */
    public void execute() {
        if (fromAccount.getMoney().compareTo(BigDecimal.ZERO) < 0) {
            commission = fromAccount.getCommission();
        }

        fromAccount.checkWithdrawMoneyFromTheAccount(this);
        toAccount.checkPutMoneyInTheAccount(this);

        fromAccount.withdrawMoneyFromTheAccount(this);
        toAccount.putMoneyInTheAccount(this);
    }

    /**
     * Method to cancel the Transaction.
     */
    public void cancel() {
        fromAccount.checkCancelTransaction(this);
        toAccount.checkCancelTransaction(this);

        toAccount.cancelTransactionAndWithdrawMoney(this);
        fromAccount.cancelTransactionAndPutMoney(this);
    }
}