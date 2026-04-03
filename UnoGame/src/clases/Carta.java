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
	public enum Tipo {
		NUMERO,
		REVERSA,
		SALTO,
		ROBA2,
		ROBA4,
		COMODIN
	}
	
	private String color;
	private Tipo tipo;
	private int numero;
	
	public Carta( String color, int numero ) {
		this.color = color;
		this.numero = numero;
		this.tipo = Tipo.NUMERO;
	}
	
	public Carta( String color, Tipo tipo) {
		this.color = color;
		this.tipo = tipo;
		this.numero = -1;
	}
	
	public void setColor(String color) {
		this.color = color;
	}
	
	public String getColor() {
		return color;
	}
	
	public Tipo getTipo() {
		return tipo;
	}
	
	public int numero() {
		return numero;
	}
	
	public boolean esJugableSobre( Carta otra ) {
		if (this.tipo == Tipo.COMODIN || this.tipo == Tipo.ROBA4)
			return true;
		if (this.color.equals(otra.color))
			return true;
		if (this.tipo == Tipo.NUMERO && otra.tipo == Tipo.NUMERO)
			return this.numero == otra.numero;
		
		return this.tipo == otra.tipo;
		
		//Version 1
		//if( otra == null ) return true;
		//return this.color.equals(otra.color) || this.numero == otra.numero;
	}

	@Override
	public String toString() {
		if (tipo == Tipo.NUMERO)
			return color + " " + numero;
		return color + " " + tipo;
		
		//Version 1
		//return "Carta [color=" + color + ", numero=" + numero + "]";
	}
}
