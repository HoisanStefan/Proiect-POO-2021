package handler;

import mappingclass.*;
import factory.Factory;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public final class Handler implements Observer {
    private final int numberOfTurns;
    private final InitialData initialData;
    private final List<UpdateEntry> monthlyUpdates;
    private List<Consumer> consumers;
    private List<Distributor> distributors;
    private List<Producer> producers;

    public Handler(final MappingClass mc) {
        this.initialData = mc.getInitialData();
        this.monthlyUpdates = mc.getMonthlyUpdates();
        this.numberOfTurns = mc.getNumberOfTurns();
        this.consumers = new ArrayList<>();
        this.distributors = new ArrayList<>();
        this.producers = new ArrayList<>();
    }

    /**
     * Creating the initial consumers and distributors from
     * initialData
     */
    public void createEntities() {
        for (int i = 0; i < initialData.getConsumers().size(); ++i) {
            this.consumers.add(new Consumer(initialData.getConsumers().get(i)));
        }
        for (int i = 0; i < initialData.getDistributors().size(); ++i) {
            this.distributors.add(new Distributor(initialData.getDistributors().get(i)));
        }
        for (int i = 0; i < initialData.getProducers().size(); ++i) {
            this.producers.add(new Producer(initialData.getProducers().get(i)));
        }
    }

    /**
     * Computing the result
     * Using the Singleton pattern we create the instance
     * for Factory
     * Using the Factory pattern we create the entities
     * we need to modulate
     * @return A mapping class containing the output data
     */
    public MappingClassOutput getResult() {
        createEntities();

        SubjectObservable observable = new SubjectObservable(this.producers);
        observable.addObserver(this);

        Factory f = Factory.getInstance();

        Billing consumer = f.computeBill("Consumer");
        Billing distributor = f.computeBill("Distributor");

        assert consumer != null;
        assert distributor != null;
        //assert producers != null;

        Distributor.getProducersForDistributors(this.distributors, this.producers);
        applyMethods(consumer, distributor);

        for (int i = 0; i < this.numberOfTurns; ++i) {
            /*
            We update the consumers and distributors with the
            monthly update
             */

            if (this.monthlyUpdates.get(i) != null) {
                if (this.monthlyUpdates.get(i).getNewConsumers() != null) {
                    for (int j = 0; j < this.monthlyUpdates.get(i).getNewConsumers().size(); ++j) {
                        Consumer c =
                                new Consumer(this.monthlyUpdates.get(i).getNewConsumers().get(j));
                        this.consumers.add(c);
                    }
                }
                if (this.monthlyUpdates.get(i).getDistributorChanges() != null) {
                    List<DistributorChange> changes =
                            this.monthlyUpdates.get(i).getDistributorChanges();
                    for (DistributorChange change : changes) {
                        for (Distributor value : this.distributors) {
                            if (value.getId() == change.getId()) {
                                value.setInfrastructureCost(change.getInfrastructureCost());
                            }
                        }
                    }
                }

            }

            applyMethods(consumer, distributor);

            if (this.monthlyUpdates.get(i) != null) {
                /* TODO: Update distributors and producers */
                if (this.monthlyUpdates.get(i).getProducerChanges() != null) {
                    List<Integer> emptyList = new ArrayList<>();
                    observable.setProducersChangedLastMonth(emptyList);
                    List<ProducerChange> changes = this.monthlyUpdates.get(i).getProducerChanges();
                    observable.changeProducerList(changes);
                }
            }


//            if (i == 1) {
//                System.out.println(this.producers);
//            }
            Distributor.updateDistributors
                    (this.distributors, this.producers, observable.getProducersChangedLastMonth());
            observable.populateMonth(i + 1, this.producers);
        }

        MappingClassOutput mco = new MappingClassOutput();
        mco.setConsumers(this.consumers);
        mco.setDistributors(this.distributors);
        mco.setEnergyProducers(this.producers);

        return mco;
    }

    /**
     * Distributors compute the new cost for the contracts
     * Consumers choose the contract
     * Consumers receive the salary and pay the contract/debt
     * Distributors pay their expenses
     *
     * @param consumer - entity for consumers generated with Factory Pattern
     * @param distributor - entity for distributors generated with Factory Pattern
     */
    public void applyMethods(final Billing consumer,
                             final Billing distributor) {
        distributor.establishContract(this.consumers, this.distributors);
        setDistributors(Distributor.getNewDistributors());

        consumer.establishContract(this.consumers, this.distributors);
        setConsumers(Consumer.getConsumerResultRound());

        consumer.getStatus(this.consumers, this.distributors);
        setConsumers(Consumer.getConsumerResultRound());

        distributor.getStatus(this.consumers, this.distributors);
        setDistributors(Distributor.getNewDistributors());
    }

    public List<Consumer> getConsumers() {
        return consumers;
    }

    public void setConsumers(final List<Consumer> consumers) {
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
    public void update(final Observable o, final Object p) {
        this.setProducers((List<Producer>) p);
    }
}
