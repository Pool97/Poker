package server.model;

/**
 * Le possibili azioni previste per un giocatore nel Poker nell'arco di una puntata. Sarebbe logicamente errato legare una singola puntata al'interno turno
 * poichè è possibile che nell'arco di un turno ci siano puntate multiple.
 *
 * In realtà esistono possibili combinazioni di queste azioni (come ad esempio 3-BET, RE-RAISE o l'ALL-IN che è un caso particolare di RAISE)
 * ma potrebbero essere aggiunte via codice sfruttando le elementari azioni presenti nella Enum.
 *
 * @author Roberto Poletti
 * @author Nipuna Perera
 */

public enum ActionType {
    SB, BB, CALL, RAISE, FOLD, CHECK, ALL_IN
}
