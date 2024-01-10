package com.shilaeva.consoleInterface;

import com.shilaeva.entities.Bank;
import java.util.Scanner;

public class ShowAccountActivePeriod {
    private final Bank bank;

    public ShowAccountActivePeriod(Bank bank) {
        this.bank = bank;
    }

    public void Handle() {
        System.out.println("Account active period: " + bank.getAccountActivePeriodYears() + " years\n");
        System.out.println("Press any key to go back.");

        new Scanner(System.in).nextLine();

        new BankHandler(bank).Handle();
    }
}
