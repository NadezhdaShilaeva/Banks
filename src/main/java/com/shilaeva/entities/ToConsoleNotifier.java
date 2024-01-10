package com.shilaeva.entities;

import com.shilaeva.interfaces.Notifier;

/**
 * Class describing object ToConsoleNotifier of the Client.
 *
 * @version 2.0
 * @author Nadezhda Shilaeva
 */
public class ToConsoleNotifier implements Notifier {
    /**
     * Constructor of the class ToConsoleNotifier.
     */
    public ToConsoleNotifier() { }

    /**
     * Method to print message to the console.
     * @param message the message of the notification.
     */
    public void sendNotification(String message) {
        System.out.println(message);
    }
}
