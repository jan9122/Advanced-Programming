import java.util.ArrayList;

class Jugador<E extends ItipoPieza> {
    private class NodePieza{
        private E pieza;
        private NodePieza seguent;

        public NodePieza(E pieza, NodePieza seguent) {
            this.pieza = pieza;
            this.seguent = seguent;
        }
    }

    private NodePieza piezasVivas;

    public Jugador(ArrayList<E> piezasIniciales) {
        // Inicialitzem la llista enllaçada amb les peces inicials
        for (E pieza : piezasIniciales) {//posa les noves al principi
            piezasVivas = new NodePieza(pieza, piezasVivas);
        }
    }

    public ArrayList<E> getPiezasVivas() {
        ArrayList<E> copia = new ArrayList<>();
        NodePieza actual = piezasVivas;
        while (actual != null) {
            copia.add(actual.pieza);
            actual = actual.seguent;
        }
        return copia;
    }

    // Método para mover una pieza usando la posición anterior
    public void moverPieza(int columnaAnterior, int filaAnterior, int nuevaColumna, int nuevaFila) {
        if (this.buscarEnPosicion(nuevaFila, nuevaColumna) != null)
            throw new RuntimeException("Posició ocupada per una peça del mateix jugador");

        E item = this.buscarEnPosicion(filaAnterior,columnaAnterior);
        if( item == null)
            throw new RuntimeException("Peça no trobada fila:" + filaAnterior + " columna:" + columnaAnterior);

        item.setPosicion(nuevaFila, nuevaColumna);
        System.out.println("Peça moguda");
    }

    // Método para buscar en una posición específica
    private E buscarEnPosicion(int nuevaFila, int nuevaColumna) {
        NodePieza actual = piezasVivas;
        while (actual != null) {
            if (actual.pieza.getFila() == nuevaFila && actual.pieza.getColumna() == nuevaColumna) {
                return actual.pieza;
            }
            actual = actual.seguent;
        }
        return null;
    }

    // Método para buscar y eliminar una pieza en una posición específica
    public boolean eliminarPiezaEnPosicion(int columna, int fila) throws FiJocException {
        NodePieza actual = piezasVivas;
        NodePieza anterior = null;
        while (actual != null) {
            if (actual.seguent == null) {
                return false;
            }
            if (actual.pieza.getFila() == fila && actual.pieza.getColumna() == columna) {
                if (anterior == null){
                    piezasVivas = actual.seguent;
                }
                else{
                    anterior.seguent = actual.seguent;
                }
                if (actual.pieza.fiJoc())
                    throw new FiJocException();
                return true;
            }
            anterior = actual;
            actual = actual.seguent;
        }
        return false;
    }
}