package mappingclass;

import handler.Consumer;

import java.util.ArrayList;
import java.util.List;

public final class MappingClassOutput {
    private List<ConsumerOutput> consumers;
    private List<DistributorOutput> distributors;
    private List<ProducerOutput> energyProducers;

    public MappingClassOutput() {
        this.consumers = new ArrayList<ConsumerOutput>();
        this.distributors = new ArrayList<DistributorOutput>();
        this.energyProducers = new ArrayList<ProducerOutput>();
    }

    public List<ConsumerOutput> getConsumers() {
        return consumers;
    }

    /**
     * Extracting the necessary data for output
     * @param consumers current state of consumers
     */
    public void setConsumers(final List<Consumer> consumers) {
        List<ConsumerOutput> c = new ArrayList<>();
        for (Consumer consumer : consumers) {
            ConsumerOutput co = new ConsumerOutput();
            co.setBudget(consumer.getBudget());
            co.setBankrupt(consumer.isBankrupt());
            co.setId(consumer.getId());
            c.add(co);
        }
        this.consumers = c;
    }

    public List<DistributorOutput> getDistributors() {
        return distributors;
    }

    /**
     * Extracting the necessary data for output
     * @param distributors current state of distributors
     */
    public void setDistributors(final List<handler.Distributor> distributors) {
        List<DistributorOutput> d = new ArrayList<>();
        for (handler.Distributor distributor : distributors) {
            DistributorOutput disOut = new DistributorOutput();
            disOut.setBankrupt(distributor.isBankrupt());
            disOut.setBudget(distributor.getBudget());
            disOut.setEnergyNeededKW(distributor.getNeededKW());
            disOut.setProducerStrategy(distributor.getStrategyType());
            disOut.setContractCost(distributor.getContractCost());
            disOut.setId(distributor.getId());
            disOut.setContracts(distributor.getContracts());
            d.add(disOut);
        }
        this.distributors = d;
    }

    public List<ProducerOutput> getEnergyProducers() {
        return energyProducers;
    }

    public void setEnergyProducers(final List<handler.Producer> energyProducers) {
        // TODO
        List<ProducerOutput> p = new ArrayList<>();
        for (handler.Producer producer : energyProducers) {
            ProducerOutput proOut = new ProducerOutput();
            proOut.setId(producer.getId());
            proOut.setEnergyPerDistributor(producer.getEnergyPerDistributor());
            proOut.setEnergyType(producer.getEnergyType());
            proOut.setMaxDistributors(producer.getMaxDistributors());
            proOut.setPriceKW(producer.getPriceKW());
            proOut.setMonthlyStats(producer.getMonthlyStats());
            p.add(proOut);
        }
        this.energyProducers = p;
    }

    @Override
    public String toString() {
        return "MappingClassOutput{"
                + "consumers=" + consumers
                + ", distributors=" + distributors
                + ", energyProducers=" + energyProducers
                + '}';
    }
}
