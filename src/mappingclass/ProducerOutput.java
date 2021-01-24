package mappingclass;

import entities.EnergyType;

import java.util.List;

public final class ProducerOutput {
    private int id;
    private int maxDistributors;
    private double priceKW;
    private EnergyType energyType;
    private int energyPerDistributor;
    private List<MonthlyStat> monthlyStats;

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

    public double getPriceKW() {
        return priceKW;
    }

    public void setPriceKW(final double priceKW) {
        this.priceKW = priceKW;
    }

    public EnergyType getEnergyType() {
        return energyType;
    }

    public void setEnergyType(final EnergyType energyType) {
        this.energyType = energyType;
    }

    public int getEnergyPerDistributor() {
        return energyPerDistributor;
    }

    public void setEnergyPerDistributor(final int energyPerDistributor) {
        this.energyPerDistributor = energyPerDistributor;
    }

    public List<MonthlyStat> getMonthlyStats() {
        return monthlyStats;
    }

    public void setMonthlyStats(final List<MonthlyStat> monthlyStats) {
        this.monthlyStats = monthlyStats;
    }
}
