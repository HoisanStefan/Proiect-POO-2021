package strategies;

import handler.Distributor;
import handler.Producer;
import handler.ProducerQuantity;

import java.util.*;
import java.util.stream.Collectors;

public class GreenStrategy implements Strategy {
    private List<Producer> producers;

    public GreenStrategy() {
    }

    public GreenStrategy(final List<Producer> producers) {
        this.producers = producers;
    }

    @Override
    public List<ProducerQuantity> doStrategy(final Distributor distributor, final List<Producer> producers) {
        int dQuantity = distributor.getNeededKW();
//        List<Producer> filteredGreen = producers
//                .stream()
//                .filter(p -> p.getEnergyType().isRenewable())
//                .collect(Collectors.toList());
        SortedSet<Producer> sortedGreen = new TreeSet<>((o1, o2) -> {
            double cmp = Boolean.compare(o2.getEnergyType().isRenewable(), o1.getEnergyType().isRenewable());

            if (cmp == 0) {
                cmp = Double.compare(o1.getPriceKW(), o2.getPriceKW());
                if (cmp == 0) {
                    cmp = Integer.compare(o2.getEnergyPerDistributor(), o1.getEnergyPerDistributor());
                }
            }

            return (int) cmp;
        });
        sortedGreen.addAll(producers);
        List<Producer> sorted = new ArrayList<>(sortedGreen);
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