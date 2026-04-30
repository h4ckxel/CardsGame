package unogame;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

import unogame.clases.Carta;

public class CartaTest {
    @Test
    public void testGetColor() {
        Carta carta = new Carta("Rojo", 7);

        String resultado = carta.getColor();

        assertEquals("Rojo", resultado, "El color de la carta debería ser Rojo");
    }
}
