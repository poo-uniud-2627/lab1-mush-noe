// Pong — Programmazione Orientata agli Oggetti e Laboratori, Udine, a.a. 2026-27
package pong;

import java.util.Random;

/** Una partita: due giocatori, due racchette, una palla, i conti di chi vince e i livelli. */
class Partita {
    /** Si gioca a 5, con almeno due punti di scarto (5 a 4 non basta, 6 a 4 sì). */
    static final int PUNTI_PER_VINCERE = 5;
    static final int SCARTO_MINIMO = 2;

    /** I livelli: quanti sono, quante vite si hanno, quanto va veloce la palla. */
    static final int NUMERO_LIVELLI = 5;
    static final int VITE = 3;
    static final double VELOCITA_AL_LIVELLO_1 = 5;
    static final double VELOCITA_IN_PIU_A_OGNI_LIVELLO = 0.5;

    private final Campo campo;
    private final Giocatore giocatoreSinistra;
    private final Giocatore giocatoreDestra;
    private final Racchetta racchettaSinistra;
    private final Racchetta racchettaDestra;
    private final Palla palla;
    private boolean iniziata;
    private final Punteggio punteggio = new Punteggio(PUNTI_PER_VINCERE, SCARTO_MINIMO);
    private final Livelli livelli;

    /** Una partita fra due persone: i livelli ci sono, ma non cambiano niente. */
    Partita(Campo campo, Giocatore giocatoreSinistra, Giocatore giocatoreDestra) {
        this(campo, giocatoreSinistra, giocatoreDestra, new Livelli(NUMERO_LIVELLI, VITE, Lato.SINISTRA));
    }

    /** Una partita a livelli: la persona gioca dal lato livelli.getUmano(). */
    Partita(Campo campo, Giocatore giocatoreSinistra, Giocatore giocatoreDestra, Livelli livelli) {
        this.livelli = livelli;
        this.campo = campo;
        this.giocatoreSinistra = giocatoreSinistra;
        this.giocatoreDestra = giocatoreDestra;
        double centroY = campo.getAltezza() / 2.0 - Racchetta.ALTEZZA / 2.0;
        this.racchettaSinistra = new Racchetta(30, centroY);
        this.racchettaDestra = new Racchetta(campo.getLarghezza() - 30 - Racchetta.LARGHEZZA, centroY);
        this.palla = new Palla(campo.getLarghezza() / 2.0, campo.getAltezza() / 2.0);
    }

    // Quello che lo Schermo può chiedere sui conti: solo leggere. Il Punteggio resta qui dentro.

    /** I punti del lato chi. */
    int getPunti(Lato chi) { return punteggio.getPunti(chi); }

    /** Chi ha vinto; null se la partita non è finita. */
    Lato getVincitore() { return punteggio.getVincitore(); }

    /** Vero se si sta giocando: la partita è cominciata e non è finita. */
    boolean isInCorso() { return iniziata && !punteggio.isFinita(); }

    // Quello che lo Schermo può chiedere sui livelli: solo leggere.
    int getLivello() { return livelli.getLivello(); }
    int getNumeroLivelli() { return livelli.getNumeroLivelli(); }
    int getVite() { return livelli.getVite(); }
    boolean isCompletato() { return livelli.isCompletato(); }
    boolean isPerso() { return livelli.isPerso(); }
    Lato getUmano() { return livelli.getUmano(); }

    /** Rimette a zero i conti e lancia la palla; se il gioco era finito, ricomincia dal livello 1. */
    void avvia() {
        if (livelli.isFinito()) livelli.ricomincia();
        punteggio.azzera();
        iniziata = true;
        palla.lancia(campo.getLarghezza() / 2.0, campo.getAltezza() / 2.0, new Random().nextBoolean(), velocita());
    }

    /** La velocità della palla al livello di adesso. */
    private double velocita() {
        return VELOCITA_AL_LIVELLO_1 + VELOCITA_IN_PIU_A_OGNI_LIVELLO * (livelli.getLivello() - 1);
    }

    /** Un passo del gioco: viene chiamato 60 volte al secondo. */
    void aggiorna() {
        if (!isInCorso()) return;
        muoviRacchette();
        palla.muovi();
        controllaRimbalzi();
        controllaPunto();
    }

    /** Chiede a ciascun giocatore cosa vuole fare, e lo fa. Non sa chi sono i giocatori. */
    private void muoviRacchette() {
        Mossa mossaSinistra = giocatoreSinistra.decidiMossa(palla, racchettaSinistra);
        Mossa mossaDestra = giocatoreDestra.decidiMossa(palla, racchettaDestra);
        racchettaSinistra.muovi(mossaSinistra, campo);
        racchettaDestra.muovi(mossaDestra, campo);
    }

    private void controllaRimbalzi() {
        double y = palla.getPosizione().y();
        boolean toccaAlto = y - palla.getRaggio() <= 0 && palla.getVelocitaY() < 0;
        boolean toccaBasso = y + palla.getRaggio() >= campo.getAltezza() && palla.getVelocitaY() > 0;
        if (toccaAlto || toccaBasso) palla.rimbalzaVerticale();

        if (palla.tocca(racchettaSinistra) && palla.getVelocitaX() < 0) palla.rimbalzaSu(racchettaSinistra);
        if (palla.tocca(racchettaDestra) && palla.getVelocitaX() > 0) palla.rimbalzaSu(racchettaDestra);
    }

    /** Se la palla è uscita da un lato, il punto va all'altro lato. */
    private void controllaPunto() {
        double x = palla.getPosizione().x();
        if (x < 0) {
            punteggio.segnaPunto(Lato.DESTRA);
            dopoIlPunto(true);
        } else if (x > campo.getLarghezza()) {
            punteggio.segnaPunto(Lato.SINISTRA);
            dopoIlPunto(false);
        }
    }

    /** Dopo un punto: se la partita è finita lo dice ai livelli, altrimenti si serve verso chi ha subito il punto. */
    private void dopoIlPunto(boolean versoSinistra) {
        if (punteggio.isFinita()) {
            livelli.partitaFinita(punteggio.getVincitore());
        } else {
            palla.lancia(campo.getLarghezza() / 2.0, campo.getAltezza() / 2.0, !versoSinistra, velocita());
        }
    }

    boolean isIniziata() { return iniziata; }
    Campo getCampo() { return campo; }
    Palla getPalla() { return palla; }
    Racchetta getRacchettaSinistra() { return racchettaSinistra; }
    Racchetta getRacchettaDestra() { return racchettaDestra; }
}
