package handler;

import mappingclass.MonthlyStat;
import mappingclass.ProducerChange;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

public final class SubjectObservable extends Observable {
    private final List<Producer> producerList;
    private List<Integer> producersChangedLastMonth;

    public SubjectObservable(List<Producer> producerList) {
        this.producerList = producerList;
    }

    /**
     * Given a list of changes for producers, we iterate through
     * all producers and change their state. Then we notify
     * the observers.
     * @param changes List of changes for the current month
     */
    public void changeProducerList(List<ProducerChange> changes) {
        for (ProducerChange change : changes) {
            for (Producer producer : this.producerList) {
                if (producer.getId() == change.getId()) {
                    producer.setEnergyPerDistributor(change.getEnergyPerDistributor());
                    this.producersChangedLastMonth.add(producer.getId());
                }
            }
        }

        setChanged();
        notifyObservers(producerList);
    }

    public List<Integer> getProducersChangedLastMonth() {
        return producersChangedLastMonth;
    }

    public void setProducersChangedLastMonth(final List<Integer> producersChangedLastMonth) {
        this.producersChangedLastMonth = producersChangedLastMonth;
    }

    /**
     * At the end of the month, we compute the monthly stats for each
     * producer
     * @param month The month for which we need to compute
     * @param producers current producers state
     */
    public void populateMonth(int month, List<Producer> producers) {
        for (Producer p : producers) {
            List<Distributor> distributors = p.getDistributors();
            MonthlyStat monthlyStat = new MonthlyStat();
            List<Integer> distributorsIds = new ArrayList<>();
            for (Distributor d : distributors) {
                distributorsIds.add(d.getId());
            }
            monthlyStat.setMonth(month);
            monthlyStat.setDistributorsIds(distributorsIds);
            List<MonthlyStat> oldList = p.getMonthlyStats();
            oldList.add(monthlyStat);
            p.setMonthlyStats(oldList);
        }
    }
}
