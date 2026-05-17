package unogame.ui;

import java.awt.Image;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import javax.swing.ImageIcon;
import unogame.clases.Carta;

/**
 * Carga las imágenes de cartas usadas por la interfaz Swing.
 */
public final class CartaImagenes {

    private static final int ANCHO = 92;
    private static final int ALTO = 138;
    private static final Map<String, ImageIcon> CACHE = new HashMap<>();

    private CartaImagenes() {
    }

    /**
     * Devuelve la imagen correspondiente a una carta.
     *
     * @param carta Carta solicitada.
     * @return Icono escalado o {@code null} si no existe el recurso.
     */
    public static ImageIcon obtener(Carta carta) {
        return cargar(nombreArchivo(carta));
    }

    /**
     * Devuelve el reverso de una carta.
     *
     * @return Icono escalado del reverso o {@code null} si no existe.
     */
    public static ImageIcon obtenerReverso() {
        return cargar("reverso.png");
    }

    private static ImageIcon cargar(String archivo) {
        if (archivo == null) return null;
        if (CACHE.containsKey(archivo)) return CACHE.get(archivo);

        URL recurso = CartaImagenes.class.getResource("/cartas/" + archivo);
        if (recurso == null) {
            CACHE.put(archivo, null);
            return null;
        }

        ImageIcon original = new ImageIcon(recurso);
        Image escalada = original.getImage().getScaledInstance(
                ANCHO, ALTO, Image.SCALE_SMOOTH);
        ImageIcon icono = new ImageIcon(escalada);
        CACHE.put(archivo, icono);
        return icono;
    }

    private static String nombreArchivo(Carta carta) {
        if (carta == null) return null;

        if (carta.getValor() == Carta.COMODIN) {
            return "wild-card-clipart-lg.png";
        }
        if (carta.getValor() == Carta.ROBA_CUATRO) {
            return "wild-draw-four-card-clipart-lg.png";
        }

        String prefijo = traducirColor(carta.getColor());
        if (prefijo == null) return null;

        switch (carta.getValor()) {
            case Carta.SALTO:
                return prefijo + "-skip-card-clipart-lg.png";
            case Carta.REVERSA:
                return prefijo + "-reverse-card-clipart-lg.png";
            case Carta.ROBA_DOS:
                return prefijo + "-draw-two-card-clipart-lg.png";
            default:
                return prefijo + "-" + carta.getValor() + "-card-clipart-lg.png";
        }
    }

    private static String traducirColor(String color) {
        if (color == null) return null;

        switch (color.toLowerCase()) {
            case "rojo":
                return "red";
            case "azul":
                return "blue";
            case "verde":
                return "green";
            case "amarillo":
                return "yellow";
            default:
                return null;
        }
    }
}
