package handler;

import entities.EnergyType;
import mappingclass.MonthlyStat;

import java.util.ArrayList;
import java.util.List;

public class Producer {
    private int id;
    private int maxDistributors;
    private int energyPerDistributor;
    private double priceKW;
    private EnergyType energyType;
    private List<Distributor> distributors;
    private List<MonthlyStat> monthlyStats;
    private static List<Producer> newProducers;

    public Producer() { }

    public Producer(final mappingclass.Producer producer) {
        this.id = producer.getId();
        this.maxDistributors = producer.getMaxDistributors();
        this.energyPerDistributor = producer.getEnergyPerDistributor();
        this.priceKW = producer.getPriceKW();
        this.energyType = producer.getEnergyType();
        this.distributors = new ArrayList<>();
        this.monthlyStats = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public int getMaxDistributors() {
        return maxDistributors;
    }

    public void setMaxDistributors(final int maxDistributors) {
        this.maxDistributors = maxDistributors;
    }

    public int getEnergyPerDistributor() {
        return energyPerDistributor;
    }

    public void setEnergyPerDistributor(final int energyPerDistributor) {
        this.energyPerDistributor = energyPerDistributor;
    }

    public double getPriceKW() {
        return priceKW;
    }

    public void setPriceKW(final double priceKW) {
        this.priceKW = priceKW;
    }

    public List<Distributor> getDistributors() {
        return distributors;
    }

    public void setDistributors(final List<Distributor> distributors) {
        this.distributors = distributors;
    }

    public EnergyType getEnergyType() {
        return energyType;
    }

    public void setEnergyType(final EnergyType energyType) {
        this.energyType = energyType;
    }

    public List<MonthlyStat> getMonthlyStats() {
        return monthlyStats;
    }

    public void setMonthlyStats(final List<MonthlyStat> monthlyStats) {
        this.monthlyStats = monthlyStats;
    }

    @Override
    public String toString() {
        return "Producer{" +
                "id=" + id +
                ", maxDistributors=" + maxDistributors +
                ", energyPerDistributor=" + energyPerDistributor +
                ", priceKW=" + priceKW +
                ", energyType=" + energyType +
                ", distributors=" + distributors +
                ", monthlyStats=" + monthlyStats +
                '}';
    }
}
