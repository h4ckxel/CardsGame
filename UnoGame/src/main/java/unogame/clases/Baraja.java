package unogame.clases;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Representa una baraja de cartas para el juego.
 * Contiene métodos para crear, mezclar y administrar las cartas.
 * 
 * La baraja incluye cartas numéricas, cartas especiales (Salto, Reversa, Roba Dos)
 * y comodines (Comodín, Roba Cuatro).
 * 
 * @author Sebastian, Acxel, Mariana, Joshua, Omar 
 * @version 1.0
 */
public class Baraja {

    /** Lista que almacena todas las cartas de la baraja. */
    private ArrayList<Carta> cartas;

    /**
     * Constructor de la clase Baraja.
     * Inicializa la lista de cartas, crea la baraja y la mezcla.
     */
    public Baraja() {
        cartas = new ArrayList<>();
        crearBaraja();
        barajear();
    }

    /**
     * Crea todas las cartas de la baraja según las reglas del juego.
     * Se generan cartas numéricas, especiales y comodines.
     */
    private void crearBaraja() {
        String[] colores = {"rojo", "azul", "verde", "amarillo"};

        for (String color : colores) {
            cartas.add(new Carta(color, 0));

            for (int i = 1; i <= 9; i++) {
                cartas.add(new Carta(color, i));
                cartas.add(new Carta(color, i));
            }

            cartas.add(new Carta(color, Carta.SALTO));
            cartas.add(new Carta(color, Carta.SALTO));
            cartas.add(new Carta(color, Carta.REVERSA));
            cartas.add(new Carta(color, Carta.REVERSA));
            cartas.add(new Carta(color, Carta.ROBA_DOS));
            cartas.add(new Carta(color, Carta.ROBA_DOS));
        }

        for (int i = 0; i < 4; i++) {
            cartas.add(new Carta("comodin", Carta.COMODIN));
            cartas.add(new Carta("comodin", Carta.ROBA_CUATRO));
        }
    }

    /**
     * Mezcla aleatoriamente las cartas de la baraja.
     */
    public void barajear() {
        Collections.shuffle(cartas);
    }

    /**
     * Roba una carta de la baraja.
     * 
     * @return La carta robada, o {@code null} si la baraja está vacía.
     */
    public Carta robarCarta() {
        if (cartas.isEmpty()) return null;
        return cartas.remove(0);
    }

    /**
     * Devuelve el número de cartas restantes en la baraja.
     * 
     * @return Cantidad de cartas disponibles.
     */
    public int cartasRestantes() {
        return cartas.size();
    }
}
