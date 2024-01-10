package com.shilaeva.interfaces;

/**
 * Class describing object Notifier of the Client.
 *
 * @version 2.0
 * @author Nadezhda Shilaeva
 */
public interface Notifier {
    /**
     * Method to send notification to notify Client.
     * @param message the message of the notification.
     */
    void sendNotification(String message);
}
