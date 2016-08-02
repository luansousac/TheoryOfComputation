import java.util.Arrays;
import java.util.Iterator;
import java.util.Set;

/* Esta classe modela um alfabeto. Porém, internamente, o alfabeto é sempre um
 conjunto de inteiros consecutivos começando em 0 */
public class Alphabet {

	static final short ENGLISH = 26;
	char minLetter = 'a';
	char maxLetter;
	public int size;
	char[] shortToChar;
	short[] charToShort = new short[256];

	public static Alphabet english() {
		char[] chars = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k',
				'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w',
				'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I',
				'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U',
				'V', 'W', 'X', 'Y', 'Z' };
		return new Alphabet(chars);
	}

	public Alphabet(char letter, int n) {
		minLetter = letter;
		maxLetter = (char) (letter + n - 1);
		size = n;
		shortToChar = new char[size];
		for (short i = 0; i < size; i++) {
			char c = (char) (minLetter + i);
			charToShort[c] = i;
			shortToChar[i] = c;
		}
	}

	/**
	 * Creates an alphabet from a boolean array <code>isLetter</code>
	 */
	public Alphabet(boolean[] isLetter) {
		int count = 0;
		for (int i = 0; i < isLetter.length; i++)
			if (isLetter[i])
				count++;
		char[] chars = new char[count];
		int current = 0;
		for (char i = 0; i < isLetter.length; i++)
			if (isLetter[i])
				chars[current++] = i;
		size = chars.length;
		shortToChar = new char[size];
		System.arraycopy(chars, 0, shortToChar, 0, size);
		Arrays.sort(shortToChar);
		for (short i = 0; i < shortToChar.length; i++)
			charToShort[shortToChar[i]] = i;
	}

	/**
	 * Creates an alphabet of size <code>n</code> beginning at character
	 * <code>a</code>.
	 */
	public Alphabet(int n) {
		this('a', n);
	}

	/**
	 * Creates an alphabet consisting in a set of characters.
	 */
	public Alphabet(Set<?> charSet) {
		size = charSet.size();
		shortToChar = new char[size];
		short n = 0;
		for (Iterator<?> i = charSet.iterator(); i.hasNext(); n++) {
			char c = ((Character) i.next()).charValue();
			charToShort[c] = n;
			shortToChar[n] = c;
		}
	}

	/* Cria um alfabeto a partir de um array de caracteres */
	public Alphabet(char[] chars) {
		size = chars.length;
		shortToChar = new char[size];
		System.arraycopy(chars, 0, shortToChar, 0, size);
		Arrays.sort(shortToChar);

		for (short i = 0; i < shortToChar.length; i++) {
			charToShort[shortToChar[i]] = i;
		}
	}

	/* Retorna o tamanho do alfabeto */
	int size() {
		return size;
	}

	/* Converte o inteiro 'i' para um 'caractere' usando o array shortToChar */
	char toChar(int i) {
		return shortToChar[i];
	}

	/* Converte o caractere 'c' para um 'short' usando o array charToShort */
	public short toShort(char c) {
		return charToShort[c];
	}

	/* Converte a string 'w' para 'short' usando o método toShort() */
	public short[] toShort(String w) {
		int n = w.length();
		short[] s = new short[n];
		for (int i = 0; i < n; i++)
			s[i] = toShort(w.charAt(i));
		return s;
	}

	/* Descobre se um determinado símbolo está no alfabeto */
	public boolean isIn(char c) {
		return toChar(toShort(c)) == c;
	}

	/* Descrição ordenada do alfabeto */
	public String toString() {
		String s = "";
		for (int i = 0; i < size; i++)
			s += toChar(i);
		return s;
	}

}
