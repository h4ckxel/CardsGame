package unogame.clases;

import java.util.Scanner;

/**
 * Representa a un jugador dentro del juego.
 * 
 * Un jugador puede ser humano o controlado por la IA. 
 * Contiene su nombre, la mano de cartas y métodos para 
 * decidir jugadas según las reglas del juego.
 * 
 * @author Omar
 * @version 1.0
 */
public class Jugador {

    /** Nombre del jugador. */
    private String  nombre;
    /** Mano de cartas del jugador. */
    private Mano    mano;
    /** Indica si el jugador es humano o IA. */
    private boolean esHumano;

    /**
     * Constructor de la clase Jugador.
     *
     * @param nombre   Nombre del jugador.
     * @param esHumano true si el jugador es humano, false si es IA.
     */
    public Jugador(String nombre, boolean esHumano) {
        this.nombre   = nombre;
        this.esHumano = esHumano;
        this.mano     = new Mano();
    }

    /** @return Nombre del jugador. */
    public String  getNombre()  { return nombre; }
    /** @return Mano de cartas del jugador. */
    public Mano    getMano()    { return mano; }
    /** @return true si el jugador es humano. */
    public boolean esHumano()   { return esHumano; }

    /**
     * Permite al jugador robar una carta de la baraja.
     *
     * @param baraja Baraja de la cual se roba la carta.
     */
    public void robarCarta(Baraja baraja) {
        Carta c = baraja.robarCarta();
        if (c != null) mano.agregarCarta(c);
    }

    /**
     * Verifica si el jugador tiene alguna jugada válida.
     *
     * @param cartaMesa Carta que está en la mesa.
     * @return true si existe jugada válida, false en caso contrario.
     */
    public boolean tieneJugadaValida(Carta cartaMesa) {
        return mano.tieneJugadaValida(cartaMesa);
    }

    // ── IA ────────────────────────────────────────────────────────────────

    /**
     * Decide la jugada de un jugador controlado por IA.
     *
     * @param cartaMesa      Carta que está en la mesa.
     * @param baraja         Baraja del juego.
     * @param coloresValidos Colores válidos para comodines.
     * @return Carta jugada por la IA o null si no hay jugada.
     */
    public Carta decidirJugadaIA(Carta cartaMesa, Baraja baraja,
                                  String[] coloresValidos) {
        Consola.animarPensando(nombre);

        int indice = mano.primerIndiceValido(cartaMesa);

        if (indice == -1) {
            Consola.animarRobando(nombre, 1);
            robarCarta(baraja);
            indice = mano.primerIndiceValido(cartaMesa);
            if (indice == -1) return null; // sin jugada tras robar
        }

        Carta jugada = mano.jugarCarta(indice);

        if (jugada.esComodin()) {
            jugada.setColorActivo(elegirColorIA());
        }

        return jugada;
    }

    /**
     * IA: elige el color más frecuente en la mano.
     *
     * @return Color elegido por la IA.
     */
    private String elegirColorIA() {
        int[]    conteo  = new int[4];
        String[] colores = {"rojo", "azul", "verde", "amarillo"};

        for (int i = 0; i < mano.size(); i++) {
            Carta c = mano.getCarta(i);
            for (int j = 0; j < colores.length; j++) {
                if (c.getColor().equals(colores[j])) conteo[j]++;
            }
        }

        int maxIdx = 0;
        for (int i = 1; i < conteo.length; i++) {
            if (conteo[i] > conteo[maxIdx]) maxIdx = i;
        }

        return colores[maxIdx];
    }

    // ── Humano ────────────────────────────────────────────────────────────

    /**
     * Permite al jugador humano decidir su jugada.
     *
     * @param cartaMesa Carta que está en la mesa.
     * @param baraja    Baraja del juego.
     * @param scanner   Scanner para leer la entrada del usuario.
     * @return Carta jugada por el humano o null si pasa turno.
     */
    public Carta decidirJugadaHumano(Carta cartaMesa, Baraja baraja,
                                      Scanner scanner) {
        System.out.println("\n  " + Consola.CIAN + Consola.NEGRITA
                + "Carta en mesa: " + Consola.RESET + cartaMesa);
        mano.mostrarMano();

        if (!mano.tieneJugadaValida(cartaMesa)) {
            System.out.println(Consola.AMARILLO
                    + "  No tienes jugada válida. Robas una carta automáticamente."
                    + Consola.RESET);
            Consola.animarRobando(nombre, 1);
            robarCarta(baraja);

            int idx = mano.primerIndiceValido(cartaMesa);
            if (idx == -1) {
                System.out.println(Consola.AMARILLO
                        + "  La carta robada tampoco es jugable. Pierdes turno."
                        + Consola.RESET);
                return null;
            }

            System.out.println(Consola.VERDE
                    + "  ¡La carta robada es jugable! Puedes jugarla o pasar turno."
                    + Consola.RESET);
            mano.mostrarMano();

            while (true) {
                System.out.print(Consola.BLANCO + Consola.NEGRITA
                        + "  Elige índice para jugarla o -1 para pasar turno: "
                        + Consola.RESET);
                int opcion;
                try {
                    opcion = Integer.parseInt(scanner.nextLine().trim());
                } catch (NumberFormatException e) {
                    System.out.println(Consola.ROJO
                            + "  Entrada inválida. Escribe un número." + Consola.RESET);
                    continue;
                }

                if (opcion == -1) return null;

                Carta vista = mano.getCarta(opcion);
                if (vista == null || !vista.esJugableSobre(cartaMesa)) {
                    System.out.println(Consola.ROJO
                            + "  Índice inválido o carta no jugable. Intenta de nuevo."
                            + Consola.RESET);
                    continue;
                }

                Carta seleccionada = mano.jugarCarta(opcion);
                if (seleccionada.esComodin()) {
                    seleccionada.setColorActivo(pedirColor(scanner));
                }
                return seleccionada;
            }
        }

        while (true) {
            System.out.print(Consola.BLANCO + Consola.NEGRITA
                    + "  Elige carta (índice): " + Consola.RESET);
            int opcion;
            try {
                opcion = Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println(Consola.ROJO
                        + "  Entrada inválida. Escribe un número." + Consola.RESET);
                continue;
            }

            // peek antes de pop
            Carta vista = mano.getCarta(opcion);
            if (vista == null) {
                System.out.println(Consola.ROJO
                        + "  Índice inválido. Intenta de nuevo." + Consola.RESET);
                continue;
            }

            if (!vista.esJugableSobre(cartaMesa)) {
                System.out.println(Consola.ROJO
                        + "  Esa carta no se puede jugar sobre la mesa. Elige otra."
                        + Consola.RESET);
                continue;
            }

            Carta seleccionada = mano.jugarCarta(opcion);
            if (seleccionada.esComodin()) {
                seleccionada.setColorActivo(pedirColor(scanner));
            }
            return seleccionada;
        }
    }

    /**
     * Solicita al jugador humano elegir un color para un comodín.
     *
     * @param scanner Scanner para leer la entrada del usuario.
     * @return Color elegido por el jugador.
     */
    private String pedirColor(Scanner scanner) {
        String[] validos = {"rojo", "azul", "verde", "amarillo"};
        while (true) {
            System.out.print(Consola.MAGENTA + Consola.NEGRITA
                    + "  Elige color ("
                    + Consola.ROJO     + "rojo"     + Consola.MAGENTA + "/"
                    + Consola.AZUL     + "azul"     + Consola.MAGENTA + "/"
                    + Consola.VERDE    + "verde"    + Consola.MAGENTA + "/"
                    + Consola.AMARILLO + "amarillo" + Consola.MAGENTA
                    + "): " + Consola.RESET);
            String entrada = scanner.nextLine().trim().toLowerCase();
            for (String v : validos) {
                if (v.equals(entrada)) return entrada;
            }
            System.out.println(Consola.ROJO
                    + "  Color inválido. Intenta de nuevo." + Consola.RESET);
        }
    }
}
