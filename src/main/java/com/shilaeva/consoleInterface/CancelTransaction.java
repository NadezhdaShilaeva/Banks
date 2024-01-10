package com.shilaeva.consoleInterface;

import com.shilaeva.services.CentralBank;
import java.util.Scanner;
import java.util.UUID;

public class CancelTransaction {
    public void Handle() {
        Scanner in = new Scanner(System.in);

        while (true) {
            System.out.println("Enter the ID of the transaction to cancel:");
            String transactionId = in.nextLine();

            try {
                CentralBank.getInstance().cancelTransaction(UUID.fromString(transactionId));
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
        new CentralBankHandler().Handle();
    }
}
