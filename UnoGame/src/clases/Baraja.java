package clases;

import java.util.ArrayList;
import java.util.Collections;

public class Baraja {
	private ArrayList<Carta> cartas;
	
	public Baraja() {
		cartas = new ArrayList<>();
		crearBaraja();
		barajear();
	}
	
	private void crearBaraja() {
		String[] colores = {"rojo", "azul", "verde", "amarillo"};
		
		for( String color : colores ) {
			for( int i = 0; i <= 9; i++ ) {
				cartas.add(new Carta(color, i));
				cartas.add(new Carta(color, i));
			}
		}
	}
	
	public void barajear() {
		Collections.shuffle(cartas);
	}
	
	public Carta robarCarta() {
		if( cartas.isEmpty() ) return null;
		return cartas.remove(0);
	}
	
	public int cartasRestantes() {
		return cartas.size();
	}
}
