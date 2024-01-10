package com.shilaeva;

import com.shilaeva.consoleInterface.CentralBankHandler;

/**
 * Class where the program execution begins.
 *
 * @version 2.0
 * @author Nadezhda Shilaeva
 */
public class Main {
    public static void main(String[] args) {
        new CentralBankHandler().Handle();
    }
}