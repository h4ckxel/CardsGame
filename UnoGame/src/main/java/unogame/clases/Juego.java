package unogame.clases;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

/**
 * Clase principal que gestiona el flujo del juego UNO.
 * Puede ejecutarse en consola o servir como motor para una interfaz gráfica.
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

    /** Eventos pendientes para interfaces gráficas. */
    private final ArrayList<String> eventos;
    /** Estado final de la partida. */
    private boolean partidaTerminada;
    /** Ganador de la partida cuando existe. */
    private Jugador ganador;
    /** Indica si el humano debe robar antes de poder actuar. */
    private boolean humanoDebeRobar;
    /** Indica si el humano puede pasar turno tras robar una carta jugable. */
    private boolean humanoPuedePasar;

    /**
     * Constructor de la clase Juego.
     * Inicializa la baraja y el lector de entrada.
     */
    public Juego() {
        baraja  = new Baraja();
        scanner = new Scanner(System.in);
        eventos = new ArrayList<>();
    }

    /**
     * Inicia la versión clásica por consola.
     */
    public void iniciar() {
        prepararPartida(RegistroJugadores.registrarConConsola(scanner), false);

        Consola.animarRepartiendo();
        mostrarEncabezado();

        while (true) {
            Jugador actual = turnoManager.getJugadorActual();
            Consola.animarTurno(actual.getNombre());
            mostrarEstadoGlobal(actual);

            boolean gano = ejecutarTurnoConsola(actual);

            if (gano) {
                Consola.linea();
                Consola.animarVictoria(actual.getNombre());
                Consola.linea();
                break;
            }

            turnoManager.avanzar();
        }
    }

    /**
     * Inicia una partida preparada para ser controlada desde Swing.
     *
     * @param nombreHumano Nombre del jugador humano.
     * @param nombresIA Nombres de los rivales controlados por IA.
     */
    public void iniciarPartidaGUI(String nombreHumano, String... nombresIA) {
        prepararPartida(RegistroJugadores.registrarDirecto(nombreHumano, nombresIA), true);
        registrarEvento("Partida iniciada.");
        registrarEvento("Carta inicial: " + cartaMesa.toDisplayString());
    }

    /**
     * Inicializa el estado común de una partida.
     *
     * @param jugadoresRegistrados Jugadores que participarán.
     * @param modoGUI {@code true} si la partida será controlada por interfaz gráfica.
     */
    private void prepararPartida(ArrayList<Jugador> jugadoresRegistrados, boolean modoGUI) {
        baraja           = new Baraja();
        jugadores        = jugadoresRegistrados;
        turnoManager     = new TurnoManager(jugadores);
        efectoCarta      = modoGUI
                ? new EfectoCarta(turnoManager, baraja, this::registrarEvento)
                : new EfectoCarta(turnoManager, baraja);
        partidaTerminada = false;
        ganador          = null;
        humanoDebeRobar  = false;
        humanoPuedePasar = false;
        eventos.clear();

        repartirCartas();
        cartaMesa = robarCartaInicial();
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
     * Roba la carta inicial, evitando comodines como en el flujo original.
     *
     * @return Carta inicial de la mesa.
     */
    private Carta robarCartaInicial() {
        Carta inicial = baraja.robarCarta();
        int intentos = 0;
        while (inicial != null && inicial.esComodin()) {
            baraja.barajear();
            inicial = baraja.robarCarta();
            if (++intentos > 20) break;
        }
        return inicial;
    }

    /**
     * Ejecuta el turno de un jugador en modo consola.
     *
     * @param jugador Jugador que realiza la jugada.
     * @return {@code true} si el jugador ganó, {@code false} en caso contrario.
     */
    private boolean ejecutarTurnoConsola(Jugador jugador) {
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
            }
        }

        if (jugada != null) {
            Consola.animarJugada(jugador.getNombre(), jugada);
            cartaMesa = jugada;

            int saltosExtra = efectoCarta.aplicar(jugada);
            for (int i = 0; i < saltosExtra; i++) {
                turnoManager.avanzar();
            }

            verificarUnoConsola(jugador);
        }

        return jugador.getMano().estaVacia();
    }

    /**
     * Prepara el turno humano dentro del flujo gráfico.
     * Si no existen jugadas válidas, deja al jugador listo para robar manualmente.
     */
    public void prepararTurnoHumanoGUI() {
        if (partidaTerminada) return;

        Jugador actual = turnoManager.getJugadorActual();
        if (!actual.esHumano()) return;

        humanoDebeRobar = false;
        humanoPuedePasar = false;
        registrarEvento("Turno de " + actual.getNombre() + ".");

        if (!actual.tieneJugadaValida(cartaMesa)) {
            humanoDebeRobar = true;
            registrarEvento("No tienes jugada válida. Debes robar una carta.");
        }
    }

    /**
     * Hace que el jugador humano robe una carta cuando las reglas lo exigen.
     *
     * @return {@code true} si se robó correctamente.
     */
    public boolean robarCartaHumanoGUI() {
        if (partidaTerminada || !humanoDebeRobar) return false;

        Jugador actual = turnoManager.getJugadorActual();
        if (!actual.esHumano()) return false;

        actual.robarCarta(baraja);
        humanoDebeRobar = false;
        humanoPuedePasar = true;
        registrarEvento(actual.getNombre() + " roba 1 carta.");

        int indice = actual.getMano().primerIndiceValido(cartaMesa);
        if (indice == -1) {
            registrarEvento("La carta robada tampoco se puede jugar. Debes terminar turno.");
        } else {
            registrarEvento("La carta robada sí se puede jugar. Puedes usarla o pasar turno.");
        }

        return true;
    }

    /**
     * Juega una carta del jugador humano desde la interfaz gráfica.
     *
     * @param indice Índice de la carta en la mano.
     * @param colorComodin Color elegido si la carta es comodín.
     * @param declaroUno {@code true} si el jugador declaró UNO cuando correspondía.
     * @return {@code true} si la jugada fue válida.
     */
    public boolean jugarCartaHumanoGUI(int indice, String colorComodin, boolean declaroUno) {
        if (partidaTerminada) return false;

        Jugador actual = turnoManager.getJugadorActual();
        if (!actual.esHumano()) return false;

        Carta vista = actual.getMano().getCarta(indice);
        if (vista == null || !vista.esJugableSobre(cartaMesa)) {
            registrarEvento("Esa carta no se puede jugar.");
            return false;
        }

        Carta jugada = actual.getMano().jugarCarta(indice);
        if (jugada.esComodin()) {
            jugada.setColorActivo(normalizarColor(colorComodin));
        }

        humanoDebeRobar = false;
        humanoPuedePasar = false;
        registrarEvento(actual.getNombre() + " juega " + jugada.toDisplayString() + ".");
        resolverJugadaGUI(actual, jugada, declaroUno);

        if (!partidaTerminada) {
            turnoManager.avanzar();
        }

        return true;
    }

    /**
     * Permite pasar turno únicamente cuando el humano robó una carta jugable.
     *
     * @return {@code true} si se pasó turno correctamente.
     */
    public boolean pasarTurnoHumanoGUI() {
        if (partidaTerminada || !humanoPuedePasar) {
            return false;
        }

        Jugador actual = turnoManager.getJugadorActual();
        if (!actual.esHumano()) return false;

        humanoDebeRobar = false;
        humanoPuedePasar = false;
        registrarEvento(actual.getNombre() + " pasa turno.");
        turnoManager.avanzar();
        return true;
    }

    /**
     * Ejecuta exactamente un turno de IA.
     *
     * @return {@code true} si se ejecutó un turno de IA.
     */
    public boolean ejecutarSiguienteTurnoIAGUI() {
        if (partidaTerminada || turnoManager.getJugadorActual().esHumano()) {
            return false;
        }

        Jugador actual = turnoManager.getJugadorActual();
        Carta jugada = actual.decidirJugadaIASilenciosa(cartaMesa, baraja);

        if (jugada == null) {
            registrarEvento(actual.getNombre() + " no tiene jugada válida y pasa turno.");
        } else {
            registrarEvento(actual.getNombre() + " juega " + jugada.toDisplayString() + ".");
            resolverJugadaGUI(actual, jugada, true);
        }

        if (!partidaTerminada) {
            turnoManager.avanzar();
        }

        return true;
    }

    /**
     * Resuelve una carta ya elegida dentro del flujo gráfico.
     */
    private void resolverJugadaGUI(Jugador jugador, Carta jugada, boolean declaroUno) {
        cartaMesa = jugada;

        int saltosExtra = efectoCarta.aplicar(jugada);
        for (int i = 0; i < saltosExtra; i++) {
            turnoManager.avanzar();
        }

        verificarUnoGUI(jugador, declaroUno);

        if (jugador.getMano().estaVacia()) {
            partidaTerminada = true;
            ganador = jugador;
            registrarEvento("Ganador: " + jugador.getNombre() + ".");
        }
    }

    /**
     * Verifica la regla UNO dentro del flujo gráfico.
     */
    private void verificarUnoGUI(Jugador jugador, boolean declaroUno) {
        if (jugador.getMano().estaVacia()) return;

        if (jugador.getMano().size() == 1) {
            if (jugador.esHumano()) {
                if (declaroUno) {
                    registrarEvento("¡UNO!");
                } else {
                    jugador.robarCarta(baraja);
                    jugador.robarCarta(baraja);
                    registrarEvento("No declaraste UNO. Robas 2 cartas.");
                }
            } else {
                registrarEvento(jugador.getNombre() + " dice: ¡UNO!");
            }
        }
    }

    /**
     * Normaliza el color activo de un comodín.
     */
    private String normalizarColor(String color) {
        if (color == null) return "rojo";

        String normalizado = color.trim().toLowerCase();
        switch (normalizado) {
            case "rojo":
            case "azul":
            case "verde":
            case "amarillo":
                return normalizado;
            default:
                return "rojo";
        }
    }

    /**
     * Verifica si un jugador debe declarar "UNO" al quedarse con una carta.
     *
     * @param jugador Jugador que debe declarar UNO.
     */
    private void verificarUnoConsola(Jugador jugador) {
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

    /** Registra un evento para la interfaz gráfica. */
    private void registrarEvento(String evento) {
        eventos.add(evento);
    }

    /**
     * Consume y limpia los eventos pendientes.
     *
     * @return Eventos generados desde la última consulta.
     */
    public List<String> consumirEventos() {
        ArrayList<String> copia = new ArrayList<>(eventos);
        eventos.clear();
        return copia;
    }

    /** @return Carta actualmente en la mesa. */
    public Carta getCartaMesa() {
        return cartaMesa;
    }

    /** @return Jugador humano registrado en la partida. */
    public Jugador getJugadorHumano() {
        for (Jugador jugador : jugadores) {
            if (jugador.esHumano()) return jugador;
        }
        return null;
    }

    /** @return Jugador que tiene el turno actual. */
    public Jugador getJugadorActual() {
        return turnoManager.getJugadorActual();
    }

    /** @return Lista inmodificable de jugadores. */
    public List<Jugador> getJugadores() {
        return Collections.unmodifiableList(jugadores);
    }

    /** @return Dirección actual del juego. */
    public int getDireccion() {
        return turnoManager.getDireccion();
    }

    /** @return Cartas restantes en la baraja. */
    public int getCartasRestantes() {
        return baraja.cartasRestantes();
    }

    /** @return {@code true} si la partida ya terminó. */
    public boolean isPartidaTerminada() {
        return partidaTerminada;
    }

    /** @return Ganador actual de la partida, o {@code null} si aún no existe. */
    public Jugador getGanador() {
        return ganador;
    }

    /** @return {@code true} si el humano puede pasar turno tras robar. */
    public boolean puedePasarTurnoHumano() {
        return humanoPuedePasar;
    }

    /** @return {@code true} si el humano debe robar antes de actuar. */
    public boolean debeRobarCartaHumano() {
        return humanoDebeRobar;
    }

    /** @return {@code true} si el turno actual pertenece a una IA. */
    public boolean esTurnoIA() {
        return !turnoManager.getJugadorActual().esHumano();
    }
}
