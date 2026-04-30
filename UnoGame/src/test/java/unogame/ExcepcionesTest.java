package unogame;

import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.Test;

import unogame.clases.Baraja;
import unogame.clases.Mano;

public class ExcepcionesTest {
    @Test
    public void testRobarDeBarajaVacia() {
        Baraja baraja = new Baraja();
        
        // Vaciamos la baraja manualmente
        while (baraja.cartasRestantes() > 0) {
            baraja.robarCarta();
        }

        // Según tu código, si está vacía devuelve null[cite: 2]
        assertNull(baraja.robarCarta(), "Debería retornar null al robar de una baraja vacía");
    }

    @Test
    public void testJugarCartaFueraDeIndice() {
        Mano mano = new Mano();
        // Intentamos jugar en un índice que no existe[cite: 8]
        assertNull(mano.jugarCarta(99), "No debería permitir jugar un índice inexistente");
    }
}
