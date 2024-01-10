package com.shilaeva.entities;

import com.shilaeva.exceptions.ClockException;
import com.shilaeva.interfaces.ClockSubscriber;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import lombok.Getter;

/**
 * Class describing singleton object Clock.
 *
 * @version 2.0
 * @author Nadezhda Shilaeva
 */
public class Clock {
    private static Clock instance;
    @Getter
    private LocalDateTime dateTimeNow;
    private Collection<ClockSubscriber> subscribers;

    private Clock(LocalDateTime dateTimeNow) {
        this.dateTimeNow = dateTimeNow;
        subscribers = new ArrayList<>();
    }

    /**
     * Method to get instance of the singleton object of Clock with the current date and time.
     * @return instance of the Clock.
     */
    public static Clock getInstance() {
        if (instance == null) {
            instance = new Clock(LocalDateTime.now());
        }

        return instance;
    }

    /**
     * Method to subscribe new subscriber to the Clock.
     * @param subscriber the subscriber which should be subscribed to the Clock.
     * @throws ClockException if the subscriber is already subscribed to the Clock.
     */
    public void subscribe(ClockSubscriber subscriber) {
        if (subscribers.stream().anyMatch(s -> s == subscriber)) {
            throw ClockException.subscriberIsAlreadyExist();
        }

        subscribers.add(subscriber);
    }

    /**
     * Method to unsubscribe the subscriber from the Clock.
     * @param subscriber the subscriber which should be unsubscribed from the Clock.
     * @throws ClockException if the subscriber is not unsubscribed from the Clock.
     */
    public void unsubscribe(ClockSubscriber subscriber) {
        if (!subscribers.remove(subscriber)) {
            throw ClockException.subscriberNotRemoved();
        }
    }

    private void notifySubscribers() {
        subscribers.forEach(s -> s.update(dateTimeNow));
    }

    /**
     * Method to one add day to the date of Clock.
     */
    public void addDay() {
        dateTimeNow = dateTimeNow.plusDays(1);
        notifySubscribers();
    }

    /**
     * Method to add one month to the date of Clock.
     */
    public void addMonth() {
        LocalDateTime newDateTime = dateTimeNow.plusMonths(1);

        while (!dateTimeNow.equals(newDateTime)) {
            addDay();
        }
    }

    /**
     * Method to add one year to the date of Clock.
     */
    public void addYear() {
        LocalDateTime newDateTime = dateTimeNow.plusYears(1);

        while (!dateTimeNow.equals(newDateTime)) {
            addDay();
        }
    }
}
