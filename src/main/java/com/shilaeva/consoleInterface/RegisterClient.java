package com.shilaeva.consoleInterface;

import com.shilaeva.entities.Bank;
import com.shilaeva.entities.Client;
import java.util.Scanner;

public class RegisterClient {
    private final Bank bank;
    public RegisterClient(Bank bank) {
        this.bank = bank;
    }

    public void Handle() {
        Scanner in = new Scanner(System.in);
        String answer;

        var clientBuilder = new Client.ClientBuilder();

        while (true) {
            System.out.println("Enter the name of client:");
            answer = in.nextLine();
            try {
                clientBuilder.setName(answer);
            } catch (Exception e) {
                System.out.println(e.getMessage());
                continue;
            }

            break;
        }

        while (true) {
            System.out.println("Enter the surname of client:");
            answer = in.nextLine();
            try {
                clientBuilder.setSurname(answer);
            } catch (Exception e) {
                System.out.println(e.getMessage());
                continue;
            }

            break;
        }

        while (true) {
            System.out.println("Enter the address of client or press enter to skip:");
            answer = in.nextLine();
            if (answer != null && answer.isEmpty()) {
                break;
            }

            try {
                clientBuilder.setAddress(answer);
            } catch (Exception e) {
                System.out.println(e.getMessage());
                continue;
            }

            break;
        }

        while (true) {
            System.out.println("Enter the passport number of client or press enter to skip:");
            answer = in.nextLine();
            if (answer != null && answer.isEmpty()) {
                break;
            }

            try {
                clientBuilder.setPassport(Integer.parseInt(answer));
            } catch (Exception e) {
                System.out.println(e.getMessage());
                continue;
            }

            break;
        }

        Client client = clientBuilder.build();
        bank.registerClient(client);

        System.out.println("The client " + client.getName() + " " + client.getSurname() + " with ID " + client.getId()
                + " is registered in the Bank " + bank.getName() + ".\n");

        System.out.println("Press any key to go back.");
        in.nextLine();
        new BankHandler(bank).Handle();
    }
}
