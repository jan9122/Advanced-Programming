import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Main {
    private static Students students = new Students();
    private static final String folderPath = "src/Files"; // Especifica el path a la carpeta de archivos

    public static void main(String[] args) {
        // Llenar la lista de estudiantes al inicio
        readAllStudents();


        Scanner scanner = new Scanner(System.in);
        boolean exit = false;

        while (!exit) {
            System.out.println("Menú Principal:");
            System.out.println("1. Mostrar llistat d'estudiants");
            System.out.println("2. Mostrar família d'un estudiant");
            System.out.println("3. Afegir un estudiant");
            System.out.println("4. Modificar un estudiant");
            System.out.println("5. Mostrar el informe");
            System.out.println("6. Guardar i Sortir");
            System.out.print("Tria una opció: ");

            int option = Integer.parseInt(scanner.nextLine());

            switch (option) {
                case 1:
                    displayAllStudentsNames();
                    break;
                case 2:
                    showStudentFamily(scanner);
                    break;
                case 3:
                    addNewStudent(scanner);
                    break;
                case 4:
                    modifyStudent(scanner);
                    break;
                case 5:
                    showReport(scanner);
                    break;
                case 6:
                    saveAllStudents();
                    exit = true;
                    break;
                default:
                    System.out.println("Opció no vàlida. Si us plau, intenta de nou.");
            }
        }

        System.out.println("Sortint del programa.");

    }

    private static void readAllStudents() {
        File folder = new File(folderPath);
        File[] files = folder.listFiles((dir, name) -> name.toLowerCase().endsWith(".txt"));
        if (files != null) {
            for (File file : files) {
                try {
                    // Cargar cada árbol binario desde el archivo
                    BinaryTree studentTree = new BinaryTree(file.getPath());
                    students.addStudent(studentTree); // Añadir el árbol binario a la lista de estudiantes
                    System.out.println("Alumne carregat des del fitxer: " + file.getName());
                    System.out.println("Arbre binari estructurat:");
                    studentTree.displayTree(); // Mostrar la estructura del árbol
                    System.out.println(); // Línea en blanco entre árboles
                } catch (IOException e) {
                    System.out.println("Error al llegir el fitxer: " + file.getName());
                }
            }
        } else {
            System.out.println("No s'han trobat fitxers a la carpeta especificada.");
        }
    }

    private static void saveAllStudents() {
        // Guardar cada árbol binario en su archivo correspondiente
        for (String studentName : students.getAllStudentsName()) {
            try {
                BinaryTree studentTree = students.getStudent(studentName);
                if (studentTree != null) {
                    studentTree.preorderSave(); // Guardar el árbol en preorden
                    System.out.println("Alumne guardat al fitxer: " + studentName);
                }
            } catch (IOException e) {
                System.out.println("Error al guardar el fitxer: " + studentName);
            }
        }
    }

    private static void displayAllStudentsNames() {
        if (students.getAllStudentsName() == null || students.getAllStudentsName().isEmpty()) {
            System.out.println("No hi ha estudiants per mostrar.");
        } else {
            System.out.println("Mostrem els noms dels estudiants:");
            for (String name : students.getAllStudentsName()) {
                System.out.println("\t" + name);
            }
        }
    }

    private static void showStudentFamily(Scanner scanner) {
        System.out.print("Indica el nom de l'estudiant: ");
        String studentName = scanner.nextLine();
        BinaryTree familyTree = students.getStudent(studentName);

        if (familyTree != null) {
            System.out.println("Família de " + studentName + ":");
            familyTree.displayTree(); // Mostrar la estructura del árbol
        } else {
            System.out.println("Estudiant no trobat.");
        }
    }

    private static void addNewStudent(Scanner scanner) {
        System.out.print("Indica el nom de l'estudiant: ");
        String name = scanner.nextLine();
        System.out.print("Indica el lloc d'origen: ");
        String placeOfOrigin = scanner.nextLine();
        System.out.print("Indica l'estat civil (0: Single, 1: Married, 2: Divorced, 3: Widowed): ");
        int maritalStatus = Integer.parseInt(scanner.nextLine());

        // Crear una nueva persona
        Person newPerson = new Person(name, placeOfOrigin, maritalStatus);
        BinaryTree familyTree = new BinaryTree(); // Crear un árbol vacío para la familia
        familyTree.addNode(newPerson, ""); // Añadir la persona como la raíz

        students.addStudent(familyTree); // Añadir el árbol a la lista de estudiantes
    }

    private static void modifyStudent(Scanner scanner) {
        System.out.print("Indica el nom de l'estudiant que vols modificar: ");
        String studentName = scanner.nextLine();

        BinaryTree familyTree = students.getStudent(studentName);
        if (familyTree != null) {
            System.out.println("Opcions: 1. Afegir membre, 2. Eliminar membre");
            int option = Integer.parseInt(scanner.nextLine());
            if (option == 1) {
                System.out.print("Indica el nom del nou membre: ");
                String newMemberName = scanner.nextLine();
                System.out.print("Indica el lloc d'origen: ");
                String placeOfOrigin = scanner.nextLine();
                System.out.print("Indica l'estat civil (0: Single, 1: Married, 2: Divorced, 3: Widowed): ");
                int marriedStatus = Integer.parseInt(scanner.nextLine());
                System.out.print("Indica la posició (L per esquerra, R per dreta): ");
                String level = scanner.nextLine();
                Person newMember = new Person(newMemberName, placeOfOrigin, marriedStatus); // Estado civil por defecto
                familyTree.addNode(newMember, level); // Añadir nuevo miembro al árbol
            } else if (option == 2) {
                System.out.print("Indica el nom del membre a eliminar: ");
                String memberName = scanner.nextLine();
                familyTree.removePerson(memberName); // Eliminar miembro
            } else {
                System.out.println("Opció no vàlida.");
            }
        } else {
            System.out.println("Estudiant no trobat.");
        }
    }

    private static void showReport(Scanner scanner) {
        System.out.print("Indica la ciutat de naixement a buscar: ");
        String birthCity = scanner.nextLine();
        System.out.print("Indica la ciutat de procedència a buscar: ");
        String originCity = scanner.nextLine();

        int totalStudents = 0;
        int totalFromBirthCity = 0;
        int totalDescentFromOriginCity = 0;
        int totalSingleParents = 0;
        int totalUnmarriedParents = 0;
        int totalWithGrandparents = 0;

        // Recorrer la lista de estudiantes y contar estadísticas
        for (String studentName : students.getAllStudentsName()) {
            BinaryTree familyTree = students.getStudent(studentName);
            if (familyTree != null) {
                totalStudents++; // Contamos todos los estudiantes

                // Contar estudiantes de la ciudad de nacimiento
                if (familyTree.isFrom(birthCity)) {
                    totalFromBirthCity++;
                }

                // Contar descendencia de la ciudad de origen
                if (familyTree.isDescentFrom(originCity)) {
                    totalDescentFromOriginCity++;
                }

                // Contar estudiantes con un único progenitor
                if (familyTree.howManyParents() == 1) {
                    totalSingleParents++;
                }

                // Contar estudiantes con progenitores no casados
                if (!familyTree.marriedParents()) {
                    totalUnmarriedParents++;
                }

                // Contar estudiantes con dos o más abuelos
                if (familyTree.howManyGrandParents() >= 2) {
                    totalWithGrandparents++;
                }
            }
        }

        // Mostrar informe
        System.out.println("Informe sobre els estudiants:");
        System.out.println("Nombre d'alumnes totals: " + totalStudents);
        System.out.println("Hi ha " + totalFromBirthCity + " alumnes de " + birthCity);
        System.out.println("Hi ha " + totalDescentFromOriginCity + " alumnes descendents de " + originCity);
        System.out.println("Hi ha " + totalSingleParents + " alumnes amb un únic progenitor.");
        System.out.println("Hi ha " + totalUnmarriedParents + " alumnes amb progenitors no casats.");
        System.out.println("Hi ha " + totalWithGrandparents + " alumnes amb dos o més avis o àvies.");
    }



}