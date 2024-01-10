package com.shilaeva.interfaces;


import com.shilaeva.entities.Client;
import com.shilaeva.models.Commission;
import com.shilaeva.models.LimitAbove;
import com.shilaeva.models.LimitBelow;
import com.shilaeva.models.Percent;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.UUID;

/**
 * Class describing object BankAccount.
 *
 * @version 2.0
 * @author Nadezhda Shilaeva
 */
public interface BankAccount {
    /**
     * Method to get id of the BankAccount.
     * @return id of the BankAccount.
     */
    UUID getId();

    /**
     * Method to get openDate of the BankAccount.
     * @return openDate of the BankAccount.
     */
    LocalDateTime getOpenDate();

    /**
     * Method to get endDate of the BankAccount.
     * @return endDate of the BankAccount.
     */
    LocalDateTime getEndDate();

    /**
     * Method to get client of the BankAccount.
     * @return client of the BankAccount.
     */
    Client getClient();

    /**
     * Method to get money on the BankAccount.
     * @return money on the BankAccount.
     */
    BigDecimal getMoney();

    /**
     * Method to get percent of the BankAccount.
     * @return percent of the BankAccount.
     */
    Percent getPercent();

    /**
     * Method to set percent of the BankAccount.
     * @param percent the new percent of the BankAccount.
     */
    void setPercent(Percent percent);

    /**
     * Method to get commission of the BankAccount.
     * @return commission of the BankAccount.
     */
    Commission getCommission();

    /**
     * Method to set commission of the BankAccount.
     * @param commission the new commission of the BankAccount.
     */
    void setCommission(Commission commission);

    /**
     * Method to get limitAbove of the BankAccount.
     * @return limitAbove of the BankAccount.
     */
    LimitAbove getLimitAbove();

    /**
     * Method to set limitAbove of the BankAccount.
     * @param limitAbove the new limitAbove of the BankAccount.
     */
    void setLimitAbove(LimitAbove limitAbove);

    /**
     * Method to get limitBelow of the BankAccount.
     * @return limitBelow of the BankAccount.
     */
    LimitBelow getLimitBelow();

    /**
     * Method to set limitBelow of the BankAccount.
     * @param limitBelow the new limitBelow of the BankAccount.
     */
    void setLimitBelow(LimitBelow limitBelow);

    /**
     * Method to determine whether a client is suspicious or not.
     * @return true if the Client is suspicious, otherwise false.
     */
    boolean isSuspicious();

    /**
     * Method to get transactionsHistory of the BankAccount.
     * @return transactionsHistory of the BankAccount.
     */
    Collection<Transaction> getTransactionsHistory();

    /**
     * Method to put money in this BankAccount.
     * @param transaction the transaction with money to put in this BankAccount.
     */
    void putMoneyInTheAccount(Transaction transaction);

    /**
     * Method to withdraw money from this BankAccount.
     * @param transaction the transaction with money to withdraw from this BankAccount.
     */
    void withdrawMoneyFromTheAccount(Transaction transaction);

    /**
     * Method to cancel transaction and put money in this BankAccount.
     * @param transaction the transaction which should be to cancel.
     */
    void cancelTransactionAndPutMoney(Transaction transaction);

    /**
     * Method to cancel transaction and withdraw money from this BankAccount.
     * @param transaction the transaction which should be to cancel.
     */
    void cancelTransactionAndWithdrawMoney(Transaction transaction);

    /**
     * Method to check if it is possible to put money in this BankAccount.
     * @param transaction the transaction with money to put in this BankAccount.
     */
    void checkPutMoneyInTheAccount(Transaction transaction);

    /**
     * Method to check if it is possible to withdraw money from this BankAccount.
     * @param transaction the transaction with money to withdraw from this BankAccount.
     */
    void checkWithdrawMoneyFromTheAccount(Transaction transaction);

    /**
     * Method to check if it is possible to cancel transaction.
     * @param transaction the transaction which should be to cancel.
     */
    void checkCancelTransaction(Transaction transaction);
}
