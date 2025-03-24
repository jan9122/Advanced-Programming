package Alumnes;

public class Assignatura {
    private final boolean mHonor;
    private double nota;
    private final int credits;
    private final String nom;

    public final int EXCELLENT = 9;
    public final int NOTABLE = 7;
    public final int APROVAT = 5;


    public Assignatura(String nom, int credits, double nota, boolean mHonor) throws IllegalArgumentException {
        if (credits < 0 || nota < 0) {
            throw new IllegalArgumentException("Els crèdits i la nota han de ser positius");
        }
        this.nom = nom;
        this.credits = credits;
        this.nota = nota;

        if (nota >= EXCELLENT) {
            this.mHonor = mHonor;
        } else {
            this.mHonor = false;
        }
    }

    public Assignatura(String nom) {
        this(nom, 0, 0.0, false);
    }

    public void setNota(double nota) {
        this.nota = nota;
    }

    public int getPunts() {
        if (nota >= EXCELLENT) {
            return 4 * credits;
        } else if (nota >= NOTABLE) {
            return 3 * credits;
        } else if (nota >= APROVAT) {
            return 2 * credits;
        } else {
            return 0;
        }
    }

    public int getCredits() {
        return credits;
    }

    @Override
    public String toString() {
        if (credits == 0)
            return nom + " - Nota: " + nota;
        // ↑ Si no té crèdits(és un alumne), no els mostra
        return nom + " (" + credits + " crèdits) - Nota: " + nota;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Assignatura a) {
            return nom.equals(a.nom);
        }
        return false;
    }

    public boolean getMatriculaHonor() {
        return mHonor;
    }

    public String getNom() {
        return nom;
    }
}
