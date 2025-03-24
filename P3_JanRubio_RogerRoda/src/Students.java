import java.util.ArrayList;

public class Students {
    private StudentNode head; // Primer nodo de la lista enlazada de estudiantes

    // Clase interna StudentNode para representar cada nodo de la lista
    private class StudentNode {
        BinaryTree studentTree;  // Árbol binario que representa la familia del estudiante
        StudentNode next;        // Siguiente nodo en la lista enlazada

        public StudentNode(BinaryTree studentTree) {
            this.studentTree = studentTree;
            this.next = null;
        }
    }

    public Students() {
        this.head = null;
    }

    // Añade un estudiante de manera ordenada alfabéticamente por nombre
    public void addStudent(BinaryTree newStudent) {
        String newStudentName = newStudent.getName();
        StudentNode newNode = new StudentNode(newStudent);

        // Si la lista está vacía o el nuevo estudiante va antes del primero
        if (head == null || newStudentName.compareToIgnoreCase(head.studentTree.getName()) < 0) {
            newNode.next = head;
            head = newNode;
            return;
        }

        // Insertar en la posición correcta manteniendo el orden alfabético
        StudentNode current = head;
        while (current.next != null && current.next.studentTree.getName().compareToIgnoreCase(newStudentName) < 0) {
            current = current.next;
        }

        newNode.next = current.next;
        current.next = newNode;
    }

    // Elimina un estudiante por nombre de la lista
    public void removeStudent(String name) {
        if (head == null) return; // Lista vacía

        // Si el estudiante a eliminar es el primero en la lista
        if (head.studentTree.getName().equalsIgnoreCase(name)) {
            head = head.next;
            return;
        }

        // Buscar el estudiante en la lista enlazada
        StudentNode current = head;
        while (current.next != null) {
            if (current.next.studentTree.getName().equalsIgnoreCase(name)) {
                current.next = current.next.next;
                return;
            }
            current = current.next;
        }
    }

    // Busca un estudiante por nombre y devuelve su árbol binario
    public BinaryTree getStudent(String name) {
        StudentNode current = head;
        while (current != null) {
            if (current.studentTree.getName().equalsIgnoreCase(name)) {
                return current.studentTree;
            }
            current = current.next;
        }
        return null; // Retorna null si no se encuentra el estudiante
    }

    // Devuelve una lista con los nombres de todos los estudiantes
    public ArrayList<String> getAllStudentsName() {
        if (head == null) return null; // Lista vacía

        ArrayList<String> names = new ArrayList<>();
        StudentNode current = head;
        while (current != null) {
            names.add(current.studentTree.getName());
            current = current.next;
        }
        return names;
    }
}
