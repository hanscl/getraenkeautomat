package main;

public enum Muenzen {
    C10(10), C20(20), C50(50), E1(100), E2(200);

    private int wertInCent;

    Muenzen(int wertInCent) {
        this.wertInCent = wertInCent;
    }

    public int getWertInCent() {
        return this.wertInCent;
    }
}
