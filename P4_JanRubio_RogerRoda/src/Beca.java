import Alumnes.Alumnes_SEC;
import Alumnes.Assignatura;
import EstructuraArbre.AcbEnll;
import EstructuraArbre.ArbreException;

import java.util.Queue;
import java.util.LinkedList;
import java.util.Random;
import java.util.Scanner;

public class Beca {

    private AcbEnll<Alumnes_SEC> arbreACB;
    private Queue<Alumnes_SEC> llistaDescendent;
    private final Scanner scanner;

    public Beca() {

        arbreACB = new AcbEnll<>();
        try {
            Alumnes_SEC alumne = exempleRosa();
            arbreACB.inserir(alumne);
            alumne = exempleEnric();
            arbreACB.inserir(alumne);
            alumne = exempleRandom();
            arbreACB.inserir(alumne);
        } catch (ArbreException e) {
            System.out.print("Error inserint alumne: ");
            System.out.println(e.getMessage());
        }

        llistaDescendent = arbreACB.getDescendentList();

        scanner = new Scanner(System.in);
    }

    /*
    * ↓ MAIN ↓
    * ↓ MAIN ↓
    * ↓ MAIN ↓
    * ↓ MAIN ↓
    * ↓ MAIN ↓
    * ↓ MAIN ↓
    */
    public static void main(String[] args) {
        Beca beca = new Beca();
        int opcio = menu();

        while (opcio != 5) {
            switch (opcio) {
                case 1:
                    beca.afegirAlumne();
                    break;
                case 2:
                    beca.esborrarAlumne(); // ← mètode creat per que quedi més ordenat
                    break;
                case 3:
                    System.out.println(beca);
                    break;
                case 4:
                    beca.esborrarAlumnesSenseMatricula();
                    break;
            }
            opcio = menu();
        }
    }

    /*
     * ↑ MAIN ↑
     * ↑ MAIN ↑
     * ↑ MAIN ↑
     * ↑ MAIN ↑
     * ↑ MAIN ↑
     * ↑ MAIN ↑
     */

    // ↓ Menú de l'aplicació
    private static int menu() {
        Scanner menuScanner = new Scanner(System.in);

        System.out.println("1. Afegir alumne");
        System.out.println("2. Esborrar un alumne a partir del seu nom");
        System.out.println("3. Mostrar tots els alumnes en ordre descendent");
        System.out.println("4. Esborrar alumnes sense matrícula d'honor");
        System.out.println("5. Sortir del programa");
        System.out.print("Introdueix una opció: ");

        // ↓ Comprova que l'opció sigui vàlida
        int select = menuScanner.nextInt();
        if (select < 1 || select > 5){
            System.out.println("Opció no vàlida");
            return menu();
        }

        return select;
    }

    private void esborrarAlumne() {
        System.out.println("Introdueix el nom de l'alumne a esborrar:");
        String nom = scanner.nextLine();
        Alumnes_SEC alumne = new Alumnes_SEC(nom);

        try {
            arbreACB.esborrar(alumne);
        } catch (ArbreException e) {
            System.out.println("Error esborrant alumne");
        }
        llistaDescendent = arbreACB.getDescendentList();
    }


    private Alumnes_SEC exempleRosa() {
        Alumnes_SEC rosa = new Alumnes_SEC("Rosa");
        Assignatura nova = new Assignatura("Fonaments de la programació", 6, 7.0, false);
        rosa.addAssignatura(nova);
        nova = new Assignatura("POO", 6, 5.0, false);
        rosa.addAssignatura(nova);
        nova = new Assignatura("EDA", 4, 9.0, false);
        rosa.addAssignatura(nova);
        nova = new Assignatura("PA", 4, 5.0, false);
        rosa.addAssignatura(nova);
        return rosa;
    }

    private Alumnes_SEC exempleEnric() {
        Alumnes_SEC Enric = new Alumnes_SEC("Enric");
        Assignatura nova = new Assignatura("Fonaments de la programació", 6, 8.0, false);
        Enric.addAssignatura(nova);
        nova = new Assignatura("POO", 6, 6.0, false);
        Enric.addAssignatura(nova);
        nova = new Assignatura("EDA", 4, 9.0, true);
        Enric.addAssignatura(nova);
        nova = new Assignatura("PA", 4, 3.0, false);
        Enric.addAssignatura(nova);
        return Enric;
    }

    private Alumnes_SEC exempleRandom() {
        Random random = new Random();

        Alumnes_SEC rng = new Alumnes_SEC("Random");
        Assignatura nova = new Assignatura("Fonaments de la programació", 6,10 * random.nextDouble(), random.nextBoolean());
        rng.addAssignatura(nova);
        nova = new Assignatura("POO", 6, 10 * random.nextDouble(), random.nextBoolean());
        rng.addAssignatura(nova);
        nova = new Assignatura("EDA", 4, 10 * random.nextDouble(), random.nextBoolean());
        rng.addAssignatura(nova);
        nova = new Assignatura("PA", 4, 10 * random.nextDouble(), random.nextBoolean());
        rng.addAssignatura(nova);
        return rng;
    }

    // ↓ Esborra els alumnes que no tenen cap matrícula d'honor
    public void esborrarAlumnesSenseMatricula() {
        while (!llistaDescendent.isEmpty()) {
            Alumnes_SEC alumne = llistaDescendent.poll();
            if (!alumne.getMatriculaHonor()){
                try {
                    arbreACB.esborrar(alumne);
                } catch (ArbreException e) {
                    System.out.println("Error esborrant alumne");
                }
            }
        }
        // ↓ Refà la llista descendent
        llistaDescendent = arbreACB.getDescendentList();
    }

    public void afegirAlumne() {
        System.out.println("Introdueix el nom de l'alumne:");
        String nom = scanner.nextLine();
        Alumnes_SEC alumne = new Alumnes_SEC(nom);

        // ↓ Fonaments de la programació
        System.out.println("Introdueix La nota de l'alumne en Fonaments de la programació:");
        double nota = scanner.nextDouble();
        boolean mHonor = false;
        if (nota >= 9){
            System.out.println("Introdueix si l'alumne té matrícula d'honor (true/false):");
            mHonor = scanner.nextBoolean();
        }
        Assignatura nova = new Assignatura("Fonaments de la programació", 6, nota, mHonor);
        alumne.addAssignatura(nova);

        // ↓ POO
        System.out.println("Introdueix La nota de l'alumne en POO:");
        nota = scanner.nextDouble();
        mHonor = false;
        if (nota >= 9){
            System.out.println("Introdueix si l'alumne té matrícula d'honor (true/false):");
            mHonor = scanner.nextBoolean();
        }
        nova = new Assignatura("POO", 6, nota, mHonor);
        alumne.addAssignatura(nova);

        // ↓ EDA
        System.out.println("Introdueix La nota de l'alumne en EDA:");
        nota = scanner.nextDouble();
        mHonor = false;
        if (nota >= 9){
            System.out.println("Introdueix si l'alumne té matrícula d'honor (true/false):");
            mHonor = scanner.nextBoolean();
        }
        nova = new Assignatura("EDA", 4, nota, mHonor);
        alumne.addAssignatura(nova);

        // ↓ PA
        System.out.println("Introdueix La nota de l'alumne en PA:");
        nota = scanner.nextDouble();
        mHonor = false;
        if (nota >= 9){
            System.out.println("Introdueix si l'alumne té matrícula d'honor (true/false):");
            mHonor = scanner.nextBoolean();
        }
        nova = new Assignatura("PA", 4, nota, mHonor);
        alumne.addAssignatura(nova);

        // ↓ Insereix l'alumne a l'arbre i actualitza la llista descendent
        try {
            arbreACB.inserir(alumne);
        } catch (ArbreException e) {
            System.out.println("Error inserint l'alumne");
        }
        llistaDescendent = arbreACB.getDescendentList();
    }

    // ↓ Indica si s'ha arribat al final de la llista
    private boolean finalRecorregut(){
        if (llistaDescendent.isEmpty()){
            System.out.println("No hi ha més alumnes a mostrar");
            return true;
        }

        return false;
    }

    // ↓ Retorna l'alumne actual i avança la llista
    private Alumnes_SEC segRecorregut(){
        if (finalRecorregut()){
            return null;
        }

        return llistaDescendent.poll();
    }

    @Override
    public String toString() {
        Alumnes_SEC alumne = segRecorregut();
        String strng = "";

        while (alumne != null){
            strng = strng + alumne.toString() + "\n";

            alumne = segRecorregut();
        }

        llistaDescendent = arbreACB.getDescendentList();
        // ↑ Refà la llista descendent

        return strng;
    }


}