package unogame.clases;
/**
 * Representa una carta dentro del juego.
 * Cada carta tiene un color, un valor y puede ser especial o comodín.
 * 
 * Incluye métodos para determinar si una carta es jugable sobre otra,
 * así como para mostrar su representación en texto.
 * 
 * Constantes disponibles:
 * <ul>
 *   <li>{@link #SALTO}</li>
 *   <li>{@link #REVERSA}</li>
 *   <li>{@link #ROBA_DOS}</li>
 *   <li>{@link #COMODIN}</li>
 *   <li>{@link #ROBA_CUATRO}</li>
 * </ul>
 * 
 * @author Sebastian, Acxel, Mariana, Joshua, Omar
 * @version 1.0
 */
public class Carta {

    /** Constante que representa la carta de Salto. */
    public static final int SALTO       = 10;
    /** Constante que representa la carta de Reversa. */
    public static final int REVERSA     = 11;
    /** Constante que representa la carta de Roba Dos. */
    public static final int ROBA_DOS    = 12;
    /** Constante que representa la carta Comodín. */
    public static final int COMODIN     = 13;
    /** Constante que representa la carta Roba Cuatro. */
    public static final int ROBA_CUATRO = 14;

    /** Color de la carta (rojo, azul, verde, amarillo o comodín). */
    private String color;
    /** Valor de la carta (numérico o especial). */
    private int valor;
    /** Color activo de la carta (puede cambiar en comodines). */
    private String colorActivo;

    /**
     * Constructor de la clase Carta.
     * 
     * @param color Color de la carta.
     * @param valor Valor de la carta.
     */
    public Carta(String color, int valor) {
        this.color       = color;
        this.valor       = valor;
        this.colorActivo = color;
    }

    /**
     * Obtiene el color original de la carta.
     * @return Color de la carta.
     */
    public String getColor()       { return color; }

    /**
     * Obtiene el valor de la carta.
     * @return Valor de la carta.
     */
    public int getValor()          { return valor; }

    /**
     * Obtiene el color activo de la carta.
     * @return Color activo de la carta.
     */
    public String getColorActivo() { return colorActivo; }

    /**
     * Establece el color activo de la carta (usado en comodines).
     * @param colorActivo Nuevo color activo.
     */
    public void setColorActivo(String colorActivo) {
        this.colorActivo = colorActivo;
    }

    /**
     * Determina si la carta es especial (Salto, Reversa, Roba Dos).
     * @return {@code true} si es especial, {@code false} en caso contrario.
     */
    public boolean esEspecial() { return valor >= SALTO; }

    /**
     * Determina si la carta es un comodín.
     * @return {@code true} si es Comodín o Roba Cuatro.
     */
    public boolean esComodin() {
        return valor == COMODIN || valor == ROBA_CUATRO;
    }

    /**
     * Verifica si la carta puede jugarse sobre otra.
     * 
     * @param otra Carta sobre la cual se intenta jugar.
     * @return {@code true} si la carta es jugable, {@code false} en caso contrario.
     */
    public boolean esJugableSobre(Carta otra) {
        if (otra == null)              return true;
        if (esComodin())               return true;
        if (color.equals("comodin"))   return true;
        return this.color.equals(otra.getColorActivo()) || this.valor == otra.valor;
    }

    /**
     * Devuelve una representación textual de la carta para mostrar en pantalla.
     * @return Texto representando la carta.
     */
    public String toDisplayString() {
        if (esComodin())
            return "[ ★ " + nombreValor() + " → " + colorActivo.toUpperCase() + " ]";
        return "[ " + color.toUpperCase() + " " + nombreValor() + " ]";
    }

    /**
     * Devuelve una representación de la carta con colores en consola.
     * @return Texto coloreado representando la carta.
     */
    @Override
    public String toString() {
        String col = Consola.colorCarta(esComodin() ? colorActivo : color);
        return col + toDisplayString() + Consola.RESET;
    }

    /**
     * Devuelve el nombre textual del valor de la carta.
     * @return Nombre del valor (ej. "SALTO", "REVERSA", "ROBA2", etc.).
     */
    private String nombreValor() {
        switch (valor) {
            case SALTO:       return "SALTO";
            case REVERSA:     return "REVERSA";
            case ROBA_DOS:    return "ROBA2";
            case COMODIN:     return "COMODÍN";
            case ROBA_CUATRO: return "ROBA4";
            default:          return String.valueOf(valor);
        }
    }
}
