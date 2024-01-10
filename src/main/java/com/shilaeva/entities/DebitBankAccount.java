package com.shilaeva.entities;

import com.shilaeva.exceptions.BankAccountException;
import com.shilaeva.exceptions.TransactionException;
import com.shilaeva.interfaces.BankAccount;
import com.shilaeva.interfaces.ClockSubscriber;
import com.shilaeva.interfaces.Transaction;
import com.shilaeva.models.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.UUID;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

public class DebitBankAccount implements BankAccount, ClockSubscriber {
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
    private BigDecimal currentPercentIncome = BigDecimal.ZERO;
    private final Collection<Transaction> transactionsHistory = new ArrayList<Transaction>();

    /**
     * Constructor of the class DebitBankAccount.
     * @param id the id of the DebitBankAccount.
     * @param client the client of the DebitBankAccount.
     * @param clock the clock with current time.
     * @param endDate the end date of the DebitBankAccount.
     * @param percent the percent of the DebitBankAccount.
     * @param limitAbove the limit below of the DebitBankAccount.
     */
    public DebitBankAccount(@NonNull UUID id, @NonNull Client client, @NonNull Clock clock,
                            @NonNull LocalDateTime endDate, @NonNull Percent percent, @NonNull LimitAbove limitAbove)
    {
        this.id = id;
        this.client = client;
        this.openDate = clock.getDateTimeNow();
        this.endDate = endDate;
        this.percent = percent;
        this.limitAbove = limitAbove;
        this.money = BigDecimal.ZERO;
        this.commission = new Commission(BigDecimal.ZERO);
        this.limitBelow = new LimitBelow(BigDecimal.ZERO);

        clock.subscribe(this);
    }

    /**
     * Method to determine whether a client is suspicious or not.
     * @return true if the Client is suspicious, otherwise false.
     */
    public boolean isSuspicious() {
        return client.getAddress() == null || client.getPassport() == null;
    }

    /**
     * Method to get transactionsHistory of the DebitBankAccount.
     * @return transactionsHistory of the DebitBankAccount.
     */
    public Collection<Transaction> getTransactionsHistory() {
        return Collections.unmodifiableCollection(transactionsHistory);
    }

    /**
     * Method to put money in this DebitBankAccount.
     * @param transaction the transaction with money to put in this DebitBankAccount.
     */
    public void putMoneyInTheAccount(@NonNull Transaction transaction) {
        checkPutMoneyInTheAccount(transaction);

        money = money.add(transaction.getMoney().getCount());
        transactionsHistory.add(transaction);
    }

    /**
     * Method to withdraw money from this DebitBankAccount.
     * @param transaction the transaction with money to withdraw from this DebitBankAccount.
     */
    public void withdrawMoneyFromTheAccount(@NonNull Transaction transaction) {
        checkWithdrawMoneyFromTheAccount(transaction);

        money = money.subtract(transaction.getMoney().getCount());
        transactionsHistory.add(transaction);
    }

    /**
     * Method to cancel transaction and put money in this DebitBankAccount.
     * @param transaction the transaction which should be to cancel.
     */
    public void cancelTransactionAndPutMoney(@NonNull Transaction transaction) {
        checkCancelTransaction(transaction);

        money = money.add(transaction.getMoney().getCount());
        transactionsHistory.remove(transaction);
    }

    /**
     * Method to cancel transaction and withdraw money from this DebitBankAccount.
     * @param transaction the transaction which should be to cancel.
     */
    public void cancelTransactionAndWithdrawMoney(@NonNull Transaction transaction) {
        checkCancelTransaction(transaction);

        money = money.subtract(transaction.getMoney().getCount());
        transactionsHistory.remove(transaction);
    }

    /**
     * Method to check if it is possible to withdraw money from this DebitBankAccount.
     * @param transaction the transaction with money to withdraw from this DebitBankAccount.
     * @throws BankAccountException if the DebitBankAccount is not active or if new sum of money is going beyond the
     * limit above.
     */
    public void checkPutMoneyInTheAccount(@NonNull Transaction transaction) {
        if (!isActive(transaction.getDateTime())) {
            throw BankAccountException.notActive(this);
        }

        if (isGoingBeyondTheLimitAbove(transaction)) {
            throw BankAccountException.goingBeyondTheLimit(this);
        }
    }

    /**
     * Method to check if it is possible to withdraw money from this DebitBankAccount.
     * @param transaction the transaction with money to withdraw from this DebitBankAccount.
     * @throws BankAccountException if the DebitBankAccount is not active or if new sum of money is going beyond the
     * limit below.
     */
    public void checkWithdrawMoneyFromTheAccount(@NonNull Transaction transaction) {
        if (!isActive(transaction.getDateTime())) {
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

    /**
     * Method to update the DebitBankAccount and add percent income to the account.
     * @param newDateTime the new date and time of the Clock.
     */
    public void update(@NonNull LocalDateTime newDateTime) {
        var timeToAddPercent = LocalDateTime.of(newDateTime.getYear(), newDateTime.getMonth(),
                newDateTime.getDayOfMonth(), 0, 0);

        if (isActive(timeToAddPercent)) {
            currentPercentIncome = currentPercentIncome.add(money.multiply(percent.getNumber())
                                                                 .divide(BigDecimal.valueOf(365)));

            if (timeToAddPercent.getDayOfMonth() == 1) {
                addPercentToAccount(timeToAddPercent);
            }
        }
    }

    private boolean isActive(@NonNull LocalDateTime dateTime) {
        return !dateTime.isAfter(endDate);
    }

    private boolean isGoingBeyondTheLimitAbove(@NonNull Transaction transaction) {
        return isSuspicious() && (money.add(transaction.getMoney().getCount()).compareTo(limitAbove.getCount()) > 0);
    }

    private boolean isGoingBeyondTheLimitBelow(@NonNull Transaction transaction) {
        return money.compareTo(transaction.getMoney().getCount()) < 0;
    }

    private void addPercentToAccount(@NonNull LocalDateTime dateTime) {
        var transaction = new ReplenishmentTransaction(UUID.randomUUID(), new TransactionMoney(currentPercentIncome),
                dateTime, this);
        transaction.execute();

        currentPercentIncome = BigDecimal.ZERO;
    }
}
