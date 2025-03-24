public interface ItipoPieza {
    //**********************
    //      NO TOCAR
    //**********************
    public abstract char getTipus();
    public abstract int getFila();
    public abstract int getColumna();
    public abstract void setPosicion(int fila, int columna) throws RuntimeException;

    public abstract boolean fiJoc();
}
