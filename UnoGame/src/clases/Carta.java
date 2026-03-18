package clases;

public class Carta {
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
