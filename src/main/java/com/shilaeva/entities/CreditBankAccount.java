package com.shilaeva.entities;

import com.shilaeva.exceptions.BankAccountException;
import com.shilaeva.exceptions.TransactionException;
import com.shilaeva.interfaces.BankAccount;
import com.shilaeva.interfaces.Transaction;
import com.shilaeva.models.Commission;
import com.shilaeva.models.LimitAbove;
import com.shilaeva.models.LimitBelow;
import com.shilaeva.models.Percent;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.UUID;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

/**
 * Class describing object CreditBankAccount.
 *
 * @version 2.0
 * @author Nadezhda Shilaeva
 */
public class CreditBankAccount implements BankAccount {
    @Getter
    private final UUID id;
    @Getter
    private final Client client;
    @Getter
    private final LocalDateTime openDate;
    @Getter
    private final LocalDateTime endDate;
    @Getter
    private BigDecimal money;
    @Getter @Setter
    private Percent percent;
    @Getter @Setter
    private Commission commission;
    @Getter @Setter
    private LimitAbove limitAbove;
    @Getter @Setter
    private LimitBelow limitBelow;
    private final Collection<Transaction> transactionsHistory = new ArrayList<Transaction>();

    /**
     * Constructor of the class CreditBankAccount.
     * @param id the id of the CreditBankAccount.
     * @param client the client of the CreditBankAccount.
     * @param clock the clock with current time.
     * @param endDate the end date of the CreditBankAccount.
     * @param commission the commission of the CreditBankAccount.
     * @param limitAbove the limit above of the CreditBankAccount.
     * @param limitBelow the limit below of the CreditBankAccount.
     */
    public CreditBankAccount(@NonNull UUID id, @NonNull Client client, @NonNull Clock clock,
                             @NonNull LocalDateTime endDate, @NonNull Commission commission,
                             @NonNull LimitAbove limitAbove, @NonNull LimitBelow limitBelow) {
        this.id = id;
        this.client = client;
        this.openDate = clock.getDateTimeNow();
        this.endDate = endDate;
        this.commission = commission;
        this.limitAbove = limitAbove;
        this.limitBelow = limitBelow;
        this.money = BigDecimal.ZERO;
        this.percent = new Percent(BigDecimal.ZERO);
    }

    /**
     * Method to determine whether a client is suspicious or not.
     * @return true if the Client is suspicious, otherwise false.
     */
    public boolean isSuspicious() {
        return client.getAddress() == null || client.getPassport() == null;
    }

    /**
     * Method to get transactionsHistory of the CreditBankAccount.
     * @return transactionsHistory of the CreditBankAccount.
     */
    public Collection<Transaction> getTransactionsHistory() {
        return Collections.unmodifiableCollection(transactionsHistory);
    }

    /**
     * Method to put money in this CreditBankAccount.
     * @param transaction the transaction with money to put in this CreditBankAccount.
     */
    public void putMoneyInTheAccount(@NonNull Transaction transaction) {
        checkPutMoneyInTheAccount(transaction);

        money = money.add(transaction.getMoney().getCount());
        if (transaction instanceof ReplenishmentTransaction) {
            money = money.subtract(transaction.getCommission().getCount());
        }

        transactionsHistory.add(transaction);
    }

    /**
     * Method to withdraw money from this CreditBankAccount.
     * @param transaction the transaction with money to withdraw from this CreditBankAccount.
     */
    public void withdrawMoneyFromTheAccount(@NonNull Transaction transaction) {
        checkWithdrawMoneyFromTheAccount(transaction);

        money = money.subtract(transaction.getMoney().getCount().add(transaction.getCommission().getCount()));
        transactionsHistory.add(transaction);
    }

    /**
     * Method to cancel transaction and put money in this CreditBankAccount.
     * @param transaction the transaction which should be to cancel.
     */
    public void cancelTransactionAndPutMoney(@NonNull Transaction transaction) {
        checkCancelTransaction(transaction);

        money = money.add(transaction.getMoney().getCount().add(transaction.getCommission().getCount()));
        transactionsHistory.remove(transaction);
    }

    /**
     * Method to cancel transaction and withdraw money from this CreditBankAccount.
     * @param transaction the transaction which should be to cancel.
     */
    public void cancelTransactionAndWithdrawMoney(@NonNull Transaction transaction) {
        checkCancelTransaction(transaction);

        money = money.subtract(transaction.getMoney().getCount());
        if (transaction instanceof ReplenishmentTransaction) {
            money = money.add(transaction.getCommission().getCount());
        }

        transactionsHistory.remove(transaction);
    }

    /**
     * Method to check if it is possible to withdraw money from this CreditBankAccount.
     * @param transaction the transaction with money to withdraw from this CreditBankAccount.
     * @throws BankAccountException if the CreditBankAccount is not active or if new sum of money is going beyond the
     * limit above.
     */
    public void checkPutMoneyInTheAccount(@NonNull Transaction transaction) {
        if (notActive(transaction.getDateTime())) {
            throw BankAccountException.notActive(this);
        }

        if (isGoingBeyondTheLimitAbove(transaction)) {
            throw BankAccountException.goingBeyondTheLimit(this);
        }
    }

    /**
     * Method to check if it is possible to withdraw money from this CreditBankAccount.
     * @param transaction the transaction with money to withdraw from this CreditBankAccount.
     * @throws BankAccountException if the CreditBankAccount is not active or if new sum of money is going beyond the
     * limit below.
     */
    public void checkWithdrawMoneyFromTheAccount(@NonNull Transaction transaction) {
        if (notActive(transaction.getDateTime())) {
            throw BankAccountException.notActive(this);
        }

        if (isGoingBeyondTheLimitBelow(transaction)) {
            throw BankAccountException.goingBeyondTheLimit(this);
        }
    }

    /**
     * Method to check if it is possible to cancel transaction.
     * @param transaction the transaction which should be to cancel.
     * @throws TransactionException if the transaction was not found in the transactionHistory of this account.
     */
    public void checkCancelTransaction(@NonNull Transaction transaction) {
        if (transactionsHistory.stream().noneMatch(t -> t == transaction)) {
            throw TransactionException.transactionNotFound(transaction, this);
        }
    }

    private boolean notActive(@NonNull LocalDateTime dateTime) {
        return dateTime.isAfter(endDate);
    }

    private boolean isGoingBeyondTheLimitAbove(@NonNull Transaction transaction) {
        return isSuspicious() && (money.add(transaction.getMoney().getCount()).compareTo(limitAbove.getCount()) > 0);
    }

    private boolean isGoingBeyondTheLimitBelow(@NonNull Transaction transaction) {
        return money.subtract(transaction.getMoney().getCount()).subtract(transaction.getCommission().getCount())
                .compareTo(limitBelow.getCount()) < 0;
    }
}
