
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

/**
 * This class implements nondeterministic finite automata. The states are
 * represented by integers and the transitions by sets of half-edges, i.e. of
 * pairs <code>(s, q)</code> of a word and a state. The half-edges are objects
 * of the class {@link HalfEdge HalfEdge}. A set of half-edges is represented as
 * a <code>Set</code> (actually a <A
 * href=" /usr/local/j2sdk1.4.2_03/docs/api/java/util/TreeSet.html">
 * TreeSet</A>). The set of initial and the set of terminal states are also
 * represented by a <code>Set</code>. The alphabet is given as an object of the
 * class {@link Alphabet Alpabet}.
 */

@SuppressWarnings("rawtypes")
public class NFA {
	Set[] next;
	Alphabet alphabet;
	Set<HalfEdge> states;
	Set initial;
	Set terminal;

	int nbStates;
	int nbLetters;
	static int Nmax = 100; // an upper bound for the nb of states

	/* Cria um AFN com n estados. */
	public NFA(int n) {
		nbStates = n;
		next = new TreeSet[Alphabet.english().size];
		states = new TreeSet<HalfEdge>();
		initial = new TreeSet<HalfEdge>();
		terminal = new TreeSet<HalfEdge>();

		for (int i = 0; i < n; i++) {
			next[i] = new TreeSet();
			states.add(new HalfEdge(i));
		}
	}

	/* Descrição do AFN */
	public String toString() {
		String s  = "Q = " + states   + "\n";
			   s += "q = " + initial  + "\n";
			   s += "F = " + terminal + "\n";
			   s += "\n";

		for (int i = 0; i < nbStates; i++) {
			s += i + ":" + next[i] + "\n";
		}

		return s;
	}

	/**
	 * Returns the set of states which can be reached from <code>p</code> by an
	 * epsilon path. Implements a depth-first search of the graph of epsilon
	 * transitions. Uses a boolean array <code>mark</code> for the exploration.
	 * 
	 * @param p
	 *            a state (i.e. a unary half-edge)
	 * @param mark
	 *            the array of marks
	 * @return the set of states obtained.
	 */
	@SuppressWarnings("unchecked")
	public Set<HalfEdge> closure(HalfEdge p, boolean[] mark) {
		Set<HalfEdge> res = new TreeSet<HalfEdge>();
		res.add(p);
		mark[p.end] = true;
		for (Iterator i = next[p.end].iterator(); i.hasNext();) {
			HalfEdge e = (HalfEdge) i.next();
			if (e.label1.length() == 0) {
				HalfEdge q = new HalfEdge(e.end);
				if (!mark[e.end])
					res.addAll(closure(q, mark));
				else
					res.add(q);
			}
		}
		return res;
	}

	/**
	 * Implements the closure of the set of states <code>s</code>.
	 */
	@SuppressWarnings("unchecked")
	public Set<HalfEdge> closure(Set<HalfEdge> s) {
		Set<HalfEdge> t = new TreeSet<HalfEdge>();
		for (Iterator<HalfEdge> i = s.iterator(); i.hasNext();) {
			boolean[] mark = new boolean[nbStates];
			t.addAll(closure(i.next(), mark));
		}
		return t;
	}

	/**
	 * Computes a set transition in a literal NFA. The complexity is
	 * <code>O(n log(n))</code> for an <code>NFA</code> with <code>n</code>
	 * states. Indeed, the set <code>s</code> has <code>O(n)</code> elements and
	 * each insertion costs time <code>log(n)</code> using a
	 * <code>TreeSet</code> to represent the sets <code>s</code> and
	 * <code>next(s, c)</code>.
	 * 
	 * @param s
	 *            the original set of states
	 * @param c
	 *            a letter
	 * @return the set of states reachable from s under input c.
	 */
	@SuppressWarnings("unchecked")
	public Set<HalfEdge> next(Set<HalfEdge> s, int c) {
		Set<HalfEdge> t = new TreeSet<HalfEdge>();
		for (Iterator<HalfEdge> i = s.iterator(); i.hasNext();) {
			HalfEdge e = i.next();
			for (Iterator j = next[e.end].iterator(); j.hasNext();) {
				HalfEdge f = (HalfEdge) j.next();
				if (f.label1.length() == 1
						&& f.label1.charAt(0) == alphabet.toChar(c)) {
					t.add(new HalfEdge(f.end));
				}
			}
		}
		return closure(t);
	}

	/**
	 * Returns the number of states of the result of the determinization
	 * algorithm without constructing the automaton.
	 * 
	 * @param t
	 *            a linked list of sets of states (implemented as TreeSet).
	 * @param p
	 *            the order of the starting set.
	 * @return the linked list of states of the determinized automaton.
	 */
	@SuppressWarnings("unchecked")
	public LinkedList<Set<HalfEdge>> count(LinkedList<Set<HalfEdge>> t, int p) {
		for (int c = 0; c < nbLetters; c++) {
			Set<HalfEdge> l = t.get(p);
			Set<HalfEdge> sc = next(l, c); /* time O(n log(n)) */
			int q = t.indexOf(sc); /* time O(n m) */
			if (q == -1) { /* sc is new */
				t.addLast(sc);
				int n = t.size() - 1;
				t = count(t, n);
			}
		}
		return t;
	}

	/**
	 * Implements the function <code>Explore(t, s, b)</code> of Section 1.3.3
	 * which returns the list of sets of half edges realizing the
	 * determinization of the NFA. The third argument is the resulting DFA. The
	 * exploration starts at the element <code>s</code> of <code>t</code> with
	 * order <code>p</code>.
	 * 
	 * @param t
	 *            a linked list of sets of states (implemented as TreeSet).
	 * @param p
	 *            the order of the starting set.
	 * @param b
	 *            the resulting DFA.
	 * @return the linked list of states of <code>b</code>.
	 */
	@SuppressWarnings("unchecked")
	public LinkedList<Set<HalfEdge>> explore(LinkedList<Set<HalfEdge>> t, int p, DFA b) {
		for (int c = 0; c < nbLetters; c++) {
			Set<HalfEdge> l = t.get(p);
			Set<HalfEdge> sc = next(l, c); /* time O(n log(n)) */
			int q = t.indexOf(sc); /* time O(n m) */
			if (q == -1) { /* sc is new */
				t.addLast(sc);
				int n = t.size() - 1;
				b.next[p][c] = n;
				t = explore(t, n, b);
			} else
				b.next[p][c] = q;
		}
		return t;
	}

	/**
	 * The same as <code>explore</code> but with an implementation of the set of
	 * states of the resulting <code>DFA</code> via a <code>HashMap</code>.
	 */
	@SuppressWarnings("unchecked")
	public int explore2(HashMap<Set<HalfEdge>, Integer> t, Set<HalfEdge> s, int nn, DFA b) {
		for (int c = 0; c < nbLetters; c++) {
			Set<HalfEdge> sc = next(s, c); /* time O(n log(n) */
			if (!t.containsKey(sc)) {
				Integer in = new Integer(nn);
				t.put(sc, in);
				b.next[t.get(s).intValue()][c] = nn;
				nn = explore2(t, sc, 1 + nn, b);
			} else
				b.next[t.get(s).intValue()][c] = t
						.get(sc).intValue();
		}
		return nn;
	}

	@SuppressWarnings("unchecked")
	public static NFA leString() {
		Scanner sc = new Scanner(System.in);
		String str = sc.nextLine();
		sc.close();

		NFA a = new NFA(str.length() + 1);
			//a.alphabet = new Alphabet(str.toCharArray());
			a.alphabet = Alphabet.english();

		int alphSize = a.alphabet.size();
		
		for (int i = 0; i < str.length(); i++) {
			if (a.alphabet.isIn(str.charAt(i))) {
				if (i == 0) a.initial.add(new HalfEdge(0));
				if (i == str.length() - 1) a.terminal.add(new HalfEdge(i + 1));
				a.next[i].add(new HalfEdge("" + str.charAt(i), i + 1));
			}
		}

		// Adiciona uma transição para o próprio estado com cada símbolo do
		// alfabeto. Válido somente para os estados inicial e final.
		for (int i = 0; i < alphSize; i++) {
			a.next[0].add(new HalfEdge("" + a.alphabet.toChar(i), 0));
			a.next[str.length()].add(new HalfEdge("" + a.alphabet.toChar(i),
													  str.length()));
		}

		return a;
	}

	/**
	 * Implements the determinization algorithm. The size of the
	 * <code>DFA</code> created is given by the static constant
	 * <code>Nmax</code>. Implements the function <code>NFAtoDFA</code> of
	 * Section 1.3.3. The set of states of the resulting <code>DFA</code> is
	 * implemented as a <code>LinkedList</code>. The complexity is
	 * <code>O(n  m^2)</code> on an <code>NFA</code> of size <code>n</code>
	 * resulting in a <code>DFA</code> of size <code>m</code>.. Indeed, each
	 * call to <code>explore</code> needs a search into the list of sets of
	 * states (which contains <code>O(m)</code> sets of size <code>O(n)</code>).
	 */

	@SuppressWarnings("unused")
	public DFA toDFA() {
		LinkedList t = new LinkedList(); /* table of sets of states */

		//System.out.println(PowerSet.generateAllSubsetsFrom(states));
		DFA b = new DFA(2^nbStates, this.alphabet);

		// calcula o e-closure do estado inicial do AFN (que é também estado
		// inicial do AFD) para gerar os estados de aceitação (finais)

		//System.out.println(closure(initial));
		//for (int i = 0; i < (2^nbStates); i++) {}

		Set I = closure(initial);
		t.add(I); /* add I to t */
		t = explore(t, 0, b);
		System.out.println(t);
		b.nbStates = t.size();
		b.nbLetters = nbLetters;
		b.alphabet = alphabet;
		b.initial = 0;
//		for (Iterator i = t.iterator(); i.hasNext();) {
//			Set p = (Set) i.next();
//			for (Iterator j = p.iterator(); j.hasNext();) {
//				HalfEdge e = (HalfEdge) j.next();
//				if (terminal.contains(e))
//					b.terminal.transfer(t.indexOf(p), 0, 1);
//			}
//		}
//		System.out.println(b);
		return b;
	}

	/**
	 * Implements the determinization algorithm. The size of the
	 * <code>DFA</code> created is first computed using the method
	 * <code>count</code>. Implements the function <code>NFAtoDFA</code> of
	 * Section 1.3.3. The set of states of the resulting <code>DFA</code> is
	 * implemented as a <code>LinkedList</code>. The complexity is
	 * <code>O(n  m^2)</code> on an <code>NFA</code> of size <code>n</code>
	 * resulting in a <code>DFA</code> of size <code>m</code>.. Indeed, each
	 * call to <code>explore</code> needs a search into the list of sets of
	 * states (which contains <code>O(m)</code> sets of size <code>O(n)</code>).
	 */
	@SuppressWarnings("unchecked")
	public DFA toDFA3() {
		LinkedList t = new LinkedList(); /* table of sets of states */
		Set I = closure(initial);
		t.add(I); /* add I to t */
		t = count(t, 0);
		int n = t.size();
		DFA b = new DFA(n, alphabet);
		t = new LinkedList(); /* table of sets of states */
		t.add(I); /* add I to t */
		t = explore(t, 0, b);
		b.nbStates = t.size();
		b.nbLetters = nbLetters;
		b.alphabet = alphabet;
		b.initial = 0;
		for (Iterator i = t.iterator(); i.hasNext();) {
			Set p = (Set) i.next();
			for (Iterator j = p.iterator(); j.hasNext();) {
				HalfEdge e = (HalfEdge) j.next();
				if (terminal.contains(e))
					b.terminal.transfer(t.indexOf(p), 0, 1);
			}
		}
		return b;
	}

	/**
	 * The same as <code>toDFA</code> but with an implementation of the set of
	 * states of the resulting <code>DFA</code> via a <code>HashMap</code>. The
	 * keys are the sets of half-edges (with the method <code>hashCode</code>
	 * overridden in the class <code>HalfEdge</code>) and the value is the name
	 * of the state. Assuming constant time performance for the functions
	 * <code>get</code> and<code>put</code>, the complexity is
	 * <code>O(m n log(n))</code> on an <code>NFA</code> of size <code>n</code>
	 * resulting in a <code>DFA</code> with <code>m</code> states.
	 */
	@SuppressWarnings("unchecked")
	public DFA toDFA2() {
		int nn = 0;
		DFA b = new DFA(Nmax, alphabet);
		HashMap t = new HashMap(); /* table of sets of states */
		Set I = closure(initial);
		t.put(I, new Integer(nn)); /* add I to t */
		nn = explore2(t, I, 1 + nn, b);
		b.nbStates = t.size();
		b.nbLetters = nbLetters;
		b.alphabet = alphabet;
		b.initial = 0;
		Set tv = t.keySet();
		for (Iterator i = tv.iterator(); i.hasNext();) {
			Set p = (Set) i.next();
			for (Iterator j = p.iterator(); j.hasNext();) {
				HalfEdge e = (HalfEdge) j.next();
				if (terminal.contains(e))
					b.terminal.transfer(((Integer) t.get(p)).intValue(), 0, 1);
			}
		}
		return b;
	}

	public static void main(String[] args) {
		System.out.println("Digite a string");
		NFA b = leString();
//		System.out.println(b);
		b.toDFA();
	}

}