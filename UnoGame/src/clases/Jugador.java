package clases;

import java.util.Scanner;

public class Jugador {

    private String nombre;
    private Mano mano;
    private boolean esHumano;

    public Jugador(String nombre, boolean esHumano) {
        this.nombre  = nombre;
        this.esHumano = esHumano;
        this.mano    = new Mano();
    }

    public String getNombre() { return nombre; }
    public Mano getMano()     { return mano; }
    public boolean esHumano() { return esHumano; }

    public void robarCarta(Baraja baraja) {
        Carta c = baraja.robarCarta();
        if (c != null) mano.agregarCarta(c);
    }

    public boolean tieneJugadaValida(Carta cartaMesa) {
        return mano.tieneJugadaValida(cartaMesa);
    }

    public Carta decidirJugadaIA(Carta cartaMesa, Baraja baraja, String[] coloresValidos) {
        Consola.animarPensando(nombre);

        int indice = mano.primerIndiceValido(cartaMesa);

        if (indice == -1) {
            Consola.animarRobando(nombre, 1);
            robarCarta(baraja);
            int nuevoIndice = mano.primerIndiceValido(cartaMesa);
            if (nuevoIndice == -1) return null;
            indice = nuevoIndice;
        }

        Carta jugada = mano.jugarCarta(indice);

        if (jugada.esComodin()) {
            jugada.setColorActivo(elegirColorIA());
        }

        return jugada;
    }

    private String elegirColorIA() {
        int[] conteo = new int[4];
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

    public Carta decidirJugadaHumano(Carta cartaMesa, Baraja baraja, Scanner scanner) {
        System.out.println("\n  " + Consola.CIAN + Consola.NEGRITA
                + "Carta en mesa: " + Consola.RESET + cartaMesa);
        mano.mostrarMano();

        if (!mano.tieneJugadaValida(cartaMesa)) {
            System.out.println(Consola.AMARILLO
                    + "  No tienes jugada válida. Robas una carta automáticamente." + Consola.RESET);
            Consola.animarRobando(nombre, 1);
            robarCarta(baraja);
            return null;
        }

        while (true) {
            System.out.print(Consola.BLANCO + Consola.NEGRITA
                    + "  Elige carta (índice) o -1 para robar: " + Consola.RESET);
            int opcion;

            try {
                opcion = Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println(Consola.ROJO + "  Entrada inválida. Escribe un número." + Consola.RESET);
                continue;
            }

            if (opcion == -1) {
                Consola.animarRobando(nombre, 1);
                robarCarta(baraja);
                System.out.println(Consola.CIAN + "  Robaste una carta." + Consola.RESET);
                int idx = mano.primerIndiceValido(cartaMesa);
                if (idx == -1) {
                    System.out.println(Consola.AMARILLO + "  La carta robada no es válida. Pierdes turno." + Consola.RESET);
                    return null;
                }
                System.out.println(Consola.VERDE + "  La carta robada es jugable!" + Consola.RESET);
                mano.mostrarMano();
                System.out.print(Consola.BLANCO + Consola.NEGRITA
                        + "  Elige índice o -1 para pasar: " + Consola.RESET);
                try {
                    opcion = Integer.parseInt(scanner.nextLine().trim());
                } catch (NumberFormatException e) {
                    return null;
                }
                if (opcion == -1) return null;
            }

            Carta seleccionada = mano.jugarCarta(opcion);

            if (seleccionada == null) {
                System.out.println(Consola.ROJO + "  Índice inválido. Intenta de nuevo." + Consola.RESET);
                continue;
            }

            if (!seleccionada.esJugableSobre(cartaMesa)) {
                System.out.println(Consola.ROJO + "  Carta no válida sobre la mesa. Regresa a tu mano." + Consola.RESET);
                mano.agregarCarta(seleccionada);
                continue;
            }

            if (seleccionada.esComodin()) {
                String color = pedirColor(scanner);
                seleccionada.setColorActivo(color);
            }

            return seleccionada;
        }
    }

    private String pedirColor(Scanner scanner) {
        String[] validos = {"rojo", "azul", "verde", "amarillo"};
        while (true) {
            System.out.print(Consola.MAGENTA + Consola.NEGRITA
                    + "  Elige color ("
                    + Consola.ROJO    + "rojo"     + Consola.MAGENTA + "/"
                    + Consola.AZUL    + "azul"     + Consola.MAGENTA + "/"
                    + Consola.VERDE   + "verde"    + Consola.MAGENTA + "/"
                    + Consola.AMARILLO+ "amarillo" + Consola.MAGENTA + "): " + Consola.RESET);
            String entrada = scanner.nextLine().trim().toLowerCase();
            for (String v : validos) {
                if (v.equals(entrada)) return entrada;
            }
            System.out.println(Consola.ROJO + "  Color inválido. Intenta de nuevo." + Consola.RESET);
        }
    }
}
