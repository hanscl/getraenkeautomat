package main;

import java.util.HashMap;
import java.util.Map;

public class Automat extends Kasse {
    // Konstanten fuer die Limitation des Automaten modells
    static int maximaleAusgabefaecher = 50;
    static int maximaleAusgabefachKapazitaet = 20;

    // und die Konfiguration dieses Automatens
    private final int anzahlAusgabefaecher;
    private final int ausgabefachKapazitaet;

    private final String standort;
    private HashMap<String, Getraenk> getraenke;

    public Automat(String standort, int anzahlAusgabefaecher, int ausgabefachKapazitaet) {
        // Ein automat muss mindestens ein Ausgabefach haben fuer mindestens ein Produkt!
        if (anzahlAusgabefaecher < 1 || ausgabefachKapazitaet < 1) {
            throw new IllegalArgumentException("Ausgabefach Kapazitaet und Anzahl Ausgabefaecher muss groesser 0 sein!");
        }
        this.standort = standort;
        // Wir bauen unseren Automaten mit Ausgabefaecher
        this.anzahlAusgabefaecher = Math.min(anzahlAusgabefaecher, maximaleAusgabefaecher);
        this.ausgabefachKapazitaet =  Math.min(ausgabefachKapazitaet, maximaleAusgabefachKapazitaet);

        this.getraenke = new HashMap<>();
    }

    public void produktHinzufuegen(String ean, String name, float preis) {
        // Preise muessen glatt auf 10 Cent enden
        if (Math.round(preis * 100) % 10 != 0) {
            throw new IllegalArgumentException("Automat hat keine 1c Muenzen zum Wechseln. Preise muessen durch 10c teilbar sein!");
        }
        if (!getraenke.containsKey(ean)) {
            getraenke.put(ean, new Getraenk(name, preis));
        }
    }

    public int vorratHerausnehmen(String ean, int anzahl) {
        int maximalZumHerausnehmen = Math.min(anzahl, getraenke.get(ean).getVorrat());
        getraenke.get(ean).herausnehmen(maximalZumHerausnehmen);

        return maximalZumHerausnehmen;
    }

    public int vorratAuffuellen(String ean, int anzahl) {
        int freierPlatzFuerProdukt = freieFaecherKapazitaet();
        // haben wir noch Platz in einem Fach mit dem gleichen Produkt?
        if (getraenke.get(ean).getVorrat() > 0) {
            freierPlatzFuerProdukt += ausgabefachKapazitaet - (getraenke.get(ean).getVorrat() % ausgabefachKapazitaet);
        }
        // den Automaten nicht ueberfuellen
        int produktAnzahlWirdAufgefuellt = Math.min(anzahl, freierPlatzFuerProdukt);
        getraenke.get(ean).auffuellen(produktAnzahlWirdAufgefuellt);

        return produktAnzahlWirdAufgefuellt;
    }

    public boolean produktIstVerfuegbar(String ean) {
        return (getraenke.get(ean).getVorrat() > 0);
    }

    public float getProduktPreis(String ean) {
        return getraenke.get(ean).getPreis();
    }

    public Map<String, Number> produktKauf(String ean, float bezahlBetrag) {
        Map<String, Number> getraenkUndWechselgeld = new HashMap<>();

        if (getraenke.get(ean).getVorrat() == 0 || bezahlBetrag < getProduktPreis(ean)) {
            getraenkUndWechselgeld.put(getraenke.get(ean).getName(), 0);
            getraenkUndWechselgeld.put("Wechselgeld", bezahlBetrag);
        }
        else {

            // wir haben vorrat und der Kunde hat genug eingeworfen => koennen wir auch wechseln?
            float wechselgeld = wechselgeldFuerVerkauf(getProduktPreis(ean), bezahlBetrag);

            if (wechselgeld == bezahlBetrag) {
                getraenkUndWechselgeld.put(getraenke.get(ean).getName(), 0);

            } else {
                getraenke.get(ean).herausnehmen(1);
                getraenkUndWechselgeld.put(getraenke.get(ean).getName(), 1);
            }
            getraenkUndWechselgeld.put("Wechselgeld", wechselgeld);
        }

        return getraenkUndWechselgeld;

    }

    private int freieFaecherKapazitaet() {
        int belegteFaecher = 0;
        for (String ean : getraenke.keySet()) {
            belegteFaecher += (int) Math.ceil(getraenke.get(ean).getVorrat() / (float) ausgabefachKapazitaet);
        }
        return (anzahlAusgabefaecher - belegteFaecher) * ausgabefachKapazitaet;
    }

    public String readAutomatenInfo() {
        return String.format("*** GETRAENKEAUTOMAT: INFO *** Standort: %s -- Kapazitaet: %d (%d Ausgabefaecher x %d pro Fach)",
                standort, ausgabefachKapazitaet * anzahlAusgabefaecher, anzahlAusgabefaecher, ausgabefachKapazitaet);
    }

    public String getraenkeListe() {
        String ret = "";
        for(String ean : getraenke.keySet()) {
            ret = ret.concat(String.format("Produkt: '%s' - EAN: %s, Preis: %.2f, Vorrat: %d\n",
                    getraenke.get(ean).getName(), ean,  getraenke.get(ean).getPreis(), getraenke.get(ean).getVorrat()));
        }
        return ret;
    }
}
