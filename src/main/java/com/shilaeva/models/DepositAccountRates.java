package com.shilaeva.models;

import com.shilaeva.exceptions.BankException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import lombok.NonNull;

/**
 * Class describing Collection of DepositAccountRates.
 *
 * @version 2.0
 * @author Nadezhda Shilaeva
 */
public class DepositAccountRates {
    private final Collection<DepositAccountRate> rates;

    private DepositAccountRates(Collection<DepositAccountRate> rates) {
        this.rates = rates;
    }

    /**
     * Method to get unmodifiable collection of rates.
     * @return unmodifiable collection of rates.
     */
    public Collection<DepositAccountRate> getRates() {
        return Collections.unmodifiableCollection(rates);
    }

    /**
     * Static method to get builder of the class DepositAccountRates.
     * @return the new instance of class DepositAccountRatesBuilder.
     */
    public static DepositAccountRatesBuilder builder() {
        return new DepositAccountRatesBuilder();
    }

    private static boolean isRateRelevant(@NonNull Collection<DepositAccountRate> rates, DepositAccountRate otherRate) {
        if (rates.stream().reduce(null, (x, y) -> y) == null) {
            return true;
        }

        return rates.stream().reduce((x, y) -> y).get().getMinMoneyCount()
                .compareTo(otherRate.getMinMoneyCount()) < 0;
    }

    /**
     * Static class to build class DepositAccountRates.
     */
    public static class DepositAccountRatesBuilder {
        private final Collection<DepositAccountRate> rates;

        public DepositAccountRatesBuilder() {
            rates = new ArrayList<DepositAccountRate>();
        }

        /**
         * Method to add rate to the collection of rates.
         * @param rate new rate, it cannot be less than the previous rate.
         * @return modified class DepositAccountRatesBuilder.
         * @throws BankException if new rate less than the previous rate.
         */
        public DepositAccountRatesBuilder addRate(@NonNull DepositAccountRate rate) {
            if (!isRateRelevant(rates, rate)) {
                throw BankException.invalidRate(rate.getMinMoneyCount());
            }

            rates.add(rate);

            return this;
        }

        /**
         * Method to finish the building of the class DepositAccountRates.
         * @return new instance of the class DepositAccountRates.
         */
        public DepositAccountRates build() {
            return new DepositAccountRates(rates);
        }
    }
}
