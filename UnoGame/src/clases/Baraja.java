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

        for (String color : colores) {
            cartas.add(new Carta(color, 0));

            for (int i = 1; i <= 9; i++) {
                cartas.add(new Carta(color, i));
                cartas.add(new Carta(color, i));
            }

            cartas.add(new Carta(color, Carta.SALTO));
            cartas.add(new Carta(color, Carta.SALTO));
            cartas.add(new Carta(color, Carta.REVERSA));
            cartas.add(new Carta(color, Carta.REVERSA));
            cartas.add(new Carta(color, Carta.ROBA_DOS));
            cartas.add(new Carta(color, Carta.ROBA_DOS));
        }

        for (int i = 0; i < 4; i++) {
            cartas.add(new Carta("comodin", Carta.COMODIN));
            cartas.add(new Carta("comodin", Carta.ROBA_CUATRO));
        }
    }

    public void barajear() {
        Collections.shuffle(cartas);
    }

    public Carta robarCarta() {
        if (cartas.isEmpty()) return null;
        return cartas.remove(0);
    }

    public int cartasRestantes() {
        return cartas.size();
    }
	private ArrayList<Carta> cartas;
	
	public Baraja() {
		cartas = new ArrayList<>();
		crearBaraja();
		barajear();
	}
	
	private void crearBaraja() {
		String[] colores = {"rojo", "azul", "verde", "amarillo"};
		
		for(String color : colores) {
			cartas.add(new Carta(color, 0));
			
			for(int i = 1; i <= 9; i++) {
				cartas.add(new Carta(color, i));
			}
			
			for (int j = 0; j < 2; j++){
				cartas.add(new Carta(color, Carta.Tipo.SALTO));
				cartas.add(new Carta(color, Carta.Tipo.REVERSA));
				cartas.add(new Carta(color, Carta.Tipo.ROBA2));
			}
		}
		
		for (int i = 0; i < 4; i++){
			cartas.add(new Carta("negro", Carta.Tipo.COMODIN));
			cartas.add(new Carta("negro", Carta.Tipo.ROBA4));
		}
	}
	
	public void barajear() {
		Collections.shuffle(cartas);
	}
	
	public Carta robarCarta() {
		if( cartas.isEmpty() ) return null;
		return cartas.remove(0);
	}
	
	/*public int cartasRestantes() {
		return cartas.size();
	}*/
}
