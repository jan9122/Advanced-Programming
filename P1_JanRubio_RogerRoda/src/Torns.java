import java.io.*;
import java.util.ArrayList;

// Clase genérica Torns que puede manejar elementos de cualquier tipo E
public class Torns<E> {
    // ArrayList privado para almacenar la lista de turnos (o elementos)
    private ArrayList<E> llistatTorns;

    // Constructor que inicializa el ArrayList
    public Torns() {
        llistatTorns = new ArrayList<E>(); // Inicializa la lista de turnos
    }

    // Constructor que carga una lista de turnos desde un archivo
    public Torns(String nomFitxer) throws IOException {
        llistatTorns = new ArrayList<>(); // Inicializa la lista
        // Carga el contenido desde el archivo a la lista
        carregarDesDeFitxer(nomFitxer);
        // Si la lista está vacía después de cargar, lanza una excepción
        if (llistatTorns.isEmpty()) {
            throw new IOException("El listado de turnos está vacío."); // "The list of turns is empty."
        }
    }

    // Método para añadir un turno (elemento) a la lista
    public void afegirTorn(E t) {
        llistatTorns.add(t); // Añade el elemento a la lista
    }

    // Método para guardar la lista de turnos en un archivo
    public void guardarFitxer(String fileName) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            // Itera a través de la lista y escribe cada elemento en el archivo
            for (E torn : llistatTorns) {
                writer.write(torn.toString()); // Convierte el elemento a String y lo escribe en el archivo
                writer.newLine(); // Escribe una nueva línea después de cada elemento
            }
            // Imprime un mensaje de éxito después de guardar el contenido
            System.out.println("Contenido guardado exitosamente en " + fileName);
        } catch (IOException e) {
            // Imprime un mensaje de error si la escritura del archivo falla
            System.out.println("Ocurrió un error al guardar el archivo: " + e.getMessage());
        }
    }

    // Método privado para cargar la lista de turnos desde un archivo
    private void carregarDesDeFitxer(String fileName) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            // Lee cada línea del archivo y la añade a la lista
            while ((line = reader.readLine()) != null) {
                llistatTorns.add((E) line); // Convierte cada línea al tipo genérico E
            }
            // Imprime un mensaje de éxito después de cargar el contenido
            System.out.println("Contenido cargado exitosamente en " + fileName);
        } catch (FileNotFoundException e) {
            // Lanza una excepción si no se encuentra el archivo
            throw new IOException("No se pudo encontrar el fichero: " + fileName, e);
        } catch (IOException e) {
            // Lanza una excepción si la lectura del archivo falla
            throw new IOException("Ocurrió un error al cargar el fichero: " + fileName, e);
        }
    }

    // Método para recuperar y eliminar el primer turno (elemento) de la lista
    public E agafarPrimerTorn() throws NoSuchFieldException {
        // Si la lista está vacía, lanza una excepción
        if (llistatTorns.isEmpty()) {
            throw new NoSuchFieldException("No quedan más turnos."); // "No more turns remain."
        }
        // Elimina y devuelve el primer elemento de la lista
        return llistatTorns.remove(0); // CON CUIDADO PUEDE FALLAR FALTA HACER TEST
    }
}
