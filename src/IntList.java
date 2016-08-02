

/* Esta classe modela uma lista de inteiros encadeada */
public class IntList {

	int val;
	IntList next;

	IntList() {}

	IntList(int p) { this(p, null); }

	IntList(int p, IntList s) { val = p; next = s; }

	static IntList add(int p, IntList s) { return new IntList(p, s); }

	public boolean equals(IntList l) {
		if (val != l.val) return false;
		if (next == null) return l.next == null;
						  return next.equals(l.next);
	}

	public String toString() {
		String s = " " + val;
		return (next == null) ? (s + ".") : (s + next);
	}

	public String show(String name) { return name + this; }

}
