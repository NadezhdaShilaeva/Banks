package com.shilaeva.consoleInterface;

import com.shilaeva.entities.Bank;
import java.util.Scanner;
import java.util.UUID;

public class AddClientAddress {
    private final Bank bank;

    public AddClientAddress(Bank bank) {
        this.bank = bank;
    }

    public void Handle() {
        Scanner in = new Scanner(System.in);

        while (true) {
            System.out.println("Enter the ID of the client:");
            String clientID = in.nextLine();

            System.out.println("Enter the address of the client:");
            String address = in.nextLine();

            try {
                bank.setClientAddress(UUID.fromString(clientID), address);
                System.out.println("The address was added to the client with ID " + clientID + ".\n");
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
