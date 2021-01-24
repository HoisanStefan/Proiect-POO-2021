package strategies;

import handler.Distributor;
import handler.Producer;
import handler.ProducerQuantity;

import java.util.List;

public interface Strategy {
    /**
     * Method implemented by each strategy that behaves accordingly
     * @param distributor current distributors state
     * @param producers current producers state
     * @return List that stores both the producers chosen by the
     * distributor and the quantity from each producer
     */
    List<ProducerQuantity> doStrategy(Distributor distributor,
                                      List<Producer> producers);
}
