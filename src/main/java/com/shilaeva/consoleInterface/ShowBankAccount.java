package com.shilaeva.consoleInterface;

import com.shilaeva.entities.CreditBankAccount;
import com.shilaeva.entities.DebitBankAccount;
import com.shilaeva.interfaces.BankAccount;
import com.shilaeva.models.DepositAccountRate;
import com.shilaeva.services.CentralBank;
import java.util.Scanner;
import java.util.UUID;

public class ShowBankAccount {
    public void Handle() {
        Scanner in = new Scanner(System.in);

        while (true) {
            System.out.println("Enter the ID of the bank account:");
            String accountID = in.nextLine();

            try {
                BankAccount account = CentralBank.getInstance().getBankAccount(UUID.fromString(accountID));

                PrintAccountInfo(account);
            } catch (Exception e) {
                System.out.println(e.getMessage());
                System.out.println("Do you want to try get information about the bank account again?");
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

    private void PrintAccountInfo(BankAccount account) {
        if (account instanceof DebitBankAccount) {
            System.out.println("The debit bank account.");
        }
        if (account instanceof DepositAccountRate) {
            System.out.println("The deposit bank account.");
        }
        if (account instanceof CreditBankAccount) {
            System.out.println("The credit bank account.");
        }
        System.out.println("Account ID:     " + account.getId());
        System.out.println("Client ID:      " + account.getClient().getId());
        System.out.println("Client name:    " + account.getClient().getName() + " " + account.getClient().getSurname());
        System.out.println("Count of money: " + account.getMoney());
        System.out.println("Percent:        " + account.getPercent().getNumber());
        System.out.println("Commission:     " + account.getCommission().getCount());
        System.out.println("Limit above:    " + account.getLimitAbove().getCount());
        System.out.println("Limit below:    " + account.getLimitBelow().getCount());
        System.out.println();
    }
}
