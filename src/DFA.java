/******************************************************************************
 *							Deterministic Finite Automata					  *
 ******************************************************************************/

/**
 * This class implements deterministic complete finite automata. The transition
 * function is represented by a double array <code>next[][]</code>. The set of
 * terminal states is given by a partition of the set of states (class
 * {@link Partition Partition}). A state <code>q</code> is terminal if
 * <code>terminal.blockName[p]=1</code> (the default value is <code>0</code>).
 */

public class DFA {
	int[][] next;		// função de transição
	int initial;		// estado inicial
	Partition terminal; // conjunto dos estados finai
	Alphabet alphabet;	// o alfabeto
	int nbStates;		// # de estados
	int nbLetters;		// # de símbolos
	// int[] info;		// for the LR analysis
	int sink;			// the sink (default -1)

	/* Cria um AFD com 'n' estados e um alfabeto 'a' */
	public DFA(int n, Alphabet a) {
		nbStates = n;
		alphabet = a;
		nbLetters = alphabet.size();
		next = new int[nbStates][nbLetters];
		terminal = new Partition(nbStates);
		// info = new int[n];
		sink = -1;
	}

	/**
	 * returns the state reached from state <code>p</code> after reading the
	 * word <code>w</code>.
	 * 
	 * @param p
	 *            starting state
	 * @param w
	 *            input word (w is not <code>null</code>
	 * @return state reached
	 */
	public int next(int p, String w) {
		return next(p, alphabet.toShort(w));
	}

	/* Transições com uma string 'w' em um AFD */
	int next(int p, short[] w) {
		for (int i : w) {
			p = next[p][i];
			if (p == -1) break;
		}
		for (int i = 0; i < w.length; i++) {
			p = next[p][w[i]];
			if (p == -1) break;
		}
		return p;
	}

	int index(int[] c) {
		int m = -1;
		for (int i = 0; i < c.length; i++)
			m = Math.max(m, c[i]);
		return 1 + m;
	}

	static DFA makeit(int choix) {
		Alphabet al = new Alphabet(2);
		if (choix == 1) {
			DFA a = new DFA(7, al); 
			int[] t = new int[] { 1, 1, 1, 1, 1, 0, 1 };
			a.next[0][0] = 0;
			a.next[0][1] = 0;
			a.next[1][0] = 2;
			a.next[1][1] = 5;
			a.next[2][0] = 3;
			a.next[2][1] = 0;
			a.next[3][0] = 3;
			a.next[3][1] = 4;
			a.next[4][0] = 3;
			a.next[4][1] = 0;
			a.next[5][0] = 6;
			a.next[5][1] = 0;
			a.next[6][0] = 0;
			a.next[6][1] = 5;
			// a.t[1]=a.t[2]=a.t[3]= a.t[4]= a.t[6]=1;
			a.terminal = new Partition(t);
			return a;
		} else {
			DFA a = new DFA(7, al);
			a.next[0][0] = 1;
			a.next[0][1] = 2;
			a.next[1][0] = 3;
			a.next[1][1] = 5;
			a.next[2][0] = 5;
			a.next[2][1] = 4;
			a.next[3][0] = 6;
			a.next[3][1] = 6;
			a.next[4][0] = 6;
			a.next[4][1] = 4;
			a.next[5][0] = 6;
			a.next[5][1] = 6;
			a.next[6][0] = 6;
			a.next[6][1] = 6;
			// a.t[1]=a.t[3]=a.t[5]= a.t[6]=1;
			return a;
		}
	}

	/* Descrição do AFD */
	public String toString() {
		String s  = "q = " + initial  + "\n";
			   s += "Q = " + nbStates + "\n";
			   s += "F = " + terminal.blockList[1] + "\n";
			   s += "\n";

		StringBuffer u = new StringBuffer(s);
		for (int i = 0; i < nbStates; i++) {
			u.append(i + ": [");
			for (int c = 0; c < nbLetters; c++) {
				u.append(next[i][c]);
				if (c != nbLetters - 1) u.append(", ");
			}
			u.append("]\n");
		}
		return u.toString();
	}

	public static void main(String[] args) throws Exception {
		DFA a;
		a = makeit(1);
//		 a.show();
		System.out.println(a);
	}

}