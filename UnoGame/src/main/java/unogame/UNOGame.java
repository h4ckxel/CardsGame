package unogame;

import unogame.clases.Juego;

/**
 * Clase principal para ejecutar el juego UNO.
 * 
 * Contiene el método {@code main} que inicializa una instancia de
 * {@link Juego} y comienza la partida.
 * 
 * @author Sebastian, Acxel, Mariana, Joshua, Omar
 * @version 1.0
 */
public class UNOGame {

    /**
     * Punto de entrada del programa.
     * 
     * @param args Argumentos de línea de comandos (no utilizados).
     */
    public static void main(String[] args) {
        Juego juego = new Juego();
        juego.iniciar();
    }
}
