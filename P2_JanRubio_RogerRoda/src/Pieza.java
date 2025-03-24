public class Pieza implements ItipoPieza{

    // atributs d'objectes
    private final char tipo;
    private int fila;
    private char columna;

    // constants de la classe
    public final char PAWN = 'p';    // Peón
    public final char KNIGHT = 'N';   // Caballo
    public final char BISHOP = 'B';   // Alfil
    public final char ROOK = 'R';     // Torre
    public final char QUEEN = 'Q';    // Dama
    public final char KING = 'K';



    public Pieza(char tipo, int fila, int columna) {
        // comprovem que no dona excepció el tipus
        this.checkTipo(tipo);
        //comprovem que no dona excepció la posició
        this.setPosicion(fila,columna);
        this.tipo = tipo;
    }

    public char getTipus() {
        return this.tipo;
    }

    public int getFila() {
        return this.fila;
    }

    public int getColumna() {
        return (int) this.columna-65;
    }

    @Override
    public void setPosicion(int fila, int columna) {
        if( fila< 0 || fila > 8 || columna < 0 || columna > 8)
            throw new IllegalArgumentException("posició incorrecte");
        this.fila = fila;
        this.columna = (char) (65+columna);
    }

    @Override
    public boolean fiJoc() {
        return this.tipo == KING;
    }

    private void checkTipo(char tipo) {
        switch (tipo) {
            case PAWN:
            case QUEEN:
            case KNIGHT:
            case ROOK:
            case KING:
            case BISHOP:
                return ;
            default:
                throw new IllegalArgumentException("Desconegut");
        }
    }

}
