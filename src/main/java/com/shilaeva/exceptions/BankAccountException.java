package com.shilaeva.exceptions;

import com.shilaeva.interfaces.BankAccount;
import com.shilaeva.entities.DepositBankAccount;

public class BankAccountException extends RuntimeException{
    private BankAccountException(String message) {
        super(message);
    }

    public static BankAccountException goingBeyondTheLimit(BankAccount bankAccount) {
        return new BankAccountException(
                "Error: the operation cannot be performed because it is beyond the limit of the account "
                + bankAccount.getId() + ".");
    }

    public static BankAccountException notActive(BankAccount bankAccount) {
        return new BankAccountException("Error: the operation cannot be performed because the bank account "
                + bankAccount.getId() + " is not active. The active period ended on " + bankAccount.getEndDate() + ".");
    }

    public static BankAccountException periodNotEnded(DepositBankAccount bankAccount) {
        return new BankAccountException("Error: the operation cannot be performed because the period of bank account "
                + bankAccount.getId() + " has not ended yet. It will end on " + bankAccount.getEndDate() + ".");
    }
}
