package unogame.clases;

import java.util.ArrayList;

/**
 * Clase encargada de administrar el flujo de turnos en el juego UNO.
 * Controla qué jugador tiene el turno actual, quién será el siguiente
 * y la dirección de juego (horario o inverso).
 * 
 * Utiliza una lista de {@link Jugador} para gestionar el orden de participación.
 * 
 * @author Sebastian, Acxel, Mariana, Joshua, Omar
 * @version 1.0
 */
public class TurnoManager {

    /** Lista de jugadores en el juego. */
    private ArrayList<Jugador> jugadores;
    /** Índice del jugador que tiene el turno actual. */
    private int turnoActual;
    /** Dirección del turno: 1 = horario, -1 = inverso. */
    private int direccion;

    /**
     * Constructor de la clase TurnoManager.
     * 
     * @param jugadores Lista de jugadores participantes.
     */
    public TurnoManager(ArrayList<Jugador> jugadores) {
        this.jugadores   = jugadores;
        this.turnoActual = 0;
        this.direccion   = 1;
    }

    /**
     * Obtiene el jugador que tiene el turno actual.
     * 
     * @return Jugador en turno.
     */
    public Jugador getJugadorActual() {
        return jugadores.get(turnoActual);
    }

    /**
     * Obtiene el siguiente jugador según la dirección actual.
     * 
     * @return Jugador siguiente en turno.
     */
    public Jugador getSiguienteJugador() {
        return jugadores.get(calcularSiguiente());
    }

    /** Avanza al siguiente jugador según la dirección actual. */
    public void avanzar() {
        turnoActual = calcularSiguiente();
    }

    /** Invierte la dirección del turno (horario ↔ inverso). */
    public void invertirDireccion() {
        direccion *= -1;
    }

    /**
     * Devuelve la dirección actual del turno.
     * 
     * @return 1 si es horario, -1 si es inverso.
     */
    public int getDireccion() {
        return direccion;
    }

    /**
     * Calcula el índice del siguiente jugador según la dirección.
     * 
     * @return Índice del siguiente jugador.
     */
    private int calcularSiguiente() {
        return ((turnoActual + direccion) % jugadores.size()
                + jugadores.size()) % jugadores.size();
    }
}
