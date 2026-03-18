package clases;

import java.util.Random;
import java.util.Scanner;

public class Juego {
	private Baraja baraja;
	private Mano manoJugador;
	private Mano manoComputadora;
	private Carta cartaMesa;
	private Scanner scanner;
	private boolean saltarTurno = false;
	
	public Juego() {
		baraja = new Baraja();
		manoJugador = new Mano();
		manoComputadora = new Mano();
		
		scanner = new Scanner(System.in);
	}
	
	public void iniciar() {
		
		repartirCartas();
		
		do {
		    cartaMesa = baraja.robarCarta();
		} while (cartaMesa.getTipo() != Carta.Tipo.NUMERO);
		
		System.out.println( "Carta inicial en la mesa: " + cartaMesa );
		System.out.println( "-------------------------------------------" );
		
		while( true ) {
			if ( saltarTurno ) {
		        System.out.println( "Jugador pierde turno." );
		        saltarTurno = false;
		    } else {
		        turnoJugador();

		        if ( manoJugador.estaVacia() ) {
		            System.out.println( "¡Ganaste!" );
		            break;
		        }
		    }
			
		    if ( saltarTurno ) {
		        System.out.println( "Computadora pierde turno." );
		        saltarTurno = false;
		    } else {
		        turnoComputadora();

		        if ( manoComputadora.estaVacia() ) {
		            System.out.println( "La computadora gana." );
		            break;
		        }
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
	    System.out.println("\n=== TU TURNO ===");
	    System.out.println("Carta en mesa: " + cartaMesa);

	    while ( true ) {
	        manoJugador.mostrarMano();
	        System.out.println("Seleccionar carta (-1 para robar): ");
	        
	        if ( !scanner.hasNextInt() ) {
	            System.out.println("Entrada invalida.");
	            scanner.next();
	            continue;
	        }

	        int opcion = scanner.nextInt();

	        // Robar carta (decisión del jugador)
	        if( opcion == -1 ) {
	            Carta robada = baraja.robarCarta();
	            manoJugador.agregarCarta(robada);
	            System.out.println("Robaste: " + robada);
	            return;
	        }

	        Carta carta = manoJugador.jugarCarta(opcion);

	        if( carta == null ) {
	            System.out.println("Indice invalido.");
	            continue;
	        }

	        if( !carta.esJugableSobre(cartaMesa) ) {
	            System.out.println( "Carta no valida." );
	            manoJugador.agregarCarta( carta );
	            continue;
	        }

	        // Jugada válida
	        cartaMesa = carta;
	        System.out.println( "Jugaste: " + carta );

	        aplicarEfecto(carta, true);
	        verificarUNO(manoJugador, true);

	        return;
	    }
	}
	
	private void turnoComputadora() {
		for( int i = 0; i < manoComputadora.size(); i++ ) {
			Carta carta = manoComputadora.getCarta(i);
			
			if( carta.esJugableSobre(cartaMesa) ) {
				manoComputadora.jugarCarta(i);
				System.out.println( "Computadora juega: " + carta );
				cartaMesa = carta;
				
				aplicarEfecto( carta, false );
				verificarUNO( manoComputadora, false );
				return; 
			}
		}
		Carta robada = baraja.robarCarta();
		manoComputadora.agregarCarta( robada );
		
		System.out.println( "Computadora roba carta." );
	}
	
	private void aplicarEfecto( Carta carta, boolean esJugador ) {
		switch( carta.getTipo() ) {
		case SALTO:
		case REVERSA:
			saltarTurno = true;
			break;
			
		case ROBA2:
			System.out.println( "Se roban 2 cartas." );
			for( int i = 0; i < 2; i++ ) {
				if( esJugador )
					manoComputadora.agregarCarta( baraja.robarCarta() );
				else
					manoJugador.agregarCarta( baraja.robarCarta() );
			}
			
			saltarTurno = true;
			break;
			
		case ROBA4:
			System.out.println("Se roban 4 cartas.");
			for (int i = 0; i < 4; i++) {
				if ( esJugador )
					manoComputadora.agregarCarta(baraja.robarCarta());
				else
					manoJugador.agregarCarta(baraja.robarCarta());
			}
			
			saltarTurno = true;
			
		case COMODIN:
			if (esJugador) {
				String color;

				while (true) {
				    System.out.println("Elige nuevo color (rojo, azul, verde, amarillo): ");
				    color = scanner.next().toLowerCase();

				    if (color.equals("rojo") || color.equals("azul") ||
				        color.equals("verde") || color.equals("amarillo")) {
				        break;
				    }

				    System.out.println("Color invalido. Intenta de nuevo.");
				}
				
				carta.setColor(color);
			}
			else {
				String[] colores = {"rojo", "azul", "verde", "amarillo"};
				String color = colores[new Random().nextInt(4)];
				carta.setColor( color );
				
				System.out.println("Computadora cambia a: " + color);
			}
			break;
		
		default:
			break;
		}
	}
	
	private void verificarUNO( Mano mano, boolean esJugador ) {
		if ( mano.size() == 1 ) {

	        if ( esJugador ) {
	            System.out.println( "Tienes una carta. Escribe 'UNO' para decir UNO:" );

	            String respuesta = scanner.next();

	            if ( !respuesta.equalsIgnoreCase("UNO") ) {
	                System.out.println( "No dijiste UNO. Robas 2 cartas." );
	                for (int i = 0; i < 2; i++) {
	                    manoJugador.agregarCarta( baraja.robarCarta() );
	                }
	            } else {
	                System.out.println( "¡UNO!" );
	            }

	        } else {
	            // Computadora (puedes hacerlo automático o con probabilidad)
	            System.out.println("Computadora dice: ¡UNO!");
	        }
	    }
	}
}
