package unogame;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

import unogame.clases.Jugador;
import unogame.clases.RegistroJugadores;
import unogame.clases.TurnoManager;

public class TurnosTest {
    
    @Test
    public void testFlujoCircularTurnos() {
        ArrayList<Jugador> jugadores = RegistroJugadores.registrarDirecto("J1", "J2", "J3");
        TurnoManager manager = new TurnoManager(jugadores);

        // Turno inicial es J1[cite: 10]
        assertEquals("J1", manager.getJugadorActual().getNombre());

        manager.avanzar(); // J2
        manager.avanzar(); // J3
        manager.avanzar(); // Debería volver a J1

        assertEquals("J1", manager.getJugadorActual().getNombre(), "El turno debió volver al primer jugador");
    }
}
