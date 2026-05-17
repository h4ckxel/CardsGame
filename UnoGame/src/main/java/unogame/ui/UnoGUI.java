package unogame.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.ArrayDeque;
import java.util.Queue;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.Timer;
import javax.swing.border.Border;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import unogame.clases.Carta;
import unogame.clases.Juego;
import unogame.clases.Jugador;

/**
 * Interfaz gráfica.
 */
public class UnoGUI extends JPanel {

    private static final Color COLOR_TEXTO = new Color(35, 35, 35);
    private static final Color COLOR_TURNO = new Color(70, 70, 70);
    private static final Color COLOR_ACCION = new Color(30, 100, 180);
    private static final Color COLOR_EFECTO = new Color(145, 70, 180);
    private static final Color COLOR_ALERTA = new Color(190, 85, 20);
    private static final Color COLOR_VICTORIA = new Color(25, 135, 70);

    private final Juego juego;

    private final JLabel estado = new JLabel();
    private final JLabel mazo = new JLabel();
    private final JPanel mesa = new JPanel(new FlowLayout(FlowLayout.CENTER, 24, 16));
    private final JPanel jugadores = new JPanel(new GridLayout(0, 1, 4, 4));
    private final JPanel mano = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
    private final JTextPane log = new JTextPane();
    private final StyledDocument documentoLog = log.getStyledDocument();
    private final JButton robarCarta = new JButton("Robar carta");
    private final JButton pasarTurno = new JButton("Pasar turno");
    private final Queue<EventoVisual> eventosPendientes = new ArrayDeque<>();

    private final Border bordeCartaNormal = BorderFactory.createEmptyBorder(4, 4, 4, 4);
    private final Border bordeCartaAnimada = BorderFactory.createLineBorder(new Color(120, 120, 120), 2);

    private JLabel cartaMesaLabel;
    private boolean animandoEventos;
    private boolean iaEnCurso;

    public UnoGUI() {
        juego = new Juego();
        juego.iniciarPartidaGUI("Jugador", "Pepe", "Toño", "Maria");

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        add(crearCabecera(), BorderLayout.NORTH);
        add(crearCentro(), BorderLayout.CENTER);
        add(crearMano(), BorderLayout.SOUTH);
        add(crearLog(), BorderLayout.EAST);
        add(crearJugadores(), BorderLayout.WEST);

        robarCarta.addActionListener(e -> {
            if (juego.robarCartaHumanoGUI()) {
                actualizarTodo();
            }
        });

        pasarTurno.addActionListener(e -> {
            if (juego.pasarTurnoHumanoGUI()) {
                actualizarTodo();
                avanzarFlujoTrasAccionHumana();
            }
        });

        juego.prepararTurnoHumanoGUI();
        actualizarTodo();
    }

    private JPanel crearCabecera() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Estado"));
        panel.add(estado, BorderLayout.WEST);
        panel.add(mazo, BorderLayout.EAST);
        return panel;
    }

    private JPanel crearCentro() {
        mesa.setBorder(BorderFactory.createTitledBorder("Mesa"));
        return mesa;
    }

    private JPanel crearJugadores() {
        jugadores.setBorder(BorderFactory.createTitledBorder("Jugadores"));
        jugadores.setPreferredSize(new Dimension(160, 180));
        return jugadores;
    }

    private JPanel crearMano() {
        JPanel contenedor = new JPanel(new BorderLayout(8, 8));
        contenedor.setBorder(BorderFactory.createTitledBorder("Tu mano"));
        contenedor.add(new JScrollPane(mano), BorderLayout.CENTER);

        JPanel controles = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        controles.add(robarCarta);
        controles.add(pasarTurno);
        contenedor.add(controles, BorderLayout.EAST);
        return contenedor;
    }

    private JScrollPane crearLog() {
        log.setEditable(false);
        log.setForeground(COLOR_TEXTO);
        JScrollPane scroll = new JScrollPane(log);
        scroll.setBorder(BorderFactory.createTitledBorder("Consola"));
        scroll.setPreferredSize(new Dimension(290, 0));
        return scroll;
    }

    private void actualizarTodo() {
        actualizarEstado();
        actualizarMesa();
        actualizarJugadores();
        actualizarMano();
        actualizarEventos();
    }

    private void actualizarEstado() {
        if (juego.isPartidaTerminada()) {
            estado.setText("Ganador: " + juego.getGanador().getNombre());
        } else if (iaEnCurso && juego.esTurnoIA()) {
            estado.setText("Turno: " + juego.getJugadorActual().getNombre() + " está pensando...");
        } else {
            String direccion = juego.getDireccion() == 1 ? "horaria" : "inversa";
            estado.setText("Turno: " + juego.getJugadorActual().getNombre()
                    + " | Dirección: " + direccion);
        }

        mazo.setText("Mazo: " + juego.getCartasRestantes() + " cartas");
        boolean turnoHumano = !juego.isPartidaTerminada() && !juego.esTurnoIA();
        robarCarta.setEnabled(turnoHumano && !iaEnCurso && juego.debeRobarCartaHumano());
        pasarTurno.setEnabled(turnoHumano && !iaEnCurso && juego.puedePasarTurnoHumano());
    }

    private void actualizarMesa() {
        mesa.removeAll();

        Carta cartaMesa = juego.getCartaMesa();
        cartaMesaLabel = crearLabelCarta(cartaMesa);
        cartaMesaLabel.setBorder(bordeCartaNormal);
        mesa.add(cartaMesaLabel);

        JPanel pila = new JPanel();
        pila.setLayout(new BoxLayout(pila, BoxLayout.Y_AXIS));
        JLabel titulo = new JLabel("Mazo");
        ImageIcon reverso = CartaImagenes.obtenerReverso();
        JLabel reversoLabel = reverso == null ? new JLabel("[MAZO]") : new JLabel(reverso);
        pila.add(titulo);
        pila.add(reversoLabel);
        mesa.add(pila);

        mesa.revalidate();
        mesa.repaint();
    }

    private void actualizarJugadores() {
        jugadores.removeAll();

        for (Jugador jugador : juego.getJugadores()) {
            String sufijo = jugador.esHumano() ? " (Tú)" : " (IA)";
            jugadores.add(new JLabel(jugador.getNombre()
                    + sufijo + ": " + jugador.getMano().size() + " cartas"));
        }

        jugadores.revalidate();
        jugadores.repaint();
    }

    private void actualizarMano() {
        mano.removeAll();

        Jugador humano = juego.getJugadorHumano();
        boolean esTurnoHumano = !juego.isPartidaTerminada()
                && !juego.esTurnoIA()
                && !iaEnCurso
                && !juego.debeRobarCartaHumano();

        for (int i = 0; i < humano.getMano().size(); i++) {
            final int indice = i;
            Carta carta = humano.getMano().getCarta(i);
            JButton boton = crearBotonCarta(carta);
            boton.setEnabled(esTurnoHumano && carta.esJugableSobre(juego.getCartaMesa()));
            boton.addActionListener(e -> jugarCarta(indice));
            mano.add(boton);
        }

        mano.revalidate();
        mano.repaint();
    }

    private JButton crearBotonCarta(Carta carta) {
        ImageIcon icono = CartaImagenes.obtener(carta);
        return icono == null ? new JButton(carta.toDisplayString()) : new JButton(icono);
    }

    private JLabel crearLabelCarta(Carta carta) {
        ImageIcon icono = CartaImagenes.obtener(carta);
        return icono == null
                ? new JLabel(carta == null ? "[SIN CARTA]" : carta.toDisplayString())
                : new JLabel(icono);
    }

    private void jugarCarta(int indice) {
        Jugador humano = juego.getJugadorHumano();
        Carta carta = humano.getMano().getCarta(indice);
        if (carta == null) return;

        String color = null;
        if (carta.esComodin()) {
            color = pedirColorComodin();
            if (color == null) return;
        }

        boolean declaroUno = true;
        if (humano.getMano().size() == 2) {
            int respuesta = JOptionPane.showConfirmDialog(
                    this,
                    "Después de jugar esta carta te quedará una.\n¿Declaras UNO?",
                    "UNO",
                    JOptionPane.YES_NO_OPTION);
            declaroUno = respuesta == JOptionPane.YES_OPTION;
        }

        if (juego.jugarCartaHumanoGUI(indice, color, declaroUno)) {
            actualizarTodo();
            animarCartaMesa();
            avanzarFlujoTrasAccionHumana();
        }
    }

    private String pedirColorComodin() {
        Object seleccion = JOptionPane.showInputDialog(
                this,
                "Elige el color activo:",
                "Comodín",
                JOptionPane.QUESTION_MESSAGE,
                null,
                new String[]{"rojo", "azul", "verde", "amarillo"},
                "rojo");

        return seleccion == null ? null : seleccion.toString();
    }

    private void avanzarFlujoTrasAccionHumana() {
        if (juego.isPartidaTerminada()) return;

        if (juego.esTurnoIA()) {
            ejecutarTurnoIAAnimado();
        } else {
            juego.prepararTurnoHumanoGUI();
            actualizarTodo();
        }
    }

    private void ejecutarTurnoIAAnimado() {
        if (iaEnCurso || juego.isPartidaTerminada() || !juego.esTurnoIA()) return;

        iaEnCurso = true;
        actualizarTodo();

        Timer pausaPensando = new Timer(700, e -> {
            ((Timer) e.getSource()).stop();
            Carta cartaAntes = juego.getCartaMesa();
            juego.ejecutarSiguienteTurnoIAGUI();
            iaEnCurso = false;
            actualizarTodo();
            if (juego.getCartaMesa() != cartaAntes) {
                animarCartaMesa();
            }

            if (juego.isPartidaTerminada()) {
                return;
            }

            if (juego.esTurnoIA()) {
                ejecutarTurnoIAAnimado();
            } else {
                Timer pausaAntesHumano = new Timer(350, evento -> {
                    ((Timer) evento.getSource()).stop();
                    juego.prepararTurnoHumanoGUI();
                    actualizarTodo();
                });
                pausaAntesHumano.setRepeats(false);
                pausaAntesHumano.start();
            }
        });
        pausaPensando.setRepeats(false);
        pausaPensando.start();
    }

    private void animarCartaMesa() {
        if (cartaMesaLabel == null) return;

        final int[] pasos = {0};
        Timer timer = new Timer(120, e -> {
            cartaMesaLabel.setBorder(
                    pasos[0] % 2 == 0 ? bordeCartaAnimada : bordeCartaNormal);
            pasos[0]++;

            if (pasos[0] >= 6) {
                cartaMesaLabel.setBorder(bordeCartaNormal);
                ((Timer) e.getSource()).stop();
            }
        });
        timer.start();
    }

    private void actualizarEventos() {
        for (String evento : juego.consumirEventos()) {
            eventosPendientes.add(new EventoVisual(evento, colorParaEvento(evento)));
        }
        animarSiguienteEvento();
    }

    private void animarSiguienteEvento() {
        if (animandoEventos || eventosPendientes.isEmpty()) return;

        EventoVisual evento = eventosPendientes.poll();
        animandoEventos = true;
        final int[] indice = {0};

        Timer timer = new Timer(14, e -> {
            try {
                if (indice[0] < evento.texto.length()) {
                    SimpleAttributeSet estilo = crearEstilo(evento.color);
                    documentoLog.insertString(
                            documentoLog.getLength(),
                            String.valueOf(evento.texto.charAt(indice[0])),
                            estilo);
                    indice[0]++;
                } else {
                    documentoLog.insertString(
                            documentoLog.getLength(),
                            System.lineSeparator(),
                            crearEstilo(COLOR_TEXTO));
                    log.setCaretPosition(documentoLog.getLength());
                    ((Timer) e.getSource()).stop();
                    animandoEventos = false;
                    animarSiguienteEvento();
                }
            } catch (BadLocationException ex) {
                ((Timer) e.getSource()).stop();
                animandoEventos = false;
            }
        });
        timer.start();
    }

    private SimpleAttributeSet crearEstilo(Color color) {
        SimpleAttributeSet estilo = new SimpleAttributeSet();
        StyleConstants.setForeground(estilo, color);
        return estilo;
    }

    private Color colorParaEvento(String evento) {
        String texto = evento.toLowerCase();
        if (texto.contains("ganador")) return COLOR_VICTORIA;
        if (texto.contains("uno")) return COLOR_ALERTA;
        if (texto.contains("salto")
                || texto.contains("reversa")
                || texto.contains("comodín")
                || texto.contains("roba 2")
                || texto.contains("roba 4")) {
            return COLOR_EFECTO;
        }
        if (texto.contains("juega") || texto.contains("roba")) return COLOR_ACCION;
        if (texto.contains("turno")) return COLOR_TURNO;
        return COLOR_TEXTO;
    }

    private static class EventoVisual {
        private final String texto;
        private final Color color;

        private EventoVisual(String texto, Color color) {
            this.texto = texto;
            this.color = color;
        }
    }
}
