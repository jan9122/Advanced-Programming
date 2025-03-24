import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        mostrarMenu();
    }

    // Muestra el menú y permite la navegación del usuario
    private static void mostrarMenu() {
        int userSelect; // Opción seleccionada por el usuario
        System.out.println("Selecciona una opción:\n" +
                "1- Partida Nueva\n" +
                "2- Reproducir Partida\n" +
                "3- Salir");
        userSelect = scanner.nextInt();
        scanner.nextLine(); // Limpia el buffer del scanner
        switch (userSelect){
            case 1:
                jugarNovaPartida(); // Inicia una nueva partida
                break;
            case 2:
                reproduirPartida(); // Reproduce una partida guardada
                System.out.println();
                mostrarMenu(); // Vuelve al menú tras reproducir la partida
                break;
            case 3:
                System.out.println("Gracias por jugar. ¡Hasta pronto!");
                System.exit(0); // Sale del programa
                break;
            default:
                System.out.println("Opción no disponible, inténtalo de nuevo:");
                mostrarMenu(); // Repite el menú si se introduce una opción inválida
                break;
        }
    }

    // Inicia una nueva partida
    private static void jugarNovaPartida() {
        ArrayList<Pieza> blancas = iniciarJuegoBlancas(); // Inicializa piezas blancas
        ArrayList<Pieza> negras = iniciarJuegoNegras();   // Inicializa piezas negras
        Jugador<Pieza> jugadorBlancas = new Jugador<>(blancas);
        Jugador<Pieza> jugadorNegras = new Jugador<>(negras);
        Torns<String> tornsGuardats = new Torns<>(); // Almacena los turnos

        boolean partidaActiva = true; // Indica si la partida está activa
        boolean whiteToMove = true;   // Controla si es turno de las blancas
        String torn = null;           // Almacena el turno actual

        // Ciclo del juego hasta que la partida se termine
        while (partidaActiva){

            if (whiteToMove){
                System.out.println("Juegan Blancas:");
            }else {
                System.out.println("Juegan Negras:");
            }
            mostrarTauler(jugadorBlancas, jugadorNegras); // Muestra el tablero
            System.out.println("Introduce tu movimiento (por ejemplo, 'C2 C4'): ");
            torn = scanner.nextLine().trim(); // Lee el turno del jugador

            if (torn.equalsIgnoreCase("exit")) {
                System.out.println("Has salido de la partida.");
                partidaActiva = false;
                break;
            }

            try{
                // Si es turno de blancas
                if (whiteToMove){
                    if (tornToPosition(torn, jugadorBlancas, jugadorNegras)){
                        tornsGuardats.afegirTorn(torn); // Guarda el turno
                        whiteToMove = false; // Cambia el turno a negras
                    }
                } else { // Si es turno de negras
                    if (tornToPosition(torn, jugadorNegras, jugadorBlancas)){
                        tornsGuardats.afegirTorn(torn); // Guarda el turno
                        whiteToMove = true;  // Cambia el turno a blancas
                    }
                }
            } catch(FiJocException e) {
                mostrarTauler(jugadorBlancas, jugadorNegras); // Muestra el tablero final
                System.out.println("¡El rey enemigo ha sido capturado!");
                tornsGuardats.afegirTorn(torn); // Guarda el último turno
                break; // Finaliza la partida
            }
        }

        // Guarda la partida al finalizar
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
        String fechaHora = now.format(formatter); // Genera la fecha y hora actuales

        String nombreArchivo = "partida_guardada_" + fechaHora + ".txt";

        tornsGuardats.guardarFitxer(nombreArchivo); // Guarda el archivo con los turnos
        System.out.println("Partida guardada en el archivo: " + nombreArchivo);
    }

    // Reproduce una partida guardada
    private static void reproduirPartida() {
        Jugador<Pieza> jugadorBlancas = new Jugador<>(iniciarJuegoBlancas()); // Inicializa piezas blancas
        Jugador<Pieza> jugadorNegras = new Jugador<>(iniciarJuegoNegras());   // Inicializa piezas negras
        String torn = null; // Turno actual

        try {
            ArrayList<String> torns = llegirTorns(); // Lee los turnos guardados

            System.out.println(torns); // Muestra los turnos en consola

            for (int i = 0; i < torns.size(); i++) {
                torn = torns.get(i);

                tornToPosition(torn, jugadorBlancas, jugadorNegras); // Mueve las blancas
                System.out.println("\n-_-_-_-_-_ " + torn + " _-_-_-_-_-");
                mostrarTauler(jugadorBlancas, jugadorNegras); // Muestra el tablero tras el movimiento

                i++;
                torn = torns.get(i);

                tornToPosition(torn, jugadorNegras, jugadorBlancas); // Mueve las negras
                System.out.println("\n-_-_-_-_-_ " + torn + " _-_-_-_-_-");
                mostrarTauler(jugadorBlancas, jugadorNegras); // Muestra el tablero tras el movimiento
            }
        }catch(FiJocException e) {
            System.out.println("\n-_-_-_-_-_ " + torn + " _-_-_-_-_-");
            System.out.println("¡El rey enemigo ha sido capturado!");
            mostrarTauler(jugadorBlancas, jugadorNegras); // Muestra el tablero final
        }catch (Exception e) {
            System.out.println("Error al reproducir la partida: " + e.getMessage());
        }
    }

    // Lee y retorna los turnos guardados
    private static ArrayList<String> llegirTorns() {
        System.out.println("Introduce el nombre del archivo de turnos: ");
        String nombreArchivo = scanner.nextLine(); // Nombre del archivo que contiene los turnos

        try {
            Torns<String> torns = new Torns<>(nombreArchivo);
            ArrayList<String> tornList = new ArrayList<>();
            try {
                // Agrega turnos uno por uno a la lista
                while (true){
                    tornList.add(torns.agafarPrimerTorn());
                }
            }catch (NoSuchFieldException e){
                System.out.println("Todos los turnos se han cargado correctamente.");
            }
            return tornList;
        }catch (Exception e){
            System.out.println("Error al leer la partida: " + e.getMessage());
            return llegirTorns(); // Intenta nuevamente si falla la lectura
        }
    }

    // Procesa un turno, moviendo una pieza en el tablero
    private static boolean tornToPosition(String torn, Jugador<Pieza> p1, Jugador<Pieza> p2) throws FiJocException {
        try {
            String[] posicions = torn.split(" ");
            if (posicions.length != 2) {
                throw new IllegalArgumentException("El turno no es válido. Formato: 'C2 C4'");
            }

            String posInicial = posicions[0];
            String posFinal = posicions[1];

            int lastCol = posInicial.charAt(0) - 'A' + 1; // Convierte la columna inicial (A=1, ..., H=8)
            int lastRow = Integer.parseInt(String.valueOf(posInicial.charAt(1))); // Fila inicial
            int newCol = posFinal.charAt(0) - 'A' + 1;   // Convierte la columna final
            int newRow = Integer.parseInt(String.valueOf(posFinal.charAt(1))); // Fila final

            // Intenta mover la pieza
            p1.moverPieza(lastCol, lastRow, newCol, newRow);

            // Verifica si hay una pieza en la nueva posición para capturar
            if (p2.eliminarPiezaEnPosicion(newCol, newRow)) {
                System.out.println("Pieza capturada en " + posFinal);
            }
            return true;

        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
            return false;
        }catch (NoSuchElementException e) {
            System.out.println("Error: Elemento no encontrado.");
            return false;
        }catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Error: Posición fuera de los límites del tablero.");
            return false;
        }catch (Exception e) {
            if (e instanceof FiJocException)
                throw e;
            System.out.println("Error procesando el turno: " + e.getMessage());
            return false;
        }
    }

    // Muestra el tablero de ajedrez
    private static void mostrarTauler(Jugador<Pieza> blanc, Jugador<Pieza> negre) {
        // Crear el tablero vacío
        char[][] tauler = new char[8][8];

        for (char[] fila : tauler){
            Arrays.fill(fila, ' '); // Rellena las casillas vacías con un espacio
        }

        // Añadir piezas blancas
        for (Pieza pieza : blanc.getPiezasVivas()) {
            tauler[pieza.getFila() - 1][pieza.getColumna() - 1] = piezaUnicode(pieza.getTipus(), false);
        }

        // Añadir piezas negras
        for (Pieza pieza : negre.getPiezasVivas()) {
            tauler[pieza.getFila() - 1][pieza.getColumna() - 1] = piezaUnicode(pieza.getTipus(), true);
        }

        // Mostrar el tablero
        System.out.println("    A    B    C    D   E    F   G   H ");
        System.out.println("   ------------------------------------");
        for (int i = 7; i >= 0; i--) {
            System.out.print((i + 1) + " |");
            for (int j = 0; j < 8; j++) {
                System.out.print(" " + tauler[i][j] + "  ");
            }
            System.out.println(" ");
            System.out.println("   ------------------------------------");
        }
        System.out.println("    A    B    C    D   E    F   G   H ");
    }

    // Convierte el tipo de pieza a su símbolo Unicode correspondiente
    private static char piezaUnicode(char tipus, boolean esBlanca) {
        if (esBlanca) {
            switch (tipus) {
                case 'K': return '♔'; // Rey blanco
                case 'Q': return '♕'; // Reina blanca
                case 'R': return '♖'; // Torre blanca
                case 'B': return '♗'; // Alfil blanco
                case 'N': return '♘'; // Caballo blanco
                case 'p': return '♙'; // Peón blanco
                default: return ' ';
            }
        } else {
            switch (tipus) {
                case 'K': return '♚'; // Rey negro
                case 'Q': return '♛'; // Reina negra
                case 'R': return '♜'; // Torre negra
                case 'B': return '♝'; // Alfil negro
                case 'N': return '♞'; // Caballo negro
                case 'p': return '♟'; // Peón negro
                default: return ' ';
            }
        }
    }

    // Inicializa las piezas blancas
    private static ArrayList<Pieza> iniciarJuegoBlancas() {
        ArrayList<Pieza> blancas = new ArrayList<>();

        for (int i = 0; i < 8; i++){
            blancas.add(new Pieza('p', 2, i + 1)); // Peones blancos
        }

        // Añadir piezas principales blancas
        blancas.add(new Pieza('R', 1, 1)); // Torre
        blancas.add(new Pieza('N', 1, 2)); // Caballo
        blancas.add(new Pieza('B', 1, 3)); // Alfil
        blancas.add(new Pieza('Q', 1, 4)); // Reina
        blancas.add(new Pieza('K', 1, 5)); // Rey
        blancas.add(new Pieza('B', 1, 6)); // Alfil
        blancas.add(new Pieza('N', 1, 7)); // Caballo
        blancas.add(new Pieza('R', 1, 8)); // Torre

        return blancas;
    }

    // Inicializa las piezas negras
    private static ArrayList<Pieza> iniciarJuegoNegras() {
        ArrayList<Pieza> negras = new ArrayList<>();

        for (int i = 0; i < 8; i++){
            negras.add(new Pieza('p', 7, i + 1)); // Peones negros
        }

        // Añadir piezas principales negras
        negras.add(new Pieza('R', 8, 1)); // Torre
        negras.add(new Pieza('N', 8, 2)); // Caballo
        negras.add(new Pieza('B', 8, 3)); // Alfil
        negras.add(new Pieza('Q', 8, 4)); // Reina
        negras.add(new Pieza('K', 8, 5)); // Rey
        negras.add(new Pieza('B', 8, 6)); // Alfil
        negras.add(new Pieza('N', 8, 7)); // Caballo
        negras.add(new Pieza('R', 8, 8)); // Torre

        return negras;
    }
}
