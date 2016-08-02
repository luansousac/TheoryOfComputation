

/* Doubly linked lists of integers. The list is a reference on the cell
   containing the first element */

public class DListInt {
	int element;
	DListInt prec, next;

	/* Creates a cell containing p */
	public DListInt(int p) { element = p; }

	/* Inserts p in front */
	public DListInt(int p, DListInt s) {
		this(p);
		if (s != null) {
			next = s;
			prec = s.prec;
			s.prec = this;
		}
	}

	/* Inserts the cell head in front of the list. */
	public DListInt insert(DListInt head) {
		next = head;
		prec = null;
		if (next != null) next.prec = this;

		return this;
	}

	/* Removes the cell head in front of the list and returns it */
	public DListInt remove(DListInt head) {
		if (prec != null) // not the first element in the list
			prec.next = next;
		else // the first element in the list
			head = next;
		if (next != null) next.prec = prec;

		prec = next = null;
		return head;
	}

	/* Returns the list ended with a dot. */
	public String toString() {
		return "" + element + " " + ((next == null) ? "." : next.toString());
	}

}
