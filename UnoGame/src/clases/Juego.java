package clases;

import java.util.ArrayList;
import java.util.Scanner;

public class Juego {

    private Baraja baraja;
    private ArrayList<Jugador> jugadores;
    private Carta cartaMesa;
    private int turnoActual;
    private int direccion;
    private Scanner scanner;

    public Juego() {
        baraja      = new Baraja();
        jugadores   = new ArrayList<>();
        direccion   = 1;
        turnoActual = 0;
        scanner     = new Scanner(System.in);
    }

    public void iniciar() {
        registrarJugadores();

        Consola.animarRepartiendo();
        repartirCartas();

        cartaMesa = baraja.robarCarta();
        while (cartaMesa.esComodin()) {
            baraja.barajear();
            cartaMesa = baraja.robarCarta();
        }

        mostrarEncabezado();

        while (true) {
            Jugador actual = jugadores.get(turnoActual);
            mostrarEstadoGlobal(actual);

            boolean gano = ejecutarTurno(actual);

            if (gano) {
                Consola.linea();
                Consola.animarVictoria(actual.getNombre());
                Consola.linea();
                break;
            }

            avanzarTurno();
        }
    }

    private void registrarJugadores() {
        Consola.titulo("U  N  O");
        System.out.print(Consola.CIAN + Consola.NEGRITA + "  Ingresa tu nombre: " + Consola.RESET);
        String nombre = scanner.nextLine().trim();
        if (nombre.isEmpty()) nombre = "Jugador";

        jugadores.add(new Jugador(nombre, true));
        jugadores.add(new Jugador("Pepe", false));
        jugadores.add(new Jugador("Toña", false));
        jugadores.add(new Jugador("Mari", false));
    }

    private void repartirCartas() {
        for (int i = 0; i < 7; i++) {
            for (Jugador j : jugadores) {
                j.robarCarta(baraja);
            }
        }
    }

    private boolean ejecutarTurno(Jugador jugador) {
        Carta jugada;

        if (jugador.esHumano()) {
            jugada = jugador.decidirJugadaHumano(cartaMesa, baraja, scanner);
        } else {
            jugada = jugador.decidirJugadaIA(
                    cartaMesa, baraja,
                    new String[]{"rojo","azul","verde","amarillo"});
            if (jugada == null) {
                System.out.println(Consola.AMARILLO + "  " + jugador.getNombre()
                        + " no tiene jugada válida y pasa turno." + Consola.RESET);
            } else {
                System.out.println(Consola.VERDE + Consola.NEGRITA
                        + "  " + jugador.getNombre() + " juega: " + Consola.RESET + jugada);
            }
        }

        if (jugada != null) {
            verificarUno(jugador);
            cartaMesa = jugada;
            aplicarEfecto(jugada, jugador);
        }

        return jugador.getMano().estaVacia();
    }

    private void aplicarEfecto(Carta carta, Jugador jugadorActual) {
        int siguiente = calcularSiguiente();

        switch (carta.getValor()) {
            case Carta.SALTO:
                Consola.animarEfecto("SALTO  " + jugadores.get(siguiente).getNombre()
                        + " pierde turno", Consola.AMARILLO);
                avanzarTurno();
                break;

            case Carta.REVERSA:
                direccion *= -1;
                Consola.animarEfecto("REVERSA  Dirección " + (direccion == 1 ? "→" : "←"), Consola.CIAN);
                break;

            case Carta.ROBA_DOS: {
                Jugador afectado = jugadores.get(siguiente);
                Consola.animarRobando(afectado.getNombre(), 2);
                afectado.robarCarta(baraja);
                afectado.robarCarta(baraja);
                Consola.animarEfecto("ROBA 2  " + afectado.getNombre()
                        + " roba 2 cartas y pierde turno", Consola.ROJO);
                avanzarTurno();
                break;
            }

            case Carta.ROBA_CUATRO: {
                Jugador afectado = jugadores.get(siguiente);
                Consola.animarRobando(afectado.getNombre(), 4);
                for (int i = 0; i < 4; i++) afectado.robarCarta(baraja);
                Consola.animarEfecto("ROBA 4  " + afectado.getNombre()
                        + " roba 4 cartas y pierde turno", Consola.ROJO);
                System.out.println(Consola.MAGENTA + Consola.NEGRITA
                        + "  Color activo: " + carta.getColorActivo().toUpperCase() + Consola.RESET);
                avanzarTurno();
                break;
            }

            case Carta.COMODIN:
                Consola.animarEfecto("COMODIN  Color activo: "
                        + carta.getColorActivo().toUpperCase(), Consola.MAGENTA);
                break;

            default:
                break;
        }
    }

    private void verificarUno(Jugador jugador) {
        if (jugador.getMano().size() == 1) {
            if (jugador.esHumano()) {
                System.out.print(Consola.AMARILLO + Consola.NEGRITA
                        + "  Tienes 1 carta! Escribe UNO para declararlo: " + Consola.RESET);
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
                Consola.animarEfecto(jugador.getNombre() + " dice: UNO!", Consola.AMARILLO);
            }
        }
    }

    private int calcularSiguiente() {
        return ((turnoActual + direccion) % jugadores.size() + jugadores.size()) % jugadores.size();
    }

    private void avanzarTurno() {
        turnoActual = calcularSiguiente();
    }

    private void mostrarEstadoGlobal(Jugador actual) {
        Consola.linea();
        String dirStr = direccion == 1 ? Consola.VERDE + "→ horario" : Consola.MAGENTA + "← inverso";
        System.out.println("  " + Consola.NEGRITA + Consola.CIAN + "TURNO: "
                + Consola.AMARILLO + actual.getNombre()
                + Consola.RESET + "   " + dirStr + Consola.RESET);
        System.out.println("  " + Consola.NEGRITA + "Mesa: " + Consola.RESET + cartaMesa);
        Consola.lineaDelgada();
        System.out.println(Consola.DIM + "  Cartas:" + Consola.RESET);
        for (int i = 0; i < jugadores.size(); i++) {
            Jugador j = jugadores.get(i);
            boolean esTurno = j == actual;
            String marker = esTurno ? Consola.AMARILLO + Consola.NEGRITA + " ► " : "   ";
            System.out.println(marker + Consola.colorNombre(i) + j.getNombre()
                    + Consola.RESET + Consola.DIM + " (" + j.getMano().size() + ")" + Consola.RESET);
        }
        Consola.linea();
    }

    private void mostrarEncabezado() {
        Consola.titulo("U  N  O");
        System.out.println(Consola.NEGRITA + "  Jugadores:" + Consola.RESET);
        for (int i = 0; i < jugadores.size(); i++) {
            Jugador j = jugadores.get(i);
            System.out.println(Consola.colorNombre(i) + "    " + j.getNombre()
                    + Consola.RESET + Consola.DIM
                    + (j.esHumano() ? "  (Tú)" : "  (IA)") + Consola.RESET);
        }
        System.out.println(Consola.NEGRITA + "\n  Carta inicial en mesa: " + Consola.RESET + cartaMesa);
        Consola.linea();
    }
}
