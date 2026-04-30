package unogame.clases;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * Clase encargada de registrar jugadores para el juego UNO.
 * Permite registrar jugadores mediante consola o directamente
 * desde parámetros, diferenciando entre humanos e IA.
 * 
 * @author Sebastian, Acxel, Mariana, Joshua, Omar
 * @version 1.0
 */
public class RegistroJugadores {

    /**
     * Registra jugadores solicitando el nombre del jugador humano
     * por consola y agregando jugadores IA predeterminados.
     * 
     * @param scanner Objeto {@link Scanner} para leer la entrada desde consola.
     * @return Lista de jugadores registrados (1 humano y varios IA).
     */
    public static ArrayList<Jugador> registrarConConsola(Scanner scanner) {
        ArrayList<Jugador> jugadores = new ArrayList<>();

        System.out.print(Consola.CIAN + Consola.NEGRITA
                + "  Ingresa tu nombre: " + Consola.RESET);
        String nombre = scanner.nextLine().trim();
        if (nombre.isEmpty()) nombre = "Jugador";

        jugadores.add(new Jugador(nombre, true));
        jugadores.add(new Jugador("Pepe",  false));
        jugadores.add(new Jugador("Toño",  false));
        jugadores.add(new Jugador("Maria", false));

        return jugadores;
    }

    /**
     * Registra jugadores directamente a partir de parámetros.
     * 
     * @param nombreHumano Nombre del jugador humano.
     * @param nombresIA Nombres de los jugadores IA.
     * @return Lista de jugadores registrados (1 humano y varios IA).
     */
    public static ArrayList<Jugador> registrarDirecto(String nombreHumano,
                                                       String... nombresIA) {
        ArrayList<Jugador> jugadores = new ArrayList<>();
        jugadores.add(new Jugador(nombreHumano, true));
        for (String ia : nombresIA) {
            jugadores.add(new Jugador(ia, false));
        }
        return jugadores;
    }
}
