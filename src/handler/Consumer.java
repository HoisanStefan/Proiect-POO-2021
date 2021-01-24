package handler;

import mappingclass.Contract;
import mappingclass.NewConsumer;
import java.util.ArrayList;
import java.util.List;

public final class Consumer implements Billing {
    private int id;
    private int budget;
    private int monthlyIncome;
    private boolean isBankrupt;
    private int contractLength;
    private int contractCost;
    private Debt debtObj;
    private Distributor myDistributor;
    private static List<Consumer> newConsumers;

    public Consumer() { }

    public Consumer(final NewConsumer newConsumer) {
        this.id = newConsumer.getId();
        this.budget = newConsumer.getInitialBudget();
        this.monthlyIncome = newConsumer.getMonthlyIncome();
        this.isBankrupt = false;
        this.contractLength = 0;
        this.contractCost = 0;
        this.debtObj = new Debt();
        this.myDistributor = new Distributor();
    }

    /**
     * Adding the money to the distributors' budgets
     * @param paidMoneyDebt money paid for consumer's debt
     * @param paidMoney money paid for the current invoice
     * @param consumer the consumer for which the change is made
     * @param distributors state of distributors
     */
    public void addMoneyToDistributor(final int paidMoneyDebt,
                                      final int paidMoney,
                                      final Consumer consumer,
                                      final List<Distributor> distributors) {
        Distributor d = consumer.getMyDistributor();
        if (paidMoneyDebt == 0) {
            for (Distributor distributor : distributors) {
                updateContractsInner(paidMoney, consumer, d, distributor);
            }
        } else {
            Distributor d1 = consumer.getDebtObj().getDistributor();
            for (Distributor distributor : distributors) {
                updateContractsInner(paidMoney, consumer, d, distributor);
                if (distributor.getId() == d1.getId()) {
                    distributor.setBudget(distributor.getBudget() + paidMoneyDebt);
                }
            }
        }

    }

    /**
     * Avoiding duplicated code for updating distributors' contracts
     * @param paidMoney money paid for the current invoice
     * @param consumer the consumer for which the change is made
     * @param d consumer's distributor
     * @param distributor state of distributors
     */
    public void updateContractsInner(final int paidMoney,
                                     final Consumer consumer,
                                     final Distributor d,
                                     final Distributor distributor) {
        if (distributor.getId() == d.getId()) {
            distributor.setBudget(distributor.getBudget() + paidMoney);
            List<Contract> contracts = distributor.getContracts();
            for (int j = 0; j < contracts.size(); ++j) {
                if (contracts.get(j).getConsumerId() == consumer.getId()) {
                    contracts.get(j).setRemainedContractMonths(consumer.getContractLength());
                    distributor.setContracts(contracts);
                }
            }
        }
    }

    /**
     * The paying process, consumers get their salaries
     * and then they try to pay their distributor,
     * they can choose to be indebted if they lack
     * the required money
     * Only non-bankrupt consumers are taken in consideration
     * @param consumers current state of consumers
     * @param distributors current state of distributors
     */
    @Override
    public void getStatus(final List<Consumer> consumers,
                          final List<Distributor> distributors) {
        newConsumers = consumers;

        for (Consumer newConsumer : newConsumers) {
            if (!newConsumer.isBankrupt()) {
                newConsumer.setBudget(newConsumer.getBudget()
                        + newConsumer.getMonthlyIncome());
            }
        }

        for (Consumer newConsumer : newConsumers) {
            if (!newConsumer.isBankrupt()) {
                int currentBudget = newConsumer.getBudget();
                if (newConsumer.getDebtObj().getDebt() != 0) {
                    int paidMoneyDebt = newConsumer.getDebtObj().getDebt();
                    int paidMoney = newConsumer.getContractCost();
                    int totalCost = paidMoneyDebt + paidMoney;
                    if (newConsumer.getBudget() >= totalCost) {
                        newConsumer.setBudget(newConsumer.getBudget() - totalCost);
                        newConsumer.setContractLength(newConsumer.getContractLength() - 1);
                        addMoneyToDistributor(paidMoneyDebt, paidMoney, newConsumer, distributors);
                        Distributor.setNewDistributors(distributors);
                        newConsumer.setDebtObj(new Debt());
                    } else {
                        newConsumer.setBankrupt(true);
                    }
                } else if (currentBudget >= newConsumer.getContractCost()) {
                    currentBudget -= newConsumer.getContractCost();
                    newConsumer.setBudget(currentBudget);
                    newConsumer.setContractLength(newConsumer.getContractLength() - 1);
                    int paidMoney = newConsumer.getContractCost();
                    addMoneyToDistributor(0, paidMoney, newConsumer, distributors);
                    Distributor.setNewDistributors(distributors);
                } else {
                    Debt debt = new Debt();
                    final double factor = 1.2;
                    int value =
                            (int) Math.round(Math.floor(factor * newConsumer.getContractCost()));
                    debt.setDebt(value);
                    debt.setDistributor(newConsumer.getMyDistributor());
                    newConsumer.setDebtObj(debt);
                    newConsumer.setContractLength(newConsumer.getContractLength() - 1);
                    // Just to update distributor's contract length with this user
                    addMoneyToDistributor(0, 0, newConsumer, distributors);
                    Distributor.setNewDistributors(distributors);
                }
            }
        }
    }

    /**
     * Every time a consumer changes his contract, we make sure
     * to update distributors' state
     * @param consumer the consumer for which the change is made
     * @param distributors current state of distributors
     */
    public void changeContracts(final Consumer consumer,
                                final List<Distributor> distributors) {
        for (Distributor distributor : distributors) {
            List<Contract> contracts = distributor.getContracts();
            if (distributor.getId() != consumer.getMyDistributor().getId()) {
                for (int j = 0; j < contracts.size(); ++j) {
                    if (contracts.get(j).getConsumerId() == consumer.getId()) {
                        contracts.remove(contracts.get(j));
                        distributor.setContracts(contracts);
                    }
                }
            } else {
                for (int j = 0; j < contracts.size(); ++j) {
                    if (contracts.get(j).getConsumerId() == consumer.getId()) {
                        contracts.remove(contracts.get(j));
                        distributor.setContracts(contracts);
                    }
                }
                Contract contract = new Contract();
                contract.setPrice(consumer.getContractCost());
                contract.setRemainedContractMonths(consumer.getContractLength());
                contract.setConsumerId(consumer.getId());
                contracts.add(contract);
                distributor.setContracts(contracts);
            }
        }

    }

    /**
     * Every time a consumer changes his contract, we make sure
     * to update both consumers' state and distributors' state
     * @param consumers current state of consumers
     * @param distributors current state of distributors
     */
    @Override
    public void establishContract(final List<Consumer> consumers,
                                  final List<Distributor> distributors) {
        newConsumers = consumers;

        /*
        If a distributor is bankrupt, he cannot have a contract
         */
        for (int i = 0; i < distributors.size(); ++i) {
            if (distributors.get(i).isBankrupt()) {
                List<Contract> contracts = new ArrayList<>();
                distributors.get(i).setContracts(contracts);
                Distributor.setNewDistributors(distributors);
            }
        }

        /*
        If a consumer is bankrupt, we delete the contract from his distributor
         */
        for (Consumer newConsumer : newConsumers) {
            if (newConsumer.isBankrupt()) {
                Distributor d = newConsumer.getMyDistributor();
                for (int j = 0; j < distributors.size(); ++j) {
                    if (distributors.get(j).getId() == d.getId()) {
                        List<Contract> contracts = distributors.get(j).getContracts();
                        for (int k = 0; k < contracts.size(); ++k) {
                            if (contracts.get(k).getConsumerId() == newConsumer.getId()) {
                                contracts.remove(contracts.get(k));
                                distributors.get(j).setContracts(contracts);
                                Distributor.setNewDistributors(distributors);
                            }
                        }
                    }
                }
            }
        }

        /*
        If a consumer has to renew his contract or the old distributor is bankrupt
         */
        for (Consumer newConsumer : newConsumers) {
            if ((newConsumer.getContractLength() == 0
                    && !newConsumer.isBankrupt())
                    || (newConsumer.getMyDistributor().isBankrupt()
                    && !newConsumer.isBankrupt())) {
                int minCost = Integer.MAX_VALUE;
                Distributor d = new Distributor();
                for (Distributor distributor : distributors) {
                    if (distributor.getContractCost() < minCost
                            && !distributor.isBankrupt()) {
                        minCost = distributor.getContractCost();
                        d = distributor;
                    }
                }
                newConsumer.setMyDistributor(d);
                newConsumer.setContractCost(minCost);
                newConsumer.setContractLength(d.getContractLength());
                changeContracts(newConsumer, distributors);
                Distributor.setNewDistributors(distributors);
            }
        }

        Distributor.setNewDistributors(distributors);
    }

    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public int getBudget() {
        return budget;
    }

    public void setBudget(final int budget) {
        this.budget = budget;
    }

    public int getMonthlyIncome() {
        return monthlyIncome;
    }

    public void setMonthlyIncome(final int monthlyIncome) {
        this.monthlyIncome = monthlyIncome;
    }

    public boolean isBankrupt() {
        return isBankrupt;
    }

    public void setBankrupt(final boolean bankrupt) {
        isBankrupt = bankrupt;
    }

    public int getContractLength() {
        return contractLength;
    }

    public void setContractLength(final int contractLength) {
        this.contractLength = contractLength;
    }

    public int getContractCost() {
        return contractCost;
    }

    public void setContractCost(final int contractCost) {
        this.contractCost = contractCost;
    }

    public static List<Consumer> getConsumerResultRound() {
        return newConsumers;
    }

    public Distributor getMyDistributor() {
        return myDistributor;
    }

    public void setMyDistributor(final Distributor myDistributor) {
        this.myDistributor = myDistributor;
    }

    public Debt getDebtObj() {
        return debtObj;
    }

    public void setDebtObj(final Debt debtObj) {
        this.debtObj = debtObj;
    }

    @Override
    public String toString() {
        return "Consumer{"
                + "id=" + id
                + ", budget=" + budget
                + ", monthlyIncome=" + monthlyIncome
                + '}';
    }
}
