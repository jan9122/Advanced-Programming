import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.NoSuchElementException;

public class Torns <E>{

    private class NodeTorn{
        public E moviment;
        public NodeTorn seguent;
        public NodeTorn(E moviment, NodeTorn seguent){
            this.moviment = moviment;
            this.seguent = seguent;
        }
    }

    private NodeTorn llistatTorns; //seqüència amb capcelera

    // Constructor que inicialitza la llista de torns
    public Torns() {
        LocalDateTime ara = LocalDateTime.now();
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
        String timestamp = ara.format(format);
        String adreca = "JanRubio_RogerRoda/Files/torns_" + timestamp + ".txt";

        this.llistatTorns =  new NodeTorn((E) adreca, null);
    }

    // Constructor que carrega la llista de torns des d'un fitxer
    public Torns(String nomFitxer) throws IOException {
        this();
        carregarDesDeFitxer(nomFitxer);
        if(llistatTorns == null) throw new IOException("Llistat buit");
    }

    // Mètode per afegir un torn al final de la llista
    public void afegirTorn(E torn) {
        NodeTorn actual = llistatTorns;
        while (actual.seguent != null) {
            actual = actual.seguent;
        }
        actual.seguent = new NodeTorn(torn, null);

    }

    // Mètode per agafar el primer torn a la llista i s'elimina
    public E agafarPrimerTorn() throws NoSuchElementException {
        if (llistatTorns.seguent == null){
            throw new NoSuchElementException("La llista de torns està buida");
        }
        E primerTorn = llistatTorns.seguent.moviment;
        llistatTorns.seguent = llistatTorns.seguent.seguent;
        return primerTorn;
    }

    // Mètode per guardar la llista de torns a un fitxer
    public void guardarAFitxer(String nomFitxer) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(nomFitxer));
        NodeTorn actual = llistatTorns.seguent;
        while (actual != null){
            writer.write(actual.moviment.toString());
            writer.newLine();
            actual = actual.seguent;
        }
        writer.close();
    }

    // Mètode per carregar la llista de torns des d'un fitxer
    private void carregarDesDeFitxer(String nomFitxer) throws IOException { 
        String linia;
        llistatTorns.moviment = (E) nomFitxer;
        BufferedReader reader = new BufferedReader(new FileReader(nomFitxer));
        while ((linia = reader.readLine()) != null) {
            afegirTorn((E) linia); //això només funcionarà amb E=String.
        }
        reader.close();
    }

}