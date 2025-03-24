// Clase que representa una pieza de ajedrez
public class Pieza implements ItipoPieza {

    // Atributos de la pieza
    char tipus;     // Tipo de pieza: p=peón, N=caballo, B=alfil, R=torre, Q=dama, K=rey
    int fila;      // Fila de la posición (índice 1, 1-8)
    int columna;   // Columna de la posición (índice 1, 1-8)

    // Posibles valores de 'tipus'
    public final char PAWN = 'p';    // Peón
    public final char KNIGHT = 'N';   // Caballo
    public final char BISHOP = 'B';   // Alfil
    public final char ROOK = 'R';     // Torre
    public final char QUEEN = 'Q';    // Dama
    public final char KING = 'K';      // Rey

    // Constructor que inicializa el tipo y la posición de la pieza
    public Pieza(char tipus, int fila, int columna) {
        // Verifica si el tipo de pieza es correcto
        if (!checkTipo(tipus))
            throw new RuntimeException("Clase incorrecta seleccionada");
        this.tipus = tipus;
        setPosicion(fila, columna); // Establece la posición
    }

    // Métodos de la interfaz ItipoPieza

    @Override
    public char getTipus() {
        return tipus; // Retorna el tipo de pieza
    }

    @Override
    public int getFila() {
        return fila; // Retorna la fila de la pieza
    }

    @Override
    public int getColumna() {
        return columna; // Retorna la columna de la pieza
    }

    @Override // Establece la posición de la pieza
    public void setPosicion(int fila, int columna) throws RuntimeException {
        // Verifica que la posición esté dentro de los límites del tablero
        if (fila > 8 || fila < 1 || columna > 8 || columna < 1)
            throw new RuntimeException("Posición fuera de los límites");
        this.fila = fila;
        this.columna = columna; // Establece la fila y la columna
    }

    @Override // Verifica si la pieza es un rey
    public boolean fiJoc() {
        return getTipus() == KING; // Devuelve verdadero si la pieza es un rey
    }

    // Verifica que el tipo de pieza sea correcto
    private boolean checkTipo(char tipo) {
        return tipo == PAWN || tipo == KNIGHT || tipo == BISHOP ||
                tipo == ROOK || tipo == QUEEN || tipo == KING; // Retorna verdadero si el tipo es válido
    }

    // Traduce el número de columna a su letra correspondiente (A-H)
    private char translateColumna() {
        return switch (columna) { // Cambia cada columna a su letra correspondiente
            case 1 -> 'A';
            case 2 -> 'B';
            case 3 -> 'C';
            case 4 -> 'D';
            case 5 -> 'E';
            case 6 -> 'F';
            case 7 -> 'G';
            case 8 -> 'H';
            default -> 'Þ'; // Si hay un error, devuelve 'Þ'
        };
    }

    @Override // Retorna la representación en forma de cadena de la pieza (Ej. A6)
    public String toString() {
        String charCluster = ""; // Inicializa una cadena vacía
        return charCluster + translateColumna() + fila; // Devuelve la columna y la fila en formato "A6"
    }

    @Override // Compara si dos piezas son iguales basándose en su posición
    public boolean equals(Object o) {
        // Verifica si el objeto es una instancia de Pieza
        if (o instanceof Pieza pieza)
            return this.columna == pieza.columna && this.fila == pieza.fila; // Devuelve verdadero si están en la misma posición

        return false; // Devuelve falso si no son iguales
    }
}
