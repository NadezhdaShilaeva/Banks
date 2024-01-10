package com.shilaeva.exceptions;

import com.shilaeva.entities.Client;
import java.math.BigDecimal;
import java.util.UUID;

public class BankException extends RuntimeException {
    private BankException(String message) {
        super(message);
    }

    public static BankException clientIsAlreadyExist(Client client) {
        return new BankException("Error: the client with ID " + client.getId() + " is already registered in the bank.");
    }

    public static BankException invalidRate(BigDecimal moneyCount) {
        return new BankException("Error: the min money count " + moneyCount
                + " of rate cannot be less than previous min money count of rate.");
    }

    public static BankException invalidPercent(BigDecimal number) {
        return new BankException("Error: the percent " + number + " is not valid.");
    }

    public static BankException invalidLimit(BigDecimal limit) {
        return new BankException("Error: the limit " + limit + " is not valid.");
    }

    public static BankException invalidCommission(BigDecimal commission) {
        return new BankException("Error: the commission " + commission + " is not valid.");
    }

    public static BankException invalidMoney(BigDecimal money) {
        return new BankException("Error: the money " + money + " cannot be less than 0.");
    }

    public static BankException invalidDaysToOpen(int daysNumber) {
        return new BankException("Error: unable to open a bank account due to lack of days " + daysNumber + ".");
    }

    public static BankException invalidPeriodYears(int yearsNumber) {
        return new BankException("Error: the years of the account active period " + yearsNumber
                + " cannot be less than 1.");
    }

    public static BankException noRequiredData(UUID id) {
        return new BankException("Error: the required data of bank (ID:" + id + ") is not specified.");
    }

    public static BankException clientNotFound(UUID id) {
        return new BankException("Error: the client with ID: " + id + " is not found.");
    }

    public static BankException accountNotFound(UUID id) {
        return new BankException("Error: the bank account with ID: " + id + " is not found.");
    }

    public static BankException transactionNotFound(UUID id) {
        return new BankException("Error: the transaction with ID: " + id + " is not found.");
    }

    public static BankException rateNotFound(BigDecimal money) {
        return new BankException("Error: no deposit rate was found for this amount of money " + money + ".");
    }
}
