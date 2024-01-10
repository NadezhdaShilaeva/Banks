package com.shilaeva.consoleInterface;

import com.shilaeva.entities.Bank;
import com.shilaeva.interfaces.BankAccount;
import java.util.Scanner;
import java.util.UUID;

public class CreateDebitBankAccount {
    private final Bank bank;

    public CreateDebitBankAccount(Bank bank) {
        this.bank = bank;
    }

    public void Handle() {
        Scanner in = new Scanner(System.in);

        while (true) {
            System.out.println("Enter the ID of the client:");
            String clientID = in.nextLine();

            try {
                BankAccount bankAccount = bank.createDebitBankAccount(UUID.fromString(clientID));
                System.out.println("The debit bank account with ID: " + bankAccount.getId()
                        + " was created for the client with ID " + clientID + ".");
            } catch (Exception e) {
                System.out.println(e.getMessage());
                System.out.println("Do you want to try again?");
                System.out.println("Enter 'yes' if you want.");

                String answer = in.nextLine();
                if (answer != null && answer.equalsIgnoreCase("yes")) {
                    continue;
                }
            }

            break;
        }

        System.out.println("Press any key to go back.");
        in.nextLine();
        new BankHandler(bank).Handle();
    }
}
