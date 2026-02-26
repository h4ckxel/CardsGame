package clases;

import java.util.ArrayList;

public class Mano {
	private ArrayList<Carta> cartas;
	
	public Mano() {
		cartas = new ArrayList<>();
	}
	
	public void agregarCarta( Carta c ) {
		cartas.add(c);
	}
	
	public Carta jugarCarta( int indice ) {
		if ( indice < 0 || indice >= cartas.size() ) return null;
		return cartas.remove(indice);
	}
	
	public int size() {
		return cartas.size();
	}
	
	public boolean estaVacia() {
		return cartas.isEmpty();
	}
	
	public void mostrarMano() {
		System.out.println("Cartas en mano: ");
		
		for( int i = 0; i < cartas.size(); i++ ) {
			System.out.println(i + ": " + cartas.get(i));
		}
	}
	
	public boolean tieneJugadaValida( Carta cartaMesa ) {
		for( Carta c : cartas ) {
			if( c.esJugableSobre(cartaMesa) ) {
				return true;
			}
		}
		return false;
	}
}
