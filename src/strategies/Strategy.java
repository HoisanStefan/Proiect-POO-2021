package strategies;

import handler.Distributor;
import handler.Producer;
import handler.ProducerQuantity;

import java.util.List;

public interface Strategy {
    List<ProducerQuantity> doStrategy(final Distributor distributor, final List<Producer> producers);
}
