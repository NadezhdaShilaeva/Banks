package com.shilaeva.consoleInterface;

import java.util.Scanner;

public class CentralBankHandler {
    public void Handle() {
        System.out.println("Hello! Welcome to the Central Bank!");
        System.out.println("What do you want to do?");
        System.out.println("1. Register new bank.");
        System.out.println("2. Choose the bank.");
        System.out.println("3. Put money to the account.");
        System.out.println("4. Withdraw money from the account.");
        System.out.println("5. Transfer money.");
        System.out.println("6. Cancel transaction.");
        System.out.println("7. Show information about the bank account.");
        System.out.println("8. Exit the central bank.");
        System.out.println("Choose one number of the menu.");

        Scanner in = new Scanner(System.in);
        String answer = in.nextLine();

        while (answer == null || !isValidAnswer(answer)) {
            System.out.println("The number is incorrect.");
            System.out.println("Choose one number of the menu again.");
            answer = in.nextLine();
        }

        int number = Integer.parseInt(answer);
        switch (number) {
            case 1 -> new RegisterBank().Handle();
            case 2 -> new ChooseBank().Handle();
            case 3 -> new PutMoneyToTheAccount().Handle();
            case 4 -> new WithdrawMoneyFromTheAccount().Handle();
            case 5 -> new TransferMoney().Handle();
            case 6 -> new CancelTransaction().Handle();
            case 7 -> new ShowBankAccount().Handle();
            default -> System.out.println("Thank you for your visit to the Central Bank!");
        }
    }

    private boolean isValidAnswer(String answer) {
        int number;

        try {
            number = Integer.parseInt(answer);
        } catch (Exception e) {
            return false;
        }

        return number >= 1 && number <= 8;
    }
}
