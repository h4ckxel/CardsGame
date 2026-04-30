package unogame;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;

import unogame.clases.Baraja;
import unogame.clases.Carta;
import unogame.clases.Jugador;

public class IATest {
    
    @Test
    public void testIACambiaColorComodin() {
        Jugador bot = new Jugador("BotTest", false);
        Carta comodin = new Carta("comodin", Carta.COMODIN);
        bot.getMano().agregarCarta(comodin);
        
        // Forzamos a la IA a decidir[cite: 7]
        Carta jugada = bot.decidirJugadaIA(new Carta("rojo", 5), new Baraja(), new String[]{"rojo"});
        
        assertNotNull(jugada.getColorActivo(), "La IA debe asignar un color activo al comodín");
        assertNotEquals("comodin", jugada.getColorActivo(), "El color activo no puede seguir siendo 'comodin'");
    }
}
