package clases;

import java.util.Scanner;

public class Juego {
	private Baraja baraja;
	private Mano manoJugador;
	private Mano manoComputadora;
	private Carta cartaMesa;
	private Scanner scanner;
	
	public Juego() {
		baraja = new Baraja();
		manoJugador = new Mano();
		manoComputadora = new Mano();
		
		scanner = new Scanner(System.in);
	}
	
	public void iniciar() {
		
		repartirCartas();
		
		cartaMesa = baraja.robarCarta();
		System.out.println( "Carta inicial en la mesa: " + cartaMesa );
		System.out.println( "-------------------------------------------" );
		
		while( true ) {
			turnoJugador();
			
			if( manoJugador.estaVacia() ) {
				System.out.println( "¡Ganaste!" );
				break;
			}
			
			turnoComputadora();
			
			if( manoComputadora.estaVacia() ) {
				System.out.println( "La computadora gana." );
				break;
			}
		}
	}
	
	private void repartirCartas() {
		for( int i = 0; i < 7; i++ ) {
			manoJugador.agregarCarta(baraja.robarCarta());
			manoComputadora.agregarCarta(baraja.robarCarta());
		}
	}
	
	private void turnoJugador() {
		System.out.println( "\n=== TU TURNO ===" );
		System.out.println( "Carta en mesa: " + cartaMesa );
		manoJugador.mostrarMano();
		
		if(!manoJugador.tieneJugadaValida(cartaMesa)) {
			System.out.println( "No tienes jugada valida. Roba carta." );
			manoJugador.agregarCarta(baraja.robarCarta());
			return;
		}
		
		while( true ) {
			System.out.println( "Elige carta a jugar (numero) o -1 para robar: " );
			int opcion = scanner.nextInt();
			
			//System.out.println( "opcion = " + opcion );
			
			if( opcion == -1 ) {
				manoJugador.agregarCarta(baraja.robarCarta());
				System.out.println( "Robaste una carta" );
			}
			
			Carta seleccionada = manoJugador.jugarCarta(opcion);
			
			if( seleccionada == null ) {
				System.out.println( "Indice invalido" );
				continue;
			}
			
			if( seleccionada.esJugableSobre(cartaMesa) ) {
				cartaMesa = seleccionada;
				System.out.println( "Jugaste: " + cartaMesa );
				return;
			} else {
				System.out.println( "Carta no valida. Regresa a tu mano." );
				manoJugador.agregarCarta(seleccionada);
			}
		}
	}
	
	private void turnoComputadora() {
		System.out.println( "\n=== TURNO COMPUTADORA ===" );
		System.out.println( "Carta en mesa: " + cartaMesa );
		
		for( int i = 0; i < manoComputadora.size(); i++ ) {
			Carta c = manoComputadora.jugarCarta(i);
			
			if( c.esJugableSobre(cartaMesa) ) {
				cartaMesa = c;
				System.out.println( "Computadora juega: "+ cartaMesa );
				return;
			} else {
				manoComputadora.agregarCarta(c); //regresa carta
			}
		}
	
		System.out.println( "Computadora roba carta." );
		manoComputadora.agregarCarta(baraja.robarCarta());
	}
}
