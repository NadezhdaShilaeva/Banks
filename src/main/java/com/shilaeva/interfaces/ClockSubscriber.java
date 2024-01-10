package com.shilaeva.interfaces;

import java.time.LocalDateTime;

/**
 * Class describing object ClockSubscriber.
 *
 * @version 2.0
 * @author Nadezhda Shilaeva
 */
public interface ClockSubscriber {
    /**
     * Method to update the instance of ClockSubscriber.
     * @param newDateTime the new date and time of the Clock.
     */
    void update(LocalDateTime newDateTime);
}
