package unogame.clases;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * Clase principal que gestiona el flujo del juego UNO.
 * Se encarga de inicializar jugadores, repartir cartas,
 * controlar turnos y aplicar efectos de las cartas especiales.
 * 
 * Utiliza las clases {@link Baraja}, {@link Jugador},
 * {@link TurnoManager}, {@link EfectoCarta} y {@link Consola}
 * para coordinar la lógica del juego.
 * 
 * @author Sebastian, Acxel, Mariana, Joshua, Omar
 * @version 1.0
 */
public class Juego {

    /** Baraja de cartas utilizada en el juego. */
    private Baraja baraja;
    /** Lista de jugadores participantes. */
    private ArrayList<Jugador> jugadores;
    /** Carta actualmente en la mesa. */
    private Carta cartaMesa;
    /** Administrador de turnos. */
    private TurnoManager turnoManager;
    /** Gestor de efectos de cartas especiales. */
    private EfectoCarta efectoCarta;
    /** Scanner para entrada de datos desde consola. */
    private Scanner scanner;

    /**
     * Constructor de la clase Juego.
     * Inicializa la baraja y el lector de entrada.
     */
    public Juego() {
        baraja  = new Baraja();
        scanner = new Scanner(System.in);
    }

    /**
     * Inicia el juego UNO.
     * Registra jugadores, reparte cartas, selecciona carta inicial
     * y controla el ciclo de turnos hasta que un jugador gane.
     */
    public void iniciar() {
        jugadores    = RegistroJugadores.registrarConConsola(scanner);
        turnoManager = new TurnoManager(jugadores);
        efectoCarta  = new EfectoCarta(turnoManager, baraja);

        Consola.animarRepartiendo();
        repartirCartas();

        // Carta inicial: no puede ser comodín
        cartaMesa = baraja.robarCarta();
        int intentos = 0;
        while (cartaMesa != null && cartaMesa.esComodin()) {
            baraja.barajear();
            cartaMesa = baraja.robarCarta();
            if (++intentos > 20) break;
        }

        mostrarEncabezado();

        while (true) {
            Jugador actual = turnoManager.getJugadorActual();
            mostrarEstadoGlobal(actual);

            boolean gano = ejecutarTurno(actual);

            if (gano) {
                Consola.linea();
                Consola.animarVictoria(actual.getNombre());
                Consola.linea();
                break;
            }

            turnoManager.avanzar();
        }
    }

    /** Reparte 7 cartas iniciales a cada jugador. */
    private void repartirCartas() {
        for (int i = 0; i < 7; i++) {
            for (Jugador j : jugadores) {
                j.robarCarta(baraja);
            }
        }
    }

    /**
     * Ejecuta el turno de un jugador.
     * 
     * @param jugador Jugador que realiza la jugada.
     * @return {@code true} si el jugador ganó, {@code false} en caso contrario.
     */
    private boolean ejecutarTurno(Jugador jugador) {
        Carta jugada;

        if (jugador.esHumano()) {
            jugada = jugador.decidirJugadaHumano(cartaMesa, baraja, scanner);
        } else {
            jugada = jugador.decidirJugadaIA(
                    cartaMesa, baraja,
                    new String[]{"rojo", "azul", "verde", "amarillo"});
            if (jugada == null) {
                System.out.println(Consola.AMARILLO + "  " + jugador.getNombre()
                        + " no tiene jugada válida y pasa turno." + Consola.RESET);
            } else {
                System.out.println(Consola.VERDE + Consola.NEGRITA
                        + "  " + jugador.getNombre() + " juega: "
                        + Consola.RESET + jugada);
            }
        }

        if (jugada != null) {
            cartaMesa = jugada;

            int saltosExtra = efectoCarta.aplicar(jugada);
            for (int i = 0; i < saltosExtra; i++) {
                turnoManager.avanzar();
            }

            verificarUno(jugador);
        }

        return jugador.getMano().estaVacia();
    }

    /**
     * Verifica si un jugador debe declarar "UNO" al quedarse con una carta.
     * 
     * @param jugador Jugador que debe declarar UNO.
     */
    private void verificarUno(Jugador jugador) {
        if (jugador.getMano().estaVacia()) return;

        if (jugador.getMano().size() == 1) {
            if (jugador.esHumano()) {
                System.out.print(Consola.AMARILLO + Consola.NEGRITA
                        + "  ¡Tienes 1 carta! Escribe UNO para declararlo: "
                        + Consola.RESET);
                String entrada = scanner.nextLine().trim();
                if (!entrada.equalsIgnoreCase("UNO")) {
                    System.out.println(Consola.ROJO
                            + "  No declaraste UNO. Robas 2 cartas." + Consola.RESET);
                    jugador.robarCarta(baraja);
                    jugador.robarCarta(baraja);
                } else {
                    Consola.animarEfecto("UNO!", Consola.AMARILLO);
                }
            } else {
                Consola.animarEfecto(jugador.getNombre() + " dice: ¡UNO!", Consola.AMARILLO);
            }
        }
    }

    /**
     * Muestra el estado global del juego en consola.
     * 
     * @param actual Jugador que tiene el turno actual.
     */
    private void mostrarEstadoGlobal(Jugador actual) {
        Consola.linea();
        int    dir    = turnoManager.getDireccion();
        String dirStr = dir == 1
                ? Consola.VERDE   + "→ horario"
                : Consola.MAGENTA + "← inverso";
        System.out.println("  " + Consola.NEGRITA + Consola.CIAN + "TURNO: "
                + Consola.AMARILLO + actual.getNombre()
                + Consola.RESET + "   " + dirStr + Consola.RESET);
        System.out.println("  " + Consola.NEGRITA + "Mesa: " + Consola.RESET + cartaMesa);
        Consola.lineaDelgada();
        System.out.println(Consola.DIM + "  Cartas:" + Consola.RESET);
        for (int i = 0; i < jugadores.size(); i++) {
            Jugador j      = jugadores.get(i);
            boolean esTurno = j == actual;
            String marker  = esTurno
                    ? Consola.AMARILLO + Consola.NEGRITA + " ► "
                    : "   ";
            System.out.println(marker + Consola.colorNombre(i) + j.getNombre()
                    + Consola.RESET + Consola.DIM
                    + " (" + j.getMano().size() + ")" + Consola.RESET);
        }
        Consola.linea();
    }

    /** Muestra el encabezado inicial del juego en consola. */
    private void mostrarEncabezado() {
        Consola.titulo("U  N  O");
        System.out.println(Consola.NEGRITA + "  Jugadores:" + Consola.RESET);
        for (int i = 0; i < jugadores.size(); i++) {
            Jugador j = jugadores.get(i);
            System.out.println(Consola.colorNombre(i) + "    " + j.getNombre()
                    + Consola.RESET + Consola.DIM
                    + (j.esHumano() ? "  (Tú)" : "  (IA)") + Consola.RESET);
        }
        System.out.println(Consola.NEGRITA + "\n  Carta inicial en mesa: "
                + Consola.RESET + cartaMesa);
        Consola.linea();
    }
}
