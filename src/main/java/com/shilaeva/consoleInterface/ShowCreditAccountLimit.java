package com.shilaeva.consoleInterface;

import com.shilaeva.entities.Bank;
import com.shilaeva.models.LimitBelow;
import java.math.BigDecimal;
import java.util.Scanner;

public class ShowCreditAccountLimit {
    private final Bank bank;

    public ShowCreditAccountLimit(Bank bank) {
        this.bank = bank;
    }

    public void Handle() {
        System.out.println("Credit account limit: " + bank.getCreditAccountLimit().getCount());
        System.out.println("Do you want to change credit account limit? (Enter yes/no)");

        Scanner in = new Scanner(System.in);
        String answer = in.nextLine();

        while (answer == null
                || (!answer.equalsIgnoreCase("yes") && !answer.equalsIgnoreCase("no"))) {
            System.out.println("The answer is not valid. Enter the answer again:");
            answer = in.nextLine();
        }

        if (answer.equalsIgnoreCase("yes")) {
            changeData(in);
        }

        System.out.println("Press any key to go back.");
        in.nextLine();
        new BankHandler(bank).Handle();
    }

    private void changeData(Scanner in) {
        while (true) {
            System.out.println("Enter new credit limit for credit accounts:");
            String answer = in.nextLine();
            try {
                bank.setCreditAccountLimit(new LimitBelow(BigDecimal.valueOf(Double.parseDouble(answer))));
                System.out.println("The credit account limit changed.");
            } catch (Exception e) {
                System.out.println(e.getMessage());
                System.out.println("Do you want to try again?");
                System.out.println("Enter 'yes' if you want.");

                answer = in.nextLine();
                if (answer != null && answer.equalsIgnoreCase("yes")) {
                    continue;
                }
            }

            break;
        }
    }
}
