package com.shilaeva.exceptions;

import com.shilaeva.entities.Bank;

public class CentralBankException extends RuntimeException {
    private CentralBankException(String message) {
        super(message);
    }

    public static CentralBankException bankIsAlreadyExist(Bank bank) {
        return new CentralBankException("Error: the bank with ID " + bank.getId()
                + " is already registered in the central bank.");
    }
}
