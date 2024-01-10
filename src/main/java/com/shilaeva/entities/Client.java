package com.shilaeva.entities;

import com.shilaeva.exceptions.ClientException;
import com.shilaeva.interfaces.Notifier;
import com.shilaeva.models.PassportData;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

/**
 * Class describing object Client.
 *
 * @version 2.0
 * @author Nadezhda Shilaeva
 */
@Getter
public class Client {
    private final UUID id;
    @Setter
    private String name;
    @Setter
    private String surname;
    @Setter
    private String address;
    @Setter
    private PassportData passport;
    @Setter
    private boolean notifiable = false;
    @Setter
    private Notifier notifier;

    private Client(UUID id, String name, String surname, String address, PassportData passport)
    {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.address = address;
        this.passport = passport;
    }

    /**
     * Method to get notification to the Client.
     * @param message the message of the getting notification.
     */
    public void getNotification(String message) {
        if (notifiable && notifier != null) {
            notifier.sendNotification(message);
        }
    }

    /**
     * Static method to get builder of the class Client.
     * @return the new instance of class ClientBuilder.
     */
    public static ClientBuilder builder() {
        return new ClientBuilder();
    }

    /**
     * Static class to build class Client.
     */
    public static class ClientBuilder {
        private final UUID id;
        private String name;
        private String surname;
        private String address;
        private PassportData passport;

        public ClientBuilder() {
            id = UUID.randomUUID();
            name = null;
            surname = null;
            address = null;
            passport = null;
        }

        /**
         * Method to set name of the Client.
         * @param name the name of the Client.
         * @return modified class ClientBuilder.
         * @throws ClientException if the name is invalid.
         */
        public ClientBuilder setName(String name) {
            if (name.isEmpty()) {
                throw ClientException.invalidName(name);
            }

            this.name = name;

            return this;
        }

        /**
         * Method to set surname of the Client.
         * @param surname the surname of the Client.
         * @return modified class ClientBuilder.
         * @throws ClientException if the surname is invalid.
         */
        public ClientBuilder setSurname(String surname) {
            if (surname.isEmpty()) {
                throw ClientException.invalidSurname(surname);
            }

            this.surname = surname;

            return this;
        }

        /**
         * Method to set address of the Client.
         * @param address the address of the Client.
         * @return modified class ClientBuilder.
         * @throws ClientException if the address is invalid.
         */
        public ClientBuilder setAddress(String address) {
            if (address.isEmpty()) {
                throw ClientException.invalidAddress(address);
            }

            this.address = address;

            return this;
        }

        /**
         * Method to set passport number of the Client.
         * @param number the passport number of the Client.
         * @return modified class ClientBuilder.
         */
        public ClientBuilder setPassport(int number) {
            passport = new PassportData(number);

            return this;
        }

        /**
         * Method to finish the building of the class Client.
         * @return new instance of the class Client.
         * @throws ClientException if name or surname of the Client is missing.
         */
        public Client build() {
            if (name == null || surname == null) {
                throw ClientException.noRequiredData(id);
            }

            return new Client(id, name, surname, address, passport);
        }
    }
}
