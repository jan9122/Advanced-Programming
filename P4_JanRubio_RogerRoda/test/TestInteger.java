
import EstructuraArbre.AcbEnll;
import EstructuraArbre.ArbreException;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Queue;

import static org.junit.jupiter.api.Assertions.*;


@TestMethodOrder(MethodOrderer.MethodName.class)
class TestInteger {
    private static AcbEnll<Integer> arbre;
    private static ArrayList<Integer> unaLista = new ArrayList<Integer> (Arrays.asList(6,8,7,9,4,5,1));

    @Test
    void testArbre0Null() {
        arbre = new AcbEnll<>();
        assertEquals(0,arbre.cardinalitat(), "Cardinalitat inicial és 0");
        assertThrows(ArbreException.class, () -> {
            arbre.arrel();
        });
    }

    @Test
    void testArbre1() {
        arbre = new AcbEnll<>();
        for (int i = 0; i < unaLista.size(); i++) {
            try {
                arbre.inserir(unaLista.get(i));
            } catch (ArbreException e) {
                fail(e.getMessage());
            }
            assertEquals(i+1,arbre.cardinalitat(), "S'ha afegit l'element a l'arbre");
        }
        assertEquals(unaLista.size(), arbre.cardinalitat(), "Tots els elements estan a l'arbre");
    }

    @Test
    void testArbre2QueueAscendent() {
        ArrayList<Integer> llistaOrdenada = new ArrayList<>(unaLista);
        Collections.sort(llistaOrdenada);

        Queue<Integer> llistaObtinguda = arbre.getAscendentList();

        assertEquals(llistaOrdenada.size(), llistaObtinguda.size(), "Les llistes tenen la mateixa mida");

        assertEquals(llistaOrdenada, llistaObtinguda, "Les llistes no són iguals!");

/*
        // comprovem la queue
        assertTrue(arbre.finalRecorregut());
        arbre.iniRecorregut(true); //ascendent
        assertFalse(arbre.finalRecorregut());

        // comprovem els elements de la queue
        for (int i = 0; i < ascendent.length; i++) {
            try {
                assertEquals(0,arbre.segRecorregut().compareTo(ascendent[i]));
            } catch (ArbreException e) {
                fail(e.getMessage());
            }
        }
        assertTrue(arbre.finalRecorregut());*/
    }

    @Test
    void testArbre2QueueDescendent() {
        ArrayList<Integer> llistaOrdenada = new ArrayList<>(unaLista);
        Collections.sort(llistaOrdenada, Collections.reverseOrder());

        Queue<Integer> llistaObtinguda = arbre.getDescendentList();

        assertEquals(llistaOrdenada.size(), llistaObtinguda.size(), "Les llistes tenen la mateixa mida");

        assertEquals(llistaOrdenada, llistaObtinguda, "Les llistes no són iguals!");
    }

    @Test
    void testArbre3inserirDuplicat() {
        assertThrows(ArbreException.class, () -> {
            arbre.inserir(7);
        });
    }

    @Test
    void testArbre4eliminarInexistent() {
        assertThrows(ArbreException.class, () -> {
            arbre.esborrar(20);
        });
    }



    @SuppressWarnings("unchecked")
    @Test
    void testArbre6DretClonat() {
        int cardinalitat = arbre.cardinalitat();
        AcbEnll<Integer> arbreDretClonat = null;
        try {
            arbreDretClonat = (AcbEnll<Integer>) arbre.fillDret();
        } catch (CloneNotSupportedException e) {
            fail(e.getMessage());
        }
        assertTrue(cardinalitat > arbreDretClonat.cardinalitat(), "Cardinalitat dret és inferior");
        //comptes quants números hi ha més gran que l'arrel
        int cardinalitatDreta = 0;
        for( Integer item : unaLista)
            if( item > unaLista.getFirst()) cardinalitatDreta++;
        assertEquals(cardinalitatDreta, arbreDretClonat.cardinalitat(), "Cardinalitat dret és correcte");

        //afegim un element a l'arbre original:
        try {
            arbre.inserir(10);
            cardinalitat++;
            //comprovem que només augmenta l'arbre original
            assertEquals(cardinalitat, arbre.cardinalitat(), "Cardinalitat de l'arbre original augmenta");
            assertEquals(cardinalitatDreta, arbreDretClonat.cardinalitat(), "No afegim cap element");
        } catch (ArbreException e) {
            fail(e.getMessage());
        }


        //afegim un element a l'arbre dret clonat:
        try {
            arbreDretClonat.inserir(11);
            cardinalitatDreta++;
            //comprovem que ara augmenta l'arbre dret clonat.
            assertEquals(cardinalitat, arbre.cardinalitat(), "No afegim cap element");
            assertEquals(cardinalitatDreta, arbreDretClonat.cardinalitat(), "Cardinalitat de l'arbre clonat augmenta");

        } catch (ArbreException e) {
            fail(e.getMessage());
        }


        // esborrem un element a l'arbre original:
        try {
            arbre.esborrar(7);
            cardinalitat--;
            //comprovem que només disminueix l'arbre original
            assertEquals(cardinalitat, arbre.cardinalitat(), "Eliminem un element a l'arbre original");
            assertEquals(cardinalitatDreta, arbreDretClonat.cardinalitat(), "No eliminem");
        } catch (ArbreException e) {
            fail(e.getMessage());
        }


        // esborrem un element a l'arbre dret clonat:
        try {
            arbreDretClonat.esborrar(7);
            cardinalitatDreta--;
            //comprovem que només disminueix l'arbre original
            assertEquals(cardinalitat, arbre.cardinalitat(),"No eliminem");
            assertEquals(cardinalitatDreta, arbreDretClonat.cardinalitat(), "Eliminem un element a l'arbre clonat");
        } catch (ArbreException e) {
            fail(e.getMessage());
        }
    }


}
