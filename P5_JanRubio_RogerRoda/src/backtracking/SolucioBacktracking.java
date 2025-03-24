package backtracking;
import estructura.Encreuades;
import estructura.PosicioInicial;

import java.util.Arrays;

public class SolucioBacktracking {

	private boolean used[]; // ← Used to check if a word is already used
	private char[][] solucio; // ← The current solution

	private char[][] millorSolucio; // ← The best solution so far for multiple solutions

	private final Encreuades repte;

	
	public SolucioBacktracking(Encreuades repte) {
		this.repte = repte;
		this.used = new boolean[repte.getItemsSize()];

		this.solucio = cloneBoard(repte.getPuzzle());

		this.millorSolucio = cloneBoard(solucio);

		// ↓ Sets all the words as unused
		for (int i = 0; i < repte.getItemsSize(); i++)
			this.used[i] = false;
	}

	public char[][] getMillorSolucio() {
		return millorSolucio;
	}

	public Runnable start(boolean optim) {

		if(!optim) {
			if (!this.backUnaSolucio(0))
				throw new RuntimeException("solució no trobada");
			guardarMillorSolucio();
		}else
			this.backMillorSolucio(0);
		return null;
	}

	/* esquema recursiu que troba una solució
	 * utilitzem una variable booleana (que retornem)
	 * per aturar el recorregut quan haguem trobat una solució
	 */
	private boolean backUnaSolucio(int indexUbicacio) {
		boolean trobada = false;
		// iterem sobre els possibles elements
		for(int indexItem = 0; indexItem < this.repte.getItemsSize() && !trobada; indexItem++) {
			//mirem si l'element es pot posar a la ubicació actual
			if(acceptable( indexUbicacio, indexItem)) {
				//posem l'element a la solució actual
				anotarASolucio(indexUbicacio, indexItem);

				if(esSolucio(indexUbicacio)) { // és solució si totes les ubicacions estan plenes
					return true;
				} else
					trobada = this.backUnaSolucio(indexUbicacio+1); //inserim la següent paraula
				if(!trobada)
					// esborrem la paraula actual, per després posar-la a una altra ubicació
					desanotarDeSolucio(indexUbicacio, indexItem);
			}
		}
		return trobada;
	}
	// ↓ Finds the best solution
	private void backMillorSolucio(int indexUbicacio) {

		// ↓ Saves the best solution if it is found
		if (esSolucio(indexUbicacio)) {  // ← Checks if a solution is found
			guardarMillorSolucio(); // ← Only saves the best solution
			return;
		}

		// ↓ Recursive mumbo jumbo
		for (int indexItem = 0; indexItem < repte.getItemsSize(); indexItem++) {
			if (acceptable(indexUbicacio, indexItem)) { // ← If a word can be placed in a location

				anotarASolucio(indexUbicacio, indexItem); // ← Puts the word in that location

				backMillorSolucio(indexUbicacio + 1); // ← Goes forth to the next location

				desanotarDeSolucio(indexUbicacio, indexItem); // ← If after the previous recursion it gets stuck,
																// it deletes the last word and, in the next iteration
																// of the loop, it tries with the next word
			}
		}
	}


	private boolean acceptable(int indexUbicacio, int indexItem) {

		PosicioInicial posicio = repte.getEspaisDisponibles().get(indexUbicacio);

		// ↓ Checks if the word is already used
		if (used[indexItem]) {
			System.out.println("No acceptable: " + Arrays.toString(repte.getItem(indexItem)) + " a " + posicio + " ja usat");
			return false;
		}

		// ↓ Checks if the item fits in the space
		if (posicio.getLength() != repte.getItem(indexItem).length) {
			System.out.println("No acceptable: " + Arrays.toString(repte.getItem(indexItem)) + " a " + posicio + " no encaixa");
			return false;
		}

		// ↓ Checks if the space is blank or if there is a letter that is not the same as the one in the item
		if (posicio.getDireccio() == 'H') {
			for (int i = 0; i < posicio.getLength(); i++) {
				char currentChar = solucio[posicio.getInitRow()][posicio.getInitCol() + i];
				if (currentChar != ' ' && currentChar != repte.getItem(indexItem)[i]) {
					System.out.println("No acceptable: " + Arrays.toString(repte.getItem(indexItem)) + " a " + posicio + " " + currentChar + " != " + repte.getItem(indexItem)[i]);
					return false;
				}
			}
		} else {
			for (int i = 0; i < posicio.getLength(); i++) {
				char currentChar = solucio[posicio.getInitRow() + i][posicio.getInitCol()];
				if (currentChar != ' ' && currentChar != repte.getItem(indexItem)[i]) {
					System.out.println("No acceptable: " + Arrays.toString(repte.getItem(indexItem)) + " a " + posicio + " " + currentChar + " != " + repte.getItem(indexItem)[i]);
					return false;
				}
			}
		}

		System.out.println("Acceptable: " + Arrays.toString(repte.getItem(indexItem)) + " a " + posicio);
		return true;
	}
	
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

		// ↓ Marks the word as used
		used[indexItem] = true;
	}

	// ↓ Deletes the word from the solution
	private void desanotarDeSolucio(int indexUbicacio, int indexItem) { // NO SOPORTA PUZZLES D'UNA SOLA PARAULA
		// ↓ Location of the word to delete
		PosicioInicial ubi = repte.getEspaisDisponibles().get(indexUbicacio);

		System.out.println("Deleted " + Arrays.toString(repte.getItem(indexItem)) + " at " + ubi);
		// ↓ Marks the word as unused
		used[indexItem] = false;

		// ↓ Horizontal
		if (repte.getEspaisDisponibles().get(indexUbicacio).getDireccio() == 'H') {

			for (int i = 0; i < repte.getItem(indexItem).length; i++) {

				try {
					if (emptyCheck(solucio[ubi.getInitRow() - 1][ubi.getInitCol() + i])) {
						try {
							if (emptyCheck(solucio[ubi.getInitRow() + 1][ubi.getInitCol() + i])) {

								solucio[ubi.getInitRow()][ubi.getInitCol() + i] = ' ';
							}
						} catch (ArrayIndexOutOfBoundsException ignored) {
							solucio[ubi.getInitRow()][ubi.getInitCol() + i] = ' ';
						}
					}
				} catch (ArrayIndexOutOfBoundsException ignored) {
					try {
						if (emptyCheck(solucio[ubi.getInitRow() + 1][ubi.getInitCol() + i])) {
							solucio[ubi.getInitRow()][ubi.getInitCol() + i] = ' ';
						}
					} catch (ArrayIndexOutOfBoundsException ignored2) {
						solucio[ubi.getInitRow()][ubi.getInitCol() + i] = ' ';
					}
				}
			}
		}
		// ↓ Vertical
		else {
			for (int i = 0; i < repte.getItem(indexItem).length; i++) {

				try {
					if (emptyCheck(solucio[ubi.getInitRow() + i][ubi.getInitCol() - 1])) {
						try {
							if (emptyCheck(solucio[ubi.getInitRow() + i][ubi.getInitCol() + 1])) {

								solucio[ubi.getInitRow() + i][ubi.getInitCol()] = ' ';
							}
						} catch (ArrayIndexOutOfBoundsException ignored) {
							solucio[ubi.getInitRow() + i][ubi.getInitCol()] = ' ';
						}
					}
				} catch (ArrayIndexOutOfBoundsException ignored) {
					try {
						if (emptyCheck(solucio[ubi.getInitRow() + i][ubi.getInitCol() + 1])) {
							solucio[ubi.getInitRow() + i][ubi.getInitCol()] = ' ';
						}
					} catch (ArrayIndexOutOfBoundsException ignored2) {
						solucio[ubi.getInitRow() + i][ubi.getInitCol()] = ' ';
					}
				}
			}
		}
	}

	// ↓ Aid for desanotarDeSolucio
		// Checks if the cell is empty
	private boolean emptyCheck(char cell) {
		return cell == ' ' || cell == '▪';
	}

	// ↓ Checks if there are no blanks in solucio
		// There may be a better way tho, ftm I'll keep it this way
	private boolean esSolucio(int index) { // ← Here I don't use the index value,
												// but I won't remove it because I don't know if it's allowed
		for (int i = 0; i < solucio.length; i++) {
			for (int j = 0; j < solucio[0].length; j++) {
				if (solucio[i][j] == ' ')
					return false;
			}
		}

		return true;
	}

	// ↓ returns the total value of solucio atm
	private int calcularFuncioObjectiu(char[][] matriu) {
		int value = 0; // ← where the values are added up and stored until return
		char c_value; // ← temporary storage for the char until added to value

		// ↓ addition process
		for (int i = 0; i < matriu.length; i++) {
			for (int j = 0; j < matriu[0].length; j++) {
				c_value = matriu[i][j];
				value += c_value;
			}
		}

		return value;
	}

	private void guardarMillorSolucio() {
		int valorActual = calcularFuncioObjectiu(solucio);
		int valorMillor = calcularFuncioObjectiu(millorSolucio);
		if (valorActual > valorMillor) {
			millorSolucio = cloneBoard(solucio);
		}

		System.out.println("Millor solució actualitzada");
	}
	
	public String toString() {
		return Arrays.deepToString(millorSolucio);
	}

	public char[][] cloneBoard(char[][] board) {
		char[][] clone = new char[board.length][board[0].length];
		for (int i = 0; i < board.length; i++) {
			clone[i] = board[i].clone();
		}
		return clone;
	}
}
