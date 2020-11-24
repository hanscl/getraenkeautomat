package test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import main.*;

public class GetraenkeAutomat {

    @Test
    public void TestGetraenkeAutomat() {
        // Erstellen eines Getraenkeautomatens
        Automat testAutomat = new Automat("Berlin Mitte", 5, 10);
        System.out.println(testAutomat.readAutomatenInfo());

        // produkte hinzufuegen
        testAutomat.produktHinzufuegen("1234", "Coca Cola", 1.2f);
        testAutomat.produktHinzufuegen("5678", "Pepsi", 1.90f);
        testAutomat.produktHinzufuegen("9001", "Sprite", 1.70f);
        testAutomat.produktHinzufuegen("X012", "Wasser", 1.00f);

        System.out.println(testAutomat.getraenkeListe());

        // warenbestand auffuellen (solange noch genug Platz ist)
        Assertions.assertEquals(12, testAutomat.vorratAuffuellen("1234", 12), "Sollte 12 Getraenke auffuellen koennen!");
        Assertions.assertEquals(9, testAutomat.vorratAuffuellen("5678", 9), "Sollte 9 Getraenke auffuellen koennen!");
        Assertions.assertEquals(15, testAutomat.vorratAuffuellen("9001", 15), "Sollte 15 Getraenke auffuellen koennen!");

        System.out.println(testAutomat.getraenkeListe());

        // versuche wasser aufzufuellen - aber kein fach verfuegbar
        Assertions.assertEquals(0, testAutomat.vorratAuffuellen("X012", 10), "Sollte fehlschlagen: 0 erwartet!");

        // Zwei Cocal Cola raus & nochmal versuchen 10 Wasser aufzufuellen! Jetzt sollte es klappen!
        Assertions.assertEquals(2, testAutomat.vorratHerausnehmen("1234", 2), "Sollte 2 Geraenke herausnehmen koennen!");
        Assertions.assertEquals(10, testAutomat.vorratAuffuellen("X012", 10), "Sollte 10 Getraenke auffuellen koennen!");
        // und jetzt Pepsi nochmal auf 1 runter, damit wir nachher mal Verkaufsprobleme haben
        Assertions.assertEquals(8, testAutomat.vorratHerausnehmen("5678", 8), "Sollte 8 Getraenke herausnehmen koennen!");

        System.out.println(testAutomat.getraenkeListe());

        // Wechselgeld auffuellen - mehr als maximal platz fuer muenzen - sollte automatisch reduziert werden auf m
        testAutomat.wechselgeldAuffuellen(120,200,100, 50,20 );
        System.out.println(testAutomat.kassenbestandAnzeigen());

        // Wechselgeld entleeren
        Assertions.assertEquals(170f, testAutomat.wechselgeldEntleeren(), "erwarte 170.00 als herausgabe");

        System.out.println(testAutomat.kassenbestandAnzeigen());

        // jetzt mal etwas weniger Wechselgeld hinein, damit wir unsere Wechselfunktion pruefen koennen!
        testAutomat.wechselgeldAuffuellen(10,5,4, 1,2 );

        System.out.println(testAutomat.kassenbestandAnzeigen());

        // Jetzt kaufen wir mal was ...
        Assertions.assertEquals(true, testAutomat.produktIstVerfuegbar("5678"), "Erwarte TRUE");

        // Preisabfrage ...
        Assertions.assertEquals(1.9f, testAutomat.getProduktPreis("5678"), "Pepsi kostest EUR 1.90");

        // Versuchen wir es mal mit weniger Geld zu kaufen :)
        System.out.println(testAutomat.produktKauf("5678", 1.50f));
        System.out.println(testAutomat.kassenbestandAnzeigen());

        // jetzt dann mit genug Geld => muessten 1 Pepsi bekommen und EUR 0.10 wechselgeld
        System.out.println(testAutomat.produktKauf("5678", 2f));
        // ist das wechselgeld und unsere Einnahment korrekt?
        Assertions.assertEquals(8.9f, testAutomat.getWechelsgeldBestand(), "Erwarte 8.90");
        Assertions.assertEquals(1.9f, testAutomat.getEinnahmen(), "Erwarte 1.90");

        System.out.println(testAutomat.kassenbestandAnzeigen());

        // Pepsi muesste jetzt leer sein
        Assertions.assertEquals(false, testAutomat.produktIstVerfuegbar("5678"), "Erwarte TRUE");

        // Jetzt versuchen wir ein ausverkauftes Getraenk zu kaufen!
        // Versuchen wir es mal mit weniger Geld zu kaufen :)
        System.out.println(testAutomat.produktKauf("5678", 5f));

        // Wichtig => der produktverkauf ist fehlgeschlagen und der kunde hat sein Geld zurueckbekommen. Also duerfte
        // das Wechselgeld im Automaten unveraendert sein.
        Assertions.assertEquals(8.9f, testAutomat.getWechelsgeldBestand(), "Erwarte 8.90");

        // jetzt mal die Einnahmen entleeren
        Assertions.assertEquals(1.9f, testAutomat.einnahmenEntleeren(), "Erwarte: 1.90");
        Assertions.assertEquals(0f, testAutomat.getEinnahmen(), "Erwarte 0");

        // Einmal das Wechselgeld reduzieren um die Wechselfunktion zu testen
        Assertions.assertEquals(8.9f, testAutomat.wechselgeldEntleeren(), "erwarte 8.9 als herausgabe");
        Assertions.assertEquals(0f, testAutomat.getWechelsgeldBestand(), "Erwarte 0");

        // jetzt mal etwas weniger Wechselgeld hinein, damit wir unsere Wechselfunktion pruefen koennen!
        testAutomat.wechselgeldAuffuellen(5,5,0, 3,0 );
        System.out.println(testAutomat.kassenbestandAnzeigen());
        System.out.println(testAutomat.produktKauf("1234", 5.00f));
        System.out.println(testAutomat.kassenbestandAnzeigen());

        // Und jetzt haben wir nicht mehr genug wechselgeld ...
        System.out.println(testAutomat.produktKauf("1234", 2.00f));
        System.out.println(testAutomat.kassenbestandAnzeigen());
    }
}
