package server.model;


/**
 * Le possibili posizioni previste dal Poker.
 * In questo caso i giocatori massimi (al momento) previsti sono sei, quindi non è possibile inserire posizioni di MIDDLE tra UTG e HJ, anche
 * se presenti nel regolamento ufficiale.
 *
 * D = Dealer: il mazziere virtuale e colui che detiene il Dealer Button.
 * SB = Small Blind: colui che sta alla sinistra del Dealer.
 * BB = Big Blind: colui che sta alla sinistra dello Small Blind.
 * UTG = Under The Gun: colui che sta alla sinistra del Big Blind. È il primo a compiere un'azione subito dopo le puntate obbligatorie di SB e BB.
 * HJ = Hi-Jack
 * CO = Cut-Off: è il penultimo a parlare poichè sta alla destra del Dealer che chiude il cerchio.
 *
 * @author Roberto Poletti
 * @author Nipuna Perera
 */

public enum Position {
    D, SB, BB, UTG, HJ, CO
}
