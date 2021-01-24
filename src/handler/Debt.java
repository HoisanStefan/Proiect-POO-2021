package handler;

public final class Debt {
    private int debt;
    private Distributor distributor;

    public Debt() {
        this.debt = 0;
        this.distributor = new Distributor();
        this.distributor.setId(-1);
    }

    public int getDebt() {
        return debt;
    }

    public void setDebt(final int debt) {
        this.debt = debt;
    }

    public Distributor getDistributor() {
        return distributor;
    }

    public void setDistributor(final Distributor distributor) {
        this.distributor = distributor;
    }
}
