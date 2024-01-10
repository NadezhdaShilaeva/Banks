package com.shilaeva.exceptions;

import java.util.UUID;

public class ClientException extends RuntimeException {
    private ClientException(String message) {
        super(message);
    }

    public static ClientException noRequiredData(UUID id) {
        return new ClientException("Error: the name or surname of client (ID:" + id + ") is not specified.");
    }

    public static ClientException invalidName(String name) {
        return new ClientException("Error: the name " + name + " of client is not valid.");
    }

    public static ClientException invalidSurname(String surname) {
        return new ClientException("Error: the surname " + surname + " of client is not valid.");
    }

    public static ClientException invalidAddress(String address) {
        return new ClientException("Error: the address " + address + " of client is not valid.");
    }

    public static ClientException invalidPassportData(int number) {
        return new ClientException("Error: passport data " + number + " is invalid.");
    }
}
