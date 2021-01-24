package strategies;

import handler.Distributor;
import handler.Producer;
import handler.ProducerQuantity;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class PriceStrategy implements Strategy {
    private List<Producer> producers;

    public PriceStrategy() {
    }

    public PriceStrategy(final List<Producer> producers) {
        this.producers = producers;
    }


    @Override
    public List<ProducerQuantity> doStrategy(final Distributor distributor, final List<Producer> producers) {
        int dQuantity = distributor.getNeededKW();

        SortedSet<Producer> sortedPrice = new TreeSet<>((o1, o2) -> {
            double cmp = Double.compare(o1.getPriceKW(), o2.getPriceKW());
            if (cmp == 0) {
                cmp = Integer.compare(o2.getEnergyPerDistributor(), o1.getEnergyPerDistributor());
            }
            return (int) cmp;
        });
        sortedPrice.addAll(producers);
        List<Producer> sorted = new ArrayList<>(sortedPrice);
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