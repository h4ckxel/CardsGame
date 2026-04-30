package unogame.clases;

/**
 * Clase encargada de aplicar los efectos de las cartas especiales
 * durante el juego. Interactúa con el {@link TurnoManager} para
 * modificar el flujo de turnos y con la {@link Baraja} para gestionar
 * las cartas robadas.
 * 
 * Los efectos incluyen:
 * <ul>
 *   <li>SALTO: el siguiente jugador pierde su turno.</li>
 *   <li>REVERSA: se invierte la dirección del juego.</li>
 *   <li>ROBA DOS: el siguiente jugador roba dos cartas y pierde turno.</li>
 *   <li>ROBA CUATRO: el siguiente jugador roba cuatro cartas y pierde turno.</li>
 *   <li>COMODÍN: se cambia el color activo.</li>
 * </ul>
 * 
 * @author Sebastian, Acxel, Mariana, Joshua, Omar
 * @version 1.0
 */
public class EfectoCarta {

    /** Administrador de turnos del juego. */
    private TurnoManager turnoManager;
    /** Baraja de cartas utilizada en el juego. */
    private Baraja       baraja;

    /**
     * Constructor de la clase EfectoCarta.
     * 
     * @param turnoManager Administrador de turnos.
     * @param baraja Baraja de cartas del juego.
     */
    public EfectoCarta(TurnoManager turnoManager, Baraja baraja) {
        this.turnoManager = turnoManager;
        this.baraja       = baraja;
    }

    /**
     * Aplica el efecto correspondiente a una carta especial.
     * 
     * @param carta Carta cuyo efecto se aplicará.
     * @return Número de turnos que deben saltarse (0 o 1).
     */
    public int aplicar(Carta carta) {
        switch (carta.getValor()) {

            case Carta.SALTO: {
                Jugador saltado = turnoManager.getSiguienteJugador();
                notificarSalto(saltado);
                return 1;
            }

            case Carta.REVERSA:
                turnoManager.invertirDireccion();
                notificarReversa(turnoManager.getDireccion());
                return 0;

            case Carta.ROBA_DOS: {
                Jugador afectado = turnoManager.getSiguienteJugador();
                afectado.robarCarta(baraja);
                afectado.robarCarta(baraja);
                notificarRobaDos(afectado);
                return 1;
            }

            case Carta.ROBA_CUATRO: {
                Jugador afectado = turnoManager.getSiguienteJugador();
                for (int i = 0; i < 4; i++) afectado.robarCarta(baraja);
                notificarRobaCuatro(afectado, carta.getColorActivo());
                return 1; 
            }

            case Carta.COMODIN:
                notificarComodin(carta.getColorActivo());
                return 0;

            default:
                return 0;
        }
    }

    /**
     * Notifica el efecto de una carta SALTO.
     * @param saltado Jugador que pierde el turno.
     */
    protected void notificarSalto(Jugador saltado) {
        Consola.animarEfecto("SALTO  " + saltado.getNombre()
                + " pierde turno", Consola.AMARILLO);
    }

    /**
     * Notifica el efecto de una carta REVERSA.
     * @param nuevaDireccion Nueva dirección del turno (1 → derecha, -1 → izquierda).
     */
    protected void notificarReversa(int nuevaDireccion) {
        Consola.animarEfecto("REVERSA  Dirección "
                + (nuevaDireccion == 1 ? "→" : "←"), Consola.CIAN);
    }

    /**
     * Notifica el efecto de una carta ROBA DOS.
     * @param afectado Jugador que roba dos cartas.
     */
    protected void notificarRobaDos(Jugador afectado) {
        Consola.animarRobando(afectado.getNombre(), 2);
        Consola.animarEfecto("ROBA 2  " + afectado.getNombre()
                + " roba 2 y pierde turno", Consola.ROJO);
    }

    /**
     * Notifica el efecto de una carta ROBA CUATRO.
     * @param afectado Jugador que roba cuatro cartas.
     * @param colorActivo Color activo elegido por el jugador.
     */
    protected void notificarRobaCuatro(Jugador afectado, String colorActivo) {
        Consola.animarRobando(afectado.getNombre(), 4);
        Consola.animarEfecto("ROBA 4  " + afectado.getNombre()
                + " roba 4 y pierde turno", Consola.ROJO);
        System.out.println(Consola.MAGENTA + Consola.NEGRITA
                + "  Color activo: " + colorActivo.toUpperCase() + Consola.RESET);
    }

    /**
     * Notifica el efecto de una carta COMODÍN.
     * @param colorActivo Color activo elegido por el jugador.
     */
    protected void notificarComodin(String colorActivo) {
        Consola.animarEfecto("COMODIN  Color activo: "
                + colorActivo.toUpperCase(), Consola.MAGENTA);
    }
}
