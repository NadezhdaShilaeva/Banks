package com.shilaeva.services;

import com.shilaeva.entities.*;
import com.shilaeva.exceptions.BankException;
import com.shilaeva.exceptions.CentralBankException;
import com.shilaeva.exceptions.TransactionException;
import com.shilaeva.interfaces.BankAccount;
import com.shilaeva.interfaces.Transaction;
import com.shilaeva.models.TransactionMoney;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.UUID;
import lombok.NonNull;

/**
 * Class describing singleton object CentralBank.
 *
 * @version 2.0
 * @author Nadezhda Shilaeva
 */
public class CentralBank {
    private static CentralBank instance;
    private final Collection<Bank> banks = new ArrayList<Bank>();

    private CentralBank() { }

    /**
     * Method to get instance of the singleton object of CentralBank.
     * @return instance of the CentralBank.
     */
    public static CentralBank getInstance() {
        if (instance == null) {
            instance = new CentralBank();
        }
        return instance;
    }

    /**
     * Method to get collection of banks of the CentralBank.
     * @return unmodifiable collection of banks of the CentralBank.
     */
    public Collection<Bank> getBanks() {
        return Collections.unmodifiableCollection(banks);
    }

    /**
     * Method to register and add new bank to the banks of the CentralBank.
     * @param bank the new bank which should be added to the banks of the CentralBank.
     */
    public void registerBank(@NonNull Bank bank) {
        if (banks.stream().anyMatch(b -> b == bank)) {
            throw CentralBankException.bankIsAlreadyExist(bank);
        }

        banks.add(bank);
    }

    /**
     * Method to put money to the BankAccount.
     * @param toAccountId the id of the BankAccount that receives the money.
     * @param money the sum of money to put to the BankAccount with id toAccountId.
     * @return the completed transaction.
     */
    public Transaction putMoneyToTheAccount(@NonNull UUID toAccountId, @NonNull TransactionMoney money) {
        BankAccount toAccount = getBankAccount(toAccountId);
        checkTransactionMoney(money, toAccount);

        var id = UUID.randomUUID();
        var transaction = new ReplenishmentTransaction(id, money, Clock.getInstance().getDateTimeNow(), toAccount);
        transaction.execute();

        return transaction;
    }

    /**
     * Method to withdraw money from the BankAccount.
     * @param fromAccountId the id of the BankAccount from which the money is withdrawn.
     * @param money the sum of money to withdraw from the BankAccount with id fromAccountId.
     * @return the completed transaction.
     */
    public Transaction withdrawMoneyFromTheAccount(@NonNull UUID fromAccountId, @NonNull TransactionMoney money) {
        BankAccount fromAccount = getBankAccount(fromAccountId);
        checkTransactionMoney(money, fromAccount);

        var id = UUID.randomUUID();
        var transaction = new WithdrawalTransaction(id, money, Clock.getInstance().getDateTimeNow(), fromAccount);
        transaction.execute();

        return transaction;
    }

    /**
     * Method to transfer money from one BankAccount to other BankAccount.
     * @param fromAccountId the id of the BankAccount from which the money is withdrawn.
     * @param toAccountId the id of the BankAccount that receives the money.
     * @param money the sum of money to transfer from one BankAccount with id fromAccountId to other BankAccount with
     *              id toAccountId.
     * @return the completed transaction.
     */
    public Transaction transferMoney(@NonNull UUID fromAccountId, @NonNull UUID toAccountId,
                                     @NonNull TransactionMoney money) {
        BankAccount fromAccount = getBankAccount(fromAccountId);
        BankAccount toAccount = getBankAccount(toAccountId);
        checkTransactionMoney(money, fromAccount);
        checkTransactionMoney(money, toAccount);

        var id = UUID.randomUUID();
        var transaction = new TransferTransaction(id, money, Clock.getInstance().getDateTimeNow(),
                fromAccount, toAccount);
        transaction.execute();

        return transaction;
    }

    /**
     * Method to cancel transaction.
     * @param transactionId the id of the Transaction which should be canceled.
     */
    public void cancelTransaction(@NonNull UUID transactionId) {
        Transaction transaction = getTransaction(transactionId);

        transaction.cancel();
    }

    /**
     * Method to get the BankAccount by its id.
     * @param bankAccountId the id of the BankAccount which should be found.
     * @return the found BankAccount.
     * @throws BankException if the BankAccount was not found.
     */
    public BankAccount getBankAccount(@NonNull UUID bankAccountId) {
        return banks.stream().map(Bank::getAccounts).flatMap(Collection::stream)
                .filter(a -> a.getId().equals(bankAccountId)).findFirst()
                .orElseThrow(() -> BankException.accountNotFound(bankAccountId));
    }

    private Bank getBankOfAccount(@NonNull BankAccount bankAccount) {
        return banks.stream().filter(b -> b.getAccounts().stream().anyMatch(a -> a == bankAccount)).findFirst()
                .orElseThrow(() -> BankException.accountNotFound(bankAccount.getId()));
    }

    private Transaction getTransaction(@NonNull UUID transactionId) {
        return banks.stream().map(Bank::getAccounts).flatMap(Collection::stream)
                .map(BankAccount::getTransactionsHistory).flatMap(Collection::stream)
                .filter(t -> t.getId().equals(transactionId)).findFirst()
                .orElseThrow(() -> BankException.transactionNotFound(transactionId));
    }

    private void checkTransactionMoney(@NonNull TransactionMoney money, @NonNull BankAccount account) {
        Bank bank = getBankOfAccount(account);

        if (money.getCount().compareTo(bank.getTransactionLimit().getCount()) > 0) {
            throw TransactionException.limitExceeding(money, bank.getTransactionLimit());
        }
    }
}