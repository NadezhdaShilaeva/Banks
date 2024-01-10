package com.shilaeva.consoleInterface;

import com.shilaeva.entities.Bank;
import com.shilaeva.entities.Client;
import com.shilaeva.entities.ToConsoleNotifier;
import java.util.Scanner;
import java.util.UUID;

public class SubscribeToBankUpdates {
    private final Bank bank;

    public SubscribeToBankUpdates(Bank bank) {
        this.bank = bank;
    }

    public void Handle() {
        Scanner in = new Scanner(System.in);

        while (true) {
            System.out.println("Enter the ID of the client:");
            String clientID = in.nextLine();

            try {
                Client client = bank.subscribeToBankUpdates(UUID.fromString(clientID));
                client.setNotifier(new ToConsoleNotifier());
                System.out.println("The clint with ID: " + clientID + " was subscribed to updates of bank "
                        + bank.getName() + ".");
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
