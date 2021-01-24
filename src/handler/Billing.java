package handler;

import java.util.List;

public interface Billing {
    /**
     * For consumers: getting salary, paying distributors
     * For distributors: paying expenses
     * @param consumers current state of consumers
     * @param distributors current state of distributors
     */
    void getStatus(List<Consumer> consumers, List<Distributor> distributors);

    /**
     * For consumers: choosing the distributor
     * For distributors: computing the contract costs
     * @param consumers current state of consumers
     * @param distributors current state of distributors
     */
    void establishContract(List<Consumer> consumers, List<Distributor> distributors);
}
