package Alumnes;

// ↓ Seqüència enllaçada
public class Alumnes_SEC implements Comparable<Alumnes_SEC>{

    private class Node {
        Node next;
        Assignatura info;

        // ↓ constructor normal
        public Node(Assignatura info) {
            this.info = info;
            this.next = null;
        }
        // ↓ Inicia la llista amb un alumne
        public Node(String nom) {
            this(new Assignatura(nom));
        }
    }

    private Node cap;

    // ↓ Inicialtitza la llista amb un alumne
    public Alumnes_SEC(String nom) {
        cap = new Node(nom);
    }

    public void addAssignatura(Assignatura nova) {
        Node aux = cap;
        int mitjana = 0;
        int mitjanaC = 0;

        while (aux.next != null) {
            aux = aux.next;
        }
        // ↓ Si ja existeix l'assignatura, la sobreescriu
        if (aux.info.equals(nova)) {
            aux.info = nova;
            return;
        }
        aux.next = new Node(nova);

        // ↓ Càlcul de la mitjana
        aux = cap;
        while (aux.next != null) {
            mitjanaC++;
            mitjana += aux.next.info.getPunts();
            aux = aux.next;
        }
        // ↓ Actualitza la mitjana
        if (mitjanaC == 0)
            cap.info.setNota(0);
        else
            cap.info.setNota(((double) mitjana) / mitjanaC);
    }

    public boolean hiHa(int punts) {
        Node aux = cap;
        while (aux != null) {
            if (aux.info.getPunts() == punts) {
                return true;
            }
            aux = aux.next;
        }
        return false;
    }

    // ↓ Retorna si almenys una assignatura té matrícula d'honor
    public boolean getMatriculaHonor() {
        Node aux = cap;
        while (aux != null) {
            if (aux.info.getMatriculaHonor()) {
                return true;
            }
            aux = aux.next;
        }
        return false;
    }

    // ↓ Compara segons el nom i la nota
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Alumnes_SEC a) {
            return cap.info.toString().equals(a.cap.info.toString());
        }
        return false;
    }

    // ↓ Compara segons els punts
    @Override
    public int compareTo(Alumnes_SEC other) {
        int result = Integer.compare(this.cap.info.getPunts(), other.cap.info.getPunts());
        if (result == 0) {
            // If scores are equal, compare by name
            result = this.cap.info.getNom().compareTo(other.cap.info.getNom());
        }
        return result;
    }

    @Override
    public String toString() {
        // ↓ Utilitza el mètode toString 'especial' de Assignatura que no mostra els crèdits
        return cap.info.toString();
    }
}