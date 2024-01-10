package com.shilaeva.consoleInterface;

import com.shilaeva.entities.Bank;
import java.util.Scanner;

public class BankHandler {
    private final Bank bank;

    public BankHandler(Bank bank) {
        this.bank = bank;
    }

    public void Handle() {
        System.out.println("Hello! Welcome to the Bank " + bank.getName() + "!");
        System.out.println("What do you want to do?");
        System.out.println("1. Register new client.");
        System.out.println("2. Add client address.");
        System.out.println("3. Add client passport.");
        System.out.println("4. Create debit bank account.");
        System.out.println("5. Create deposit bank account.");
        System.out.println("6. Create credit bank account.");
        System.out.println("7. Subscribe client to bank updates.");
        System.out.println("8. Show deposit account rates.");
        System.out.println("9. Show active period of the bank account.");
        System.out.println("10. Show transaction limit.");
        System.out.println("11. Show debit percent.");
        System.out.println("12. Show commission.");
        System.out.println("13. Show credit account limit.");
        System.out.println("14. Show account limit above.");
        System.out.println("15. Exit the bank.");
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
            case 1 -> new RegisterClient(bank).Handle();
            case 2 -> new AddClientAddress(bank).Handle();
            case 3 -> new AddClientPassport(bank).Handle();
            case 4 -> new CreateDebitBankAccount(bank).Handle();
            case 5 -> new CreateDepositBankAccount(bank).Handle();
            case 6 -> new CreateCreditBankAccount(bank).Handle();
            case 7 -> new SubscribeToBankUpdates(bank).Handle();
            case 8 -> new ShowDepositAccountRates(bank).Handle();
            case 9 -> new ShowAccountActivePeriod(bank).Handle();
            case 10 -> new ShowTransactionLimit(bank).Handle();
            case 11 -> new ShowDebitPercent(bank).Handle();
            case 12 -> new ShowCommission(bank).Handle();
            case 13 -> new ShowCreditAccountLimit(bank).Handle();
            case 14 -> new ShowAccountLimitAbove(bank).Handle();
            default -> {
                System.out.println("Thank you for your visit to the Bank " + bank.getName() + "!\n");
                new CentralBankHandler().Handle();
            }
        }
    }

    private boolean isValidAnswer(String answer) {
        int number;

        try {
            number = Integer.parseInt(answer);
        } catch (Exception e) {
            return false;
        }

        return number >= 1 && number <= 15;
    }
}
