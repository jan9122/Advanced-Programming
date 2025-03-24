public class Person {
    private String name;
    private String placeOfOrigin;
    private int maritalStatus;

    // Constantes para los posibles valores de maritalStatus
    public static final int SINGLE = 0;
    public static final int MARRIED = 1;
    public static final int DIVORCED = 2;
    public static final int WIDOWED = 3;

    // Constructor completo con validación de maritalStatus
    public Person(String name, String placeOfOrigin, int maritalStatus) {
        this.name = name;
        this.placeOfOrigin = placeOfOrigin;

        // Validación de maritalStatus
        if (maritalStatus < SINGLE || maritalStatus > WIDOWED) {
            throw new IllegalArgumentException("Estado civil no válido");
        }
        this.maritalStatus = maritalStatus;
    }

    // Constructor que recibe un String en el formato especificado y lo parsea
    public Person(String data) {
        String[] parts = data.split(", ");

        if (parts.length != 3) {
            throw new IllegalArgumentException("Formato de datos inválido: " + data);
        }

        // Extraer los valores desde el String formateado
        this.name = parts[0].split(": ")[1].trim(); // Limpia espacios en blanco
        this.placeOfOrigin = parts[1].split(": ")[1].trim(); // Limpia espacios en blanco

        // Limpia espacios en blanco y elimina caracteres no deseados
        String status = parts[2].split(": ")[1].trim().toLowerCase().replace(";", "").replace(" ", "");

        // Convertir el estado civil de texto a constante numérica
        switch (status) {
            case "single":
                this.maritalStatus = SINGLE;
                break;
            case "married":
                this.maritalStatus = MARRIED;
                break;
            case "divorced":
                this.maritalStatus = DIVORCED;
                break;
            case "widowed":
                this.maritalStatus = WIDOWED;
                break;
            default:
                throw new IllegalArgumentException("Estado civil no válido: " + status);
        }
    }


    // Getters para los atributos
    public String getName() {
        return name;
    }

    public String getPlaceOfOrigin() {
        return placeOfOrigin;
    }

    public int getMaritalStatus() {
        return maritalStatus;
    }

    // Método toString en el formato requerido
    @Override
    public String toString() {
        String status;
        switch (maritalStatus) {
            case SINGLE:
                status = "Single";
                break;
            case MARRIED:
                status = "Married";
                break;
            case DIVORCED:
                status = "Divorced";
                break;
            case WIDOWED:
                status = "Widowed";
                break;
            default:
                status = "Unknown";
        }
        return "Name: " + name + ", place of Origin: " + placeOfOrigin + ", marital status: " + status;
    }
}
