// Pong — Programmazione Orientata agli Oggetti e Laboratori, Udine, a.a. 2026-27
package giocatori;

import java.util.Random;
import pong.Giocatore;
import pong.Livelli;
import pong.Mossa;
import pong.Palla;
import pong.Racchetta;

/**
 * La soluzione più semplice della lezione 1: la racchetta insegue la palla.
 * Con i livelli sbaglia: ogni volta che la palla arriva verso di lui, valuta male dove arriverà,
 * di un po' a caso; ai livelli bassi sbaglia di più, e a volte la manca del tutto.
 */
public class GiocatoreAutomatico implements Giocatore {
    /**
     * Di quanti pixel al massimo sbaglia la valutazione al livello 1, e quanto meno a ogni livello.
     * La racchetta è alta 80 e la palla ha raggio 8: un errore oltre 48 pixel vuol dire mancarla.
     */
    static final double ERRORE_AL_LIVELLO_1 = 90;
    static final double ERRORE_IN_MENO_A_OGNI_LIVELLO = 12;

    private final Livelli livelli;   // null: non sbaglia mai, come nella lezione 1
    private final Random caso = new Random();
    private double errore;           // l'errore scelto per la palla che sta arrivando
    private boolean arrivava;        // se al fotogramma prima la palla veniva verso di lui

    /** Il giocatore della lezione 1: non sbaglia mai. */
    public GiocatoreAutomatico() {
        this.livelli = null;
    }

    /** Un giocatore che sbaglia meno a ogni livello. */
    public GiocatoreAutomatico(Livelli livelli) {
        this.livelli = livelli;
    }

    @Override
    public Mossa decidiMossa(Palla palla, Racchetta racchetta) {
        double dove = palla.getPosizione().y() + erroreDiValutazione(palla, racchetta);
        if (dove < racchetta.getCentroY() - 3) return Mossa.SU;
        if (dove > racchetta.getCentroY() + 3) return Mossa.GIU;
        return Mossa.FERMO;
    }

    /** Quando la palla comincia a venire verso di lui, sceglie a caso di quanto sbagliare per quella palla. */
    private double erroreDiValutazione(Palla palla, Racchetta racchetta) {
        if (livelli == null) return 0;
        boolean arriva = (racchetta.getX() - palla.getPosizione().x()) * palla.getVelocitaX() > 0;
        if (arriva && !arrivava) {
            double massimo = Math.max(0, ERRORE_AL_LIVELLO_1 - ERRORE_IN_MENO_A_OGNI_LIVELLO * (livelli.getLivello() - 1));
            errore = (caso.nextDouble() * 2 - 1) * massimo;
        }
        arrivava = arriva;
        return errore;
    }
}
