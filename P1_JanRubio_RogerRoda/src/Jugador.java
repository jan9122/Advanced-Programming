import java.util.ArrayList;

// Clase que representa a un jugador en el juego de ajedrez
public class Jugador<E extends ItipoPieza> {
    private ArrayList<E> piezasVivas; // Lista que contiene todas las piezas que el jugador tiene

    // Constructor que inicializa el jugador con un conjunto de piezas vivas
    public Jugador(ArrayList<E> alivePieces) {
        this.piezasVivas = alivePieces; // Asigna las piezas vivas al jugador
    }

    // Método para obtener las piezas vivas del jugador
    public ArrayList<E> getPiezasVivas() {
        return piezasVivas; // Devuelve la lista de piezas vivas
    }

    // Cambia la fila y columna de una pieza dada por un nuevo conjunto de coordenadas
    public void moverPieza(int lastCol, int lastRow, int newCol, int newRow) {
        // Obtiene la pieza en la posición de destino
        Pieza pieza = (Pieza) buscarEnPosicion(newCol, newRow);
        // Verifica si la posición está ocupada
        if (pieza != null) {
            throw new RuntimeException("Ya hay una pieza en esa ubicación");
        }

        // Obtiene la pieza en la posición de origen
        pieza = (Pieza) buscarEnPosicion(lastCol, lastRow);
        // Verifica si hay una pieza para mover
        if (pieza == null)
            throw new RuntimeException("No hay ninguna pieza disponible en esa ubicación");

        // Establece la nueva posición para la pieza
        pieza.setPosicion(newRow, newCol);
    }

    // Busca una pieza en una ubicación específica
    private E buscarEnPosicion(int column, int row) {
        for (E element : piezasVivas) {
            // Verifica si el elemento es una instancia de Pieza
            if (element instanceof Pieza pieza) {
                // Utiliza los métodos getter para una mejor encapsulación
                if (pieza.getFila() == row && pieza.getColumna() == column) {
                    return element; // Retorna la pieza encontrada
                }
            }
        }
        return null; // Retorna null si no se encuentra ninguna pieza en la posición especificada
    }

    // Retorna verdadero si es posible eliminar la pieza, de lo contrario falso
    public boolean eliminarPiezaEnPosicion(int col, int row) throws FiJocException {
        // Obtiene la pieza en la posición especificada
        Pieza pieza = (Pieza) buscarEnPosicion(col, row);
        // Verifica si no hay ninguna pieza
        if (pieza == null)
            return false;

        // Verifica si la pieza es un rey
        if (pieza.fiJoc())
            throw new FiJocException("El rey enemigo ha sido capturado.");

        // Elimina la pieza de la lista de piezas vivas y devuelve el resultado
        return piezasVivas.remove(pieza);
    }
}
