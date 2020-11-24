package main;

public class Kasse {
    private Muenzen[] muenzen = Muenzen.values();
    private final int[] maximalWechselgeld = new int[]{100, 100, 100, 50, 20};
    private int[] wechselgeldBestand = new int[5];
    private float einnahmen;

    public void wechselgeldAuffuellen(int c10, int c20, int c50, int e1, int e2) {
        int[] neueMuenzen = new int[]{c10, c20, c50, e1, e2};
        for (int i = 0; i < muenzen.length; i++) {
            wechselgeldBestand[i] = Math.min(maximalWechselgeld[i], wechselgeldBestand[i] + neueMuenzen[i]);
        }
    }

    protected float wechselgeldFuerVerkauf(float kaufBetrag, float bezahlBetrag) {
        int wechselBetragInCent = Math.round((bezahlBetrag - kaufBetrag) * 100);

        if (wechselBetragInCent == 0) {
            return 0;
        }

        // konnten nicht wechseln; gib bezahl zurueck
        int[] geldAusgabe = wechselgeldInMuenzen(wechselBetragInCent);
        if(geldAusgabe == null) {
            return bezahlBetrag;
        }

        // kauf betrag ist jetzt unsere einnahme
        einnahmen += kaufBetrag;

        return berechneBetrag(geldAusgabe);
    }

    private int[] wechselgeldInMuenzen(int wechselBetragInCent) {
        int[] wechselGeldInMuenzen = new int[5];
        int[] tempWechselBestand = wechselgeldBestand.clone();
        for (int i = muenzen.length - 1; i >= 0; i--) {
            // maximal betrag fuer die momentane muenze
            int restBetrag = wechselBetragInCent % muenzen[i].getWertInCent();
            wechselGeldInMuenzen[i] = (wechselBetragInCent - restBetrag) / muenzen[i].getWertInCent();
            int restMuenzenInDenom = tempWechselBestand[i] - wechselGeldInMuenzen[i];

            // sicherstellen dass wir genug muenzen in dieser denomination haben
            if (restMuenzenInDenom >= 0) {
                tempWechselBestand[i] = restMuenzenInDenom;
                wechselBetragInCent = restBetrag;
                continue;
            }

            // nicht genug muenzen in dieser denom -- den betrag wieder erhoehen und die muenzen im wechselbetrag reduzieren
            wechselBetragInCent = restBetrag + (restMuenzenInDenom * muenzen[i].getWertInCent() * -1);
            wechselGeldInMuenzen[i] += restMuenzenInDenom;
            tempWechselBestand[i] = 0;
        }

        //  Der wechsel betrag muss jetzt 0 sein. falls nicht, dann hatten wir nicht genug wechselgeld!
        if (wechselBetragInCent == 0) {
            // wechselaktion durchfuehren
            wechselgeldBestand = tempWechselBestand;
            return wechselGeldInMuenzen;
        }
        else {
            return null;
        }
    }

    public float einnahmenEntleeren() {
        float ret = einnahmen;
        einnahmen = 0;
        return ret;
    }

    public float wechselgeldEntleeren() {
        float wechselgeldTotal = berechneBetrag(wechselgeldBestand);
        wechselgeldBestand = new int[5];
        return (wechselgeldTotal);
    }

    public float getEinnahmen() {
        return einnahmen;
    }

    public float getWechelsgeldBestand() {
        return berechneBetrag(wechselgeldBestand);
    }

    protected float berechneBetrag(int[] muenzenAnzahl) {
        int betragInCent = 0;
        for (int i = 0; i < muenzenAnzahl.length; i++) {
            betragInCent += muenzenAnzahl[i] * muenzen[i].getWertInCent();
        }
        return betragInCent / 100.0f;
    }

    public String kassenbestandAnzeigen() {
       return String.format("Einnahmen: %.2f -- Wechselgeld: %.2f [10c:%d, 20c:%d, 50c:%d, 1 Euro:%d, 2 Euro: %d]",
               einnahmen, berechneBetrag(wechselgeldBestand), wechselgeldBestand[0], wechselgeldBestand[1], wechselgeldBestand[2],
               wechselgeldBestand[3], wechselgeldBestand[4]);
    }
}


