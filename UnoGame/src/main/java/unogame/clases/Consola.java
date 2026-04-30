package unogame.clases;

/**
 * Clase utilitaria para manejar la salida en consola con colores,
 * estilos de texto y animaciones relacionadas con el juego.
 * 
 * Proporciona métodos para mostrar efectos visuales como animaciones
 * de "pensando", "robando cartas", "repartiendo", "victoria", así como
 * líneas decorativas y títulos estilizados.
 * 
 * @author Sebastian, Acxel, Mariana, Joshua, Omar
 * @version 1.0
 */
public class Consola {

    /** Código ANSI para resetear estilos. */
    public static final String RESET     = "\u001B[0m";
    /** Código ANSI para texto en negrita. */
    public static final String NEGRITA   = "\u001B[1m";
    /** Código ANSI para texto tenue. */
    public static final String DIM       = "\u001B[2m";

    /** Color rojo en consola. */
    public static final String ROJO      = "\u001B[31m";
    /** Color verde en consola. */
    public static final String VERDE     = "\u001B[32m";
    /** Color amarillo en consola. */
    public static final String AMARILLO  = "\u001B[33m";
    /** Color azul en consola. */
    public static final String AZUL      = "\u001B[34m";
    /** Color magenta en consola. */
    public static final String MAGENTA   = "\u001B[35m";
    /** Color cian en consola. */
    public static final String CIAN      = "\u001B[36m";
    /** Color blanco en consola. */
    public static final String BLANCO    = "\u001B[37m";

    /** Fondo rojo en consola. */
    public static final String BG_ROJO   = "\u001B[41m";
    /** Fondo verde en consola. */
    public static final String BG_VERDE  = "\u001B[42m";
    /** Fondo amarillo en consola. */
    public static final String BG_AMARILLO = "\u001B[43m";
    /** Fondo azul en consola. */
    public static final String BG_AZUL   = "\u001B[44m";
    /** Fondo magenta en consola. */
    public static final String BG_MAGENTA= "\u001B[45m";

    /**
     * Devuelve el color correspondiente a una carta.
     *
     * @param color Nombre del color de la carta.
     * @return Código ANSI con el color y estilo aplicado.
     */
    public static String colorCarta(String color) {
        switch (color.toLowerCase()) {
            case "rojo":     return ROJO + NEGRITA;
            case "azul":     return AZUL + NEGRITA;
            case "verde":    return VERDE + NEGRITA;
            case "amarillo": return AMARILLO + NEGRITA;
            case "comodin":  return MAGENTA + NEGRITA;
            default:         return BLANCO;
        }
    }

    /**
     * Devuelve un color para mostrar nombres de jugadores.
     *
     * @param indice Índice del jugador.
     * @return Código ANSI con el color y estilo aplicado.
     */
    public static String colorNombre(int indice) {
        switch (indice % 4) {
            case 0: return CIAN + NEGRITA;
            case 1: return AMARILLO + NEGRITA;
            case 2: return MAGENTA + NEGRITA;
            case 3: return VERDE + NEGRITA;
            default: return BLANCO;
        }
    }

    /** Limpia la línea actual en consola. */
    public static void limpiarLinea() {
        System.out.print("\r\u001B[K");
    }

    /**
     * Pausa la ejecución por un tiempo determinado.
     *
     * @param ms Milisegundos a esperar.
     */
    public static void sleep(long ms) {
        try { Thread.sleep(ms); } catch (InterruptedException ignored) {}
    }

    /**
     * Muestra una animación indicando que un jugador está pensando.
     *
     * @param nombre Nombre del jugador.
     */
    public static void animarPensando(String nombre) {
        String[] frames = { "⠋", "⠙", "⠹", "⠸", "⠼", "⠴", "⠦", "⠧", "⠇", "⠏" };
        System.out.print(AMARILLO);
        for (int i = 0; i < 14; i++) {
            System.out.print("\r  " + frames[i % frames.length] + "  " + nombre + " está pensando...");
            System.out.flush();
            sleep(90);
        }
        limpiarLinea();
        System.out.print(RESET);
    }

    /**
     * Muestra una animación indicando que un jugador roba cartas.
     *
     * @param nombre Nombre del jugador.
     * @param cantidad Número de cartas robadas.
     */
    public static void animarRobando(String nombre, int cantidad) {
        String[] frames = { "▱▱▱", "▰▱▱", "▰▰▱", "▰▰▰" };
        System.out.print(CIAN);
        for (int rep = 0; rep < 3; rep++) {
            for (String f : frames) {
                System.out.print("\r  " + f + "  " + nombre + " roba " + cantidad + " carta(s)...");
                System.out.flush();
                sleep(100);
            }
        }
        limpiarLinea();
        System.out.print(RESET);
    }

    /**
     * Muestra un efecto animado con texto resaltado.
     *
     * @param texto Texto del efecto.
     * @param color Color ANSI aplicado.
     */
    public static void animarEfecto(String texto, String color) {
        System.out.print(color + NEGRITA);
        for (int i = 0; i < 3; i++) {
            System.out.print("\r  >>> " + texto + " <<<");
            System.out.flush();
            sleep(180);
            System.out.print("\r       " + texto + "    ");
            System.out.flush();
            sleep(180);
        }
        System.out.println("\r  >>> " + texto + " <<<" + RESET);
    }

    /** Muestra animación de barajar y repartir cartas. */
    public static void animarRepartiendo() {
        System.out.print(VERDE + NEGRITA);
        String[] pasos = {
            "  Barajando cartas   ",
            "  Barajando cartas.  ",
            "  Barajando cartas.. ",
            "  Barajando cartas..."
        };
        for (int i = 0; i < 12; i++) {
            System.out.print("\r" + pasos[i % pasos.length]);
            System.out.flush();
            sleep(120);
        }
        System.out.println(RESET);

        System.out.print(CIAN + NEGRITA);
        String[] pasos2 = {
            "  Repartiendo cartas   ",
            "  Repartiendo cartas.  ",
            "  Repartiendo cartas.. ",
            "  Repartiendo cartas..."
        };
        for (int i = 0; i < 16; i++) {
            System.out.print("\r" + pasos2[i % pasos2.length]);
            System.out.flush();
            sleep(100);
        }
        System.out.println(RESET);
    }

    /**
     * Muestra animación de victoria para un jugador.
     *
     * @param nombre Nombre del jugador ganador.
     */
    public static void animarVictoria(String nombre) {
        String[] trofeos = {"🏆", "✨", "🎉", "👑", "🎊"};
        sleep(200);
        for (int i = 0; i < 5; i++) {
            System.out.print("\r  " + trofeos[i % trofeos.length]
                    + "  " + AMARILLO + NEGRITA + "¡" + nombre + " ha ganado!" + RESET
                    + "  " + trofeos[(i + 2) % trofeos.length]);
            System.out.flush();
            sleep(250);
        }
        System.out.println();
    }

    /** Imprime una línea gruesa decorativa en consola. */
    public static void linea() {
        System.out.println(DIM + "  ════════════════════════════════════════════════" + RESET);
    }

    /** Imprime una línea delgada decorativa en consola. */
    public static void lineaDelgada() {
        System.out.println(DIM + "  ────────────────────────────────────────────────" + RESET);
    }

    /**
     * Imprime un título centrado con estilo.
     *
     * @param texto Texto del título.
     */
    public static void titulo(String texto) {
        linea();
        int pad = (48 - texto.length()) / 2;
        StringBuilder sb = new StringBuilder("  ");
        for (int i = 0; i < pad; i++) sb.append(' ');
        System.out.println(NEGRITA + CIAN + sb + texto + RESET);
        linea();
    }
}
