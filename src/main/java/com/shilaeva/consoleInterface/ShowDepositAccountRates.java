package com.shilaeva.consoleInterface;

import com.shilaeva.entities.Bank;
import com.shilaeva.models.DepositAccountRate;
import java.util.Scanner;

public class ShowDepositAccountRates {
    private final Bank bank;

    public ShowDepositAccountRates(Bank bank) {
        this.bank = bank;
    }

    public void Handle() {
        System.out.printf("%1$22s%2$10s\n", "Minimum count of money", "Percent");
        for (DepositAccountRate rate : bank.getDepositAccountRates().getRates()) {
            System.out.printf("%1$22s%2$10s\n", rate.getMinMoneyCount(), rate.getPercent().getNumber());
        }

        System.out.println();
        System.out.println("Press any key to go back.");
        new Scanner(System.in).nextLine();
        new BankHandler(bank).Handle();
    }
}
