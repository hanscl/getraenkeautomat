package main;

public class Getraenk {
    private String name;
    private float preis;
    private int vorrat;


    public Getraenk(String name, float preis) {
        this.name = name;
        this.preis = preis;
    }

    public String getName() {
        return name;
    }

    public float getPreis() {
        return preis;
    }

    public int getVorrat() {
        return vorrat;
    }

    public void auffuellen(int anzahl) {
        vorrat += anzahl;
    }

    public void herausnehmen(int anzahl) {
        vorrat -= anzahl;
    }
}
