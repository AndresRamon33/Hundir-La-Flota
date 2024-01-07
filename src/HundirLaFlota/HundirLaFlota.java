package HundirLaFlota;

import java.util.Scanner;

import java.util.Iterator;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Random;

public class HundirLaFlota {
	public static final String RESET = "\u001B[0m";
	public static final String ROJO = "\u001B[31m";
	public static final String VERDE = "\u001B[32m";
	public static final String AZUL = "\u001B[34m";
	public static final String NARANJA = "\u001B[38;5;208m";
	public static final String GRIS = "\u001B[38;5;240m";
	// public static final String GRIS_OSCURO = "\u001B[38;5;240m";
	// public static final String GRIS_CLARO = "\u001B[38;5;250m";
	public static final String barco = "■";

	// PARA ASEGURARSE QUE ES LA PRIMERA FICHA DEL BARCO Y QUE SE PUEDA PONER EN
	// CUALQUIER LUGAR DEL MAPA, TENGO HAMBRE COMO ENTRAN UNOS MACARRONES
	static int comienzoBarco = 0;

	// NOS ASEGURAMOS QUE SOLO SE PONEN EN UNA DIRECCIÓN
	static int direccionHorizontal = 0;
	static int direccionVertical = 0;

	// RELLENA LA MATRIZ PARA EL RIVAL SIEMPRE ALEATORIA
	static boolean matrizAleatoria = true;

	// CREAR LISTA PARA COMPROBAR EL TOCADO Y HUNDIDO
	static List<String> ListaPos = new ArrayList<>();
	static List<String> ListaPosRival = new ArrayList<>();
	static List<String> ListaPosaux = new ArrayList<>();

	// DETECTAR QUE ES PRIMERA VEZ
	static boolean primeraVez = true;

	// EVITAR QUE EL PROGRAMA LLAME A LA FUNCION UNA Y OTRA VEZ, POR LO MENOS
	// EVITAR QUE SE VEA POR PANTALLA
	static int evitarRecursividad = 1;
	static boolean activarRecursividad = false;

	// ACTIVAR MODO TRAMPOSO
	static boolean tramposo = false;

	// DIFICULTAD BOT
	static boolean dificultadFacil = false;

	// BOT:
	static boolean encontroUno = false; // PARA GENERAR O NO POS ALEATORIAS
	// PARA VER CUALES SON LAS POS DEL PRIMER TOCADO Y EL ULTIMO TOCADO REALIZADO,
	// DE ESTA FORMA VOLVEMOS A ACCEDER A ELLAS
	// CUNADO SE PRECISE
	static int resguardoCol = 0;
	static int resguardoFil = 0;
	static int ultimaCol = 0;
	static int ultimaFil = 0;
	// CONTROLAR EN QUE MOMENTO DE ADIVINAR BARCO SE ENCUENTRA
	static int numPosEncontradas = 0;
	// PARA ELEGIR DIRECCION
	static int direccionDespuesDeAdivinar = -1;
	// VECES FALLADAS POR LA MAQUINA
	static int fallosBot = 0;
	static int totalFrases = 1;
	static int trasncursoPartida = 10;
	static boolean acerierto = true;

	public static void main(String[] args) {

		Scanner sc = new Scanner(System.in);
		int lancha = 5;
		int buque = 3;
		int acorazado = 1;
		int portaaviones = 1;
		boolean seguir = true;
		while (seguir) {

			// REINICIAR TODAS LAS VARAIBLES ESTATICAS NECESARIAS

			ListaPosaux.clear();
			ListaPos.clear();
			ListaPosRival.clear();
			primeraVez = true;
			evitarRecursividad = 1;
			encontroUno = false;
			activarRecursividad = false;
			trasncursoPartida = 10;

			System.out.println();
			System.out.printf("%40s%n", "╔══════════════════╗");
			System.out.printf("%40s%n", "║      ELIGE:      ║");
			System.out.printf("%40s%n", "╠══════════════════╣");
			System.out.printf("%40s%n", "║ 1. Jugar         ║");
			System.out.printf("%40s%n", "║                  ║");
			System.out.printf("%40s%n", "║ 2. Dificultad    ║");
			System.out.printf("%40s%n", "║                  ║");
			System.out.printf("%40s%n", "║ 3. Extras        ║");
			System.out.printf("%40s%n", "║                  ║");
			System.out.printf("%40s%n", "║ 4. Salir         ║");
			System.out.printf("%40s%n", "╚══════════════════╝");
			try {
				int respuesta = sc.nextInt();

				switch (respuesta) {
				case 1:
					crearTablero(lancha, buque, acorazado, portaaviones);
					break;
				case 2:
					int d = elegirDificultad();

					if (d == 1) {
						lancha = 5;
						buque = 3;
						acorazado = 1;
						portaaviones = 1;

					} else if (d == 2) {
						lancha = 2;
						buque = 1;
						acorazado = 1;
						portaaviones = 1;

					} else if (d == 3) {
						lancha = 1;
						buque = 1;
						acorazado = 0;
						portaaviones = 0;
					}

					break;
				case 3:
					extras();
					break;
				case 4:
					System.out.println("¡Argh, gracias por participar en esta odisea, valiente corsario!");
					seguir = false;
					break;
				default:
					System.out
							.println("¡Elige una opción digna de un verdadero capitán, o prepárate para el abordaje!");

				}
			} catch (InputMismatchException e) {
				System.out.println("¡Elige una opción digna de un verdadero capitán, o prepárate para el abordaje!");
				sc.nextLine();// EVITAR BUCLE INFINITO OU MAMA HA COSTADO MAS DE LO QUE DEBERIA
			}
		}

	}

	public static void crearTablero(int l, int b, int z, int p) {

		// DECLARACION VARIABLES
		Scanner sc = new Scanner(System.in);

		if (evitarRecursividad == 0) {

		} else {
			String[][] tablero = new String[10][10];
			String[][] tableroRival = new String[10][10];
			String[][] tableroRivalHecho = new String[10][10];

			// PEDIR DATOS
			rellenarMatriz(tablero);
			rellenarMatriz(tableroRival);
			rellenarMatriz(tableroRivalHecho);
			// CREAR MATRIZ DEL RIVAL
			colocarFichas(tableroRivalHecho, l, b, z, p);

			// COPIAR ELEMENTOS A RIVAL Y BORRAR POSAUX POR SI EL USARIO QIERE UNA MATRIZ
			// RANDOM
			ListaPosRival.addAll(ListaPosaux);
			ListaPosaux.clear();

			if (evitarRecursividad != 0) {
				// HACEMOS QUE LE PIDA AL USUARIO
				if (primeraVez) {
					PreguntarUsario();
				}
				primeraVez = false;
				colocarFichas(tablero, l, b, z, p);

				// SI LA MATRIZ ES ALEATORIA LA ÑISTA DE POS ES LO RELLENADO CON POSAUX
				if (matrizAleatoria) {
					ListaPos.addAll(ListaPosaux);
				}

				// EVITAMOS QUE SE IMPRIMA UNA NUEVA MATRIZ POR CONSOLA
				matrizAleatoria = true;
			}

			if (evitarRecursividad != 0) {
				// CALCULAMOS EL TOTAL DE POSCIONES QUE HAY BARCOS PARA CONTROLAR EL FIN DE
				// PARTIDA
				int totalBarcos = (l * 1) + (b * 3) + (z * 4) + (p * 5);
				// System.out.println(totalBarcos);

				// AREGLAR LISTA POSRIVAL, NO SE PORQUE NO SE BOORAN LOS ELEMNTOS COPRRECTAMENTE
				// :(
				ListaPosRival = arreglarPosRival();

				// System.out.println("Elementos en el set: " + ListaPos);
				// System.out.println("Elementos en el Rival: " + ListaPosRival);
				if (!activarRecursividad) {
					System.out.println();
					System.out.println();
					System.out.println();
				}

				System.out.println("Que la batalla comience, ¡pulsa Enter para continuar la travesía en estos mares plagados de astucia y cañonazos! ");
				sc.nextLine();

				imprimirColores();
				if (!tramposo) {
					imprimirMatriz(tablero, tableroRival);
				} else {
					imprimirMatrizTramposo(tablero, tableroRival, tableroRivalHecho);
				}

				System.out.println();
				System.out.println();
				System.out.println();

				jugar(tablero, tableroRival, tableroRivalHecho, totalBarcos);

			}
		}
	}

	public static void jugar(String tablero[][], String tableroRival[][], String[][] tableroRivalHecho, int total) {
		Scanner sc = new Scanner(System.in);

		// DECLARACION DE VARAIBLES
		String pos = null;
		boolean fin = false;
		boolean entradaValida = false;
		int fila = 0;
		int columna = 0;
		String columnaString;
		// boolean rival=false;

		// BUCLE HASTA QUE SE ACABE LA PARTIDA
		while (!fin) {
			while (!entradaValida) {
				try {
					System.out.print("Introduce una posición: ");
					pos = sc.next();
					pos = pos.toUpperCase();
					// OBTENER INFO, BUENA DATA CRACK
					fila = obtenerNumero(pos);
					fila -= 1;
					columnaString = obtenerLetra(pos);
					columna = cambiarLetra(columnaString);

					// COMPROBAR QUE ESTA TODO OK
					if (fila < 0 || fila >= 10 || columna < 0 || columna >= 10
							|| !tableroRival[columna][fila].equals("~")) {
						throw new IllegalArgumentException("Posición inválida.");
					}

					// LA ENTRADA ES VALIDA SALGO DEL FUCKING BUCLE SUUUUU
					entradaValida = true;

				} catch (StringIndexOutOfBoundsException | IllegalArgumentException e) {
					// MANEJO LA EXCEPCON SI LA ENTRADA ES INCORRECTA Y VOLVEMOS
					if (pos.isEmpty()) {
						imprimirColores();
						if (!tramposo) {
							imprimirMatriz(tablero, tableroRival);
						} else {
							imprimirMatrizTramposo(tablero, tableroRival, tableroRivalHecho);
						}
					} else {
						System.out.print("Por favor, introduce una posición válida.\n");
						System.out.print("Para números, utiliza valores entre 1 y 10.\n");
						System.out.print("Para letras, utiliza valores entre A y J.  EJEMPLO: B2.\n");

						imprimirColores();
						if (!tramposo) {
							imprimirMatriz(tablero, tableroRival);
						} else {
							imprimirMatrizTramposo(tablero, tableroRival, tableroRivalHecho);
						}
					}

				}
			}
			// PONER AL TABLERO
			if (tableroRivalHecho[columna][fila].equals("~")) {
				System.out.println();
				if (trasncursoPartida < 20) {
					trasncursoPartida += totalFrases;
				}
				acerierto = false;
				System.out.println("AGUA");
				System.out.println();
				tableroRival[columna][fila] = "O";
			} else if (tableroRivalHecho[columna][fila].equals((ROJO + barco + RESET))
					|| tableroRivalHecho[columna][fila].equals((AZUL + barco + RESET))
					|| tableroRivalHecho[columna][fila].equals((VERDE + barco + RESET))
					|| tableroRivalHecho[columna][fila].equals((NARANJA + barco + RESET))) {
				if (trasncursoPartida > 0) {
					trasncursoPartida -= totalFrases;
				}
				acerierto = true;
				comprobarTocadoHundido(pos, ListaPosRival);
				tableroRival[columna][fila] = (GRIS + barco + RESET);
			}

			imprimirColores();
			if (!tramposo) {
				imprimirMatriz(tablero, tableroRival);
			} else {
				imprimirMatrizTramposo(tablero, tableroRival, tableroRivalHecho);
			}
			System.out.println();
			System.out.println();
			System.out.println();

			// }

			// RESTABLECER BIIIIEN
			entradaValida = false;

			fin = comprobarFinalPartida(tableroRival, total);

			if (fin) {
				
				System.out.println("FIN DE PARTIDA");
				System.out.println();
				System.out.println("¡HAS GANADO!");
				System.out.println();
				System.out.println();
				
				if (dificultadFacil && tramposo ) {
					System.out.println("¡Arrr, has salido victorioso en la batalla más fácil que podrías haber enfrentado! ¿Te embriaga el orgullo, valiente de pacotilla?");
				}else if(dificultadFacil && !tramposo) {
					System.out.println("¡Vaya, has vencido a mi primo! ¿No piensas que es hora de buscar un desafío más formidable, o prefieres seguir rondando por aguas tranquilas? ");
				}else if(!dificultadFacil && tramposo){
					System.out.println("Me venciste. ¿Orgulloso? Ni un ápice, pues una victoria con trampas es como un cofre de tesoros sin joyas auténticas. Toma tu victoria hueca, ¡que mi honor pirata sigue intacto!");
				}else if(!dificultadFacil && !tramposo) {
					System.out.println("¡Honor y respeto, valiente capitán! Ha sido una batalla justa y limpia. Mis más sinceras felicitaciones por tu valentía en la refriega.");
				}
				System.out.println();
				System.out.println();
				primeraVez = true;
				evitarRecursividad = 0;
				break;
			}
			sc.nextLine();

			if (!dificultadFacil) {
				generarFrasesTurnoJUgador();
				System.out.println();
			}

			System.out.println("Pulsa enter para continuar:");
			sc.nextLine();
			System.out.println("TURNO DEL RIVAL: ");
			System.out.println();
			System.out.println();
			String posRival = "";
			if (dificultadFacil) {
				boolean cont = true;

				while (cont) {
					fila = (int) (Math.random() * 10);
					columna = (int) (Math.random() * 10);
					if (tablero[columna][fila].equals(GRIS + barco + RESET) || tablero[columna][fila].equals("O")) {

					} else {
						cont = false; //
					}
				}

				String columnaPasada = pasarNumeroALetra(columna);
				String filaPasada = pasarNumeroANumeroString(fila);
				posRival = columnaPasada + filaPasada;

				if (tablero[columna][fila].equals("~")) {
					System.out.println();
					System.out.println("Eligo: " + posRival);
					if (trasncursoPartida > 0) {
						trasncursoPartida -= totalFrases;
					}
					acerierto = false;
					System.out.println("AGUA");
					System.out.println();
					tablero[columna][fila] = "O";
				} else if (tablero[columna][fila].equals((ROJO + barco + RESET))
						|| tablero[columna][fila].equals((AZUL + barco + RESET))
						|| tablero[columna][fila].equals((VERDE + barco + RESET))
						|| tablero[columna][fila].equals((NARANJA + barco + RESET))) {
					System.out.println("Eligo " + posRival);
					if (trasncursoPartida < 20) {
						trasncursoPartida += totalFrases;
					}
					acerierto = true;
					comprobarTocadoHundido(posRival, ListaPos);
					tablero[columna][fila] = (GRIS + barco + RESET);
				}

			} else {
				jugarRival(tablero);
			}

			imprimirColores();
			if (!tramposo) {
				imprimirMatriz(tablero, tableroRival);
			} else {
				imprimirMatrizTramposo(tablero, tableroRival, tableroRivalHecho);
			}
			System.out.println();
			System.out.println();
			System.out.println();

			fin = comprobarFinalPartida(tablero, total);

			if (fin) {
				System.out.println("FIN DE PARTIDA");
				System.out.println();
				System.out.println("¡HAS PERDIDO!");
				System.out.println();
				System.out.println();
				
				if (dificultadFacil && tramposo ) {
					System.out.println("Arrr, perdiste! ¿Cómo pudiste dejar escapar esto? ¡Era imposible perder, por los siete mares! ¿Eres tan malo que ni siquiera puedes ganar esto? En fin, te lo mereces por tramposo.");
				}else if(dificultadFacil && !tramposo) {
					System.out.println("¡Ahoy, mira qué sorpresa! Es la primera vez que veo a mi primo triunfar. Parece que esto no es lo tuyo, amigo. Quizás deberías afinar tus habilidades con un poco más de práctica");
				}else if(!dificultadFacil && tramposo){
					System.out.println("Ahoy, rufián de pacotilla, ¡toma yaaa! Ni con tus artimañas trapaceras has logrado doblegarme. Soy el gran Capitán Bucanero Putero, con 8000 hombres bajo mi mando.");
				}else if(!dificultadFacil && !tramposo) {
					System.out.println("Argh, la victoria es mía, ¿qué esperabas? Mis otros 920 triunfos brillan como el tesoro más preciado, confirmando que soy el mejor capitán de los siete mares. Vuelve cuando no seas más que un lastimoso perdedor.");
				}
				System.out.println();
				System.out.println();
				System.out.println("Este era mi grandioso campo de batalla que no has podido derrotar:");
				imprimirMatrizJugador(tableroRivalHecho);
				System.out.println("Pulsa enter para continuar:");
				sc.nextLine();
				primeraVez = true;
				evitarRecursividad = 0;
				break;
			}

			if (!dificultadFacil) {
				generarFrasesTurnoRival();
				System.out.println();
			}

		}

	}

	public static void imprimirColores() {

		System.out.println("LANCHA:       1 " + ROJO + barco + RESET);
		System.out.println("BUQUE:        3 " + VERDE + barco + RESET);
		System.out.println("ACORAZADO:    4 " + AZUL + barco + RESET);
		System.out.println("PORTAAVIONES: 5 " + NARANJA + barco + RESET);

	}

	public static void imprimirMatriz(String[][] m, String[][] m2) {

		System.out.printf("%33sJugador:\t\t%27sBucanero Putero:\n", "", "");
		char letra = 'A';
		// IMPRIMIR NUMEROS
		System.out.printf("%18s", " ");
		for (int i = 1; i <= 10; i++) {
			System.out.print(i + "   ");
		}
		System.out.print("              ");
		for (int i = 1; i <= 10; i++) {
			System.out.print(i + "   ");
		}
		System.out.println();

		// IMPRIMIR MATRIZ
		for (int i = 0; i < m.length; i++) {
			System.out.print("              " + letra + "   ");
			for (int j = 0; j < m.length; j++) {
				System.out.print(m[i][j] + "   ");
			}
			// OTRA MATRIZ
			System.out.print("           " + letra + "   ");

			for (int j = 0; j < m2.length; j++) {
				System.out.print(m2[i][j] + "   ");
			}

			System.out.println();
			letra++;
		}
		System.out.println();
		System.out.println();
	}

	public static void imprimirMatrizTramposo(String[][] m, String[][] m2, String[][] m3) {

		System.out.printf("%28sEl Sinvergüenza:\t\t%27sBucanero Putero:", "", "");
		imprimirMensajesAleatorios();
		char letra = 'A';

		// IMPRIMIR NUMEROS
		System.out.printf("%18s", " ");
		for (int i = 1; i <= 10; i++) {
			System.out.print(i + "   ");
		}
		System.out.print("              ");
		for (int i = 1; i <= 10; i++) {
			System.out.print(i + "   ");
		}
		System.out.print("              ");
		for (int i = 1; i <= 10; i++) {
			System.out.print(i + "   ");
		}
		System.out.println();

		// IMPRIMIR MATRIZ
		for (int i = 0; i < m.length; i++) {
			System.out.print("              " + letra + "   ");
			for (int j = 0; j < m.length; j++) {
				System.out.print(m[i][j] + "   ");
			}
			// OTRA MATRIZ
			System.out.print("           " + letra + "   ");
			for (int j = 0; j < m2.length; j++) {
				System.out.print(m2[i][j] + "   ");
			}
			// MATRIZ m3
			System.out.print("           " + letra + "   ");
			for (int j = 0; j < m3.length; j++) {
				System.out.print(m3[i][j] + "   ");
			}

			System.out.println();
			letra++;
		}
		System.out.println();
		System.out.println();

	}

	public static void rellenarMatriz(String[][] m) {

		for (int i = 0; i < m.length; i++) {
			for (int j = 0; j < m.length; j++) {
				m[i][j] = "~";

			}
		}
	}

	public static int obtenerNumero(String s) {
		// APARTIR DE LA POS 1
		int numero = Integer.parseInt(s.substring(1));
		return numero;
	}

	public static String obtenerLetra(String s) {

		char letra = s.toUpperCase().charAt(0);
		return Character.toString(letra);

	}

	public static int cambiarLetra(String s) {
		int n = 0;

		switch (s) {
		case "A":
			n = 0;
			break;
		case "B":
			n = 1;
			break;
		case "C":
			n = 2;
			break;
		case "D":
			n = 3;
			break;
		case "E":
			n = 4;
			break;
		case "F":
			n = 5;
			break;
		case "G":
			n = 6;
			break;

		case "H":
			n = 7;
			break;

		case "I":
			n = 8;
			break;

		case "J":
			n = 9;
			break;

		default:
			n = 10;

		}
		return n;
	}

	public static String pasarNumeroALetra(int n) {

		String s = "";

		switch (n) {
		case 0:
			s = "A";
			break;
		case 1:
			s = "B";
			break;
		case 2:
			s = "C";
			break;
		case 3:
			s = "D";
			break;
		case 4:
			s = "E";
			break;
		case 5:
			s = "F";
			break;
		case 6:
			s = "G";
			break;
		case 7:
			s = "H";
			break;
		case 8:
			s = "I";
			break;
		case 9:
			s = "J";
			break;

		}

		return s;

	}

	public static String pasarNumeroANumeroString(int n) {

		n += 1;
		String s = n + "";

		return s;

	}

	public static void imprimirMatrizJugador(String m[][]) {
		System.out.println();
		System.out.printf("%58sColoca las fichas:\n", "");
		char letra = 'A';

		// IMPRIMIR NUMEROS
		System.out.printf("%48s", " ");
		for (int i = 1; i <= 10; i++) {
			System.out.print(i + "   ");
		}
		System.out.println();

		// IMPRIMIR MATRIZ
		for (int i = 0; i < m.length; i++) {
			if (letra == 'C') {
				System.out.print("          LANCHA:       1 " + ROJO + barco + RESET + "                 C   ");
			} else if (letra == 'D') {
				System.out.print("          BUQUE:        3 " + VERDE + barco + RESET + "                 D   ");
			} else if (letra == 'E') {
				System.out.print("          ACORAZADO:    4 " + AZUL + barco + RESET + "                 E   ");
			} else if (letra == 'F') {
				System.out.print("          PORTAAVIONES: 5 " + NARANJA + barco + RESET + "                 F   ");
			} else {
				System.out.printf("%44s%s   ", "", letra);
			}
			for (int j = 0; j < m.length; j++) {
				System.out.print(m[i][j] + "   ");
			}
			System.out.println();
			letra++;
		}
		System.out.println();
		System.out.println();

	}

	public static void eliminarTablero(int l, int b, int z, int p, String m[][]) {

		rellenarMatriz(m);
		ListaPos.clear();
		colocarFichas(m, l, b, z, p);
	}

	public static void imprimirMensajesAleatorios() {
		String[] mensajes = { "                                       Nadie te quiere:",
				"                                    Cenas solo en Navidad:",
				"                         ¿Te gusta hacer trampa o solo no sabes jugar?",
				"                               Mi sobrinito juega mejor que tú:",
				"                                     No tienes amigos: ",
				"                                     Ets un cap de suro: ",
				"                              Juegas a esto solo, por algo será...",
				"                                      Cómprate una vida: ",
				"                                         Illojuaner: ",
				"                                  Nadie quiere acercarse a ti: ",
				"                                       Tu perro te odia: ",
				"                                 PantaloncillosMcbueniceillos:",
				"                                    Jugador en Decadencia: ",
				"                                           Caes Mal: ",
				"                                  Bombardeen Renfe Cercanias:" };

		Random random = new Random();
		int numeroAleatorio = random.nextInt(15);
		System.out.println(mensajes[numeroAleatorio]);
	}

	public static String[][] colocarFichas(String[][] m, int l, int b, int z, int p) {

		String[][] espejo = new String[10][10];

		// LANCHAS
		for (int j = 0; j < l; j++) {
			rellenarMatriz(espejo);
			comienzoBarco = 0;
			direccionVertical = 0;
			direccionHorizontal = 0;
			if (!matrizAleatoria && !activarRecursividad) {
				System.out.print("LANCHA " + (j + 1) + ":");
				ListaPos.add("|");
			} else {
				ListaPosaux.add("|");
			}
			for (int i = 0; i < 1; i++) {
				if (!matrizAleatoria && !activarRecursividad) {
					imprimirMatrizJugador(m);
				}

				pedirEntrada(m, espejo, ROJO, l, b, z, p);
				comienzoBarco++;
				if (!matrizAleatoria) {
					System.out.println();
				}

			}
		}

		// BUQUES
		for (int j = 0; j < b; j++) {
			rellenarMatriz(espejo);
			comienzoBarco = 0;
			direccionVertical = 0;
			direccionHorizontal = 0;
			if (!matrizAleatoria && !activarRecursividad) {
				System.out.print("BUQUE " + (j + 1) + ":");
				ListaPos.add("|");
			} else {
				ListaPosaux.add("|");
			}
			for (int i = 0; i < 3; i++) {
				if (!matrizAleatoria && !activarRecursividad) {
					imprimirMatrizJugador(m);
				}
				pedirEntrada(m, espejo, VERDE, l, b, z, p);
				comienzoBarco++;
				if (!matrizAleatoria) {
					System.out.println();
				}

			}
		}
		// ACORAZADO
		for (int j = 0; j < z; j++) {
			rellenarMatriz(espejo);
			comienzoBarco = 0;
			direccionVertical = 0;
			direccionHorizontal = 0;
			if (!matrizAleatoria && !activarRecursividad) {
				System.out.print("ACORAZADO " + (j + 1) + ":");
				ListaPos.add("|");
			} else {
				ListaPosaux.add("|");
			}
			for (int i = 0; i < 4; i++) {
				if (!matrizAleatoria && !activarRecursividad) {
					imprimirMatrizJugador(m);
				}
				pedirEntrada(m, espejo, AZUL, l, b, z, p);
				comienzoBarco++;
				if (!matrizAleatoria) {
					System.out.println();
				}

			}
		}

		// PORTAAVIONES
		for (int j = 0; j < p; j++) {
			rellenarMatriz(espejo);
			comienzoBarco = 0;
			direccionVertical = 0;
			direccionHorizontal = 0;
			if (!matrizAleatoria && !activarRecursividad) {
				System.out.print("PORTAAVIONES " + (j + 1) + ":");
				ListaPos.add("|");
			} else {
				ListaPosaux.add("|");
			}
			for (int i = 0; i < 5; i++) {
				if (!matrizAleatoria && !activarRecursividad) {
					imprimirMatrizJugador(m);
				}
				pedirEntrada(m, espejo, NARANJA, l, b, z, p);
				comienzoBarco++;
				if (!matrizAleatoria) {
					System.out.println();
				}

			}
		}

		if (matrizAleatoria) {
			ListaPosaux.add("|");
		} else if (!matrizAleatoria && !activarRecursividad) {
			ListaPos.add("|");
		}else {
			
		}

		return m;
	}

	public static String[][] pedirEntrada(String[][] m, String espejo[][], String color, int l, int b, int z, int p) {

		Scanner sc = new Scanner(System.in);
		int contador = 1;
		int fila = 0;
		int columna = 0;
		int intentosFallidos = 0;
		String columnaString;
		boolean entradaValida;
		boolean borrarBarco = true;
		String posRival = "";

		// SI EL USARIO FALLA, QUE NO CUENTE EL TURNO
		while (contador > 0) {
			try {

				// YA NO SE QUE ESTOY HACIENDO PERO FUNCIONA HACIENDO ESTO NOS EVITAMOS QUE LA
				// CONSOLA
				// TENGA QUE PROCESAR TODO CREO, Y TARDA MENOS EN RECORRER EL PROGRAMA
				if (evitarRecursividad == 0) {
					break;
				}
				String pos = "";

				if (!matrizAleatoria) {
					if (activarRecursividad) {
						break;
					}
					System.out.print("Introduce posición: ");
					pos = sc.nextLine();
					pos = pos.toUpperCase();
				} else {
					// MATRIZ ALEATORIA
					fila = (int) (Math.random() * 10);
					columna = (int) (Math.random() * 10);
				}

				// BORRAR TODA LA MATRIZ
				if (pos.equals("BORRAR")) {
					System.out.println();
					System.out.println();
					eliminarTablero(l, b, z, p, m);
					activarRecursividad = true;

				}

				// BORRAR EL ULTIMO BARCO

				// OBTENER INFO, BUENA DATA CRACK
				if (!matrizAleatoria) {
					fila = obtenerNumero(pos);
					fila -= 1;
					columnaString = obtenerLetra(pos);
					columna = cambiarLetra(columnaString);
				}
				entradaValida = comprobarBarcoEsCorrecto(m, espejo, fila, columna, color);

				// COMPROBAR QUE ESTA TODO OK
				if (!entradaValida) {
					throw new IllegalArgumentException("Posición inválida.");
				}
				// LA ENTRADA ES VALIDA SALGO DEL FUCKING BUCLE SUUUUU

				m[columna][fila] = (color + barco + RESET);
				espejo[columna][fila] = (color + barco + RESET);
				if (!matrizAleatoria) {
					ListaPos.add(pos);
				} else if (matrizAleatoria) {
					String columnaPasada = pasarNumeroALetra(columna);
					String filaPasada = pasarNumeroANumeroString(fila);
					posRival = columnaPasada + filaPasada;
					ListaPosaux.add(posRival);
				}
				contador = 0;

			} catch (StringIndexOutOfBoundsException | IllegalArgumentException e) {
				// MANEJO LA EXCEPCON SI LA ENTRADA ES INCORRECTA Y VOLVEMOS INCREMENTANDO
				if (!matrizAleatoria && !activarRecursividad) {
					System.out.print("Por favor, introduce una posición válida.\n");
					System.out.print("Para números, utiliza valores entre 1 y 10.\n");
					System.out.print("Para letras, utiliza valores entre A y J.  EJEMPLO: B2.\n");
					System.out.print("Puedes poner BORRAR si quieres eliminar todo el tablero..\n");
					imprimirMatrizJugador(m);
				}
				intentosFallidos++;

				contador++;
			}

			if (intentosFallidos >= 100 && matrizAleatoria) {
				ListaPosaux.clear();
				crearTablero(l, b, z, p);
				// evitarBucleInfinito(l,b,z,p);

			}

		}
		return m;

	}

	public static boolean comprobarBarcoEsCorrecto(String[][] m, String espejo[][], int fila, int columna,
			String color) {

		if (fila < 0 || fila >= m.length || columna < 0 || columna >= m[0].length) {
			return false;
		}

		if (comienzoBarco <= 0 && m[columna][fila].equals("~")) {
			return true;

		} else if (!m[columna][fila].equals("~")) {
			if (!matrizAleatoria) {
				System.out.println("Ya hay un barco en esa posición.");
			}
			return false;

		} else if (espejo[columna][fila].equals("~") && direccionHorizontal == 0
				&& ((fila > 0 && espejo[columna][fila - 1].equals(color + barco + RESET))
						|| (fila < m.length - 1 && espejo[columna][fila + 1].equals(color + barco + RESET)))) {
			direccionVertical = 1;
			return true;
		} else if (espejo[columna][fila].equals("~") && direccionVertical == 0
				&& ((columna > 0 && espejo[columna - 1][fila].equals(color + barco + RESET))
						|| (columna < m[0].length - 1 && espejo[columna + 1][fila].equals(color + barco + RESET)))) {
			direccionHorizontal = 1;
			return true;

		}
		return false;

	}

	public static List<String> arreglarPosRival() {

		int cantidadElementos = ListaPos.size();

		// Verificar si la cantidad de elementos a conservar es menor que el tamaño
		// actual
		if (cantidadElementos < ListaPosRival.size()) {
			// Conservar solo los últimos cantidadElementos elementos
			List<String> nuevosElementos = ListaPosRival.subList(ListaPosRival.size() - cantidadElementos,
					ListaPosRival.size());

			// Crear una nueva lista y agregar los elementos conservados
			List<String> nuevaLista = new ArrayList<>(nuevosElementos);
			return nuevaLista;
		}

		return ListaPosRival;
	}

	public static void PreguntarUsario() {
		Scanner sc = new Scanner(System.in);
		boolean seguir = true;
		System.out.println(
				"¡Arrrr grumete! ¿Quieres forjar tu propio campo de batalla con tus propias manos y astucia    (s), ");
		System.out.println("o prefieres saquear uno que ya esté listo para la batalla?   (n)");
		System.out.println("¡Di tu palabra, y que los vientos de " + "la fortuna soplen a tu favor! (s/n)");
		while (seguir) {
			String respuesta = sc.nextLine();
			respuesta = respuesta.toLowerCase();
			if (respuesta.equals("s")) {
				seguir = false;
				matrizAleatoria = false;
			} else if (respuesta.equals("n")) {

				seguir = false;
				matrizAleatoria = true;
			} else {
				System.out.println("¡Has optado por una elección desacertada, ¿eres tonto?");
				System.out.println(
						"no disponemos de toda la jornada, decide con juicio o enfrenta las consecuencias (s/n).");
			}
		}

	}

	public static boolean comprobarFinalPartida(String m[][], int total) {

		int contador = 0;

		for (int i = 0; i < m.length; i++) {
			for (int j = 0; j < m.length; j++) {
				if (m[i][j].equals((GRIS + barco + RESET))) {
					contador++;
				}
			}
		}

		if (contador == total) {
			return true;
		} else {
			return false;
		}

	}

	public static void comprobarTocadoHundido(String s, List<String> lista) {
		int aux = lista.indexOf(s);

		if (aux >= 0 && aux < lista.size() - 1) {
			if (lista.get(aux - 1).equals("|") && lista.get(aux + 1).equals("|")) {
				System.out.println("TOCADO Y HUNDIDO");
				System.out.println();
				lista.remove(aux);
			} else {
				System.out.println("TOCADO");
				System.out.println();
				lista.remove(aux);
			}
		}
	}

	public static int elegirDificultad() {

		Scanner sc = new Scanner(System.in);
		boolean seguir = true;
		int dificultad = 0;
		while (seguir) {
			System.out.println();
			System.out.printf("%40s%n", "╔════════════════════════════════════════════════════════╗");
			System.out.printf("%40s%n", "║           ¿EN QUÉ TIPO DE AGUAS QUIERES LUCHAR?        ║");
			System.out.printf("%40s%n", "╠════════════════════════════════════════════════════════╣");
			System.out.printf("%40s%n", "║      1.-EMBOSCADA EN LAS RUTAS COMERCIALES (FÁCIL)     ║");
			System.out.printf("%40s%n", "║                                                        ║");
			System.out.printf("%40s%n", "║     2.-DESAFÍO EN LA RUTA NAVAL INTERMEDIA (NORMAL)    ║");
			System.out.printf("%40s%n", "║                                                        ║");
			System.out.printf("%40s%n", "║  3.-SILENCIO EN LA RUTA DE LOS NAUFRAGIOS (DIFÍCIL)    ║");
			System.out.printf("%40s%n", "║                                                        ║");
			System.out.printf("%40s%n", "║                4.- ¿EN QUÉ CONSISTEN?                  ║");
			System.out.printf("%40s%n", "╚════════════════════════════════════════════════════════╝");

			try {
				int respuesta = sc.nextInt();
				String cont;

				switch (respuesta) {
				case 1:
					dificultad = 1;
					System.out.println("▶ Dificultad establecida: FÁCIL.");
					totalFrases = 1;
					seguir = false;
					break;
				case 2:
					dificultad = 2;
					System.out.println("▶ Dificultad establecida: NORMAL.");
					totalFrases = 2;
					seguir = false;
					break;
				case 3:
					dificultad = 3;
					System.out.println("▶ Dificultad establecida: DIFÍCIL.");
					totalFrases = 4;
					seguir = false;
					break;
				case 4:
					System.out.println("Muy buena pregunta mi estimado perdedor, en el nivel más retador,"
							+ " solo dos barcos desafiarán los mares, una lancha y un buque.");
					System.out.println();
					System.out.println("En el nivel medio, cinco formidables barcos, con dos lanchas,"
							+ " un buque, un acorazado y un majestuoso portaaviones, se alinean para el enfrentamiento. ");
					System.out.println();
					System.out.println(
							"Y en el nivel más fácil, una auténtica flotilla de diez barcos, con cinco lanchas, tres buques,"
									+ " un acorazado y un imponente portaaviones, surcan las aguas. ¿Tienes el coraje para derribarlos todos?");
					System.out.println();
					System.out.println();
					System.out.println("TAMAÑO DE LOS BARCOS: ");
					System.out.println();
					imprimirColores();
					System.out.println();
					System.out.println();
					System.out.println("Pulsa enter para continuar: ");
					sc.nextLine();
					sc.nextLine();

					break;
				default:
					System.out
							.println("¡Elige una opción digna de un verdadero capitán, o prepárate para el abordaje!");
					System.out.printf("%40s%n", " ");
				}
			} catch (InputMismatchException e) {
				System.out.println("¡Elige una opción digna de un verdadero capitán, o prepárate para el abordaje!");
				sc.nextLine();// EVITAR BUCLE INFINITO OU MAMA HA COSTADO MAS DE LO QUE DEBERIA
			}

		}

		return dificultad;
	}

	public static void extras() {
		Scanner sc = new Scanner(System.in);
		boolean seguir = true;
		while (seguir) {
			System.out.println();
			System.out.printf("%40s%n", "╔════════════════════════════════╗");
			System.out.printf("%40s%n", "║      ¿QUÉ QUIERES HACER?       ║");
			System.out.printf("%40s%n", "╠════════════════════════════════╣");
			System.out.printf("%40s%n", "║       1.- TRUCO DEL MAR        ║");
			System.out.printf("%40s%n", "║                                ║");
			System.out.printf("%40s%n", "║   2.- INTELIGENCIA DEL RIVAL   ║");
			System.out.printf("%40s%n", "║                                ║");
			System.out.printf("%40s%n", "║     3.- ¿CÓMO SE JUEGA?        ║");
			System.out.printf("%40s%n", "║                                ║");
			System.out.printf("%40s%n", "║    4.- CHISTES DE PIRATAS      ║");
			System.out.printf("%40s%n", "║                                ║");
			System.out.printf("%40s%n", "║          5.- SALIR             ║");
			System.out.printf("%40s%n", "╚════════════════════════════════╝");

			try {
				int respuesta = sc.nextInt();
				String cont;

				switch (respuesta) {
				case 1:
					System.out.println();
					System.out.println("Ah, el Truco del Mar, ese maquiavélico artefacto ");
					System.out.println(
							"que permite a los codiciosos piratas vislumbrar los secretos de la flota enemiga. ");
					System.out.println("¡Pero que los vientos soplen a favor de quienes prefieren la batalla justa");
					System.out.println("y la victoria honrada, lejos de las argucias de esos botarates! ");
					System.out.println();
					System.out.println();

					boolean seguir2 = true;
					while (seguir2) {
						System.out.println("¿Y bien? ¿Qué escogerás, una batalla justa y limpia  (s)");
						System.out.println("o careces de afecto materno?  (n)");
						String respuesta2 = sc.next();
						respuesta2 = respuesta2.toLowerCase();

						if (respuesta2.equals("s")) {
							tramposo = false;
							System.out.println();
							System.out.println();
							System.out.print("¡Sabia elección! ");
							System.out.println();
							seguir2 = false;

						} else if (respuesta2.equals("n")) {
							tramposo = true;
							System.out.println();
							System.out.println();
							System.out.print("Tus padres son primos.");
							System.out.println();
							seguir2 = false;

						} else {
							System.out.println("¡Has optado por una elección desacertada, ¿eres tonto?");
							System.out.println(
									"no disponemos de toda la jornada, decide con juicio o enfrenta las consecuencias.");
						}
					}

					break;
				case 2:
					System.out.println();
					boolean seguir3 = true;
					while (seguir3) {
						System.out.println(
								"¿Te atreverías a desafiar a un intrépido capitán con más de 920 victorias  (s)");
						System.out.println(
								"o prefieres enfrentarte a mi primo, el borracho? Su mayor logro, mantenerse más de 5 segundos de pie.  (n)");
						String respuesta2 = sc.next();
						respuesta2 = respuesta2.toLowerCase();

						if (respuesta2.equals("s")) {
							dificultadFacil = false;
							System.out.println();
							System.out.println();
							System.out.print("¡Nuestra batalla será legendaria!");
							System.out.println();
							seguir3 = false;

						} else if (respuesta2.equals("n")) {
							dificultadFacil = true;
							System.out.println();
							System.out.println();
							System.out.print("No eres muy de retos, eh...");
							System.out.println();
							seguir3 = false;

						} else {
							System.out.println("¡Has optado por una elección desacertada, ¿eres tonto?");
							System.out.println(
									"no disponemos de toda la jornada, decide con juicio o enfrenta las consecuencias.");
						}
					}

					break;

				case 3:
					tutorial();
					break;
				case 4:
					chistes();
					break;

				case 5:

					seguir = false;
					break;
				default:
					System.out
							.println("¡Elige una opción digna de un verdadero capitán, o prepárate para el abordaje!");
					System.out.printf("%40s%n", " ");
				}
			} catch (InputMismatchException e) {
				System.out.println("¡Elige una opción digna de un verdadero capitán, o prepárate para el abordaje!");
				sc.nextLine();// EVITAR BUCLE INFINITO OU MAMA HA COSTADO MAS DE LO QUE DEBERIA
			}

		}

	}

	public static void jugarRival(String m[][]) {
		int fila = 0;
		int columna = 0;
		String posRival;
		int cont = 0;

		// 1
		if (!encontroUno) {
			// ELEGIR POSCION ALEATORIA SI NO HAY UN BARCO
			if (fallosBot <= 4) {
				while (true) {
					fila = (int) (Math.random() * 10);
					columna = (int) (Math.random() * 10);
					if (m[columna][fila].equals(GRIS + barco + RESET) || m[columna][fila].equals("O")) {

					} else {
						// NOS QUEDAMOS CON LA POS DEL PRIMER TOCADO, POR SI SE PRECISA
						resguardoCol = columna;
						resguardoFil = fila;
						break;
					}
				}
			} else {
				while (true) {
					fila = (int) (Math.random() * 10);
					columna = (int) (Math.random() * 10);
					if (m[columna][fila].equals(GRIS + barco + RESET) || m[columna][fila].equals("O")
							|| m[columna][fila].equals("~")) {

					} else {
						// NOS QUEDAMOS CON LA POS DEL PRIMER TOCADO, POR SI SE PRECISA
						resguardoCol = columna;
						resguardoFil = fila;
						break;
					}
				}

			}
			String columnaPasada = pasarNumeroALetra(columna);
			String filaPasada = pasarNumeroANumeroString(fila);
			posRival = columnaPasada + filaPasada;

			if (m[columna][fila].equals("~")) {
				System.out.println();
				System.out.println("Eligo: " + posRival);
				cont++;
				if (trasncursoPartida > 0) {
					trasncursoPartida -= totalFrases;
				}
				acerierto = false;
				System.out.println("AGUA");
				fallosBot++;
				System.out.println();
				m[columna][fila] = "O";

			} else if (m[columna][fila].equals((ROJO + barco + RESET))
					|| m[columna][fila].equals((AZUL + barco + RESET))
					|| m[columna][fila].equals((VERDE + barco + RESET))
					|| m[columna][fila].equals((NARANJA + barco + RESET))) {
				System.out.println("Eligo " + posRival);
				cont++;
				if (trasncursoPartida < 20) {
					trasncursoPartida += totalFrases;
				}
				acerierto = true;
				comprobarTocadoHundidoRival(posRival);
				m[columna][fila] = (GRIS + barco + RESET);
			}

			// 2
		} else if (encontroUno && numPosEncontradas < 2) {
			// ELEGIR LA SIGUIENTE POSICION DESPUES DE UN TOCADO
			int direccion = (int) (Math.random() * 4); // 0: ARRIBA, 1: ABAJO AL CENTRO Y PA DENTRO, 2: IZQUIERDA, 3:
			// DERECHA
			int nextFila = resguardoFil;
			int nextColumna = resguardoCol;
			int contador = 0;
			while (true) {
				if (contador >= 15) {
					break;
				}
				if (direccion == 0 && nextColumna - 1 >= 0
						&& (m[nextColumna - 1][nextFila].equals("~")
								|| m[nextColumna - 1][nextFila].equals((ROJO + barco + RESET))
								|| m[nextColumna - 1][nextFila].equals((VERDE + barco + RESET))
								|| m[nextColumna - 1][nextFila].equals((NARANJA + barco + RESET))
								|| m[nextColumna - 1][nextFila].equals((AZUL + barco + RESET)))) {
					// 🡩
					nextColumna--;
					break;
				} else if (direccion == 1 && nextColumna + 1 < 10
						&& (m[nextColumna + 1][nextFila].equals("~")
								|| m[nextColumna + 1][nextFila].equals((ROJO + barco + RESET))
								|| m[nextColumna + 1][nextFila].equals((VERDE + barco + RESET))
								|| m[nextColumna + 1][nextFila].equals((NARANJA + barco + RESET))
								|| m[nextColumna + 1][nextFila].equals((AZUL + barco + RESET)))) {
					// 🡣
					nextColumna++;
					break;
				} else if (direccion == 2 && nextFila - 1 >= 0
						&& (m[nextColumna][nextFila - 1].equals("~")
								|| m[nextColumna][nextFila - 1].equals((ROJO + barco + RESET))
								|| m[nextColumna][nextFila - 1].equals((VERDE + barco + RESET))
								|| m[nextColumna][nextFila - 1].equals((NARANJA + barco + RESET))
								|| m[nextColumna][nextFila - 1].equals((AZUL + barco + RESET)))) {
					// 🡠
					nextFila--;
					break;
				} else if (direccion == 3 && nextFila + 1 < 10
						&& (m[nextColumna][nextFila + 1].equals("~")
								|| m[nextColumna][nextFila + 1].equals((ROJO + barco + RESET))
								|| m[nextColumna][nextFila + 1].equals((VERDE + barco + RESET))
								|| m[nextColumna][nextFila + 1].equals((NARANJA + barco + RESET))
								|| m[nextColumna][nextFila + 1].equals((AZUL + barco + RESET)))) {
					// 🡢
					nextFila++;
					break;
				} else {
					// SI LA DIRECCION NO ES VALIDA ELEIGE OTRA
					direccion = (int) (Math.random() * 4);
					contador++;
				}
			}
			fila = nextFila;
			columna = nextColumna;
			// NOS QUEDAMOS CON LA ULTIMA POS PARA AVANZAR
			ultimaFil = fila;
			ultimaCol = columna;
			direccionDespuesDeAdivinar = direccion;

			String columnaPasada = pasarNumeroALetra(columna);
			String filaPasada = pasarNumeroANumeroString(fila);
			posRival = columnaPasada + filaPasada;

			if (m[columna][fila].equals("~")) {
				System.out.println();
				System.out.println("Eligo: " + posRival);
				cont++;
				if (trasncursoPartida > 0) {
					trasncursoPartida -= totalFrases;
				}
				acerierto = false;
				System.out.println("AGUA");
				System.out.println();
				m[columna][fila] = "O";
			} else if (m[columna][fila].equals((ROJO + barco + RESET))
					|| m[columna][fila].equals((AZUL + barco + RESET))
					|| m[columna][fila].equals((VERDE + barco + RESET))
					|| m[columna][fila].equals((NARANJA + barco + RESET))) {
				System.out.println("Eligo " + posRival);
				cont++;
				if (trasncursoPartida < 20) {
					trasncursoPartida += totalFrases;
				}
				acerierto = true;
				comprobarTocadoHundidoRival(posRival);
				m[columna][fila] = (GRIS + barco + RESET);
			}

			// 3

		} else if (encontroUno && numPosEncontradas >= 2) {
			int nextFila = ultimaFil;
			int nextColumna = ultimaCol;

			if (direccionDespuesDeAdivinar == 0 && nextColumna - 1 >= 0
					&& (m[nextColumna - 1][nextFila].equals("~")
							|| m[nextColumna - 1][nextFila].equals((ROJO + barco + RESET))
							|| m[nextColumna - 1][nextFila].equals((VERDE + barco + RESET))
							|| m[nextColumna - 1][nextFila].equals((NARANJA + barco + RESET))
							|| m[nextColumna - 1][nextFila].equals((AZUL + barco + RESET)))) {
				// 🡩
				nextColumna--;
			} else

			if (direccionDespuesDeAdivinar == 1 && nextColumna + 1 < 10
					&& (m[nextColumna + 1][nextFila].equals("~")
							|| m[nextColumna + 1][nextFila].equals((ROJO + barco + RESET))
							|| m[nextColumna + 1][nextFila].equals((VERDE + barco + RESET))
							|| m[nextColumna + 1][nextFila].equals((NARANJA + barco + RESET))
							|| m[nextColumna + 1][nextFila].equals((AZUL + barco + RESET)))) {
				// 🡣
				nextColumna++;

			} else if (direccionDespuesDeAdivinar == 2 && nextFila - 1 >= 0
					&& (m[nextColumna][nextFila - 1].equals("~")
							|| m[nextColumna][nextFila - 1].equals((ROJO + barco + RESET))
							|| m[nextColumna][nextFila - 1].equals((VERDE + barco + RESET))
							|| m[nextColumna][nextFila - 1].equals((NARANJA + barco + RESET))
							|| m[nextColumna][nextFila - 1].equals((AZUL + barco + RESET)))) {
				// 🡠
				nextFila--;

			} else

			if (direccionDespuesDeAdivinar == 3 && nextFila + 1 < 10
					&& (m[nextColumna][nextFila + 1].equals("~")
							|| m[nextColumna][nextFila + 1].equals((ROJO + barco + RESET))
							|| m[nextColumna][nextFila + 1].equals((VERDE + barco + RESET))
							|| m[nextColumna][nextFila + 1].equals((NARANJA + barco + RESET))
							|| m[nextColumna][nextFila + 1].equals((AZUL + barco + RESET)))) {
				// 🡢
				nextFila++;

			} else {
				int cont2 = 0;
				nextFila = resguardoFil;
				nextColumna = resguardoCol;
				direccionDespuesDeAdivinar = (int) (Math.random() * 4);

				while (true) {
					if (cont2 >= 15) {
						break;
					} else if (direccionDespuesDeAdivinar == 0 && nextColumna - 1 >= 0
							&& (m[nextColumna - 1][nextFila].equals("~")
									|| m[nextColumna - 1][nextFila].equals((ROJO + barco + RESET))
									|| m[nextColumna - 1][nextFila].equals((VERDE + barco + RESET))
									|| m[nextColumna - 1][nextFila].equals((NARANJA + barco + RESET))
									|| m[nextColumna - 1][nextFila].equals((AZUL + barco + RESET)))) {
						// 🡩
						nextColumna--;
						break;
					} else if (direccionDespuesDeAdivinar == 1 && nextColumna + 1 < 10
							&& (m[nextColumna + 1][nextFila].equals("~")
									|| m[nextColumna + 1][nextFila].equals((ROJO + barco + RESET))
									|| m[nextColumna + 1][nextFila].equals((VERDE + barco + RESET))
									|| m[nextColumna + 1][nextFila].equals((NARANJA + barco + RESET))
									|| m[nextColumna + 1][nextFila].equals((AZUL + barco + RESET)))) {
						// 🡣
						nextColumna++;
						break;
					} else if (direccionDespuesDeAdivinar == 2 && nextFila - 1 >= 0
							&& (m[nextColumna][nextFila - 1].equals("~")
									|| m[nextColumna][nextFila - 1].equals((ROJO + barco + RESET))
									|| m[nextColumna][nextFila - 1].equals((VERDE + barco + RESET))
									|| m[nextColumna][nextFila - 1].equals((NARANJA + barco + RESET))
									|| m[nextColumna][nextFila - 1].equals((AZUL + barco + RESET)))) {
						// 🡠
						nextFila--;
						break;
					} else if (direccionDespuesDeAdivinar == 3 && nextFila + 1 < 10
							&& (m[nextColumna][nextFila + 1].equals("~")
									|| m[nextColumna][nextFila + 1].equals((ROJO + barco + RESET))
									|| m[nextColumna][nextFila + 1].equals((VERDE + barco + RESET))
									|| m[nextColumna][nextFila + 1].equals((NARANJA + barco + RESET))
									|| m[nextColumna][nextFila + 1].equals((AZUL + barco + RESET)))) {
						// 🡢
						nextFila++;
						break;
					} else {
						// SI LA DIRECCION NO ES VALIDA ELEIGE OTRA
						direccionDespuesDeAdivinar = (int) (Math.random() * 4);
						cont2++;
					}

				}

			}
			fila = nextFila;
			columna = nextColumna;
			String columnaPasada = pasarNumeroALetra(columna);
			String filaPasada = pasarNumeroANumeroString(fila);
			posRival = columnaPasada + filaPasada;

			if (m[columna][fila].equals("~")) {
				System.out.println();
				System.out.println("Eligo: " + posRival);
				cont++;
				if (trasncursoPartida > 0) {
					trasncursoPartida -= totalFrases;
				}
				acerierto = false;
				System.out.println("AGUA");
				ultimaFil = resguardoFil;
				ultimaCol = resguardoCol;
				if (direccionDespuesDeAdivinar == 0) {
					direccionDespuesDeAdivinar = 1;
				} else if (direccionDespuesDeAdivinar == 1) {
					direccionDespuesDeAdivinar = 0;
				} else if (direccionDespuesDeAdivinar == 2) {
					direccionDespuesDeAdivinar = 3;
				} else if (direccionDespuesDeAdivinar == 3) {
					direccionDespuesDeAdivinar = 2;
				}
				System.out.println();
				m[columna][fila] = "O";
			} else if (m[columna][fila].equals((ROJO + barco + RESET))
					|| m[columna][fila].equals((AZUL + barco + RESET))
					|| m[columna][fila].equals((VERDE + barco + RESET))
					|| m[columna][fila].equals((NARANJA + barco + RESET))) {
				ultimaFil = fila;
				ultimaCol = columna;
				if (trasncursoPartida < 20) {
					trasncursoPartida += totalFrases;
				}
				System.out.println("Eligo " + posRival);
				cont++;
				acerierto = true;
				comprobarTocadoHundidoRival(posRival);
				m[columna][fila] = (GRIS + barco + RESET);
			}

		}
		if (cont == 0) {
			// ULTIMO RECURSO
			// System.out.print("UltRecurso");
			while (true) {
				fila = (int) (Math.random() * 10);
				columna = (int) (Math.random() * 10);
				if (m[columna][fila].equals(GRIS + barco + RESET) || m[columna][fila].equals("O")
						|| m[columna][fila].equals("~")) {

				} else {
					// NOS QUEDAMOS CON LA POS DEL PRIMER TOCADO, POR SI SE PRECISA
					resguardoCol = columna;
					resguardoFil = fila;
					break;
				}
			}

			String columnaPasada = pasarNumeroALetra(columna);
			String filaPasada = pasarNumeroANumeroString(fila);
			posRival = columnaPasada + filaPasada;

			if (m[columna][fila].equals("~")) {
				System.out.println();
				System.out.println("Eligo: " + posRival);
				if (trasncursoPartida > 0) {
					trasncursoPartida -= totalFrases;
				}
				acerierto = false;
				System.out.println("AGUA");
				fallosBot++;
				System.out.println();
				m[columna][fila] = "O";

			} else if (m[columna][fila].equals((ROJO + barco + RESET))
					|| m[columna][fila].equals((AZUL + barco + RESET))
					|| m[columna][fila].equals((VERDE + barco + RESET))
					|| m[columna][fila].equals((NARANJA + barco + RESET))) {
				System.out.println("Eligo " + posRival);
				if (trasncursoPartida < 20) {
					trasncursoPartida += totalFrases;
				}
				acerierto = true;
				comprobarTocadoHundidoRival(posRival);
				m[columna][fila] = (GRIS + barco + RESET);

			}
		}

	}

	public static void comprobarTocadoHundidoRival(String s) {

		int aux = ListaPos.indexOf(s);

		if (aux >= 0 && aux < ListaPos.size() - 1) {
			if (ListaPos.get(aux - 1).equals("|") && ListaPos.get(aux + 1).equals("|")) {
				System.out.println("TOCADO Y HUNDIDO");
				numPosEncontradas = 0;
				fallosBot = 0;
				encontroUno = false;
				System.out.println();
				ListaPos.remove(aux);
			} else {
				System.out.println("TOCADO");
				numPosEncontradas++;
				encontroUno = true;
				fallosBot = 0;
				System.out.println();
				ListaPos.remove(aux);
			}
		}

	}

	public static void generarFrasesTurnoJUgador() {

		String[] acertoEmpatados = { "¡Argh, un golpe certero, pero la batalla aún no ha terminado!",
				"¡Tocaste mi barco, pero el verdadero desafío apenas comienza!",
				"¡Has acertado, pero en estas aguas, la verdadera prueba es mantener ese rumbo!",
				"Voy a quitarte esa sonrisa de la cara a cañonazos...", "La suerte del principiante, supongo...",
				"La balanza oscila, pero no voy a ceder terreno tan fácilmente.",
				"Un buen golpe, ¡pero aún estamos igualados!",
				"Un disparo normalito, tuviste suerte de que impactó en mi querido barco...",
				"No me preocupa, algún disparo tenías que acertar.",
				"¡Un buen disparo, pero esto no es más que el preludio de mi contraataque estratégico!",
				"Me diste... ¡pero mi espíritu pirata no se doblega fácilmente!",
				"No temo a un payaso como tú, ¡yarrr!",
				"Disfruta de tu hazaña pero esto está más parejo de lo que piensas.",
				"No tienes ni la más mínima idea de cómo lo has logrado.", "Esto no ha hecho más que empezar.",
				"Sé que cada movimiento cuenta... pero aún estamos empatados en esta batalla marina.",
				"¡Acertaste, pero mi bandera sigue ondeando y mi tripulación está lista para el contragolpe!",
				"¡Me has dado, pero la victoria no se regala, aún deberás ganártela con sangre sudor y lágrimas!",
				"Bien por ti...", "Puedo devolvértelo multiplicado por tres.",
				"No festejes, te recuerdo que seguimos igualados en esta contienda.",
				"Me diste ¡pero aún tengo más de un truco en mi cofre de estrategias!",
				"¡El enfrentamiento épico continúa, ninguno cede ante la marea!",
				"Mi flota podrá aguantar esto y mucho más.", "Bueno, ¡los mares son impredecibles, no lo olvides!",
				"¿Empatados? No por mucho tiempo, amigo mío. Mi astucia no conoce límites.",
				"¿Un barco perdido? Eso solo aumenta la emoción de mi inminente victoria.",
				"¿Me has dado? Bueno, ahora lamentarás tus acciones.",
				"Igualdad momentánea, pero ¿crees que podrás superar al rey de los mares?",
				"¡Eso no es más que una caricia para mi barco! La victoria será mía." };

		String[] falloEmpatados = { "¡Arr, parece que tienes menos puntería que un pulpo con parche!",
				"Jajaja, tu habilidad para encontrar barcos es tan mala como mi loro tratando de hablar.",
				"Jajaja, hasta los cangrejos en el fondo del mar se ríen de ti.",
				"No me has dado pero la batalla sigue siendo pareja ¿por qué?",
				"Incluso con tu poca habilidad de disparo, seguimos reñidos...",
				"Incluso con tu escasa destreza en el tiro, seguimos en pie de igualdad...",
				"¡Me temo que tus disparos están tan perdidos como un barco en la Niebla Espectral!",
				"¿Es este tu modo pirata de bailar con las olas? Porque no estás dando en nada.",
				"¡Ah, la legendaria táctica de disparar al azar, claro que sí!",
				"¡Vaya, parece que necesitas un curso intensivo en navegación y tiro al blanco!",
				"¡Ahoy, compañero! Aunque la suerte te haya traicionado, nuestra contienda sigue en punto muerto.",
				"¿Por qué la partida está tan enredada como una red de pesca?",
				"¡Arrggg! ¡Eres malisimo y aún así no consigo tener ventaja!",
				"¡Mis barcos te agradecen por proporcionarles una experiencia de spa con tanto agua!",
				"¡Mi abuelo me decía que este océano está lleno de barcos, pero parece que solo tú no puedes encontrar ninguno.",
				"Mi rival es un inútil... y aún así no logro sacarle ventaja, ¡maldición, arrrr!",
				"Gracias a tu error, sacaré una ventaja clara.", "¡Qué amable, pretende darme ventaja! Gracias.",
				"¿Eso fue un ataque o un intento de hacer música con el sonido de tus disparos?",
				"Tu error era previsible, tomaré la ventaja y con ella la victoria.",
				"Luchando torpemente, y aún así debo confesar que esto está nivelado.",
				"¿Igualada, dices? Parece que la marea está girando a mi favor.",
				"¿Tu brújula está rota o simplemente no sabes dónde apuntar?",
				"No me has dado. Aun así, el juego está tan parejo como el balance de un barco en alta mar. Esto no me gusta..",
				"Romperé esta igualada como pienso romper cada uno de tus barcos.",
				"Tus disparos pueden perderse en el agua, ¡pero la paridad persiste en nuestra batalla!",
				"¿Estás practicando para ser el capitán de un barco de pesca? Porque con esa puntería, seguro atraparías más peces que barcos enemigos.",
				"¡Tus cañones deben de estar de huelga, porque no están dispuestos a trabajar y dar en el blanco!",
				"Gracias a tu error, la igualdad se romperá a mi favor.", "Pagarás caro este error." };

		String[] perdiendoUnPocoJugadorAcerto = { "Me has dado, pero aún así la ventaja sigue siendo mía.",
				"¿Crees que un solo golpe cambiará el rumbo?",
				"¡Bah! ¿Cómo has acertado? Aunque, la victoria sigue siendo una tarea difícil para ti.",
				"La suerte del principiante...", "Enhorabuena, alguna vez tenías que acertar, patán.",
				"No me preocupas, el que va ganando soy yo.",
				"La victoria no se define con un solo tiro. La situación sigue a mi favor.",
				"Espero que no sea el inicio de una remontada...", "Mi ventaja no es clara, no debo confiarme.",
				"No te lo crees ni tú.", "¡Deja de intentar remontar, acepta tu derrota!",
				"¡Le has dado! No es algo muy común en ti.", "Sigo liderando esta batalla. La ventaja está de mi lado.",
				"Nooo, ¡déjame ganar!", "No sonrías tanto, ¿recuerdas quién va ganando?", "Pero qué suerte tienes...",
				"No pienses ni por asomo que estás a mi altura...",
				"La batalla no está decidida del todo... Bien, no puedo fallar ahora.",
				"Has dado en el blanco, lo admito. Bien jugado.", "Esto no estaba en mis planes...",
				"No me vas a remontar.",
				"Acertaste. Pero la ventaja sigue siendo mía, y estoy listo para contrarrestar tus movimientos.",
				"Aún tengo el control.", "Esto no es bueno, podría perder mi ventaja.",
				"Fue un mal tiro, en realidad. Lo que pasa es que el viento está a tu favor...",
				"No puedo perder el privilegio de ir ganando... Eh, si aciertas la siguiente, eres furro.",
				"Reconozco tu acierto, pero la verdadera pregunta es si puedes darle la vuelta a la situación.",
				"No debo confiarme.", "¿Sí? Ahora verás.", "La ventaja sigue estando en mis manos." };

		String[] perdiendoUnPocoJugadorFallo = { "¡Ja! Un intento más de tu parte que cae al agua.",
				"¿Estás practicando para un festival de fuegos artificiales?",
				"Fallaste de nuevo, ¿alguna vez has considerado cambiar de estrategia? La ventaja está firmemente en mis manos.",
				"Tu puntería parece tan nula como una brújula rota.",
				"Gracias a tu error, sigo en control de la batalla.", "No estás ni cerca de alcanzarme.",
				"Tu falta de precisión solo refuerza mi posición dominante.",
				"Otro error de cálculo de tu parte. ¿Realmente crees que puedes ganar así?",
				"Perfecto, ahora buscaré ponerme muy por delante en esta contienda.", "Mi victoria se acerca.",
				"Fallaste nuevamente. ¿Te sorprende?",
				"Otro error que pagarás caro. La batalla sigue siendo una historia de mi dominio.",
				"Tus disparos parecen más un espectáculo de fuegos artificiales que una amenaza real.",
				"Si sigues así, no tardaré en sacar una ventaja abrumadora. Gracias.",
				"Tu falta de puntería es asombrosa.",
				"¿Tienes alguna estrategia o solo estás lanzando cañonazos al azar?", "La victoria sigue siendo mía.",
				"¿Cuántos barcos crees que podrías hundir con esa precisión?", "Sigo ganando.",
				"Genial, ya estoy un poco más cerca de la victoria.",
				"Ahora te enseñaré cómo tira un capitán de verdad.",
				"Tus fallos son como una sinfonía de errores. La ventaja está en mis manos, y la melodía es de mi victoria.",
				"¿Realmente pensaste que podrías darme caza con ese tiro?",
				"Perfecto, sigo a la cabeza, pero no debo confiarme.",
				"Otro disparo perdido. ¿Te estás divirtiendo? Porque yo sí, liderando esta batalla.",
				"¿Acaso tus cañones están de vacaciones?", "Otro fallo tuyo, otro paso hacia mi victoria segura.",
				"Mi turno para aumentar mis posibilidades de victoria.", "Otro fallo tuyo, y sigo liderando.",
				"No estás ni cerca de cambiar el curso de esta batalla." };

		String[] perdiendoMuchoJugadorAcierto = {
				"Un raro acierto de tu parte. Sin embargo, la victoria sigue siendo inminente para mí.",
				"Has dado en el blanco, pero la brecha entre nosotros es demasiado grande.",
				"No importa, la derrota te espera.",
				"Un golpe certero, pero incluso con eso, la victoria es prácticamente mía.",
				"Es demasiado tarde para ti, te estoy metiendo una paliza.",
				"Das pena, te has puesto las pilas muy tarde.",
				"Aunque has acertado, la realidad es que estás a punto de sufrir una aplastante derrota.",
				"¿No sabes cuándo rendirte?", "Eres tan malo que podría hasta decirte dónde están mis barcos.",
				"Aunque has acertado, la realidad es que estás a punto de sufrir una aplastante derrota.",
				"Te aplaudiré por ese acierto, pero no cambia el hecho de que mi victoria es inevitable.",
				"Has dado en el blanco, pero incluso con eso, la brecha entre nosotros es insuperable. ¿Tienes alguna esperanza realmente?",
				"Claramente fue suerte. Si fueras bueno con el cañón, no irías así de mal.",
				"Has dado en el blanco, pero la realidad es que estás a punto de enfrentar una derrota abrumadora.",
				"Ya no tienes oportunidad de remontar.",
				"¡Jajajaja! ¿De verdad crees que puedes remontar a estas alturas?",
				"Aciertas un tiro, pero incluso con eso, la realidad es que mi triunfo es inminente.",
				"Has dado uno. No es muy usual en ti. ¿Quieres que me detenga para ver si puedes remontar algo?",
				"Pues muy bien, de lo que te sirve...", "Por fin le das a uno. Ya me aburría. ¡Qué partida más fácil!",
				"Zzzzz... Eh, eh, ¡le has dado! ¡Al fin!", "Estás a punto de sufrir una aplastante derrota.",
				"Un tiro preciso, pero la derrota sigue siendo tu destino. ¿Cómo planeas recuperarte?",
				"Eh, ¿me han dado? ¿Pero acaso tengo un rival? No me había dado cuenta.",
				"Este tiro no compensa la pésima batalla que estás haciendo.", "Al fin haces algo. ¡Qué malo eres!",
				"Me aburrooooo...", "¡Me diste! ¡Qué bien! Bueno, ahora destruyo toda tu flota.",
				"No me preocupas, te estoy dando la del atún.", "¡Le has dado! Rápido, pide un deseo." };

		String[] perdiendoMuchoJugadorFallo = { "Otro intento desastroso...",
				"Tus fallas son tan predecibles como las olas. La derrota te sigue como una sombra, incapaz de escapar.",
				"Fallas de nuevo. ¿Te estás divirtiendo siendo mi juguete en esta contienda desigual?",
				"¿Cuándo te rendirás?",
				"Otro disparo perdido. ¿Acaso crees que con ese nivel de habilidad puedes cambiar el curso de esta paliza?",
				"No me lo puedo creer, ¿cómo puedes ser tan malo?",
				"Aunque intentas, la realidad es que estás lejos de igualar mi destreza.",
				"En mis tantas batallas, nunca vi a alguien tan malo.",
				"¿Quieres que deje de disparar? Tal vez así puedes ganar alguna batalla.", "Qué pena das, jajaja.",
				"Me sabe hasta mal verte jugar así.", "¿No te cansas de perder?", "Otro error en tu cuenta.",
				"La brecha es demasiado grande, y tus fallas son solo la guinda del pastel.",
				"La brecha es tan grande que tus intentos son prácticamente irrelevantes.",
				"No me lo puedo creer. ¿Piensas golpear alguna vez mi barco?", "Qué aburrido estoy. No eres un reto...",
				"Vuelves a fallar, ¿quieres que salte mi turno?", "Tus disparos erráticos son casi cómicos.",
				"¿Y si me dices dónde están tus últimos barcos y terminamos con esto rápido?",
				"Pero, ¿cómo puedes fallar tanto?",
				"Tus tiros errados son solo un recordatorio constante de tu inferioridad.",
				"Tu habilidad está a años luz de mi maestría en el mar.", "La brecha entre nosotros es insuperable.",
				"Qué batalla más aburrida, ¿podemos acabar ya?",
				"¿Cuándo admitirás que esta batalla ya no tiene sentido para ti?", "Qué pena das...",
				"¿Otro fallo? Bueno, acabemos con esto...", "Al menos lo has intentado.", "Terminemos con esto." };

		String[] ganadoUnpocoJugadorAcierto = { "Espera... estoy perdiendo, pero ¿cómo?",
				"Le has dado, pero entonces tu racha sigue aumentando. ¡Oh no!",
				"Mis barcos están en peligro después de tu tiro.",
				"¡Un golpe directo! Esto se está volviendo más difícil de lo que pensaba.",
				"No te emociones, que aún no he perdido.",
				"¡No! ¿Cómo voy a empezar mi remontada si no dejas de acertar?",
				"Bueno, te estaba dejando un poco de ventaja, pero creo que ya es suficiente.",
				"Necesito reevaluar mi estrategia rápidamente, antes de que sea demasiado tarde.",
				"Este acierto tuyo complica las cosas.",
				"Tu precisión está mejorando, y mi confianza está disminuyendo, quiero decir, esto solo aumenta mis ganas de ganar....",
				"¡Maldición! Pero que sepas que aún no he perdido.", "Si no acierto ahora... esto podría ir a peor.",
				"Este acierto tuyo complica las cosas. Debo concentrarme más.",
				"Mi victoria ya no está tan segura como antes.",
				"Y otra vez que aciertas... pero calma, su ventaja aún no es abrumadora.",
				"Cada acierto tuyo cuenta ahora más que nunca.", "El juego está en tu favor...",
				"Mis defensas están debilitándose con cada tiro acertado tuyo.",
				"Tu puntería está mejorando, y mi situación empeora.",
				"Estás aumentando la brecha con cada tiro acertado.", "No te alegres tanto, pienso remontar.",
				"Maldición, esto no puede seguir así.", "Esto no acabará así.",
				"No estoy preocupado... ¿me ves preocupado? ¡PORQUE NO LO ESTOY!!!",
				"Que sepas que esta batalla está lejos de terminar.",
				"No, no, no, no... es imposible que vaya perdiendo.",
				"Un golpe crítico. La victoria se desvanece ante mis ojos.",
				"Ahora verás, pienso remontar aquí y ahora.",
				"No puedo rendirme, por muy difícil que esté la situación.",
				"Debo ser más astuto para contrarrestar tus aciertos o perderé esta batalla." };

		String[] ganadoUnpocoJugadorFallo = {
				"¡Respiro aliviado! Tu error me da una oportunidad para revertir la situación.",
				"A pesar de tu ventaja, tu fallo me ofrece una pequeña luz de esperanza.",
				"Un alivio momentáneo. Tu fallo podría ser mi oportunidad para recuperarme.",
				"Tu tiro errado me da un respiro en medio de la derrota aparente.",
				"Aunque estás ganando, tu error me brinda una pequeña ventana de oportunidad.",
				"¡Fallaste! Quizás aún tengo una oportunidad de dar vuelta a la partida.",
				"Tu fallo inesperado me da una pequeña esperanza de cambiar el curso del juego.",
				"¡Por fin! La suerte está de mi lado. Tu error podría ser mi boleto para remontar.",
				"Aunque estás en la delantera, tu tiro errado me da una oportunidad para resistir.",
				"Un pequeño error de tu parte, ahora no puedo fallar.",
				"Menos mal, ya creía que eras una máquina de acertar.", "Por fin algo de suerte, espero que perdure.",
				"A pesar de tu ventaja, tu fallo me ofrece una oportunidad para luchar de nuevo.",
				"El viento parece soplar a mi favor después de tu tiro fallido.",
				"Un respiro momentáneo. Tu error me da una pequeña posibilidad de recuperación.",
				"Tu fallo me da esperanzas...",
				"¡Fallaste tu oportunidad, jajaja! Aunque bueno, no estoy para hablar...",
				"¡Perfecto! Hora de la remontada.",
				"La batalla podría estar girando a mi favor después de tu tiro fallido.",
				"Ahora tengo que replantear mi estrategia y recuperar terreno.",
				"Esta es la perfecta oportunidad de remontar.",
				"La marea podría estar cambiando después de tu fallo inesperado.", "¡Un fallo crucial!",
				"La suerte está de mi lado después de tu tiro fallido.",
				"¡Fallaste! Este error podría ser mi boleto para un regreso espectacular.",
				"Aunque estás ganando, tu tiro errado me da la oportunidad de cambiar la batalla.",
				"¡Genial! Me he alegrado más de lo que debería...", "¡Ahora es mi turno!", "Toca remontar.",
				"No puedo estar cabizbajo, esto no es el final, y tu fallo es prueba de ello." };

		String[] ganadoMuchoJugadorAcierto = {
				"La derrota es abrumadora, cada golpe a mis barcos es como un puñal en el corazón.",
				"Mis esperanzas se desvanecen con cada acierto tuyo, una paliza que no puedo evitar.",
				"Cada uno de tus disparos exitosos es un recordatorio cruel de mi incompetencia en esta batalla.",
				"La tristeza se apodera de mí mientras veo cómo mi flota se desmorona bajo tu implacable ataque.",
				"La desolación me envuelve al ver que cada movimiento tuyo es un paso más hacia mi rendición.",
				"Mis barcos, una vez orgullosos, ahora yacen hundidos, simbolizando mi derrota inminente.",
				"Estoy perdido en un mar de desesperación, incapaz de encontrar un resquicio de esperanza.",
				"Cada acierto tuyo es como un lamento en mi alma, un recordatorio constante de mi fracaso.",
				"La tristeza se mezcla con la resignación mientras acepto la inevitabilidad de mi derrota.",
				"Mi flota, una sombra de lo que fue, se desvanece ante tus imparables ataques.",
				"La desolación es palpable con cada barco que se hunde, mi derrota se vuelve ineludible.",
				"La triste realidad se asienta mientras observo impotente cómo tu estrategia me aplasta.",
				"Cada acierto tuyo es como un eco de mi propia incompetencia en esta batalla.",
				"La tristeza se refleja en el agua que rodea mis barcos hundidos, símbolos de mi desesperanza.",
				"La desolación me embarga al ver que cada uno de tus movimientos es una sentencia de muerte para mis barcos.",
				"Mis esperanzas naufragan junto con mis barcos, y la desolación se apodera de mi alma.",
				"Cada impacto exitoso tuyo es un golpe a mi espíritu, una rendición forzada por tu maestría.",
				"La tristeza se cierne como una niebla densa mientras mi flota es devastada sin piedad.",
				"Mi derrota es inevitable, y cada acierto tuyo es un clavo en el ataúd de mis posibilidades.",
				"La desesperanza me envuelve, y cada disparo certero tuyo es como un suspiro final.",
				"Cada barco que se hunde es un susurro de mi fracaso, una melodía triste en este océano de derrota.",
				"La tristeza se intensifica con cada uno de tus aciertos, sellando mi destino en este juego desolador.",
				"Mi flota despedazada refleja la ruina de mis esperanzas, todo gracias a tus certeros disparos.",
				"La desolación es total, y tu habilidad para acertar es como un martillo que aplasta mis últimas ilusiones.",
				"La tristeza se manifiesta en cada ola que rodea mis barcos hundidos, testigos silenciosos de mi desgracia.",
				"Cada acierto tuyo es como un golpe de realidad, hundiéndome más en la depresión de la derrota.",
				"La desolación se instala en mi ser mientras tus disparos precisos destrozan lo que queda de mi flota.",
				"Mis barcos, ahora juguetes rotos en tus manos, representan mi derrota más amarga.",
				"La tristeza pesa sobre mí con cada uno de tus éxitos, marcando el fin inevitable de esta batalla.",
				"Estoy cansado, jefe." };

		String[] ganadoMuchoJugadorFallo = { "Aunque fallaste, la desesperanza me ahoga, no hay vuelta atrás.",
				"Aunque fallaste, no tengo fuerzas para seguir.",
				"¿Para qué seguir? Aunque falles, ya no encuentro fuerzas para luchar.",
				"Aunque tus tiros fallen, mi alma está quebrada, sin esperanza.",
				"Cada error tuyo prolonga mi agonía, ya no queda consuelo.",
				"¿Por qué seguir con esta farsa? Incluso tus fallos no alivian mi desesperación.",
				"Aunque falles, la sombra de la derrota es demasiado oscura para disiparse.",
				"Mis lágrimas no se detienen, tus fallos no alivian mi tormento.",
				"Aunque tus tiros fallen, mi rendición emocional ya está consumada.",
				"No hay consuelo en tus fallos, solo prolongan mi sufrimiento.",
				"Tus errores son como dagas en mi corazón, la esperanza ya no existe.",
				"Aunque falles, mi resignación es total, ya no veo un camino a seguir.",
				"Tus fallos solo aumentan mi pesar, la esperanza se desvanece.",
				"Aunque tus tiros fallen, mi rendición es inevitable, no hay escape.",
				"Incluso con tus fallos, el peso de la derrota es insoportable.",
				"Ya no hay vuelta atrás, tus fallos no pueden cambiar mi destino.",
				"Aunque falles, mi derrota es ineludible, no hay consuelo en tus errores.",
				"Quizás en otro momento pensaría que se puede remontar... ya no.",
				"Aunque tus tiros fallen, mi desolación persiste, sin respiro.",
				"¿Para qué seguir jugando? Incluso con tus fallos, mi derrota es innegable.",
				"Aunque falles, la tristeza me consume, ya no hay fuerzas para resistir.",
				"¿Has fallado para que mi angustia dure más?",
				"Incluso con tus errores, mi rendición es completa, sin esperanza de cambio.",
				"Aunque falles, la amargura de mi derrota no se disipa.",
				"¿Por qué prolongar esto? Tus fallos no pueden restaurar mi esperanza perdida.",
				"Aunque tus tiros fallen, mi desánimo persiste, sin consuelo.",
				"Incluso con tus fallos, la realidad de mi derrota es inmutable.",
				"Mátame, ¿no ves que estoy sufriendo?",
				"Aunque falles, mi tristeza es profunda, ya no hay chispa de esperanza.",
				"Incluso con tus fallos, mi rendición es completa, sin fuerzas para seguir." };

		if (trasncursoPartida >= -5 && trasncursoPartida <= 3 && acerierto) {
			imprimirMensaje(ganadoMuchoJugadorAcierto);
		} else if (trasncursoPartida >= -5 && trasncursoPartida <= 3 && !acerierto) {
			imprimirMensaje(ganadoMuchoJugadorFallo);
		} else if (trasncursoPartida >= 4 && trasncursoPartida <= 7 && acerierto) {
			imprimirMensaje(ganadoUnpocoJugadorAcierto);
		} else if (trasncursoPartida >= 4 && trasncursoPartida <= 7 && !acerierto) {
			imprimirMensaje(ganadoUnpocoJugadorFallo);
		} else if (trasncursoPartida >= 8 && trasncursoPartida <= 12 && acerierto) {
			imprimirMensaje(acertoEmpatados);
		} else if (trasncursoPartida >= 8 && trasncursoPartida <= 12 && !acerierto) {
			imprimirMensaje(falloEmpatados);
		} else if (trasncursoPartida >= 13 && trasncursoPartida <= 16 && acerierto) {
			imprimirMensaje(perdiendoUnPocoJugadorAcerto);
		} else if (trasncursoPartida >= 13 && trasncursoPartida <= 16 && !acerierto) {
			imprimirMensaje(perdiendoUnPocoJugadorFallo);
		} else if (trasncursoPartida >= 17 && trasncursoPartida <= 30 && acerierto) {
			imprimirMensaje(perdiendoMuchoJugadorAcierto);
		} else if (trasncursoPartida >= 17 && trasncursoPartida <= 30 && !acerierto) {
			imprimirMensaje(perdiendoMuchoJugadorFallo);
		}

	}

	public static void generarFrasesTurnoRival() {

		String[] acertoEmpatadosRival = { "Uno menos, pero la igualdad persiste...",
				"¿Estás seguro de que no necesitas gafas, marinero? Porque claramente no viste venir ese cañonazo a tu querido navío.",
				"¡Qué coincidencia! Justo cuando pensabas que esto estaba igualado.",
				"¿Quién necesita suerte cuando se tiene habilidad? Claro que la partida sigue igualada...",
				"¿Te atreverías a llamarte capitán después de eso?",
				"Ni disparando correctamente consigo romper esta igualdad.",
				"Estas son buenas noticias, más no debo confiarme.",
				"Donde pongo el ojo, pongo la bala, ¡así que vigila bien tus movimientos, que mi mirada está fija en ti!",
				"Una parte menos, pero la igualdad persiste.",
				"Un consejo de capitán a capitán: cuando veas mis cañones apuntando hacia ti, tal vez deberías rendirte. ¿O prefieres seguir perdiendo barcos?",
				"Tengo que seguir así, esta batalla no puede ser pareja eternamente.",
				"¡Sí! no tienes nada que hacer contra mí.", "Aunque acerté, esto sigue bastante igualado...",
				"Con este disparo, sacaré la ventaja que necesitaba.",
				"¿Qué? ¡Le he dado, increíble! Eh, quiero decir, todo de acuerdo a mis planes...",
				"¡Ah, el sonido reconfortante de un barco naufragando!", "¿Acaso no lo viste venir?",
				"Te va a costar muchos doblones repararlo...",
				"Y a pesar de todo, sigues aferrándote a mantener esta igualdad.",
				"No te sientas mal, es que soy muy bueno...",
				"Un chapuzón para tu barco, pero la igualdad persiste. ¿Listo para el siguiente movimiento?",
				"¡Qué lástima para tu flota! Pero no pienso confiarme.",
				"No pienso perder, y este cañonazo es prueba de ello.",
				"¡Vamos! Y el siguiente será 6 veces más fuerte.", "¡Que retumbe el estruendo marino!",
				"¿Cómo puede ser que aún estemos empatados?", "¿Qué? ¿Esperabas que fallara? Lo siento, pero no.",
				"¡Vamos! Uno menos para la victoria.",
				"Esta contienda sigue igualada, pero ¿podrás hacer un disparo tan perfecto como el mío?",
				"Parece que mi puntería es más afilada que cualquier espada." };

		String[] falloEmpatadosRival = { "Maldición, fallé.",
				"Que no se te suba a la cabeza, esto no está decantado a ningún lado.",
				"Ehhh, fue mala suerte, ya verás. Pienso destruir tu flota.",
				"¡Miseria marina! Mis tiros fueron en vano.",
				"BAH, fallé. ¿Dónde están mis tiradores cuando se necesitan? Espera, lo que quiero decir es que... Eh, no necesito a nadie, ¡soy el mejor tirador!",
				"Afortunadamente, este error no es muy crítico. Tengo la oportunidad de ganar aún.",
				"Con esta igualdad, cualquier error podría ser mortal, como un paso en falso en la cubierta de un barco en plena tormenta.",
				"No te rías, ¡por favor! Todos cometemos errores.", "Cambiaré de cañón...",
				"Disfruta de mi error, pero con una batalla tan pareja, no deberías dormirte en los laureles.",
				"Nooo, quería romper la igualdad con este tiro.", "Me puedo permitir este error...",
				"Es que aún estoy un poco dormido de la siesta...",
				"¡Cielos oscuros! Mis disparos no encontraron su blanco.",
				"¡Demonios! Mis cañones fallaron, pero no te confíes. La igualdad nos envuelve como la niebla en alta mar.",
				"Si crees que esto me va a frenar, estás muy equivocado.",
				"Con esta paridad, este error pudo salirme muy caro... Digo, ¡ehhh, fallé a propósito! Así será más divertido ganarte.",
				"Al següent mes i millor.", "Está bien, aún seguimos iguales, pero no puedo permitirme más fallos.",
				"¡Tragedia marina! Mis cañones han perdido el rumbo, pero la batalla aún no ha escrito su epílogo.",
				"Puto Gastón Álvarez me ha quitado 3 puntos en el fantasy.",
				"¡Desgracia y desaliento! Mis balas danzaron con las olas.", "Te recuerdo que esto sigue parejo..",
				"No me voy a desesperar, ¡que lo sepas! Por el momento...",
				"Tranquilo... Este fallo no te da la victoria, ni mucho menos.", "Ni una sola herida infligida...",
				"¡Maldición! Ni un rasguño en tu cubierta, pero esto no garantiza tu victoria. La balanza sigue en equilibrio.",
				"Debo alterar mi táctica antes de que este patán consiga una ventaja clara.",
				"¡Nooo! Mis balas son ahora comida marina...", "Bueno, no siempre se puede acertar." };

		String[] ganadoUnPocoRivalAcierto = { "Mi victoria sigue siendo innegable, ¿te queda alguna sorpresa?",
				"Has sentido el peso de mi artillería, cada vez más cerca de ganar esta batalla.",
				"La batalla cada vez está más decidida a mi favor.", "¿Puedes sentir la derrota acercándose?",
				"Un tiro perfecto. La victoria ya está a la vista", "Estoy un paso más cerca de la victoria total.",
				"Soy demasiado bueno para ti.",
				"No te sientas mal, soy un capitán con más de 920 victorias. No tienes nada que hacer contra mí.",
				"¡Intenta hacerlo mejor!", "Esto se está volviendo demasiado fácil, ¿no crees?",
				"Mi puntería es imparable.", "Cada vez más cerca de una victoria aplastante.", "Tu flota se desmorona.",
				"¿Puedes sentir cómo se desvanece tu esperanza de ganar?",
				"Mis disparos son precisos. La victoria es solo cuestión de tiempo.",
				"En nada, seré conocido en todos los mares.", "Empiezo a sacar una buena ventaja, ¿no crees?",
				"Unos cuantos tiros más como este, y tendré todo a mi favor.",
				"Mis cañones no perdonan. Estoy un paso más cerca de la gloria.",
				"¿Te queda alguna estrategia para revertir esto?", "Si esto sigue así, en breve te daré una paliza.",
				"Te he dado de nuevo. La ventaja sigue siendo mía, y tu resistencia se desvanece.",
				"La victoria está a la vuelta de la esquina.", "Pronto te quedarás sin opciones para recuperarte.",
				"Mis cañones no conocen la derrota.", "¿Cuánto más puedes resistir mi avance?",
				"Mi victoria aún no está asegurada. No pienso bajar la intensidad.",
				"No puedo confiarme... no del todo.", "Mis disparos son letales.",
				"Bien, otro golpe preciso. Queda menos para una ventaja abrumadora." };

		String[] ganandoUnPocoRivalFallo = {
				"Un pequeño tropiezo, pero incluso con errores, la victoria sigue estando de mi lado.",
				"Incluso los mejores cometen errores.", "Aunque fallé, aún lidero la batalla.",
				"Un fallo ocasional. No te emociones demasiado, la ventaja sigue siendo mía.",
				"Un tiro errado, pero eso no cambia el hecho de que la victoria está cerca para mí.",
				"Maldición, no puedo permitirme estos errores. Pueden salirme caros si son recurrentes.",
				"Un error insignificante.", "La batalla está firmemente bajo mi control, a pesar de ese fallo.",
				"Tengo que evitar estos fallos. Podría perder mi preciosa ventaja.",
				"Incluso con este fallo, la ventaja sigue siendo mía.",
				"He cometido un pequeño error, pero eso no altera la realidad: sigo ganando.",
				"Aunque fallé en este turno, la victoria general sigue en mis manos.",
				"No puedo cometer estos errores cuando la victoria está tan cerca.",
				"Un disparo mal calculado, pero eso no cambia el hecho de que mi dominio persiste.",
				"Aunque este tiro falló, la ventaja sigue siendo mía.", "¡Nooo! No debo desconcentrarme.",
				"¡Qué rabia! Justo cuando parecía que podía ganar.",
				"No te emociones con mi fallo, la ventaja sigue siendo mía.",
				"Un error momentáneo, nada de qué preocuparse.", "¡Qué fallo!",
				"He fallado, pero no puedes cambiar el curso de los acontecimientos.", "Solo ha sido mala suerte...",
				"Un pequeño traspié, pero la victoria general sigue en mis manos. ¿Tienes alguna sorpresa preparada?",
				"Puedo permitirme este error.", "No hay que alterarse, estoy bien.",
				"¡Maldición! Con lo bien que estaba.",
				"Un error en mi parte, pero eso no cambia el hecho de que la ventaja sigue siendo mía.",
				"Aun con este fallo, sigo ganando. No lo olvides.", "Qué suerte tienes.",
				"Este error no es grave, no te emociones..." };

		String[] ganandoMuchoRivalAcierto = {
				"¿Realmente pensaste que podrías competir contra mi genialidad en el mar?",
				"¿Eso es lo mejor que puedes hacer?",
				"Un barco menos para ti, y la derrota se siente cada vez más inevitable.",
				"Este juego ya no es una batalla, es un despliegue triunfal de mi supremacía en el mar.",
				"No te desesperes, es que soy muy bueno. Nunca tuviste nada que hacer.",
				"Jajaja, y le doy a tu barco. Ya es lo que te faltaba.", "Ves, así se hunde un barco, perdedor.",
				"Te estarás preguntando cómo soy tan bueno.",
				"Bueno, cuando acabemos, en breve ya te firmo un autógrafo.",
				"Mis tácticas son imparables, y tus barcos son meras piezas de un rompecabezas que se desarma.",
				"Venga, ríndete. No pierdas a todos tus hombres en una batalla ya perdida.",
				"¿Puedes sentir cómo se desvanece tu esperanza con cada barco que se hunde?",
				"¿Realmente creías que tenías alguna posibilidad? Mis tácticas son impenetrables.",
				"Soy demasiado bueno para ti. Busca un rival más asequible.", "¿Te arrepientes de haberme desafiado?",
				"Cada vez que acierto, es otro paso hacia tu rendición inevitable.",
				"¿De verdad creíste que tenías alguna oportunidad contra mí?",
				"La diferencia de habilidad es tan abrumadora que esto ya no es un juego, es una ejecución.",
				"Estoy haciendo pedazos tu flota.", "¿Cómo puedo ser tan bueno? Es increíble.",
				"Qué bien, Uzuni me ha hecho 12 puntos en el fantasy siendo del puto Granada.",
				"¿Cómo puedo ser tan bueno?", "Ya no me sorprende acertar. ¿Puedes abandonar, por favor?",
				"Bien, ¡le he dado! Ya queda menos para tu derrota.",
				"Y otro acierto, ¡vamos! ¿No te da pena jugar así de mal?",
				"Cada movimiento mío es otro paso hacia tu rendición inevitable.", "Pero qué bueno soy.",
				"¡Y otro acierto! Esto es una masacre.", "Presiento que esta batalla terminará en breve.",
				"En fin, era lo esperado. Espera, mi siguiente turno será igual o mejor." };

		String[] ganandoMuchoRivalFallo = { "Incluso con este fallo, estoy a punto de hundirte por completo.",
				"Aunque mi último intento fue en vano, la victoria sigue siendo inevitable.",
				"El mar es testigo de mi dominio, a pesar de este pequeño tropiezo.",
				"Puede que haya fallado ahora, pero tus posibilidades de ganar siguen siendo nulas.",
				"Eres tan malo que fallé porque me das pena.",
				"Eh, eh, eh, relájate. Hasta los mejores capitanes fallan de vez en cuando.",
				"¡Qué humilde soy! Fallando para que creas que tienes una oportunidad.",
				"Bueno, no puedo acertar siempre. De todos modos, esta batalla no durará mucho más.",
				"El juego está en mis manos, aunque este disparo no encontrara su objetivo.",
				"La paliza continúa, incluso si este turno no fue mi mejor momento.",
				"Mi superioridad es tan clara que incluso un fallo no cambiará el resultado final.",
				"Aunque esta vez no di en el blanco, tu derrota es cable.",
				"Un pequeño contratiempo, pero no vas a durar mucho más.",
				"La victoria sigue siendo mía, incluso con este pequeño tropiezo en el camino.",
				"No me preocupa, alguna vez tenía que fallar.", "Bueno esta batalla es mia.",
				"No fue un fallo... había un delfín que se estaba riendo de mí...",
				"La victoria sigue siendo mía, a pesar de este pequeño desliz.",
				"No me importa, a esta batalla le quedan 2 turnos.", "Sé que he fallado, pero deberías rendirte.",
				"Incluso con este tropiezo, la batalla está decidida a mi favor.",
				"La brecha entre nosotros es tan grande que incluso un fallo no te acerca a la victoria.",
				"Eres tan malo que me has pegado la mala suerte de fallar todos los disparos.",
				"Bueno, alguna vez tenía que pasar...", "Maldición, quería acabar esta batalla lo antes posible.",
				"Esto está ganado, no me preocupas.", "Mi estrategia sigue intacta, a pesar de este error momentáneo.",
				"Un pequeño contratiempo...", "De todas formas, voy a ganar...",
				"Maldición, tengo que acabar ya, ¡tengo cena en 30 minutos!" };

		String[] perdiendoUnpocoRivalAcierto = {
				"¡Un golpe directo! Este acierto podría ser el punto de inflexión que necesitaba.",
				"Mi estrategia da frutos. Acerté un barco clave para cambiar el rumbo.",
				"Un tiro certero. Tal vez aún hay esperanza de darle la vuelta a esta situación.",
				"¡Impacto! Este acierto podría ser el comienzo de mi remontada.",
				"Mis tácticas están dando resultados. Acerté donde más duele.",
				"Un golpe crucial. Quizás aún puedo darle la vuelta a esta batalla.",
				"Mi precisión no ha fallado. Este acierto es la chispa que necesitaba.",
				"Un tiro preciso. La victoria aún podría ser posible.",
				"¡Sí! Acerté, y la esperanza se enciende de nuevo en esta batalla.",
				"Mi estrategia está surtiendo efecto. Cada acierto me acerca a la remontada.",
				"Este tiro bien dirigido podría cambiar completamente el juego a mi favor.",
				"Un acierto que resuena. La posibilidad de la victoria se aviva.",
				"¡Impacté uno de tus barcos! La tide está cambiando a mi favor.",
				"Mis disparos están encontrando su objetivo. La remontada está en marcha.",
				"Un golpe certero. Tal vez aún puedo revertir esta situación desfavorable.",
				"Mi puntería está mejorando. Este acierto podría ser el comienzo de mi resurgimiento.",
				"El mar responde a mi favor con este acierto preciso.",
				"¡Acerté! Un paso más cerca de darle la vuelta a esta partida.",
				"Mi estrategia de ataque está funcionando. Este acierto podría cambiar todo.",
				"Cada acierto es una oportunidad para acercarme a la victoria.",
				"La precisión de mis disparos podría ser la clave para remontar este juego.",
				"Un tiro directo. La esperanza renace con cada acierto.",
				"¡Lo logré! Este acierto me da una nueva oportunidad en la batalla.",
				"La marea está cambiando con cada uno de mis tiros certeros.",
				"Este acierto es un rayo de esperanza en medio de la derrota aparente.",
				"¡Sí! Mi puntería está mejorando, y la victoria no está tan lejos.",
				"Cada acierto es un paso más hacia la remontada que parecía imposible.",
				"Un tiro preciso. Tal vez aún puedo revertir esta desventaja.",
				"Este acierto cambia el panorama. La remontada está en marcha.", "No esta perdido." };

		String[] perdiendoUnpocoRivalFallo = {
				"¡Otro fallo! La desesperación se apodera de mí mientras intento remontar.",
				"Aunque estoy detrás, mi esperanza no se desvanece con cada disparo fallido.",
				"Cada tiro errado es como un golpe a mi última oportunidad de victoria.",
				"La batalla se vuelve más intensa, pero mis fallos son un lastre pesado.",
				"Otro barco intacto tuyo, otro golpe a mi esperanza de remontada.",
				"Mis disparos erráticos reflejan la agonía de estar al borde de la derrota.",
				"Cada fallo es una llamada de atención sobre lo lejos que estoy de la victoria.",
				"La desesperación aumenta con cada oportunidad perdida de cambiar el juego.",
				"Fallar nuevamente me sume más en la oscura realidad de la derrota.",
				"La remontada parece más distante con cada disparo que no alcanza su objetivo.",
				"Mis fallos son como cadenas que me atan a la derrota inevitable.",
				"La esperanza titila débilmente mientras mis disparos siguen sin encontrar blanco.",
				"Cada tiro errado resuena con la triste realidad de mi situación actual.",
				"La frustración crece con cada barco tuyo que permanece indemne.",
				"Mis disparos desviados son eco de la lucha desesperada por cambiar el destino.",
				"La remontada se aleja mientras mis tiros caen en el vacío una y otra vez.",
				"Otro fallo, y mi sueño de cambiar el curso de la batalla se desvanece.",
				"La derrota se cierne más cerca con cada disparo que no logra su objetivo.",
				"La desesperanza me envuelve mientras mis tiros continúan sin acertar.",
				"Mis fallos son como eslabones en una cadena que me arrastra hacia la perdición.",
				"Cada tiro errado es un recordatorio cruel de mi situación precaria.",
				"La remontada se desdibuja mientras mis disparos siguen sin impactar.",
				"Otro fallo, y la victoria se aleja como un sueño inalcanzable.",
				"La agonía se profundiza con cada disparo que no logra perforar tus defensas.",
				"Mis tiros errados son como un eco siniestro de mi derrota inminente.",
				"Fallar una vez más aumenta la sensación de impotencia en esta batalla.",
				"La remontada parece más ilusoria con cada oportunidad que desaprovecho.",
				"Mis disparos desviados son como puñales que perforan mi esperanza restante.",
				"Cada fallo me sume más en la tristeza de aceptar la derrota que se avecina.",
				"La desilusión crece con cada intento fallido de cambiar el rumbo de la batalla." };

		String[] perdiendoMuchoRivalAcierto = { "Aunque acerté, la derrota ya es inevitable.",
				"Mi tiro acertado es solo un destello en la oscuridad de mi derrota.",
				"Aunque acerté un barco, la sombra de la derrota es demasiado abrumadora.",
				"La victoria es solo una ilusión, mi acierto no cambia la realidad desoladora.",
				"Aunque di en el blanco, ya no me importa el resultado de esta batalla es claro.",
				"Mi acierto es como una gota en el océano de mi derrota inminente.",
				"Aunque acerté, la tristeza eclipsa cualquier alegría que podría haber sentido.",
				"Mi tiro preciso no cambia la sensación de derrota que me envuelve.",
				"Aunque acerté un barco, ya no veo sentido en seguir luchando, me has humillado.",
				"La batalla ya no me importa, incluso con mi tiro certero.",
				"Aunque acerté, la amargura de la derrota persiste.",
				"Mi acierto es solo un suspiro en medio de mi resignación.",
				"Aunque di en el blanco, ya no siento la emoción de la victoria.",
				"Mi tiro acertado es como un eco distante en el paisaje sombrío de mi derrota.",
				"Menos mal, ya pensaba que era malísimo. Esto demuestra que soy malo a secas.",
				"Mi victoria parcial no cambia la inevitabilidad de mi derrota total.",
				"Aunque di en el blanco, ya no me importa el resultado de esta contienda.",
				"Mi tiro certero no puede eclipsar la sensación de desánimo que me embarga.",
				"Aunque acerté un barco, la tristeza anula cualquier posible celebración.",
				"La victoria es solo un detalle insignificante en medio de mi desolación.",
				"Aunque di en el blanco, mi espíritu ya está derrotado.",
				"Mi acierto es como un destello fugaz en la oscuridad de mi rendición.",
				"Aunque acerté, ya no hay lugar para la emoción de la victoria en mi corazón.",
				"Mi tio me da por culo.", "Aunque di en el blanco, la tristeza eclipsa cualquier alegría potencial.",
				"Mi victoria momentánea no puede cambiar el panorama sombrío de mi derrota.",
				"Aunque acerté un barco, ya no me importa el resultado final.",
				"Mi tiro certero es como una pizca de luz en la oscuridad de mi derrota total.",
				"Aunque di en el blanco, ya no creo poder ganar.",
				"Mi acierto es solo una nota silenciosa en la sinfonía de mi derrota." };

		String[] perdiendoRivalMuchoFallo = { "Soy un capitán con más de 920 victorias. ¿Cómo he acabado así?",
				"Con cada fallo, siento que mi carrera pirata llega a su amargo final.",
				"Mis errores son como puñales en mi orgullo pirata, la rabia me consume.",
				"El último fallo es el colmo de mi derrota, la tristeza y la ira se entrelazan.",
				"La paliza es completa, mi último error es la gota que derrama el vaso de mi rabia.",
				"Mis fallas resonarán como el eco de mi desgracia en el océano de la derrota.",
				"La impotencia me ahoga, cada fallo es un recordatorio cruel de mi declive pirata.",
				"La rabia bulle en mi interior, mis fallas son el epitafio de mi carrera.",
				"Cada tiro errado es una herida en mi alma pirata, la tristeza se mezcla con la ira.",
				"Mis fallos son como cadenas que arrastran mi carrera hacia su fin amargo.",
				"La humillación es completa, cada error es un golpe a mi legado pirata.",
				"Mi último fallo es el clavo en el ataúd de mi gloria pasada, la rabia me consume.",
				"Cada tiro errado es una mancha indeleble en la historia de mi vida pirata.",
				"La impotencia me carcome, cada fallo es una marca en mi derrota definitiva.",
				"Mis errores son el triste epílogo de una carrera pirata que llega a su fin.",
				"La rabia se desborda con cada tiro fallido, mi orgullo pirata despedazado.",
				"Mis fallos son la triste sinfonía que acompaña el ocaso de mi vida en el mar.",
				"La humillación es palpable, mi última derrota es un golpe a mi identidad pirata.",
				"La rabia arde en mi interior, cada fallo es un puñal en mi corazón pirata.",
				"Mis errores son el testamento de mi declive, la tristeza y la rabia se entrelazan.",
				"La paliza es más amarga con cada tiro errado, mi carrera pirata yace en ruinas.",
				"Cada fallo es una condena, la rabia se mezcla con la tristeza en mi naufragio pirata.",
				"La impotencia me envuelve, cada error es una herida en mi legado pirata.",
				"Mis fallos son la prueba irrefutable de mi derrota, la rabia se apodera de mí.",
				"La humillación es total, mis errores son el epílogo oscuro de mi vida pirata.",
				"Cada tiro errado es un golpe a mi reputación, la tristeza se tiñe de rabia.",
				"La rabia me consume con cada fallo, mi carrera pirata se desmorona.",
				"Mis errores son el precio de mi arrogancia, la humillación es completa.",
				"Aleix García, 70 millones me he gastado en ti y me haces ese partido de mierda.",
				"Mis fallos son la triste despedida de una carrera pirata que llega a su fin." };

		if (trasncursoPartida >= 8 && trasncursoPartida <= 12 && acerierto) {
			imprimirMensaje(acertoEmpatadosRival);
		} else if (trasncursoPartida >= 8 && trasncursoPartida <= 12 && !acerierto) {
			imprimirMensaje(falloEmpatadosRival);
		} else if (trasncursoPartida >= 13 && trasncursoPartida <= 16 && acerierto) {
			imprimirMensaje(ganadoUnPocoRivalAcierto);
		} else if (trasncursoPartida >= 13 && trasncursoPartida <= 16 && !acerierto) {
			imprimirMensaje(ganandoUnPocoRivalFallo);
		} else if (trasncursoPartida >= 17 && trasncursoPartida <= 30 && acerierto) {
			imprimirMensaje(ganandoMuchoRivalAcierto);
		} else if (trasncursoPartida >= 17 && trasncursoPartida <= 30 && !acerierto) {
			imprimirMensaje(ganandoMuchoRivalFallo);
		} else if (trasncursoPartida >= 4 && trasncursoPartida <= 7 && acerierto) {
			imprimirMensaje(perdiendoUnpocoRivalAcierto);
		} else if (trasncursoPartida >= 4 && trasncursoPartida <= 7 && !acerierto) {
			imprimirMensaje(perdiendoUnpocoRivalFallo);
		} else if (trasncursoPartida >= -5 && trasncursoPartida <= 3 && acerierto) {
			imprimirMensaje(perdiendoMuchoRivalAcierto);
		} else if (trasncursoPartida >= -5 && trasncursoPartida <= 3 && !acerierto) {
			imprimirMensaje(perdiendoRivalMuchoFallo);
		}

	}

	public static void imprimirMensaje(String v[]) {

		int indiceAleatorio = (int) (Math.random() * 30);

		System.out.println(v[indiceAleatorio]);

	}

	public static void tutorial() {
		Scanner sc = new Scanner(System.in);
		System.out.println(
				"¡Hola pequeño idiota!  Parece que tu seso necesita un faro que lo ilumine. Si tienes alguna ");
		System.out.println("pregunta en concreta te jodes, miraremos los aspectos generales del juego por encima, ");
		System.out.println("¿entendido?");
		System.out.println();
		System.out.println(
				"Bueno, has logrado llegar hasta aquí sin naufragar en los menús, ¡bravo, astuto navegante! Ahora, ");
		System.out.println("desentrañemos los misterios que ocultan (pulsa enter para continuar):");
		sc.nextLine();
		System.out.println();
		System.out.printf("%40s%n", "╔════════════════════════════════╗");
		System.out.printf("%40s%n", "║      ¿QUÉ QUIERES HACER?       ║");
		System.out.printf("%40s%n", "╠════════════════════════════════╣");
		System.out.printf("%40s%n", "║        1.-TRUCO DEL MAR        ║");
		System.out.printf("%40s%n", "║                                ║");
		System.out.printf("%40s%n", "║   2.- INTELIGENCIA DEL RIVAL   ║");
		System.out.printf("%40s%n", "║                                ║");
		System.out.printf("%40s%n", "║     3.- ¿CÓMO SE JUEGA?        ║");
		System.out.printf("%40s%n", "║                                ║");
		System.out.printf("%40s%n", "║    4.- CHISTES DE PIRATAS      ║");
		System.out.printf("%40s%n", "║                                ║");
		System.out.printf("%40s%n", "║          5.- SALIR             ║");
		System.out.printf("%40s%n", "╚════════════════════════════════╝");
		System.out.println();
		System.out.println("Rápidamente, el truco del mar, hmm, no lo uses. Es para personas que necesitan atención. ");
		System.out.println(
				"Sirve para que puedas ver mi flota perfecta y bonita en medio de la batalla. ¡Algo inaceptable!");
		System.out.println();
		System.out.println(
				"¡Inteligencia del rival! Arrr, es cosa simple. Elige contra quién luchar, mi persona, un intrépido ");
		System.out.println("y valiente capitán, o el patán de mi primo, que bebe 70 botellas de ron por minuto.");
		System.out.println();
		System.out.println(
				"Y los chistes de piratas son el verdadero juego y su mejor función, escucha unos divertidísimos ");
		System.out.println("chistes de bucaneros y ¡MUEREEE! De risa. (Pulsa enter para continuar)");
		sc.nextLine();
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.printf("%40s%n", "╔══════════════════╗");
		System.out.printf("%40s%n", "║      ELIGE:      ║");
		System.out.printf("%40s%n", "╠══════════════════╣");
		System.out.printf("%40s%n", "║ 1. Jugar         ║");
		System.out.printf("%40s%n", "║                  ║");
		System.out.printf("%40s%n", "║ 2. Dificultad    ║");
		System.out.printf("%40s%n", "║                  ║");
		System.out.printf("%40s%n", "║ 3. Extras        ║");
		System.out.printf("%40s%n", "║                  ║");
		System.out.printf("%40s%n", "║ 4. Salir         ║");
		System.out.printf("%40s%n", "╚══════════════════╝");
		System.out.println();
		System.out.println();
		System.out.println(
				"Seguimos, “dificultad del tablero”, ajusta el número de navíos en la batalla. Entra más tarde e ");
		System.out.println("investiga las opciones.");
		System.out.println();
		System.out.println("Ahora, nos zambullimos de lleno en la opción de jugar. (pulsa enter para continuar)");
		System.out.println();
		System.out.println();
		sc.nextLine();
		String[][] tableroTuto = new String[10][10];
		rellenarMatriz(tableroTuto);
		imprimirMatrizJugador(tableroTuto);
		System.out.println(
				"Bienvenido a la fase de forja de tu tablero. Aquí es donde darás vida a tu flota. Al colocar tu ");
		System.out.println("primer barco, tienes la libertad de escoger la posición a tu antojo, siempre y cuando te ");
		System.out.println(
				"mantengas dentro de los confines del tablero. Por ejemplo, \"z999\" está fuera de servicio, ya ");
		System.out.println("que no existe en estos mares. ¡Recuerda mantener la disciplina y ceñirte al tablero! ¡A ");
		System.out.println("navegar con destreza!(pulsa enter para continuar)");
		tableroTuto[3][4] = (VERDE + barco + RESET);
		sc.nextLine();
		System.out.println();
		System.out.println();
		System.out.println();
		imprimirMatrizJugador(tableroTuto);
		System.out.println("Supongamos que has clavado tu elección en D5, ¡por los siete mares! ¡Excelente! Hemos ");
		System.out.println(
				"dejado la primera huella de nuestro barco, y según la información de la izquierda, este navío es ");
		System.out.println(
				"un BUQUE, ocupando 3 espacios en total. Ahora, toca incrustar las otras 2 partes, pero ya no ");
		System.out.println(
				"puedes lanzarlas al azar, debes seguir una lógica marina. Deben estar pegadas al original en ");
		System.out.println(
				"D5, comprendes. Tus opciones son: D4, D6, C5, E5. Y, por supuesto, solo puedes elegir casillas ");
		System.out.println("vacías, marcadas con el símbolo \"~\".(Pulsa enter para continuar)");
		tableroTuto[3][3] = (VERDE + barco + RESET);
		sc.nextLine();
		System.out.println();
		System.out.println();
		System.out.println();
		imprimirMatrizJugador(tableroTuto);
		System.out
				.println("Hemos colocado en D4, ¡maravilloso! Ahora solo falta una pieza más, y nuestro buque estará ");
		System.out.println(
				"listo para surcar los mares. Pero ojo, las reglas han cambiado, ya no se trata solo de elegir ");
		System.out.println("casillas al azar. Presta atención al cambio de mareas:(pulsa enter para continuar)");
		tableroTuto[2][4] = (VERDE + barco + RESET);
		sc.nextLine();
		System.out.println();
		System.out.println();
		System.out.println();
		imprimirMatrizJugador(tableroTuto);
		System.out.println("Ya no podemos clavar nuestro barco en C5, pues solo permiten posiciones horizontales o ");
		System.out.println("verticales no se pueden ambas a la vez, ¡y nada de diagonales! Dado que hemos colocado ");
		System.out.println("nuestro buque horizontalmente, solo nos quedan dos opciones como botellas en el mar ");
		System.out.println("embravecido: D3 o D6. (pulsa enter para continuar)");
		tableroTuto[2][4] = ("~");
		tableroTuto[3][2] = (VERDE + barco + RESET);
		sc.nextLine();
		System.out.println();
		System.out.println();
		System.out.println();
		imprimirMatrizJugador(tableroTuto);
		System.out.println("Con este último movimiento, hemos clavado la última pieza de nuestro buque. Ya no queda ");
		System.out.println(
				"mucho por ondear, pero si te toca soltar más barcos, mi consejo de capitán experimentado es ");
		System.out.println("que los esparzas como gaviotas en la brisa. Mantén las distancias, no los amontones como ");
		System.out.println(
				"monedas de oro, y dificulta el saqueo al rival, ¡o sea, a mí! Un momento... olvida lo que acabo ");
		System.out.println("de decir, ¿entendido?(pulsa enter para continuar)");
		tableroTuto[3][1] = (ROJO + barco + RESET);
		sc.nextLine();
		System.out.println();
		System.out.println();
		System.out.println();
		imprimirMatrizJugador(tableroTuto);
		System.out.println("Bueno ya no importa, fíjate he puesto esta lancha muy cerca de nuestro buque. Mejor ");
		System.out
				.println("cambiemos eso antes de que el enemigo se aproveche de nuestras distracciones. Pero espera, ");
		System.out.println(
				"¿te preguntas si hay una herramienta astuta y práctica para deshacernos de ese último barco ");
		System.out
				.println("añadido? A lo que te respondo con un rotundo ¡No! Si te equivocas, amigo mío, tendrás que ");
		System.out.println(
				"borrar todo el tablero y empezar de 0, así que no te equivoques, o sí. La verdad me la pela.   En ");
		System.out.println(
				"fin, practiquemos el arte de borrar tableros. Introduce \"BORRAR\" y haz desaparecer todo ese ");
		System.out.println("enredo:");
		String respuesta = "";
		while (!respuesta.equals("BORRAR")) {
			System.out.println();
			System.out.println("Venga, escribe BORRAR.");
			respuesta = sc.next();
			respuesta = respuesta.toUpperCase();

			if (!respuesta.equals("BORRAR")) {
				System.out.println("¿Eres tonto? Eso no es BORRAR.");
			}

		}
		sc.nextLine();
		rellenarMatriz(tableroTuto);
		System.out.println();
		System.out.println();
		System.out.println();
		imprimirMatrizJugador(tableroTuto);
		System.out.println(
				"¡Perfecto, ya está borrado! Y esto sería todo respecto a la creación de tableros. Solo me queda ");
		System.out.println(
				"decir que se te permite crear el tablero de manera aleatoria. De esta forma, puedes jugar sin ");
		System.out.println(
				"tener que perder el tiempo creando uno, pero cabe recalcar que tal vez no sea el tablero más ");
		System.out.println(
				"adecuado para la batalla. Si te gusta jugar con el azar, ¡es una opción ideal! No recomendado ");
		System.out.println("para personas ludópatas… En fin, crearé un tablero aleatorio para que puedas ver la ");
		System.out.println("explicación de cómo se juega. (Pulsa enter para continuar)");
		sc.nextLine();
		tableroTuto[5][6] = (ROJO + barco + RESET);
		tableroTuto[9][9] = (VERDE + barco + RESET);
		tableroTuto[8][9] = (VERDE + barco + RESET);
		tableroTuto[7][9] = (VERDE + barco + RESET);
		String[][] tableroTutoRival = new String[10][10];
		String[][] tableroTutoRivalHecho = new String[10][10];
		rellenarMatriz(tableroTutoRival);
		rellenarMatriz(tableroTutoRivalHecho);
		tableroTutoRivalHecho[6][2] = (ROJO + barco + RESET);
		tableroTutoRivalHecho[1][7] = (VERDE + barco + RESET);
		tableroTutoRivalHecho[1][8] = (VERDE + barco + RESET);
		tableroTutoRivalHecho[1][6] = (VERDE + barco + RESET);
		System.out.println();
		System.out.println();
		System.out.println();
		imprimirMatriz(tableroTuto, tableroTutoRival);
		System.out.println(
				"Jugar es tan sencillo como encontrar ron en una taberna pirata. Echa un vistazo al tablero de la ");
		System.out.println(
				"izquierda, que es tuyo, y al de la derecha, el de tu rival, o sea, yo. Comienza tu asalto eligiendo ");
		System.out.println("una posición, como, por ejemplo, A1 (pulsa enter para continuar).");
		sc.nextLine();
		tableroTutoRival[0][0] = "O";
		System.out.println();
		System.out.println("AGUA");
		System.out.println();
		imprimirMatriz(tableroTuto, tableroTutoRival);
		System.out.println(
				"¿Lo ves? ¡AGUA! Has errado tu disparo y en mi tablero, ese fallo se plasma con un O, como una ");
		System.out.println(
				"bofetada del océano. Ahora, es mi turno. Si elijo F7, ¿qué acontecerá? (Pulsa enter para continuar)");
		sc.nextLine();
		tableroTuto[5][6] = (GRIS + barco + RESET);
		System.out.println();
		System.out.println("TOCADO Y HUNDIDO");
		System.out.println();
		imprimirMatriz(tableroTuto, tableroTutoRival);
		System.out.println(
				"Ves tu lancha ha cambiado de color, eso quiere decir que mi golpe le dio a uno de tus barcos, ");
		System.out.println("pero además como no solo lo he tocado si no que lo he hundido ya sé que por esa zona no ");
		System.out.println("tengo que investigar más, venga te vuelve a tocar, elige B9. (Pulsa enter para continuar)");
		sc.nextLine();
		tableroTutoRival[1][8] = (GRIS + barco + RESET);
		System.out.println();
		System.out.println("TOCADO");
		System.out.println();
		imprimirMatriz(tableroTuto, tableroTutoRival);
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println(
				"Me has dado, la prueba de ello es que en mi tablero ha salido un " + (GRIS + barco + RESET) + ".");
		System.out.println(
				"Sin embargo, no todo está perdido, ya que ha sido un TOCADO y no un HUNDIDO. Ahora, en tu próximo turno, ");
		System.out.println(
				"deberás seguir explorando esa área, ¡porque el resto de mi barco estará escondido por allí! ");
		System.out.println();
		System.out.println(
				"Y eso, mi valiente corsario, es el juego en su esencia. El primero en enviar al fondo marino toda ");
		System.out.println("la flota del otro se alza con la victoria. (Pulsa enter para continuar)");
		sc.nextLine();
		System.out.println();
		System.out.println();
		System.out.println();
		imprimirMatrizTramposo(tableroTuto, tableroTutoRival, tableroTutoRivalHecho);
		System.out.println("Y casi se me olvida, en el hipotético caso de que tus padres te abandonaran de pequeño y ");
		System.out.println(
				"eligieras hacer trampas, estos serían los tableros. El de la izquierda es el tuyo, el del centro es ");
		System.out.println("el mío vacío, y el de la derecha es el que te permite ver mi preciada flota :'(");
		System.out.println();
		System.out.println("Y con esto concluye este tutorial, ¿alguna pregunta?");
		sc.nextLine();
		System.out.println("Me alegra saber que todo te ha quedado claro. Disfruta del juego.");
		System.out.println();
		System.out.println();

	}

	public static void chistes() {
		Scanner sc = new Scanner(System.in);
		List<String> chistes = new ArrayList<>();

		chistes.add("Capitán, hay un agujero en el barco.\nLlame al pirata patapalo.\n¿Para qué?\nPues pa tapalo...");
		chistes.add("Un pirata le dice a sus marinos:\n¡Suban las velas!\ny los de abajo se quedaron sin luz...");
		chistes.add("Me gustaría ser pirata, no por el oro ni por la plata, sino por lo que tienes entre las patas...");
		chistes.add("¿Cuál es el colmo de un pirata?\nQue su novia le regale una peli original.");
		chistes.add("En un barco pirata, la tripulación es llamada por su capitán, que es tartamudo, y les dice:\n"
				+ "Es-es-es-escu-cu-cucharme cu-cu-cu-cu-cuando yo di-di-di-di-diga ti-ti-ti-tierra to-to-todos al a-a-a-gua.\n"
				+ "Cuando ya pasa un tiempo, el capitán dice:\nTi-ti-ti-ti-ti-ti-ti…\nLa tripulación, que creía que era tierra, se tira por la borda, y el capitán termina de decir:\nTi-ti-ti-tiburones...");
		chistes.add("Piratas en el mar:\n¡Abordar el barco!\nY el barco quedó precioso…");
		chistes.add(
				"Primer acto: sale un dinosaurio vestido de pirata\nSegundo acto: sale el mismo dinosaurio vestido de pirata\n"
						+ "Tercer acto: sale el mismo dinosaurio vestido de pirata\n¿Cómo se llamo la obra?\nDino a la pirateria...");
		chistes.add("En un barco pirata:\nCapitán, a la derecha veo un barco lleno de oro.\n"
				+ "Marinero, hable bien – responde el capitán - ¡se dice “estribor”!\n"
				+ "Capitán, a la derecha veo un barco lleno de estribor...");
		chistes.add("Son unos piratas que embarcan en una isla (los piratas son medio sordos). El capitán dice:\n"
				+ "¡¡¡Una boa!!!\nY su tripulación dice:\n¡Viva los novios!");
		chistes.add("En un barco pirata:\nCapitán, a la derecha veo un barco lleno de oro.\n"
				+ "Marinero, hable bien – responde el capitán - ¡se dice “estribor”!\n"
				+ "Capitán, a la derecha veo un barco lleno de estribor...");
		chistes.add("¿Por qué la calavera pirata no fue a la pista de baile?\n"
				+ "¿Porque no tenía cuerpo para bailar?\n" + "No, porque era feo, gordo y nadie lo quería.");
		chistes.add("¿Qué hace un pirata con un papel higiénico?\n¡Arrr-guño!");
		chistes.add("¿Cómo llaman los piratas a sus hijos?\n"
				+ "-No lo sé, estoy sufriendo mucho por dentro. Esto no es un chiste. Ayuda.\n"
				+ "-No, los llaman Ar-hijos.\n" + "-Jajaja, ArHijos.");
		chistes.add("¿Cuál es el animal marino más famoso entre los piratas?\n¡La arrr-dilla!");
		chistes.add("¿Qué hace un pirata con su ropa vieja?\n¡La arrr-ruga!");
		chistes.add("Un pirata entra a un bar con el timón del barco en la bragueta.\n"
				+ "El camarero le dice: \"Oye, ¿sabes que tienes el timón abierto?\"\n"
				+ "Y el pirata responde: \"¡Sí, es por donde dirijo mis negocios!\"");
		chistes.add("¿Cómo se llama el primo vegetariano del capitán Garfio?\n¡José Mota !");
		chistes.add("Un pirata llega al médico con el timón del barco en la entrepierna.\n"
				+ "El médico le pregunta: \"¿Y eso?\"\n"
				+ "El pirata responde: \"No sé, pero cada vez que doblo una esquina me da un giro.\"");
		chistes.add("¿Qué hace un pirata cuando termina de comer?\n¡Arrr-otar!");
		chistes.add("¿Por qué los piratas no pueden jugar a las cartas?\nPorque se les sientan en la arrr-mada.");
		chistes.add("¿Cuál es el animal favorito de los piratas?\n¡La arrr-dilla!");
		chistes.add("¿Cómo llaman los piratas a un grupo de amigos?\n¡Arrr-migos!");
		chistes.add("¿Cuál es la bebida favorita de los piratas?\n¡El arrr-rum!");
		chistes.add("¿Cómo se llama el pirata que siempre llega tarde?\n¡El capitán Gar-tarde!");
		chistes.add("Un pirata le pregunta a otro:\n" + "¿Cómo hiciste para conseguir ese parche en el ojo?\n"
				+ "Fue en una batalla, un mosquito me picó.");
		chistes.add("¿Por qué los piratas no pueden aprender el abecedario?\nPorque siempre se pierden en la \"X\".");
		chistes.add("Un pirata entra a una tienda y le pregunta al dependiente:\n" + "- ¿Tienen pantalones a rayas?\n"
				+ "- Raya la que me voy a hacer yo.");
		chistes.add("¿Cómo se llama el hermano vegetariano de Jack Sparrow?\n¡Jack Esparrago! Jajaja mátame.");
		chistes.add("Un pirata llega a la tienda y pregunta:\n¿Cuánto cuesta ese perfume?\n50 doblones.\n¡Pues vaya olor más caro!");
		chistes.add("¿Cuánto vale el bus?\n- 1 €.\n- Pues bajarse que me lo llevo.");


				

		while (!chistes.isEmpty()) {
			// Imprimir chiste al azar
			int indiceChiste = (int) (Math.random() * chistes.size());
			String chiste = chistes.get(indiceChiste);
			System.out.println("Chiste:");
			System.out.println(chiste);

			// Eliminar el chiste de la lista
			chistes.remove(indiceChiste);

			// Pedir al usuario que presione Enter o escriba "callate ya"
			System.out.println();
			System.out.println("Presiona Enter para escuchar otro chiste o escribe 'callate ya' para salir.");
			System.out.println();

			String respuesta = sc.nextLine();
			respuesta = respuesta.toLowerCase();

			if (respuesta.equalsIgnoreCase("callate ya")) {
				System.out.println();
				System.out.println("Tampoco hace falta que me hables así :(");
				System.out.println();
				break;
			}
		}

		// Si la lista de chistes está vacía
		if (chistes.isEmpty()) {
			System.out.println();
			System.out.println("¡Gracias por escucharme!");
			System.out.println();
		}

	}

}
