package unogame.ui;

import javax.swing.JFrame;

/**
 * Ventana principal de la interfaz gráfica.
 */
public class VentanaUNO extends JFrame {

    public VentanaUNO() {
        setTitle("UNO");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(new UnoGUI());
        setSize(980, 620);
        setLocationRelativeTo(null);
    }
}
