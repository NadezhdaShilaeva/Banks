package com.shilaeva.exceptions;

public class ClockException extends RuntimeException {
    private ClockException (String message) {
        super(message);
    }

    public static ClockException subscriberIsAlreadyExist() {
        return new ClockException("Error: the subscriber is already subscribed to the clock.");
    }

    public static ClockException subscriberNotRemoved() {
        return new ClockException("Error: failed to unsubscribe the subscriber from the clock.");
    }
}
