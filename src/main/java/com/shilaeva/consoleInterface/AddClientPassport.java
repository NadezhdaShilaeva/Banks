package com.shilaeva.consoleInterface;

import com.shilaeva.entities.Bank;
import com.shilaeva.models.PassportData;
import java.util.Scanner;
import java.util.UUID;

public class AddClientPassport {
    private final Bank bank;

    public AddClientPassport(Bank bank) {
        this.bank = bank;
    }

    public void Handle() {
        Scanner in = new Scanner(System.in);

        while (true) {
            System.out.println("Enter the ID of the client:");
            String clientID = in.nextLine();

            System.out.println("Enter the passport number of the client:");
            String passport = in.nextLine();

            try {
                bank.setClientPassport(UUID.fromString(clientID), new PassportData(Integer.parseInt(passport)));
                System.out.println("The passport number was added to the client with ID " + clientID + ".\n");
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
