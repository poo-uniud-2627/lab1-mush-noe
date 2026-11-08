// Pong — Programmazione Orientata agli Oggetti e Laboratori, Udine, a.a. 2026-27
// Consegna 3 (laboratorio 1): i test ufficiali delle aggiunte al Punteggio e della classe Livelli.
// Ogni test è un caso del contratto.
// I test della consegna 1 (PunteggioTest) restano: devono passare anche loro.
package pong;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.junit.jupiter.api.Test;

class ConsegnaTreTest {

    // ---- attrezzi

    /** Segna i punti di una sequenza: "SSD" vuol dire sinistra, sinistra, destra. */
    private static void punti(Punteggio p, String sequenza) {
        for (char c : sequenza.toCharArray()) {      // un carattere della stringa alla volta
            if (c == 'S') p.segnaPunto(Lato.SINISTRA);
            else p.segnaPunto(Lato.DESTRA);
        }
    }

    /** Come punti, ma controlla repOK dopo ogni punto. */
    private static void puntiConRepOK(Punteggio p, String sequenza) {
        for (char c : sequenza.toCharArray()) {
            if (c == 'S') p.segnaPunto(Lato.SINISTRA);
            else p.segnaPunto(Lato.DESTRA);
            assertTrue(p.repOK(), "repOK falso dopo i punti " + p.getStoria());
        }
    }

    // ---- le eccezioni per gli argomenti sbagliati

    @Test
    void ilCostruttoreRifiutaPuntiPerVincereMinoriDiUno() {
        assertThrows(IllegalArgumentException.class, () -> new Punteggio(0, 2));
        assertThrows(IllegalArgumentException.class, () -> new Punteggio(-11, 2));
    }

    @Test
    void ilCostruttoreRifiutaScartoMinimoMinoreDiUno() {
        assertThrows(IllegalArgumentException.class, () -> new Punteggio(11, 0));
        assertThrows(IllegalArgumentException.class, () -> new Punteggio(11, -2));
    }

    @Test
    void ilCostruttoreAccettaIConfini() {
        Punteggio p = new Punteggio(1, 1);
        p.segnaPunto(Lato.DESTRA);
        assertEquals(Lato.DESTRA, p.getVincitore());
    }

    @Test
    void segnaPuntoConNullLanciaENonCambiaNiente() {
        Punteggio p = new Punteggio(11, 2);
        p.segnaPunto(Lato.SINISTRA);
        assertThrows(IllegalArgumentException.class, () -> p.segnaPunto(null));
        assertEquals(1, p.getPunti(Lato.SINISTRA));
        assertEquals(0, p.getPunti(Lato.DESTRA));
        assertEquals(List.of(Lato.SINISTRA), p.getStoria());
    }

    @Test
    void segnaPuntoConNullLanciaAncheAPartitaFinita() {
        Punteggio p = new Punteggio(1, 1);
        p.segnaPunto(Lato.SINISTRA);
        assertThrows(IllegalArgumentException.class, () -> p.segnaPunto(null));
    }

    @Test
    void getPuntiConNullLancia() {
        Punteggio p = new Punteggio(11, 2);
        assertThrows(IllegalArgumentException.class, () -> p.getPunti(null));
    }

    // ---- repOK: vero dopo ogni chiamata con le precondizioni vere (un repOK troppo severo è rosso qui)

    @Test
    void repOKAllInizio() {
        assertTrue(new Punteggio(11, 2).repOK());
        assertTrue(new Punteggio(1, 1).repOK());
    }

    @Test
    void repOKDuranteUnaPartitaConIVantaggi() {
        Punteggio p = new Punteggio(11, 2);
        puntiConRepOK(p, "SDSDSDSDSDSDSDSDSDSD" + "SDSDDD");
        assertEquals(Lato.DESTRA, p.getVincitore());
        p.segnaPunto(Lato.SINISTRA);              // a partita finita: non cambia niente
        assertTrue(p.repOK());
    }

    @Test
    void repOKDopoAnnullaEAzzera() {
        Punteggio p = new Punteggio(3, 1);
        puntiConRepOK(p, "SSS");
        p.annullaUltimoPunto();
        assertTrue(p.repOK());
        p.annullaUltimoPunto();
        p.annullaUltimoPunto();
        p.annullaUltimoPunto();                   // storia vuota: non cambia niente
        assertTrue(p.repOK());
        p.azzera();
        assertTrue(p.repOK());
    }

    @Test
    void repOKDopoGetStoriaCambiata() {
        Punteggio p = new Punteggio(11, 2);
        punti(p, "SSD");
        try { p.getStoria().clear(); } catch (UnsupportedOperationException e) { /* non si lascia cambiare */ }
        assertTrue(p.repOK());
    }

    // ---- Livelli: il costruttore

    @Test
    void unGiocoNuovoCominciaDalLivello1ConTutteLeVite() {
        Livelli l = new Livelli(5, 3, Lato.SINISTRA);
        assertEquals(1, l.getLivello());
        assertEquals(3, l.getVite());
        assertEquals(5, l.getNumeroLivelli());
        assertEquals(Lato.SINISTRA, l.getUmano());
        assertFalse(l.isCompletato());
        assertFalse(l.isPerso());
        assertFalse(l.isFinito());
    }

    @Test
    void ilCostruttoreDiLivelliRifiutaZeroLivelliOZeroVite() {
        assertThrows(IllegalArgumentException.class, () -> new Livelli(0, 3, Lato.SINISTRA));
        assertThrows(IllegalArgumentException.class, () -> new Livelli(5, 0, Lato.SINISTRA));
        assertThrows(IllegalArgumentException.class, () -> new Livelli(-1, -1, Lato.SINISTRA));
    }

    @Test
    void ilCostruttoreDiLivelliRifiutaUmanoNull() {
        assertThrows(IllegalArgumentException.class, () -> new Livelli(5, 3, null));
    }

    @Test
    void ilCostruttoreDiLivelliAccettaIConfini() {
        Livelli l = new Livelli(1, 1, Lato.DESTRA);
        assertEquals(1, l.getLivello());
        assertEquals(1, l.getVite());
        assertFalse(l.isFinito());
    }

    // ---- Livelli: vincere

    @Test
    void vincendoSiPassaAlLivelloDopoELeViteNonCambiano() {
        Livelli l = new Livelli(5, 3, Lato.SINISTRA);
        l.partitaFinita(Lato.SINISTRA);
        assertEquals(2, l.getLivello());
        assertEquals(3, l.getVite());
        l.partitaFinita(Lato.SINISTRA);
        assertEquals(3, l.getLivello());
        assertFalse(l.isFinito());
    }

    @Test
    void vincereNonRestituisceLeVitePerse() {
        Livelli l = new Livelli(5, 3, Lato.SINISTRA);
        l.partitaFinita(Lato.DESTRA);
        l.partitaFinita(Lato.SINISTRA);
        assertEquals(2, l.getLivello());
        assertEquals(2, l.getVite());
    }

    @Test
    void contaIlLatoDellUmanoNonLaSinistra() {
        Livelli l = new Livelli(5, 3, Lato.DESTRA);
        l.partitaFinita(Lato.DESTRA);
        assertEquals(2, l.getLivello());
        l.partitaFinita(Lato.SINISTRA);
        assertEquals(2, l.getLivello());
        assertEquals(2, l.getVite());
    }

    @Test
    void vintoLUltimoLivelloIlGiocoECompletatoEIlLivelloRestaLUltimo() {
        Livelli l = new Livelli(3, 3, Lato.SINISTRA);
        vinci(l, 3);
        assertTrue(l.isCompletato());
        assertTrue(l.isFinito());
        assertFalse(l.isPerso());
        assertEquals(3, l.getLivello());
        assertEquals(3, l.getVite());
    }

    @Test
    void conUnSoloLivelloBastaUnaVittoria() {
        Livelli l = new Livelli(1, 3, Lato.SINISTRA);
        l.partitaFinita(Lato.SINISTRA);
        assertTrue(l.isCompletato());
        assertEquals(1, l.getLivello());
    }

    // ---- Livelli: perdere

    @Test
    void perdendoSiPerdeUnaVitaEIlLivelloNonCambia() {
        Livelli l = new Livelli(5, 3, Lato.SINISTRA);
        l.partitaFinita(Lato.SINISTRA);
        l.partitaFinita(Lato.DESTRA);
        assertEquals(2, l.getLivello());
        assertEquals(2, l.getVite());
        assertFalse(l.isFinito());
    }

    @Test
    void conLUltimaVitaIlGiocoEPerso() {
        Livelli l = new Livelli(5, 3, Lato.SINISTRA);
        perdi(l, 3);
        assertEquals(0, l.getVite());
        assertTrue(l.isPerso());
        assertTrue(l.isFinito());
        assertFalse(l.isCompletato());
        assertEquals(1, l.getLivello());
    }

    @Test
    void perdereLUltimaVitaAllUltimoLivelloEPersoNonCompletato() {
        Livelli l = new Livelli(2, 1, Lato.SINISTRA);
        l.partitaFinita(Lato.SINISTRA);
        l.partitaFinita(Lato.DESTRA);
        assertTrue(l.isPerso());
        assertFalse(l.isCompletato());
        assertEquals(2, l.getLivello());
    }

    // ---- Livelli: a gioco finito non cambia niente

    @Test
    void aGiocoCompletatoNonCambiaNiente() {
        Livelli l = new Livelli(2, 3, Lato.SINISTRA);
        vinci(l, 2);
        l.partitaFinita(Lato.SINISTRA);
        l.partitaFinita(Lato.DESTRA);
        assertTrue(l.isCompletato());
        assertFalse(l.isPerso());
        assertEquals(2, l.getLivello());
        assertEquals(3, l.getVite());
    }

    @Test
    void aGiocoPersoNonCambiaNiente() {
        Livelli l = new Livelli(5, 2, Lato.SINISTRA);
        perdi(l, 2);
        l.partitaFinita(Lato.DESTRA);
        assertEquals(0, l.getVite());
        l.partitaFinita(Lato.SINISTRA);
        assertEquals(1, l.getLivello());
        assertTrue(l.isPerso());
        assertFalse(l.isCompletato());
    }

    @Test
    void partitaFinitaConNullLanciaENonCambiaNiente() {
        Livelli l = new Livelli(5, 3, Lato.SINISTRA);
        l.partitaFinita(Lato.SINISTRA);
        l.partitaFinita(Lato.DESTRA);
        assertThrows(IllegalArgumentException.class, () -> l.partitaFinita(null));
        assertEquals(2, l.getLivello());
        assertEquals(2, l.getVite());
    }

    @Test
    void partitaFinitaConNullLanciaAncheAGiocoFinito() {
        Livelli l = new Livelli(1, 1, Lato.SINISTRA);
        l.partitaFinita(Lato.DESTRA);
        assertThrows(IllegalArgumentException.class, () -> l.partitaFinita(null));
    }

    // ---- Livelli: ricominciare

    @Test
    void ricominciaRiportaAlLivello1ConTutteLeVite() {
        Livelli l = new Livelli(5, 3, Lato.SINISTRA);
        vinci(l, 2);
        perdi(l, 1);
        l.ricomincia();
        assertEquals(1, l.getLivello());
        assertEquals(3, l.getVite());
        assertFalse(l.isFinito());
    }

    @Test
    void ricominciaDopoUnGiocoCompletatoOPerso() {
        Livelli vinto = new Livelli(2, 3, Lato.SINISTRA);
        vinci(vinto, 2);
        vinto.ricomincia();
        assertFalse(vinto.isCompletato());
        vinto.partitaFinita(Lato.SINISTRA);
        assertEquals(2, vinto.getLivello());

        Livelli perso = new Livelli(2, 2, Lato.SINISTRA);
        perdi(perso, 2);
        perso.ricomincia();
        assertFalse(perso.isPerso());
        assertEquals(2, perso.getVite());
    }

    // ---- attrezzi per i livelli

    private static void vinci(Livelli l, int volte) {
        for (int i = 0; i < volte; i++) l.partitaFinita(l.getUmano());
    }

    private static void perdi(Livelli l, int volte) {
        Lato altro = l.getUmano() == Lato.SINISTRA ? Lato.DESTRA : Lato.SINISTRA;
        for (int i = 0; i < volte; i++) l.partitaFinita(altro);
    }
}
