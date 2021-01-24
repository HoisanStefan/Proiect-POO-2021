package handler;

import mappingclass.MonthlyStat;
import mappingclass.ProducerChange;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Observable;

public class SubjectObservable extends Observable {
    private List<Producer> producerList;
    private List<MonthlyStat> monthlyStats;
    private List<Integer> producersChangedLastMonth;
    public SubjectObservable() { }

    public SubjectObservable(List<Producer> producerList) {
        this.producerList = producerList;
    }

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

    public List<Producer> getProducerList() {
        return producerList;
    }

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
