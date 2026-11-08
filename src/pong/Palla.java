// Pong — Programmazione Orientata agli Oggetti e Laboratori, Udine, a.a. 2026-27
package pong;

import geometria.Posizione;
import java.util.Random;

/** La palla: una posizione, una velocità, un raggio. Sa muoversi e rimbalzare. */
public class Palla {
    /** Quanti pixel percorre la palla in orizzontale a ogni fotogramma (60 fotogrammi al secondo). */
    static final double VELOCITA_INIZIALE = 5;

    /** Di quanto si moltiplica la velocità a ogni rimbalzo su una racchetta. 1.0 = non cambia mai. */
    static final double ACCELERAZIONE_A_OGNI_RIMBALZO = 1.0;

    /** Ogni quanti rimbalzi sulle racchette la palla accelera, e di quanto (1.15 = il 15% più veloce). */
    static final int RIMBALZI_PER_ACCELERARE = 4;
    static final double ACCELERAZIONE_OGNI_TOT_RIMBALZI = 1.15;
    /** La velocità oltre la quale la palla non accelera più (in orizzontale, pixel per fotogramma). */
    static final double VELOCITA_MASSIMA = 14;

    static final int RAGGIO = 8;

    private Posizione posizione;
    private double velocitaX;
    private double velocitaY;
    private int rimbalziDaLancio;   // rimbalzi sulle racchette dall'ultimo lancio
    private final Random caso = new Random();

    Palla(double x, double y) {
        this.posizione = new Posizione(x, y);
    }

    // ---- quello che chiunque può chiedere alla palla

    /** Dove si trova la palla (il suo centro). Posizione è un record: chi la riceve non può spostare la palla. */
    public Posizione getPosizione() { return posizione; }
    public double getVelocitaX() { return velocitaX; }
    public double getVelocitaY() { return velocitaY; }
    public int getRaggio() { return RAGGIO; }

    // ---- quello che può chiedere solo il codice del pacchetto pong (la Partita)

    /** Rimette la palla in un punto e la lancia verso destra o verso sinistra, con un po' di angolo a caso. */
    void lancia(double x, double y, boolean versoDestra) {
        lancia(x, y, versoDestra, VELOCITA_INIZIALE);
    }

    /** Come lancia, ma con la velocità data (pixel per fotogramma in orizzontale): la usano i livelli. */
    void lancia(double x, double y, boolean versoDestra, double velocita) {
        posizione = new Posizione(x, y);
        rimbalziDaLancio = 0;
        velocitaX = versoDestra ? velocita : -velocita;
        velocitaY = (caso.nextDouble() * 2 - 1) * velocita * 0.6;
    }

    void muovi() {
        posizione = posizione.spostata(velocitaX, velocitaY);
    }

    /** Rimbalzo sul bordo alto o basso del campo: si inverte solo la direzione verticale. */
    void rimbalzaVerticale() {
        velocitaY = -velocitaY;
    }

    /** Vero se la palla, in questo istante, si sovrappone alla racchetta. */
    boolean tocca(Racchetta racchetta) {
        double x = posizione.x();
        double y = posizione.y();
        boolean sovrappostaInOrizzontale = x + RAGGIO >= racchetta.getX()
                && x - RAGGIO <= racchetta.getX() + racchetta.getLarghezza();
        boolean sovrappostaInVerticale = y + RAGGIO >= racchetta.getY()
                && y - RAGGIO <= racchetta.getY() + racchetta.getAltezza();
        return sovrappostaInOrizzontale && sovrappostaInVerticale;
    }

    /**
     * Rimbalzo su una racchetta: la palla torna indietro e prende un angolo che dipende
     * da dove ha colpito la racchetta (al centro va dritta, sui bordi va in diagonale).
     */
    void rimbalzaSu(Racchetta racchetta) {
        double velocita = Math.abs(velocitaX) * ACCELERAZIONE_A_OGNI_RIMBALZO;
        rimbalziDaLancio++;
        if (rimbalziDaLancio % RIMBALZI_PER_ACCELERARE == 0) {
            velocita = Math.min(velocita * ACCELERAZIONE_OGNI_TOT_RIMBALZI, Math.max(VELOCITA_MASSIMA, Math.abs(velocitaX)));
        }
        double scostamento = (posizione.y() - racchetta.getCentroY()) / (racchetta.getAltezza() / 2.0);
        velocitaY = scostamento * velocita;
        velocitaX = velocitaX > 0 ? -velocita : velocita;
        // la palla viene spostata appena fuori dalla racchetta, così non rimbalza due volte
        if (velocitaX > 0) {
            posizione = new Posizione(racchetta.getX() + racchetta.getLarghezza() + RAGGIO, posizione.y());
        } else {
            posizione = new Posizione(racchetta.getX() - RAGGIO, posizione.y());
        }
    }
}
