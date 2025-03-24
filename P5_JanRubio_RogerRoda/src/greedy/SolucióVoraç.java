package greedy;

import estructura.Encreuades;
import estructura.PosicioInicial;

import java.util.Arrays;

public class SolucióVoraç {

    private char[][] solucio; // ← Stores the puzzle copied from repte
    private boolean[] used; // ← Tick list of all words to see if they have been used or nah

    private final Encreuades repte;

    public SolucióVoraç(Encreuades repte) {
        this.repte = repte;

        this.solucio = repte.getPuzzle().clone();

        this.used = new boolean[repte.getItemsSize()];

        // ↓ Sets all the words as unused
        for (int i = 0; i < repte.getItemsSize(); i++)
            this.used[i] = false;

    }

    // ↓ Ignition, does all and returns the "solution"
    public char[][] getSolucio() {

        solucio = greedy();
        return solucio;
    }

    // ↓ Returns the best it can do, no matter if it is not a full solution
    private char[][] greedy() {

        int theChosenOne; // ← index of the most valuable (and unused) word

        while(!esSolucio() && !allUsed()){ // ← Keeps rolling until finds a solution or empties its possibilities

            theChosenOne = theBest(); // ← Assigns one word as the most important to use
            used[theChosenOne] = true; // ← Marks the word so it's never chosen again

            // ↓ tries to input the word in every space available until it eventually fits in one (or not)
            for (int i = 0; i < repte.getEspaisDisponibles().size(); i++){
                if (acceptable(i, theChosenOne)){
                    anotarASolucio(i, theChosenOne);
                    break;
                }
            }

        }
        return solucio;
    }

    /**
     * A aquesta classe
     * podeu definir els mètodes privats que vulgueu
     **/

    // ↓ Checks if there are no blanks in solucio
        // There may be a better way tho, ftm I'll keep it this way
    private boolean esSolucio(/*int index*/) { // ← Copied from backtracking
                                                // I have now removed the useless variable tho
        for (int i = 0; i < solucio.length; i++) {
            for (int j = 0; j < solucio[0].length; j++) {
                if (solucio[i][j] == ' ')
                    return false;
            }
        }

        return true;
    }

    // ↓ Checks if there are unused words
    private boolean allUsed(){
        for (int i = 0; i < used.length; i++){
            if (!used[i]) return false;
        }

        return true;
    }

    // ↓ Returns the candidate that gives the biggest amount of points, and is unused
    private int theBest(){

        int index_best = -1; // ← Best word checked until now
        int index_contender = 0; // ← New word to be checked

        // ↓ Searches for a word that has not been used and that gives more points (than the first one available)
        for (int i = 0; i < repte.getItemsSize(); i++){
            if (!used[i]){
                index_contender = i;
                if (index_best == -1 || getValue(index_contender) > getValue(index_best)) {
                    index_best = index_contender;
                }
            }
        }
        return index_best;
    }

    // ↓ Returns the amount of points of a word
    private int getValue(int index_word){
        char[] word = repte.getItem(index_word);
        int value = 0;
        char letter;

        // ↓ Adds up the ASCII value (or whatever characters Java uses. I really don't know)
        for (char i : word){
            letter = i;
            value += letter;
        }

        return value;
    }

    // ↓ Checks if a word can be written down in a spot of the solution
    private boolean acceptable(int indexUbicacio, int indexItem) {

        PosicioInicial posicio = repte.getEspaisDisponibles().get(indexUbicacio);

        // This method no longer checks if the word has been already used

        // ↓ Checks if the item fits in the space
        if (posicio.getLength() != repte.getItem(indexItem).length) {
            //System.out.println("No acceptable: " + Arrays.toString(repte.getItem(indexItem)) + " a " + posicio + " no encaixa");
            return false;
        }

        // ↓ Checks if the space is blank or if there is a letter that is not the same as the one in the item
        if (posicio.getDireccio() == 'H') {
            for (int i = 0; i < posicio.getLength(); i++) {
                char currentChar = solucio[posicio.getInitRow()][posicio.getInitCol() + i];
                if (currentChar != ' ' && currentChar != repte.getItem(indexItem)[i]) {
                    //System.out.println("No acceptable: " + Arrays.toString(repte.getItem(indexItem)) + " a " + posicio + " " + currentChar + " != " + repte.getItem(indexItem)[i]);
                    return false;
                }
            }
        } else {
            for (int i = 0; i < posicio.getLength(); i++) {
                char currentChar = solucio[posicio.getInitRow() + i][posicio.getInitCol()];
                if (currentChar != ' ' && currentChar != repte.getItem(indexItem)[i]) {
                    //System.out.println("No acceptable: " + Arrays.toString(repte.getItem(indexItem)) + " a " + posicio + " " + currentChar + " != " + repte.getItem(indexItem)[i]);
                    return false;
                }
            }
        }

        return true;
    }

    // ↓ Writes down a word in the solution
    private void anotarASolucio(int indexUbicacio, int indexItem) {

        // ↓ Puts the word in the solution
        if (repte.getEspaisDisponibles().get(indexUbicacio).getDireccio() == 'H')
            for (int i = 0; i < repte.getItem(indexItem).length; i++)
                solucio[repte.getEspaisDisponibles().get(indexUbicacio).getInitRow()]
                        [repte.getEspaisDisponibles().get(indexUbicacio).getInitCol() + i] = repte.getItem(indexItem)[i];
        else
            for (int i = 0; i < repte.getItem(indexItem).length; i++)
                solucio[repte.getEspaisDisponibles().get(indexUbicacio).getInitRow() + i]
                        [repte.getEspaisDisponibles().get(indexUbicacio).getInitCol()] = repte.getItem(indexItem)[i];

        // This method does no longer mark words as used
    }
}
