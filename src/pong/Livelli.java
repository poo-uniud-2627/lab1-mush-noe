// Pong — Programmazione Orientata agli Oggetti e Laboratori, Udine, a.a. 2026-27
// Consegna 3: i livelli. Scadenza: domenica 8 novembre 2026.
package pong;

/**
 * Il gioco a livelli contro il giocatore automatico: a che livello si è e quante vite restano.
 * Si vince un livello vincendo una partita; perdendo una partita si perde una vita.
 * La Partita gli dice com'è finita ogni partita; il giocatore automatico e la velocità della palla
 * dipendono dal livello.
 *
 * IL PEZZO MANCANTE: i corpi dei metodi qui sotto (e i campi che vi servono).
 * Ogni metodo ha il suo contratto, nel commento sopra. I test ufficiali (ConsegnaTreTest) lo controllano.
 */
public class Livelli {

    // scrivete qui i campi che vi servono
    private final int numeroLivelli;
    private final int viteIniziali;
    private final Lato umano;
    private int livello;
    private int vite;
    private boolean completato;

    /**
     * Un gioco nuovo.
     * Pretende: niente.
     * Promette: livello 1, con tutte le vite; il gioco non è completato né perso.
     * Lancia IllegalArgumentException se numeroLivelli o vite è minore di 1, o se umano è null.
     */
    public Livelli(int numeroLivelli, int vite, Lato umano) {
        if (numeroLivelli < 1 || vite < 1) {
            throw new IllegalArgumentException("numeroLivelli e vite devono essere almeno 1");
        } else if (umano == null) {
            throw new IllegalArgumentException("umano non può essere null");
        }
        this.numeroLivelli = numeroLivelli;
        this.viteIniziali = vite;
        this.umano = umano;
        this.livello = 1;
        this.vite = vite;
        this.completato = false;
    }

    /** Il livello a cui si gioca, fra 1 e getNumeroLivelli(). */
    public int getLivello() {
        return this.livello; // scrivete qui
    }

    /** Quanti livelli ha il gioco. */
    public int getNumeroLivelli() {
        return this.numeroLivelli;
    }

    /** Le vite che restano, fra 0 e quelle iniziali. */
    public int getVite() {
        return this.vite;
    }

    /** Il lato su cui gioca la persona (l'altro è il giocatore automatico). */
    public Lato getUmano() {
        return this.umano;
    }

    /** Vero se è stato vinto l'ultimo livello. */
    public boolean isCompletato() {
        return this.completato; // scrivete qui
    }

    /** Vero se le vite sono finite. */
    public boolean isPerso() {
        return this.vite==0; // scrivete qui
    }

    /** Vero se il gioco è completato o perso. */
    public boolean isFinito() {
        return isCompletato()||isPerso(); // scrivete qui
    }

    /**
     * Registra com'è finita una partita.
     * Pretende: niente.
     * Promette: se il gioco è finito (completato o perso), non cambia niente. Altrimenti:
     *           se ha vinto umano, si passa al livello dopo; se era l'ultimo, il gioco è completato
     *           e il livello resta l'ultimo; le vite non cambiano.
     *           Se ha vinto l'altro lato, una vita in meno e il livello non cambia;
     *           con l'ultima vita il gioco è perso.
     * Lancia IllegalArgumentException se vincitore è null, e non cambia niente.
     */
    public void partitaFinita(Lato vincitore) {
        if (vincitore == null){
            throw new IllegalArgumentException("vincitore non può essere null");
        } 
        if (isFinito()){
            return;
        }
        if (vincitore == this.umano) {
            if (this.livello == this.numeroLivelli){
                this.completato = true;
            } else {
                this.livello++;
            }
        } else {
            this.vite--;
        }
        // scrivete qui
    }

    /**
     * Si ricomincia da capo.
     * Pretende: niente.
     * Promette: livello 1, con tutte le vite; il gioco non è completato né perso.
     */
    public void ricomincia() {
       this.livello = 1;
       this.vite = this.viteIniziali;
       this.completato = false; // scrivete qui
    }
}
