package com.shilaeva.consoleInterface;

import com.shilaeva.entities.Bank;
import com.shilaeva.services.CentralBank;
import java.util.Scanner;

public class ChooseBank {
    public void Handle() {
        if (CentralBank.getInstance().getBanks().size() == 0) {
            System.out.println("There are no registered banks.\n");
            new CentralBankHandler().Handle();

            return;
        }

        System.out.println("List of banks registered in the Central Bank:");
        int bankIndex = 1;
        for (Bank bank : CentralBank.getInstance().getBanks()) {
            System.out.println(bankIndex + ". " + bank.getName());
            bankIndex++;
        }

        Scanner in = new Scanner(System.in);

        System.out.println("Choose one number of the banks.");
        String answer = in.nextLine();

        while (answer == null || !isValidAnswer(answer)) {
            System.out.println("The number is incorrect.");
            System.out.println("Choose one number of the banks.");
            answer = in.nextLine();
        }

        int number = Integer.parseInt(answer);

        new BankHandler(CentralBank.getInstance().getBanks().stream().toList().get(number - 1)).Handle();
    }

    private boolean isValidAnswer(String answer) {
        int number;

        try {
            number = Integer.parseInt(answer);
        } catch (Exception e) {
            return false;
        }

        return number >= 1 && number <= CentralBank.getInstance().getBanks().size();
    }
}
