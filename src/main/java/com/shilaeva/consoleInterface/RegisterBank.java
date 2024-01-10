package com.shilaeva.consoleInterface;

import com.shilaeva.entities.Bank;
import com.shilaeva.models.DepositAccountRate;
import com.shilaeva.models.DepositAccountRates;
import com.shilaeva.services.CentralBank;
import java.math.BigDecimal;
import java.util.Scanner;

public class RegisterBank {
    public void Handle() {
        String answer = null;

        var bankBuilder = new Bank.BankBuilder();

        Scanner in = new Scanner(System.in);
        while (answer == null) {
            System.out.println("Enter the name of bank:");
            answer = in.nextLine();
        }

        bankBuilder.setName(answer);

        while (true) {
            System.out.println("Enter the percent for debit account:");
            answer = in.nextLine();
            try {
                bankBuilder.setDebitPercent(BigDecimal.valueOf(Double.parseDouble(answer)));
            } catch (Exception e) {
                System.out.println(e.getMessage());
                continue;
            }

            break;
        }

        while (true) {
            System.out.println("Enter the commission for credit account:");
            answer = in.nextLine();
            try {
                bankBuilder.setCommission(BigDecimal.valueOf(Double.parseDouble(answer)));
            } catch (Exception e) {
                System.out.println(e.getMessage());
                continue;
            }

            break;
        }

        while (true) {
            System.out.println("Enter the credit limit for credit account:");
            answer = in.nextLine();
            try {
                bankBuilder.setCreditAccountLimit(BigDecimal.valueOf(Double.parseDouble(answer)));
            } catch (Exception e) {
                System.out.println(e.getMessage());
                continue;
            }

            break;
        }

        while (true) {
            System.out.println("Enter the limit above for accounts:");
            answer = in.nextLine();
            try {
                bankBuilder.setAccountLimitAbove(BigDecimal.valueOf(Double.parseDouble(answer)));
            } catch (Exception e) {
                System.out.println(e.getMessage());
                continue;
            }

            break;
        }

        while (true) {
            System.out.println("Enter the limit above for transactions with account:");
            answer = in.nextLine();
            try {
                bankBuilder.setTransactionLimit(BigDecimal.valueOf(Double.parseDouble(answer)));
            } catch (Exception e) {
                System.out.println(e.getMessage());
                continue;
            }

            break;
        }

        while (true) {
            System.out.println("Enter the active period in years for debit and credit accounts:");
            answer = in.nextLine();
            try {
                bankBuilder.setAccountActivePeriod(Integer.parseInt(answer));
            } catch (Exception e) {
                System.out.println(e.getMessage());
                continue;
            }

            break;
        }

        bankBuilder.setDepositAccountRates(createDepositAccountRates(in));

        Bank bank = bankBuilder.build();
        CentralBank.getInstance().registerBank(bank);

        System.out.println("The bank " + bank.getName() + " with ID " + bank.getId()
                + " is registered in the Central Bank.\n");

        System.out.println("Press any key to go back.");
        in.nextLine();
        new CentralBankHandler().Handle();
    }

    private DepositAccountRates createDepositAccountRates(Scanner in) {
        System.out.println("Create rates for the deposit bank accounts.");
        System.out.println("Attention: the initial amount of each next rate must be greater than the previous one.\n");

        var builder = new DepositAccountRates.DepositAccountRatesBuilder();

        String answer = null;
        while (answer == null || !isFinishAnswer(answer)) {
            System.out.println("1. Add rate.");
            System.out.println("2. Finish.");
            System.out.println("Choose one number of the menu.");
            answer = in.nextLine();

            while (answer == null || !isValidAnswer(answer)) {
                System.out.println("The number is incorrect.");
                System.out.println("Choose one number of the menu again.");
                answer = in.nextLine();
            }

            int number = Integer.parseInt(answer);
            switch (number)
            {
                case 1:
                    try {
                        builder.addRate(createDepositAccountRate(in));
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case 2:
                    break;
            }
        }

        return builder.build();
    }

    private DepositAccountRate createDepositAccountRate(Scanner in) {
        String answer;
        BigDecimal minMoneyCount;
        BigDecimal percent;

        while (true) {
            System.out.println("Enter the minimum count of money to new rate:");
            answer = in.nextLine();
            try {
                minMoneyCount = BigDecimal.valueOf(Double.parseDouble(answer));
            } catch (Exception e) {
                System.out.println(e.getMessage());
                continue;
            }

            break;
        }

        while (true) {
            System.out.println("Enter the percent to new rate:");
            answer = in.nextLine();
            try {
                percent = BigDecimal.valueOf(Double.parseDouble(answer));
            } catch (Exception e) {
                System.out.println(e.getMessage());
                continue;
            }

            break;
        }

        return new DepositAccountRate(minMoneyCount, percent);
    }

    private boolean isValidAnswer(String answer) {
        int number;

        try {
            number = Integer.parseInt(answer);
        } catch (Exception e) {
            return false;
        }

        return number >= 1 && number <= 2;
    }

    private boolean isFinishAnswer(String answer) {
        int number;

        try {
            number = Integer.parseInt(answer);
        } catch (Exception e) {
            return false;
        }

        return number == 2;
    }

}
