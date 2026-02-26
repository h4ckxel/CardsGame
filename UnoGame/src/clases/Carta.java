package clases;

public class Carta {
	private String color;
	private int numero;
	
	public Carta( String color, int numero ) {
		this.color = color;
		this.numero = numero;
	}
	
	public String getColor() {
		return color;
	}
	
	public int numero() {
		return numero;
	}
	
	public boolean esJugableSobre( Carta otra ) {
		if( otra == null ) return true;
		return this.color.equals(otra.color) || this.numero == otra.numero;
	}

	@Override
	public String toString() {
		return "Carta [color=" + color + ", numero=" + numero + "]";
	}
}
