// Pong — Programmazione Orientata agli Oggetti e Laboratori, Udine, a.a. 2026-27
package pong;

import giocatori.GiocatoreAutomatico;
import java.awt.event.KeyEvent;

/** L'avvio: sceglie i giocatori e apre la finestra. In VS Code: «Run» sopra main. */
public class Main {

    public static void main(String[] args) {
        Tastiera tastiera = new Tastiera();
        // Il gioco a livelli: voi a sinistra con le frecce, il giocatore automatico a destra.
        Livelli livelli = new Livelli(Partita.NUMERO_LIVELLI, Partita.VITE, Lato.SINISTRA);
        Giocatore sinistra = new GiocatoreTastiera(tastiera, KeyEvent.VK_UP, KeyEvent.VK_DOWN);
        Giocatore destra = new GiocatoreAutomatico(livelli);
        Partita partita = new Partita(new Campo(800, 500), sinistra, destra, livelli);

        Schermo.mostra(partita, tastiera);
    }
}
