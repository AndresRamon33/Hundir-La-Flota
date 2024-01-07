package HundirLaFlota;

import java.util.Scanner;

import java.util.Iterator;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;

public class quecojone {
	public static final String RESET = "\u001B[0m";
	public static final String ROJO = "\u001B[31m";
	public static final String VERDE = "\u001B[32m";
	public static final String AZUL = "\u001B[34m";
	public static final String NARANJA = "\u001B[38;5;208m";
	public static final String GRIS = "\u001B[38;5;240m";
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
	static boolean finTotal = false;
	// DETECTAR QUE ES PRIMERA VEZ
	static boolean primeraVez = true;

	public static void main(String[] args) {

		Scanner sc = new Scanner(System.in);

		int lancha = 5;
		int buque = 3;
		int acorazado = 1;
		int portaaviones = 1;
		boolean seguir = true;
		while (seguir) {
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

		// CALCULAMOS EL TOTAL DE POSCIONES QUE HAY BARCOS PARA CONTROLAR EL FIN DE
		// PARTIDA
		int totalBarcos = (l * 1) + (b * 3) + (z * 4) + (p * 5);
		System.out.println(totalBarcos);

		// AREGLAR LISTA POSRIVAL, NO SE PORQUE NO SE BOORAN LOS ELEMNTOS COPRRECTAMENTE
		// :(
		ListaPosRival = arreglarPosRival();

		// ELIMINAMOS ESPACIOS EN LAS LISTAS
		System.out.println("Elementos en el set: " + ListaPos);
		System.out.println("Elementos en el Rival: " + ListaPosRival);
		System.out.println();
		System.out.println();
		System.out.println();

		imprimirColores();
		imprimirMatriz(tablero, tableroRival);
		// imprimirMatrizJugador(tableroRivalHecho);

		System.out.println();
		System.out.println();
		System.out.println();

		jugar(tablero, tableroRival, tableroRivalHecho, totalBarcos);

	}

	public static void evitarBucleInfinito(String m[][], int l, int b, int z, int p) {

		rellenarMatriz(m);
		colocarFichas(m, l, b, z, p);

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
		int contador = 0;

		// BUCLE HASTA QUE SE ACABE LA PARTIDA
		while (!fin) {
			while (!entradaValida) {
				try {
					System.out.print("Introduce una posición: ");
					pos = sc.nextLine();
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
					System.out.print("Por favor, introduce una posición válida.\n");
					System.out.print("Para números, utiliza valores entre 1 y 10.\n");
					System.out.print("Para letras, utiliza valores entre A y J.  EJEMPLO: B2.\n");
					imprimirColores();
					imprimirMatriz(tablero, tableroRival);

				}
			}
			// PONER AL TABLERO
			// if (!posicionYaBombardeada) {
			if (tableroRivalHecho[columna][fila].equals("~")) {
				System.out.println("AGUA");
				tableroRival[columna][fila] = "O";
			} else if (tableroRivalHecho[columna][fila].equals((ROJO + barco + RESET))
					|| tableroRivalHecho[columna][fila].equals((AZUL + barco + RESET))
					|| tableroRivalHecho[columna][fila].equals((VERDE + barco + RESET))
					|| tableroRivalHecho[columna][fila].equals((NARANJA + barco + RESET))) {

				comprobarTocadoHundido(pos);
				tableroRival[columna][fila] = (GRIS + barco + RESET);
			}

			contador++;
			imprimirColores();
			imprimirMatriz(tablero, tableroRival);
			System.out.println();
			System.out.println();
			System.out.println();

			// }

			// RESTABLECER BIIIIEN
			entradaValida = false;

			fin = comprobarFinalPartida(tableroRival, total);

			if (fin) {
				System.out.println("FIN DE PARTIDA");
				primeraVez = true;
				finTotal = true;
				break;
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
				System.out.print("            LANCHA:       " + ROJO + barco + RESET + "                 C   ");
			} else if (letra == 'D') {
				System.out.print("            BUQUE:        " + VERDE + barco + RESET + "                 D   ");
			} else if (letra == 'E') {
				System.out.print("            ACORAZADO:    " + AZUL + barco + RESET + "                 E   ");
			} else if (letra == 'F') {
				System.out.print("            PORTAAVIONES: " + NARANJA + barco + RESET + "                 F   ");
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

	public static String[][] colocarFichas(String[][] m, int l, int b, int z, int p) {

		String[][] espejo = new String[10][10];

		// LANCHAS
		for (int j = 0; j < l; j++) {
			rellenarMatriz(espejo);
			comienzoBarco = 0;
			direccionVertical = 0;
			direccionHorizontal = 0;
			if (!matrizAleatoria) {
				System.out.print("LANCHA " + (j + 1) + ":");
				ListaPos.add("|");
			} else {
				ListaPosaux.add("|");
			}
			for (int i = 0; i < 1; i++) {
				if (!matrizAleatoria) {
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
			if (!matrizAleatoria) {
				System.out.print("BUQUE " + (j + 1) + ":");
				ListaPos.add("|");
			} else {
				ListaPosaux.add("|");
			}
			for (int i = 0; i < 3; i++) {
				if (!matrizAleatoria) {
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
			if (!matrizAleatoria) {
				System.out.print("ACORAZADO " + (j + 1) + ":");
				ListaPos.add("|");
			} else {
				ListaPosaux.add("|");
			}
			for (int i = 0; i < 4; i++) {
				if (!matrizAleatoria) {
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
			if (!matrizAleatoria) {
				System.out.print("PORTAAVIONES " + (j + 1) + ":");
				ListaPos.add("|");
			} else {
				ListaPosaux.add("|");
			}
			for (int i = 0; i < 5; i++) {
				if (!matrizAleatoria) {
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
		} else {
			ListaPos.add("|");
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

				String pos = "";

				if (!matrizAleatoria) {
					System.out.print("Introduce posición: ");
					pos = sc.nextLine();
					pos = pos.toUpperCase();
				} else {
					// MATRIZ ALEATORIA
					fila = (int) (Math.random() * 10);
					columna = (int) (Math.random() * 10);
				}

				// BORRAR TODA LA MATRIZ
				// if (pos.equals("BORRAR TODO")) {
				// crearTablero(l, b, z, p);

				// }

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
				 if (!matrizAleatoria) {
				System.out.println(intentosFallidos);
				System.out.print("Por favor, introduce una posición válida.\n");
				System.out.print("Para números, utiliza valores entre 1 y 10.\n");
				System.out.print("Para letras, utiliza valores entre A y J.  EJEMPLO: B2.\n");
				imprimirMatrizJugador(m);
				}
				intentosFallidos++;

				contador++;
			}

			if (intentosFallidos >= 400 && matrizAleatoria) {
				ListaPosaux.clear();
				// crearTablero( l, b, z, p);
				evitarBucleInfinito(m, l, b, z, p);

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
				"¡Arrrr grumete! ¿Quieres forjar tu propio campo de batalla con tus propias manos y astucia(s), ");
		System.out.println("o prefieres saquear uno que ya esté listo para la batalla?(n)");
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
				System.out.println("no disponemos de toda la jornada, decide con juicio o enfrenta las consecuencias.");
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

	public static void comprobarTocadoHundido(String s) {

		int aux = ListaPosRival.indexOf(s);

		if (aux >= 0 && aux < ListaPosRival.size() - 1) {
			if (ListaPosRival.get(aux - 1).equals("|") && ListaPosRival.get(aux + 1).equals("|")) {
				System.out.println("TOCADO Y HUNDIDO");
				System.out.println();
				ListaPosRival.remove(aux);
			} else {
				System.out.println("TOCADO");
				System.out.println();
				ListaPosRival.remove(aux);
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
			System.out.printf("%40s%n", "║                 4. ¿EN QUÉ CONSISTEN?                  ║");
			System.out.printf("%40s%n", "╚════════════════════════════════════════════════════════╝");

			try {
				int respuesta = sc.nextInt();
				String cont;

				switch (respuesta) {
				case 1:
					dificultad = 1;
					System.out.println("▶ Dificultad establecida: FÁCIL.");
					seguir = false;
					break;
				case 2:
					dificultad = 2;
					System.out.println("▶ Dificultad establecida: NORMAL.");
					seguir = false;
					break;
				case 3:
					dificultad = 3;
					System.out.println("▶ Dificultad establecida: DIFÍCIL.");
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
					System.out.println("Introduzca cualquier tecla para continuar: ");
					sc.nextLine();
					sc.next();
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

	}

}