package mappingclass;

import java.util.List;

public final class InitialData {
    private List<NewConsumer> consumers;
    private List<Distributor> distributors;
    private List<Producer> producers;

    public List<NewConsumer> getConsumers() {
        return consumers;
    }

    public void setConsumers(final List<NewConsumer> consumers) {
        this.consumers = consumers;
    }

    public List<Distributor> getDistributors() {
        return distributors;
    }

    public void setDistributors(final List<Distributor> distributors) {
        this.distributors = distributors;
    }

    public List<Producer> getProducers() {
        return producers;
    }

    public void setProducers(final List<Producer> producers) {
        this.producers = producers;
    }

    @Override
    public String toString() {
        return "InitialData{"
                + "consumers=" + consumers
                + ", distributors=" + distributors
                + ", producers=" + producers
                + '}';
    }
}
