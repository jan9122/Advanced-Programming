import java.io.*;
import java.util.Scanner;

public class BinaryTree {
    private NodeA root; // Raíz del árbol

    // Clase interna NodeA
    private class NodeA {
        Person info;      // Información de la persona
        NodeA left, right; // Referencias al nodo izquierdo y derecho

        public NodeA(Person info) {
            this.info = info;
            this.left = null;
            this.right = null;
        }
    }

    // Constructor vacío
    public BinaryTree() {
        this.root = null;
    }

    // Constructor que carga un árbol desde un archivo en formato preorden
    public BinaryTree(String filename) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            this.root = preorderLoad(br);
        }
    }

    private NodeA preorderLoad(BufferedReader br) throws IOException {
        try {
            String line = br.readLine();

            if (line == null) {//A la cua de l'arxiu queda una linea vuida
                return null;
            }

            String[] parts = line.split(";");
            int parentCounter = 2;
            for (String part : parts) {
                if (part.equals(" ")) parentCounter--;
            }

            NodeA node = null;
            if (parts[0].isEmpty()){
                node = new NodeA (null);
            } else {
                node = new NodeA (new Person(parts[0]));
            }

            //primer càs:
            if (root == null) root = node;
            //Treballar segons nombre de pares
            switch(parentCounter){
                case 2://Cas dos pares
                    node.left = preorderLoad(br);
                    node.right = preorderLoad(br);
                    break;
                case 1://Cas un pare
                    node.left = preorderLoad(br);
                    break;
                case 0://Cas no pares (para)
                    break;
            }
            return node;

        } catch (IOException e){
            return null; //final
        }

        /* ATTEMPT#2
        String line = br.readLine();

        NodeA node = null;

        if (line == null)
            return new NodeA(null);

        try {
            String[] parts = line.split(";");//parte la string para ver cuantos padres tiene

            if (!parts[0].isEmpty()) {//mira que no esté vacía por cuando hay uno muerto
                switch (parts.length) {
                    case 3:
                        node = new NodeA(new Person(parts[0]));
                        node.left = preorderLoad(br);
                        node.right = preorderLoad(br);
                        break;
                    case 2:
                        node = new NodeA(new Person(parts[0]));
                        node.left = preorderLoad(br);
                        break;
                    case 1:
                        node = new NodeA(new Person(parts[0]));
                        break;
                    default:
                        return null;
                }
            }else {
                node = new NodeA(null);
                switch (parts.length) {
                    case 2:
                        node.left = preorderLoad(br);
                        node.right = preorderLoad(br);
                        break;
                    case 1:
                        node.left = preorderLoad(br);
                        break;
                }
            }
            return node;
        } catch (IllegalArgumentException e) {
            System.err.println("Error al procesar la línea: " + line + ". " + e.getMessage());
            return null; // Ignorar esta línea y continuar
        }

         */
        //------------------------------------------------------------------------------------//
        /* ATTEMPT#1
        if (line == null || line.trim().equals(";")) {
            return null; // Nodo vacío representado por ";"
        }

        try {
            Person person = new Person(line.trim()); // Crear persona desde el texto de la línea
            NodeA node = new NodeA(person);
            node.left = preorderLoad(br);  // Construir subárbol izquierdo
            node.right = preorderLoad(br); // Construir subárbol derecho
            return node;
        } catch (IllegalArgumentException e) {
            System.err.println("Error al procesar la línea: " + line + ". " + e.getMessage());
            return null; // Ignorar esta línea y continuar
        }
        */
    }

    // Devuelve el nombre de la persona en la raíz del árbol
    public String getName() {
        return root != null ? root.info.getName() : null;
    }

    // Añade un nodo en la posición especificada por "level"
    public void addNode(Person unaPersona, String level) {
        root = addNodeRecursive(root, unaPersona, level, 0);
    }

    private NodeA addNodeRecursive(NodeA node, Person unaPersona, String level, int index) {
        if (index == level.length()) {//basecase
            if (node == null) {
                return new NodeA(unaPersona);

            } else {//mira si no és null i demana per si reeplaçar o no
                Scanner scanner = new Scanner(System.in);
                System.out.println("Estàs sobreescrivint una Persona existent: "
                        + node.info.getName() + ", vols continuar?[Y/.]");

                String answer = "" + scanner.nextLine().charAt(0);

                if (answer.equalsIgnoreCase("Y")) {
                    return new NodeA(unaPersona);
                } else {
                    System.out.println("Cancel·lant...");
                    return node;
                }
            }
        }
        if (node == null) node = new NodeA(null);
        // ↑ Crea una persona en cas que es vulgui posar un pare abans que el seu fill
        if (level.charAt(index) == 'L') {
            node.left = addNodeRecursive(node.left, unaPersona, level, index + 1);
        } else if (level.charAt(index) == 'R') {
            node.right = addNodeRecursive(node.right, unaPersona, level, index + 1);
        }
        /*
        if (node == null) {
            return new NodeA(unaPersona);
        }

        if (index < level.length()) {
            if (level.charAt(index) == 'L') {
                node.left = addNodeRecursive(node.left, unaPersona, level, index + 1);
            } else if (level.charAt(index) == 'R') {
                node.right = addNodeRecursive(node.right, unaPersona, level, index + 1);
            }
        }*/
        return node;
    }

    // Muestra el árbol binario estructurado
    public void displayTree() {
        displayTreeRecursive(root, 0);
    }

    private void displayTreeRecursive(NodeA node, int level) {
        if (node == null) return;

        if (node.info == null) {
            // Imprime "*dead" en el nivel actual si el nodo está vacío
            for (int i = 0; i < level; i++) {
                System.out.print("\t"); // Indentación según el nivel
            }
            System.out.println("*dead");
        }else {
            // Imprime la información del nodo actual
            for (int i = 0; i < level; i++) {
                System.out.print("\t"); // Indentación según el nivel
            }
            System.out.println(node.info.getName());
        }
        // Llamadas recursivas para los hijos izquierdo y derecho
        displayTreeRecursive(node.left, level + 1);
        displayTreeRecursive(node.right, level + 1);
    }


    // Guarda el árbol en preorden en un archivo
    public void preorderSave() throws IOException {
        if (root == null) throw new IllegalStateException("Árbol vacío");

        // Define la carpeta donde se guardarán los archivos
        String folderPath = "src/Files";

        // Especifica el archivo con la ruta completa
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(folderPath + "/" + root.info.getName() + ".txt"))) {
            preorderSaveRecursive(root, bw);
        }
    }


    private void preorderSaveRecursive(NodeA node, BufferedWriter bw) throws IOException {
        if (node == null) return;

        if (node.info != null){
            bw.write(node.info.toString());
        }
        if (node.left == null)
            bw.write("; ");
        if (node.right == null)
            bw.write("; ");

        bw.write("\n");

        preorderSaveRecursive(node.left, bw);
        preorderSaveRecursive(node.right, bw);

        /*
        if (node.left != null){

        }
        if (node.right != null) {

        }
        */



        /*if (node == null) return;
        if (node.info == null) {
            bw.write(";\n"); // Nodo vacío
        } else {
            bw.write(node.info.toString() + "\n");
        }
        preorderSaveRecursive(node.left, bw);
        preorderSaveRecursive(node.right, bw);*/
    }

    // Elimina un miembro de la familia excepto la raíz
    public void removePerson(String name) {
        if (root != null && !root.info.getName().equalsIgnoreCase(name)) {
            root = removePersonRecursive(root, name);
        }
    }

    private NodeA removePersonRecursive(NodeA node, String name) {
        if (node == null) return null;

        if (node.info.getName().equalsIgnoreCase(name)) {
            return null; // Eliminar nodo si coincide el nombre
        }

        node.left = removePersonRecursive(node.left, name);
        node.right = removePersonRecursive(node.right, name);
        return node;
    }

    // Comprueba si la persona en la raíz proviene de un lugar específico
    public boolean isFrom(String place) {
        return root != null && root.info.getPlaceOfOrigin().equalsIgnoreCase(place);
    }

    // Comprueba si algún miembro del árbol proviene de un lugar específico
    public boolean isDescentFrom(String place) {
        return isDescentFromRecursive(root, place);
    }

    private boolean isDescentFromRecursive(NodeA node, String place) {
        if (node == null) return false;
        if (node.info != null && node.info.getPlaceOfOrigin().equalsIgnoreCase(place)) return true;
        
        return isDescentFromRecursive(node.left, place) || isDescentFromRecursive(node.right, place);
    }

    // Retorna el número de progenitores (0, 1 o 2)
    public int howManyParents() {
        int count = 0;
        if (root != null) {
            if (root.left != null) count++;
            if (root.right != null) count++;
        }
        return count;
    }

    // Cuenta el número de abuelos en el árbol
    public int howManyGrandParents() {
        return countNodesAtDepth(root, 2);
    }

    private int countNodesAtDepth(NodeA node, int depth) {
        if (node == null) {
            return 0;
        }
        if (depth == 0) {
            return 1; // Contar el nodo actual
        }
        // Llamadas recursivas para los hijos, disminuyendo la profundidad
        return countNodesAtDepth(node.left, depth - 1) + countNodesAtDepth(node.right, depth - 1);
    }

    // Comprueba si ambos progenitores están casados
    public boolean marriedParents() {
        if (root == null || root.left == null || root.right == null) return false;

        return root.left.info.getMaritalStatus() == Person.MARRIED &&
                root.right.info.getMaritalStatus() == Person.MARRIED;
    }
}
