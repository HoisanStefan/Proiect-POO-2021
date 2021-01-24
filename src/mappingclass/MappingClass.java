package mappingclass;

import java.util.List;

public final class MappingClass {
    private int numberOfTurns;
    private InitialData initialData;
    private List<UpdateEntry> monthlyUpdates;

    public int getNumberOfTurns() {
        return numberOfTurns;
    }

    public void setNumberOfTurns(final int numberOfTurns) {
        this.numberOfTurns = numberOfTurns;
    }

    public List<UpdateEntry> getMonthlyUpdates() {
        return monthlyUpdates;
    }

    public void setMonthlyUpdates(final List<UpdateEntry> monthlyUpdates) {
        this.monthlyUpdates = monthlyUpdates;
    }

    public InitialData getInitialData() {
        return initialData;
    }

    public void setInitialData(final InitialData initialData) {
        this.initialData = initialData;
    }
}
