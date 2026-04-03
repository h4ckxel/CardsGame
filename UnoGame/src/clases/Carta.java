package clases;

public class Carta {

    public static final int SALTO     = 10;
    public static final int REVERSA   = 11;
    public static final int ROBA_DOS  = 12;
    public static final int COMODIN   = 13;
    public static final int ROBA_CUATRO = 14;

    private String color;
    private int valor;
    private String colorActivo;

    public Carta(String color, int valor) {
        this.color = color;
        this.valor = valor;
        this.colorActivo = color;
    }

    public String getColor() {
        return color;
    }

    public int getValor() {
        return valor;
    }

    public String getColorActivo() {
        return colorActivo;
    }

    public void setColorActivo(String colorActivo) {
        this.colorActivo = colorActivo;
    }

    public boolean esEspecial() {
        return valor >= SALTO;
    }

    public boolean esComodin() {
        return valor == COMODIN || valor == ROBA_CUATRO;
    }

    public boolean esJugableSobre(Carta otra) {
        if (otra == null) return true;
        if (esComodin()) return true;
        if (color.equals("comodin")) return true;
        return this.color.equals(otra.getColorActivo()) || this.valor == otra.valor;
    }

    private String nombreValor() {
        switch (valor) {
            case SALTO:         return "SALTO";
            case REVERSA:       return "REVERSA";
            case ROBA_DOS:      return "ROBA2";
            case COMODIN:       return "COMODÍN";
            case ROBA_CUATRO:   return "ROBA4";
            default:            return String.valueOf(valor);
        }
    }

    @Override
    public String toString() {
        String col = Consola.colorCarta(esComodin() ? colorActivo : color);
        if (esComodin()) {
            return col + "[ ★ " + nombreValor() + " → " + colorActivo.toUpperCase() + " ]" + Consola.RESET;
        }
        return col + "[ " + color.toUpperCase() + " " + nombreValor() + " ]" + Consola.RESET;
    }
}
