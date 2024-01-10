package com.shilaeva.consoleInterface;

import com.shilaeva.interfaces.Transaction;
import com.shilaeva.models.TransactionMoney;
import com.shilaeva.services.CentralBank;
import java.math.BigDecimal;
import java.util.Scanner;
import java.util.UUID;

public class TransferMoney {
    Scanner in = new Scanner(System.in);

    public void Handle() {
        while (true) {
            System.out.println("Enter the ID of withdrowal account:");
            String fromAccountID = in.nextLine();

            System.out.println("Enter the ID of replenishment account:");
            String toAccountID = in.nextLine();

            System.out.println("Enter the count of money to transport between the accounts:");
            String money = in.nextLine();
            try {
                Transaction transaction = CentralBank.getInstance().transferMoney(
                        UUID.fromString(fromAccountID),
                        UUID.fromString(toAccountID),
                        new TransactionMoney(BigDecimal.valueOf(Double.parseDouble(money))));

                System.out.println("The transaction with ID " + transaction.getId() + " executed.\n");
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
