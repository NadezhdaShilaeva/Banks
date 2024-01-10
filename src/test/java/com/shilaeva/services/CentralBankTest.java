package com.shilaeva.services;

import com.shilaeva.exceptions.BankAccountException;
import com.shilaeva.interfaces.BankAccount;
import com.shilaeva.interfaces.Transaction;
import com.shilaeva.models.DepositAccountRates;
import com.shilaeva.models.DepositAccountRate;
import com.shilaeva.entities.Bank;
import com.shilaeva.entities.Client;
import com.shilaeva.models.TransactionMoney;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

class CentralBankTest {
    private static DepositAccountRates rates = DepositAccountRates.builder()
            .addRate(new DepositAccountRate(BigDecimal.ZERO,  BigDecimal.valueOf(3)))
            .addRate(new DepositAccountRate(BigDecimal.valueOf(50000), BigDecimal.valueOf(3.5)))
            .addRate(new DepositAccountRate(BigDecimal.valueOf(100000), BigDecimal.valueOf(4)))
            .build();

    private CentralBank centralBank = CentralBank.getInstance();

    private Bank bank = Bank.builder()
            .setName("Tinkoff")
            .setDebitPercent(BigDecimal.valueOf(5))
            .setCommission(BigDecimal.valueOf(100))
            .setCreditAccountLimit(BigDecimal.valueOf(-50000))
            .setAccountLimitAbove(BigDecimal.valueOf(100000))
            .setTransactionLimit(BigDecimal.valueOf(100000))
            .setAccountActivePeriod(4)
            .setDepositAccountRates(rates)
            .build();

    private Client client = Client.builder()
            .setName("Nadezhda")
            .setSurname("Shiliaeva")
            .build();

    @Test
    public void operationsWithDebitAccount_getRelevantMoneyCount() {
        centralBank.registerBank(bank);
        bank.registerClient(client);

        BankAccount debitAccount = bank.createDebitBankAccount(client.getId());
        centralBank.putMoneyToTheAccount(debitAccount.getId(), new TransactionMoney(BigDecimal.valueOf(5000)));
        Transaction transaction = centralBank
                .withdrawMoneyFromTheAccount(debitAccount.getId(), new TransactionMoney(BigDecimal.valueOf(2000)));
        centralBank.cancelTransaction(transaction.getId());

        assertEquals(BigDecimal.valueOf(5000), debitAccount.getMoney());
    }

    @Test
    public void operationsWithDepositAccount_getRelevantMoneyCount() {
        centralBank.registerBank(bank);
        bank.registerClient(client);

        BankAccount depositAccount = bank.createDepositBankAccount(client.getId(), BigDecimal.valueOf(20000), 30);
        centralBank.putMoneyToTheAccount(depositAccount.getId(), new TransactionMoney(BigDecimal.valueOf(5000)));
        centralBank.putMoneyToTheAccount(depositAccount.getId(), new TransactionMoney(BigDecimal.valueOf(1000)));
        centralBank.putMoneyToTheAccount(depositAccount.getId(), new TransactionMoney(BigDecimal.valueOf(500)));

        assertEquals(BigDecimal.valueOf(26500), depositAccount.getMoney());
        Assertions.assertThrows(BankAccountException.class, () -> centralBank
                .withdrawMoneyFromTheAccount(depositAccount.getId(), new TransactionMoney(BigDecimal.valueOf(5000))));
    }

    @Test
    public void operationsWithCreditAccount_getRelevantMoneyCount()
    {
        centralBank.registerBank(bank);
        bank.registerClient(client);

        BankAccount creditAccount = bank.createCreditBankAccount(client.getId());
        centralBank.withdrawMoneyFromTheAccount(creditAccount.getId(), new TransactionMoney(BigDecimal.valueOf(5000)));
        centralBank.withdrawMoneyFromTheAccount(creditAccount.getId(), new TransactionMoney(BigDecimal.valueOf(1000)));
        centralBank.putMoneyToTheAccount(creditAccount.getId(), new TransactionMoney(BigDecimal.valueOf(500)));

        assertEquals(BigDecimal.valueOf(-5700), creditAccount.getMoney());
        Assertions.assertThrows(BankAccountException.class, () -> centralBank
                .withdrawMoneyFromTheAccount(creditAccount.getId(), new TransactionMoney(BigDecimal.valueOf(50000))));
    }

    @Test
    public void operationsOfTransferTransaction_getRelevantMoneyCount()
    {
        centralBank.registerBank(bank);
        bank.registerClient(client);

        Client client2 = Client.builder()
                .setName("Nadezhda")
                .setSurname("Shiliaeva")
                .build();

        bank.registerClient(client2);

        BankAccount debitAccount = bank.createDebitBankAccount(client.getId());
        BankAccount debitAccount2 = bank.createDebitBankAccount(client2.getId());
        centralBank.putMoneyToTheAccount(debitAccount.getId(), new TransactionMoney(BigDecimal.valueOf(5000)));
        centralBank.putMoneyToTheAccount(debitAccount2.getId(), new TransactionMoney(BigDecimal.valueOf(1000)));
        centralBank.transferMoney(debitAccount.getId(), debitAccount2.getId(),
                new TransactionMoney(BigDecimal.valueOf(2000)));

        assertEquals(BigDecimal.valueOf(3000), debitAccount.getMoney());
        assertEquals(BigDecimal.valueOf(3000), debitAccount2.getMoney());
    }
}