package com.shilaeva.consoleInterface;

import com.shilaeva.entities.Bank;
import com.shilaeva.interfaces.BankAccount;
import java.math.BigDecimal;
import java.util.Scanner;
import java.util.UUID;

public class CreateDepositBankAccount {
    private final Bank bank;

    public CreateDepositBankAccount(Bank bank) {
        this.bank = bank;
    }

    public void Handle() {
        Scanner in = new Scanner(System.in);

        while (true) {
            System.out.println("Enter the ID of the client:");
            String clientID = in.nextLine();

            System.out.println("Enter the count of money to open the deposit account:");
            String money = in.nextLine();

            System.out.println("Enter the count of days to open the deposit account:");
            String days = in.nextLine();

            try {
                BankAccount bankAccount = bank.createDepositBankAccount(UUID.fromString(clientID),
                        BigDecimal.valueOf(Double.parseDouble(money)), Integer.parseInt(days));
                System.out.println("The credit bank account with ID: " + bankAccount.getId() + " with percent "
                        + bankAccount.getPercent() + " was created for the client with ID " +  clientID + ".");
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
