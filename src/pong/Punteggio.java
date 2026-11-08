// Pong — Programmazione Orientata agli Oggetti e Laboratori, Udine, a.a. 2026-27
// Consegna 1: il Punteggio. Scadenza: domenica 25 ottobre 2026.
package pong;

import java.util.ArrayList;
import java.util.List;

/**
 * I conti di una partita: chi ha segnato, quanti punti ha ciascun lato, chi ha vinto.
 * La Partita gli dice chi ha fatto punto; lo Schermo chiede i punti alla Partita, che li chiede a lui.
 *
 * La regola per vincere: un lato vince quando ha almeno {@code puntiPerVincere} punti e almeno
 * {@code scartoMinimo} punti più dell'altro. Esempi, con 11 punti e scarto 2: 11 a 9 vince;
 * 11 a 10 si continua; 12 a 10 vince. Con scarto 1 vince chi arriva per primo a 11.
 *
 * IL PEZZO MANCANTE: i corpi dei metodi qui sotto (e i campi che vi servono).
 * Ogni metodo ha il suo contratto, nel commento sopra: cosa pretende, cosa promette.
 * I test ufficiali (cartella test) controllano i contratti.
 */
public class Punteggio {

    private final int puntiPerVincere;
    private final int scartoMinimo;
    private final List<Lato> storia = new ArrayList<>();

    /**
     * Una partita nuova, 0 a 0, con la sua regola per vincere.
     * Pretende: niente.
     * Promette: una partita 0 a 0, non finita, senza vincitore e con la storia vuota.
     * Lancia `IllegalArgumentException` se `puntiPerVincere` è minore di 1 o
     *  `scartoMinimo` è minore di 1.
     */

public Punteggio(int puntiPerVincere, int scartoMinimo) {
    if (puntiPerVincere < 1 || scartoMinimo < 1) {
        throw new IllegalArgumentException("puntiPerVincere e scartoMinimo devono essere almeno 1");
    }
    this.puntiPerVincere = puntiPerVincere;
    this.scartoMinimo = scartoMinimo;
}

    /**
     * Il lato chi ha fatto un punto.
     * Pretende: niente.
     * Promette: se la partita non era finita, il lato chi ha un punto in più, l'altro lato non
     *           cambia e il punto si aggiunge in fondo alla storia; se con questo punto il lato chi
     *           vince (regola in cima alla classe), la partita è finita e il vincitore è chi.
     *           Se la partita era già finita, non cambia niente.
     * Se `chi` è null lancia `IllegalArgumentException`, anche a partita finita, e non cambia niente.
     */
    public void segnaPunto(Lato chi) {
        if (chi == null) {
            throw new IllegalArgumentException("Il lato non può essere null");
        }
        if (!isFinita()) {
            storia.add(chi);
        }
    }

    /**
     * Pretende: niente.
     * Promette: i punti del lato chi. Non cambia niente.
     * Lancia `IllegalArgumentException` se `chi` è null.
     */
    public int getPunti(Lato chi) {
        if (chi == null) {
            throw new IllegalArgumentException("Il lato non può essere null");
        }
        int count = 0;
        for (Lato l : storia) {
            if (l == chi) {
                count++;
            }
        }
        return count;
    }

    /**
     * Promette: vero se la partita è finita, cioè se un lato ha vinto. Non cambia niente.
     */
    public boolean isFinita() {
        int pSinistra = getPunti(Lato.SINISTRA);
        int pDestra = getPunti(Lato.DESTRA);

        int max = Math.max(pSinistra, pDestra);
        int scarto = Math.abs(pSinistra - pDestra);

        return max >= puntiPerVincere && scarto >= scartoMinimo;
    }

    /**
     * Promette: il lato che ha vinto; null se la partita non è finita. Non cambia niente.
     */
    public Lato getVincitore() {
        if (!isFinita()) {
            return null;
        }
        return getPunti(Lato.SINISTRA) > getPunti(Lato.DESTRA) ? Lato.SINISTRA : Lato.DESTRA;
    }

    /**
     * L'arbitro ci ripensa: toglie l'ultimo punto della storia (l'ultimo segnato e non già annullato).
     * Pretende: niente.
     * Promette: se nella storia c'è almeno un punto, l'ultimo punto viene tolto, dai conti e dalla
     *           storia, come se non fosse mai stato segnato. Se era il punto che aveva chiuso la
     *           partita, la partita torna in corso, senza vincitore. Se la storia è vuota, non cambia
     *           niente.
     */
    public void annullaUltimoPunto() {
        if (!storia.isEmpty()) {
            storia.remove(storia.size() - 1);
        }
    }

    /**
     * La storia dei punti, dal primo all'ultimo: per ogni punto segnato (e non annullato), il lato
     * che l'ha segnato.
     * Pretende: niente.
     * Promette: una lista con i punti in ordine. Chi riceve la lista non può cambiare il Punteggio
     *           attraverso di lei: aggiungere, togliere o cambiare elementi della lista ricevuta
     *           (se la lista lo permette) non cambia i punti, il vincitore né la storia del Punteggio.
     *           Non promette altro: la lista ricevuta può restare com'era o mostrare anche i punti
     *           segnati dopo; chi la usa non deve contare né sull'una né sull'altra cosa.
     */
    public List<Lato> getStoria() {
        return List.copyOf(storia);
    }

    /**
     * Una partita nuova con la stessa regola.
     * Pretende: niente.
     * Promette: 0 a 0, non finita, senza vincitore, storia vuota; la regola per vincere non cambia.
     */
    public void azzera() {
        storia.clear();
    }
    
    /** repOK
     * storia non contiente null
     * i punti di un lato sono quanti compaiono nella storia
    * - storia non contiene null;
    * - nessun punto della storia viene dopo quello che ha chiuso la partita.
    */
    boolean repOK() {
        int sx = 0;
        int dx = 0;
        for (int i = 0; i < storia.size(); i++){
            if (storia.get(i) == null){
                return false;
            }
            if ((Math.max(sx, dx) >= puntiPerVincere) && (Math.abs(sx - dx) >= scartoMinimo)) {
                return false; 
            }
            if (storia.get(i) == Lato.SINISTRA){
                sx++;
            } else {
                dx++;
            }
        }
        return true;
    }
}
