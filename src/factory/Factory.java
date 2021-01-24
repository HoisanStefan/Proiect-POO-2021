package factory;

import handler.Billing;
import handler.Consumer;
import handler.Distributor;

public final class Factory {
    private static Factory instance = null;

    private Factory() { }

    /**
     * Singleton Pattern
     * @return object for the singleton class
     */
    public static Factory getInstance() {
        if (instance == null) {
            instance = new Factory();
        }
        return instance;
    }

    /**
     * Creating the entities needed with Factory Pattern
     * @param type - which entity we need to create
     * @return the entity used for modulation
     */
    public Billing computeBill(final String type) {
        if (type.equals("Consumer")) {
            return new Consumer();
        } else if (type.equals("Distributor")) {
            return new Distributor();
        }
        return null;
    }
}
