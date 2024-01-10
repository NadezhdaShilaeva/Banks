package com.shilaeva.entities;

import com.shilaeva.exceptions.BankException;
import com.shilaeva.interfaces.BankAccount;
import com.shilaeva.models.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.UUID;
import lombok.Getter;
import lombok.NonNull;

/**
 * Class describing object Bank.
 *
 * @version 2.0
 * @author Nadezhda Shilaeva
 */
public class Bank {
    @Getter
    private final UUID id;
    @Getter
    private final String name;
    private final Collection<Client> clients;
    private final Collection<BankAccount> accounts;
    @Getter
    private Percent debitPercent;
    @Getter
    private Commission commission;
    @Getter
    private LimitBelow creditAccountLimit;
    @Getter
    private LimitAbove accountLimitAbove;
    @Getter
    private LimitAbove transactionLimit;
    @Getter
    private DepositAccountRates depositAccountRates;
    @Getter
    private int accountActivePeriodYears;

    private Bank(
            UUID id,
            String name,
            Percent debitPercent,
            Commission commission,
            LimitBelow creditAccountLimit,
            LimitAbove accountLimitAbove,
            LimitAbove transactionLimit,
            int accountActivePeriod,
            DepositAccountRates depositAccountRates) {
        this.id = id;
        this.name = name;
        this.debitPercent = debitPercent;
        this.commission = commission;
        this.creditAccountLimit = creditAccountLimit;
        this.accountLimitAbove = accountLimitAbove;
        this.transactionLimit = transactionLimit;
        this.accountActivePeriodYears = accountActivePeriod;
        this.depositAccountRates = depositAccountRates;

        clients = new ArrayList<Client>();
        accounts = new ArrayList<BankAccount>();
    }

    /**
     * Static method to get builder of the class Bank.
     * @return the new instance of class BankBuilder.
     */
    public static BankBuilder builder() {
        return new BankBuilder();
    }

    /**
     * Method to get unmodifiable collection of clients of the bank.
     * @return unmodifiable collection of Clients of the bank.
     */
    public Collection<Client> getClients() {
        return Collections.unmodifiableCollection(clients);
    }

    /**
     * Method to get unmodifiable collection of accounts of the bank.
     * @return unmodifiable collection of Accounts of the bank.
     */
    public Collection<BankAccount> getAccounts() {
        return Collections.unmodifiableCollection(accounts);
    }

    /**
     * Method to set transactionLimit of the Bank.
     * @param transactionLimit the new transactionLimit of the Bank.
     */
    public void setTransactionLimit(@NonNull LimitAbove transactionLimit) {
        this.transactionLimit = transactionLimit;

        accounts.forEach(a -> a.getClient().getNotification("The limit above of transactions changed to "
                + transactionLimit.getCount() + "."));
    }

    /**
     * Method to set percent of the Bank.
     * @param percent the new percent of the Bank.
     */
    public void setDebitPercent(@NonNull Percent percent) {
        debitPercent = percent;

        accounts.forEach(a -> {
            if (a instanceof DebitBankAccount) {
                a.setPercent(percent);
                a.getClient().getNotification("The debit bank account percent changed to "
                        + percent.getNumber() + ".");
            }
            });
    }

    /**
     * Method to set commission of the Bank.
     * @param commission the new commission of the Bank.
     */
    public void setCommission(@NonNull Commission commission) {
        this.commission = commission;

        accounts.forEach(a -> {
            if (a instanceof DebitBankAccount) {
                a.setCommission(commission);
                a.getClient().getNotification("The commission changed to " + commission.getCount() + ".");
            }
        });
    }

    /**
     * Method to set creditAccountLimit of the Bank.
     * @param creditAccountLimit the new creditAccountLimit of the Bank.
     */
    public void setCreditAccountLimit(@NonNull LimitBelow creditAccountLimit)
    {
        this.creditAccountLimit = creditAccountLimit;

        accounts.forEach(a -> {
            if (a instanceof CreditBankAccount) {
                a.setLimitBelow(creditAccountLimit);
                a.getClient().getNotification("The limit below for the credit bank accounts changed to "
                        + creditAccountLimit.getCount() + ".");
            }
        });
    }

    /**
     * Method to set accountLimitAbove of the Bank.
     * @param accountLimitAbove the new accountLimitAbove of the Bank.
     */
    public void setAccountLimitAbove(@NonNull LimitAbove accountLimitAbove)
    {
        this.accountLimitAbove = accountLimitAbove;

        accounts.forEach(a -> {
            a.setLimitAbove(accountLimitAbove);
            a.getClient().getNotification("The limit above for the bank account money for suspicious clients changed to "
                    + accountLimitAbove.getCount() + ".");
        });
    }

    /**
     * Method to register and add new client to clients of the Bank.
     * @param client the new client which should be added to the clients of the Bank.
     */
    public void registerClient(@NonNull Client client) {
        if (clients.stream().anyMatch(c -> c == client)) {
            throw BankException.clientIsAlreadyExist(client);
        }

        clients.add(client);
    }

    /**
     * Method to set address of the client with id clientId.
     * @param clientId the id of the client who set the address.
     * @param address the address of the client which should be set.
     */
    public void setClientAddress(@NonNull UUID clientId, @NonNull String address) {
        Client client = getClient(clientId);
        client.setAddress(address);
    }

    /**
     * Method to set passport of the client with id clientId.
     * @param clientId the id of the client who set the passport.
     * @param passport the passport of the client which should be set.
     */
    public void setClientPassport(@NonNull UUID clientId, @NonNull PassportData passport) {
        Client client = getClient(clientId);
        client.setPassport(passport);
    }

    /**
     * Method to create new DebitBankAccount to the client with id clientId.
     * @param clientId the id of the client who create DebitBankAccount.
     * @return new instance of DebitBankAccount.
     */
    public DebitBankAccount createDebitBankAccount(@NonNull UUID clientId)
    {
        Client client = getClient(clientId);
        var id = UUID.randomUUID();

        LocalDateTime endDate = Clock.getInstance().getDateTimeNow().plusYears(accountActivePeriodYears);
        var newDebitAccount = new DebitBankAccount(id, client, Clock.getInstance(), endDate, debitPercent,
                accountLimitAbove);
        accounts.add(newDebitAccount);

        return newDebitAccount;
    }

    /**
     * Method to create new DepositBankAccount to the client with id clientId.
     * @param clientId the id of the client who create DepositBankAccount.
     * @param money the sum of money to create DepositBankAccount.
     * @param daysNumber the number of days for which DepositBankAccount opens.
     * @return new instance of DepositBankAccount.
     * @throws BankException if the sum of money is less than 0 or if days to open are less than 1 or if relevant rate
     * was not found.
     */
    public DepositBankAccount createDepositBankAccount(@NonNull UUID clientId, @NonNull BigDecimal money, int daysNumber)
    {
        Client client = getClient(clientId);

        BigDecimal minMoney = BigDecimal.ZERO;
        int minDaysNumber = 1;

        if (money.compareTo(minMoney) < 0) {
            throw BankException.invalidMoney(money);
        }

        if (daysNumber < minDaysNumber) {
            throw BankException.invalidDaysToOpen(daysNumber);
        }

        var id = UUID.randomUUID();

        DepositAccountRate relevantRate = depositAccountRates.getRates().stream()
                .filter(r -> r.getMinMoneyCount().compareTo(money) <= 0).reduce(null, (x, y) -> y);
        if (relevantRate == null) {
            throw BankException.rateNotFound(money);
        }

        Percent relevantPercent = relevantRate.getPercent();
        LocalDateTime endDate = Clock.getInstance().getDateTimeNow().plusDays(daysNumber);
        var newDepositAccount = new DepositBankAccount(id, client, Clock.getInstance(), endDate, relevantPercent, money,
                accountLimitAbove);
        accounts.add(newDepositAccount);

        return newDepositAccount;
    }

    /**
     * Method to create new CreditBankAccount to the client with id clientId.
     * @param clientId the id of the client who create CreditBankAccount.
     * @return new instance of CreditBankAccount.
     */
    public CreditBankAccount createCreditBankAccount(@NonNull UUID clientId) {
        Client client = getClient(clientId);
        var id = UUID.randomUUID();

        LocalDateTime endDate = Clock.getInstance().getDateTimeNow().plusYears(accountActivePeriodYears);
        var newCreditAccount = new CreditBankAccount(id, client, Clock.getInstance(), endDate, commission,
                accountLimitAbove, creditAccountLimit);
        accounts.add(newCreditAccount);

        return newCreditAccount;
    }

    /**
     * Method to subscribe client to updates of the Bank.
     * @param clientId the id of the client who should be notified of the bank updates.
     * @return the modified Client.
     */
    public Client subscribeToBankUpdates(@NonNull UUID clientId) {
        Client client = getClient(clientId);
        client.setNotifiable(true);

        return client;
    }

    private Client getClient(@NonNull UUID id) {
        return clients.stream().filter(c -> c.getId().equals(id)).findFirst()
                .orElseThrow(() -> BankException.clientNotFound(id));
    }

    /**
     * Static class to build class Bank.
     */
    public static class BankBuilder {
        private final UUID id;
        private String name;
        private Percent debitPercent;
        private Commission commission;
        private LimitBelow creditAccountLimit;
        private LimitAbove accountLimitAbove;
        private LimitAbove transactionLimit;
        private int accountActivePeriodYears;
        private DepositAccountRates depositAccountRates;

        public BankBuilder() {
            id = UUID.randomUUID();
            name = null;
            debitPercent = null;
            commission = null;
            creditAccountLimit = null;
            accountLimitAbove = null;
            transactionLimit = null;
            accountActivePeriodYears = 0;
            depositAccountRates = null;
        }

        /**
         * Method to set name of the Bank.
         * @param name the name of the Bank.
         * @return modified class BankBuilder.
         */
        public BankBuilder setName(@NonNull String name) {
            this.name = name;

            return this;
        }

        /**
         * Method to set debitPercent of the Bank.
         * @param debitPercent the debitPercent of the Bank.
         * @return modified class BankBuilder.
         */
        public BankBuilder setDebitPercent(@NonNull BigDecimal debitPercent) {
            this.debitPercent = new Percent(debitPercent);

            return this;
        }

        /**
         * Method to set commission of the Bank.
         * @param commission the commission of the Bank.
         * @return modified class BankBuilder.
         */
        public BankBuilder setCommission(@NonNull BigDecimal commission) {
            this.commission = new Commission(commission);

            return this;
        }

        /**
         * Method to set creditAccountLimit of the Bank.
         * @param creditAccountLimit the limit of the credit account of the Bank.
         * @return modified class BankBuilder.
         */
        public BankBuilder setCreditAccountLimit(@NonNull BigDecimal creditAccountLimit) {
            this.creditAccountLimit = new LimitBelow(creditAccountLimit);

            return this;
        }

        /**
         * Method to set accountLimitAbove of the Bank.
         * @param accountLimitAbove the limit above for the account of the Bank.
         * @return modified class BankBuilder.
         */
        public BankBuilder setAccountLimitAbove(@NonNull BigDecimal accountLimitAbove) {
            this.accountLimitAbove = new LimitAbove(accountLimitAbove);

            return this;
        }

        /**
         * Method to set transactionLimit of the Bank.
         * @param transactionLimit the limit for transaction of the Bank.
         * @return modified class BankBuilder.
         */
        public BankBuilder setTransactionLimit(@NonNull BigDecimal transactionLimit) {
            this.transactionLimit = new LimitAbove(transactionLimit);

            return this;
        }

        /**
         * Method to set accountActivePeriodYears of the Bank.
         * @param accountActiveYearsPeriod the account active period of the Bank in years.
         * @return modified class BankBuilder.
         * @throws BankException if the accountActiveYearsPeriod is not more than 0.
         */
        public BankBuilder setAccountActivePeriod(int accountActiveYearsPeriod) {
            if (accountActiveYearsPeriod <= 0) {
                throw BankException.invalidPeriodYears(accountActiveYearsPeriod);
            }

            this.accountActivePeriodYears = accountActiveYearsPeriod;

            return this;
        }

        /**
         * Method to set depositAccountRates of the Bank.
         * @param depositAccountRates the deposit account rates of the Bank.
         * @return modified class BankBuilder.
         */
        public BankBuilder setDepositAccountRates(@NonNull DepositAccountRates depositAccountRates) {
            this.depositAccountRates = depositAccountRates;

            return this;
        }

        /**
         * Method to finish the building of the class Bank.
         * @return new instance of the class Bank.
         * @throws BankException if any required data is missing.
         */
        public Bank build() {
            if (name == null || debitPercent == null || commission == null || creditAccountLimit == null
                    || accountLimitAbove == null || transactionLimit == null || accountActivePeriodYears == 0
                    || depositAccountRates == null) {
                throw BankException.noRequiredData(id);
            }

            return new Bank(
                    id,
                    name,
                    debitPercent,
                    commission,
                    creditAccountLimit,
                    accountLimitAbove,
                    transactionLimit,
                    accountActivePeriodYears,
                    depositAccountRates);
        }
    }
}
