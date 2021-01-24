package factory;

import handler.Producer;
import strategies.*;

import java.util.List;

public class StrategyFactory {
    public Strategy createStrategy(EnergyChoiceStrategyType type, List<Producer> producers) {
        switch (type) {
            case GREEN -> {
                return new GreenStrategy(producers);
            }
            case PRICE -> {
                return new PriceStrategy(producers);
            }
            case QUANTITY -> {
                return new QuantityStrategy(producers);
            }
        }
        throw new IllegalArgumentException("The strategy type " + type + " is not recognized.");
    }
}
