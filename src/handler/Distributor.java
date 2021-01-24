package handler;

import factory.StrategyFactory;
import mappingclass.Contract;
import strategies.Strategy;
import strategies.EnergyChoiceStrategyType;

import java.util.ArrayList;
import java.util.List;

public final class Distributor implements Billing {
    private int id;
    private int contractLength;
    private int budget;
    private int infrastructureCost;
    private int productionCost;
    private int neededKW;
    private boolean isBankrupt;
    private EnergyChoiceStrategyType strategyType;
    private List<Contract> contracts;
    private List<ProducerQuantity> producersAndQuantities;
    private int contractCost;
    private static List<Distributor> newDistributors;

    public Distributor() {
        this.isBankrupt = false;
    }

    public Distributor(final mappingclass.Distributor distributor) {
        this.contractLength = distributor.getContractLength();
        this.id = distributor.getId();
        this.budget = distributor.getInitialBudget();
        this.infrastructureCost = distributor.getInitialInfrastructureCost();
        this.productionCost = 0;
        this.contracts = new ArrayList<>();
        this.isBankrupt = false;
        this.contractCost = 0;
        this.neededKW = distributor.getEnergyNeededKW();
        this.strategyType = distributor.getProducerStrategy();
    }

    /**
     * Paying distributors' expenses and then deleting
     * bankrupt consumers (we provided them energy already)
     * @param consumers current state of consumers
     * @param distributors current state of distributors
     */
    @Override
    public void getStatus(final List<Consumer> consumers,
                          final List<Distributor> distributors) {
        newDistributors = distributors;

        for (int i = 0; i < newDistributors.size(); ++i) {
            if (!distributors.get(i).isBankrupt) {
                int infrastructure = newDistributors.get(i).getInfrastructureCost();
                int production = newDistributors.get(i).getProductionCost();
                int totalCost;
                int currentBudget = newDistributors.get(i).getBudget();
                if (newDistributors.get(i).getContracts().size() == 0) {
                    totalCost = infrastructure;
                } else {
                    totalCost = infrastructure
                            + production * newDistributors.get(i).getContracts().size();
                }
                if (currentBudget >= totalCost) {
                    currentBudget -= totalCost;
                    newDistributors.get(i).setBudget(currentBudget);
                } else {
                    currentBudget -= totalCost;
                    newDistributors.get(i).setBankrupt(true);
                    newDistributors.get(i).setBudget(currentBudget);
                }
            }
        }

        for (Distributor newDistributor : newDistributors) {
            List<Contract> newContracts = newDistributor.getContracts();
            for (Consumer consumer : consumers) {
                if (consumer.isBankrupt()) {
                    for (int k = 0; k < newContracts.size(); ++k) {
                        if (newContracts.get(k).getConsumerId() == consumer.getId()) {
                            newContracts.remove(newContracts.get(k));
                            newDistributor.setContracts(newContracts);
                        }
                    }
                }
            }
        }
    }

    /**
     * Computing contracts' cost
     * @param consumers current state of consumers
     * @param distributors current state of distributors
     */
    @Override
    public void establishContract(final List<Consumer> consumers,
                                  final List<Distributor> distributors) {
        newDistributors = distributors;

        for (Distributor newDistributor : newDistributors) {
            double cost = 0;
            List<ProducerQuantity> pq = newDistributor.getProducersAndQuantities();
            for (var producer : pq) {
                cost += producer.getQuantity() * producer.getProducer().getPriceKW();
            }
            newDistributor.setProductionCost((int)Math.round(Math.floor(cost / 10)));
        }

        for (Distributor newDistributor : newDistributors) {
            final double factor = 0.2;
            if (newDistributor.getContracts().size() == 0) {
                newDistributor.setContractCost(newDistributor.getInfrastructureCost()
                        + newDistributor.getProductionCost()
                        + (int)
                        Math.round(Math.floor(factor * newDistributor.getProductionCost())));
            } else {
                double temp = Math.floor((double) newDistributor.getInfrastructureCost()
                        / newDistributor.getContracts().size());
                newDistributor.setContractCost((int) Math.round(temp
                        + newDistributor.getProductionCost()
                        + (int)
                        Math.round(Math.floor(factor * newDistributor.getProductionCost()))));
            }
        }
    }

    public static void getProducersForDistributors(final List<Distributor> distributors,
                                            final List<Producer> producers) {
        StrategyFactory sf = new StrategyFactory();

        for (int i = 0; i < distributors.size(); ++i) {
            Distributor d = distributors.get(i);
            List<ProducerQuantity> producersAdded;
            Strategy strategy = sf.createStrategy(d.getStrategyType(), producers);
            if (!d.isBankrupt()) {
                producersAdded = strategy.doStrategy(d, producers);
                d.setProducersAndQuantities(producersAdded);
                for (ProducerQuantity pq : producersAdded) {
                    Producer p = pq.getProducer();
                    for (Producer original : producers) {
                        if (original.getId() == p.getId()) {
                            List<Distributor> oldList = original.getDistributors();
                            oldList.add(d);
                            original.setDistributors(oldList);
                        }
                    }
                }
            }
        }
    }

    public static void deleteThisDistributorFromProducers(final Distributor d,
                                                   final List<Producer> producers) {
        for (Producer p : producers) {
            List<Distributor> subscribers = p.getDistributors();
            subscribers.remove(d);
            p.setDistributors(subscribers);
        }
    }

    public static void updateDistributors(final List<Distributor> distributors,
                                          final List<Producer> producers,
                                          final List<Integer> changedProducers) {
        StrategyFactory sf = new StrategyFactory();

        for (Distributor d : distributors) {
            List<ProducerQuantity> subscribed = d.getProducersAndQuantities();
            if (d.isBankrupt()) {
                Distributor.deleteThisDistributorFromProducers(d, producers);
            } else {
                boolean found = false;
                for (ProducerQuantity pq : subscribed) {
                    if (changedProducers.contains(pq.getProducer().getId())) {
                        found = true;
                        break;
                    }
                }
                if (found) {
                    deleteThisDistributorFromProducers(d, producers);
                    Strategy strategy = sf.createStrategy(d.getStrategyType(), producers);
                    List<ProducerQuantity> producersAdded;
                    producersAdded = strategy.doStrategy(d, producers);
                    d.setProducersAndQuantities(producersAdded);
                    for (ProducerQuantity pq : producersAdded) {
                        Producer p = pq.getProducer();
                        for (Producer original : producers) {
                            if (original.getId() == p.getId()) {
                                List<Distributor> oldList = original.getDistributors();
                                oldList.add(d);
                                original.setDistributors(oldList);
                            }
                        }
                    }
                }
            }
        }
    }

    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public int getContractLength() {
        return contractLength;
    }

    public void setContractLength(final int contractLength) {
        this.contractLength = contractLength;
    }

    public int getBudget() {
        return budget;
    }

    public void setBudget(final int budget) {
        this.budget = budget;
    }

    public int getInfrastructureCost() {
        return infrastructureCost;
    }

    public void setInfrastructureCost(final int infrastructureCost) {
        this.infrastructureCost = infrastructureCost;
    }

    public int getProductionCost() {
        return productionCost;
    }

    public int getNeededKW() {
        return neededKW;
    }

    public void setNeededKW(final int neededKW) {
        this.neededKW = neededKW;
    }

    public void setProductionCost(final int productionCost) {
        this.productionCost = productionCost;
    }

    public EnergyChoiceStrategyType getStrategyType() {
        return strategyType;
    }

    public void setStrategyType(final EnergyChoiceStrategyType strategyType) {
        this.strategyType = strategyType;
    }

    public List<ProducerQuantity> getProducersAndQuantities() {
        return producersAndQuantities;
    }

    public void setProducersAndQuantities(final List<ProducerQuantity> producersAndQuantities) {
        this.producersAndQuantities = producersAndQuantities;
    }

    public boolean isBankrupt() {
        return isBankrupt;
    }

    public void setBankrupt(final boolean bankrupt) {
        isBankrupt = bankrupt;
    }

    public List<Contract> getContracts() {
        return contracts;
    }

    public void setContracts(final List<Contract> contracts) {
        this.contracts = contracts;
    }

    public static List<Distributor> getNewDistributors() {
        return newDistributors;
    }

    public static void setNewDistributors(final List<Distributor> distributors) {
        newDistributors = distributors;
    }

    public int getContractCost() {
        return contractCost;
    }

    public void setContractCost(final int contractCost) {
        this.contractCost = contractCost;
    }
}
