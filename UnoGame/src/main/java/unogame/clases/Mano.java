package unogame.clases;

import java.util.ArrayList;

/**
 * Representa la mano de cartas de un jugador en el juego UNO.
 * Permite agregar, jugar y consultar cartas, así como verificar
 * si existen jugadas válidas sobre la carta en mesa.
 * 
 * También incluye métodos para mostrar la mano en consola con
 * formato visual y para obtener índices de cartas jugables.
 * 
 * @author Sebastian, Acxel, Mariana, Joshua, Omar
 * @version 1.0
 */
public class Mano {

    /** Lista de cartas que componen la mano del jugador. */
    private ArrayList<Carta> cartas;

    /** Constructor que inicializa la mano vacía. */
    public Mano() {
        cartas = new ArrayList<>();
    }

    /**
     * Agrega una carta a la mano.
     * 
     * @param c Carta a agregar (si no es {@code null}).
     */
    public void agregarCarta(Carta c) {
        if (c != null) cartas.add(c);
    }

    /**
     * Juega una carta de la mano en el índice indicado.
     * 
     * @param indice Posición de la carta en la mano.
     * @return Carta jugada, o {@code null} si el índice es inválido.
     */
    public Carta jugarCarta(int indice) {
        if (indice < 0 || indice >= cartas.size()) return null;
        return cartas.remove(indice);
    }

    /**
     * Obtiene la carta en el índice indicado sin removerla.
     * 
     * @param indice Posición de la carta en la mano.
     * @return Carta en la posición, o {@code null} si el índice es inválido.
     */
    public Carta getCarta(int indice) {
        if (indice < 0 || indice >= cartas.size()) return null;
        return cartas.get(indice);
    }

    /**
     * Devuelve el número de cartas en la mano.
     * 
     * @return Cantidad de cartas.
     */
    public int size() {
        return cartas.size();
    }

    /**
     * Verifica si la mano está vacía.
     * 
     * @return {@code true} si no hay cartas, {@code false} en caso contrario.
     */
    public boolean estaVacia() {
        return cartas.isEmpty();
    }

    /** Muestra todas las cartas de la mano en consola con sus índices. */
    public void mostrarMano() {
        System.out.println(Consola.NEGRITA + Consola.BLANCO + "  Tus cartas:" + Consola.RESET);
        for (int i = 0; i < cartas.size(); i++) {
            System.out.println(Consola.DIM + "    " + i + Consola.RESET
                    + "  →  " + cartas.get(i));
        }
    }

    /**
     * Verifica si existe alguna carta jugable sobre la carta en mesa.
     * 
     * @param cartaMesa Carta actual en la mesa.
     * @return {@code true} si hay jugada válida, {@code false} en caso contrario.
     */
    public boolean tieneJugadaValida(Carta cartaMesa) {
        for (Carta c : cartas) {
            if (c.esJugableSobre(cartaMesa)) return true;
        }
        return false;
    }

    /**
     * Devuelve el índice de la primera carta jugable sobre la carta en mesa.
     * 
     * @param cartaMesa Carta actual en la mesa.
     * @return Índice de la carta jugable, o -1 si no existe.
     */
    public int primerIndiceValido(Carta cartaMesa) {
        for (int i = 0; i < cartas.size(); i++) {
            if (cartas.get(i).esJugableSobre(cartaMesa)) return i;
        }
        return -1;
    }
}
