package factory;

import strategies.EnergyChoiceStrategyType;
import strategies.QuantityStrategy;
import strategies.PriceStrategy;
import strategies.GreenStrategy;
import strategies.Strategy;


public final class StrategyFactory {
    /**
     * Factory class for strategies
     * @param type Strategy type
     * @return Corresponding strategy instance
     */
    public Strategy createStrategy(EnergyChoiceStrategyType type) {
        switch (type) {
            case GREEN -> {
                return new GreenStrategy();
            }
            case PRICE -> {
                return new PriceStrategy();
            }
            case QUANTITY -> {
                return new QuantityStrategy();
            }
            default -> throw new IllegalArgumentException(
                    "The strategy type " + type + " is not recognized.");
        }
    }
}
