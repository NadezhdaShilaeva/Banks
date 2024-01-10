package com.shilaeva.consoleInterface;

import com.shilaeva.entities.Bank;
import com.shilaeva.models.Percent;
import java.math.BigDecimal;
import java.util.Scanner;

public class ShowDebitPercent {
    private final Bank bank;

    public ShowDebitPercent(Bank bank) {
        this.bank = bank;
    }

    public void Handle() {
        System.out.println("Debit percent: " + bank.getDebitPercent().getNumber());
        System.out.println("Do you want to change debit percent? (Enter yes/no)");

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
            System.out.println("Enter new percent for debit account:");
            String answer = in.nextLine();
            try {
                bank.setDebitPercent(new Percent(BigDecimal.valueOf(Double.parseDouble(answer))));
                System.out.println("The debit percent changed.");
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
