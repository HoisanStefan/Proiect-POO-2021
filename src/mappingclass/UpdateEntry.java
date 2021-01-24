package mappingclass;

import java.util.List;

public final class UpdateEntry {
    private List<NewConsumer> newConsumers;
    private List<DistributorChange> distributorChanges;
    private List<ProducerChange> producerChanges;

    public List<NewConsumer> getNewConsumers() {
        return newConsumers;
    }

    public void setNewConsumers(final List<NewConsumer> newConsumers) {
        this.newConsumers = newConsumers;
    }

    public List<DistributorChange> getDistributorChanges() {
        return distributorChanges;
    }

    public void setDistributorChanges(final List<DistributorChange> distributorChanges) {
        this.distributorChanges = distributorChanges;
    }

    public List<ProducerChange> getProducerChanges() {
        return producerChanges;
    }

    public void setProducerChanges(final List<ProducerChange> producerChanges) {
        this.producerChanges = producerChanges;
    }

    @Override
    public String toString() {
        return "UpdateEntry{"
                + "newConsumers=" + newConsumers
                + ", distributorChanges=" + distributorChanges
                + ", producerChanges=" + producerChanges
                + '}';
    }
}
