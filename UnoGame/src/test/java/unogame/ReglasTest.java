package unogame;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import unogame.clases.Baraja;
import unogame.clases.Carta;
import unogame.clases.EfectoCarta;
import unogame.clases.Jugador;
import unogame.clases.RegistroJugadores;
import unogame.clases.TurnoManager;

public class ReglasTest {
    private Baraja baraja;
    private TurnoManager turnoManager;
    private EfectoCarta efecto;
    private ArrayList<Jugador> jugadores;

    @BeforeEach
    public void setup() {
        baraja = new Baraja();
        jugadores = RegistroJugadores.registrarDirecto("Humano", "IA1", "IA2");
        turnoManager = new TurnoManager(jugadores);
        efecto = new EfectoCarta(turnoManager, baraja);
    }

    @Test
    public void testEfectoRobaCuatro() {
        Carta roba4 = new Carta("comodin", Carta.ROBA_CUATRO);
        Jugador victima = turnoManager.getSiguienteJugador();
        int cartasAntes = victima.getMano().size();

        efecto.aplicar(roba4);

        // Verificamos que la víctima ahora tenga 4 cartas más[cite: 5, 7]
        assertEquals(cartasAntes + 4, victima.getMano().size(), "La víctima debió robar 4 cartas");
    }

    @Test
    public void testEfectoReversa() {
        int direccionInicial = turnoManager.getDireccion();
        Carta reversa = new Carta("rojo", Carta.REVERSA);

        efecto.aplicar(reversa);

        // La dirección debe multiplicarse por -1[cite: 5, 10]
        assertEquals(direccionInicial * -1, turnoManager.getDireccion(), "La dirección debió invertirse");
    }
}
