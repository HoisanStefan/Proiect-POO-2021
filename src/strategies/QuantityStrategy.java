package strategies;

import handler.Distributor;
import handler.Producer;
import handler.ProducerQuantity;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

public final class QuantityStrategy implements Strategy {
    public QuantityStrategy() { }

    @Override
    public List<ProducerQuantity> doStrategy(final Distributor distributor,
                                             final List<Producer> producers) {
        int dQuantity = distributor.getNeededKW();

        SortedSet<Producer> sortedQuantity = new TreeSet<>((o1, o2) -> {
            double cmp;
                cmp = Integer.compare(o2.getEnergyPerDistributor(), o1.getEnergyPerDistributor());
                if (cmp == 0) {
                    cmp = Double.compare(o1.getPriceKW(), o2.getPriceKW());
                    if (cmp == 0) {
                        cmp = Integer.compare(o1.getId(), o2.getId());
                    }
                }

            return (int) cmp;
        });
        sortedQuantity.addAll(producers);
        List<Producer> sorted = new ArrayList<>(sortedQuantity);
        List<ProducerQuantity> finalList = new ArrayList<>();

        int currentQuantity = 0;
        if (!sorted.isEmpty()) {
            while (currentQuantity < dQuantity && !sorted.isEmpty()) {
                if (sorted.get(0).getDistributors().size() >= sorted.get(0).getMaxDistributors()) {
                    sorted.remove(0);
                }
                currentQuantity += sorted.get(0).getEnergyPerDistributor();
                ProducerQuantity pq = new ProducerQuantity();
                pq.setQuantity(sorted.get(0).getEnergyPerDistributor());
                pq.setProducer(sorted.get(0));
                finalList.add(pq);
                sorted.remove(0);
            }
        }

        return finalList;
    }
}
