import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class PowerSet {

	public static <T> Set<Set<T>> generateAllSubsetsFrom(Set<T> originalSet) {
		Set<Set<T>> sets = new HashSet<Set<T>>();

		if (originalSet.isEmpty()) {
			sets.add(new HashSet<T>());
			System.out.println("entrou no if");
			return sets;
		}

		List<T> list = new ArrayList<T>(originalSet);
		T head = list.get(0);
		Set<T> rest = new HashSet<T>(list.subList(1, list.size()));
		for (Set<T> set : generateAllSubsetsFrom(rest)) {
			Set<T> newSet = new HashSet<T>();
			newSet.add(head);
			newSet.addAll(set);
			sets.add(set);
			sets.add(newSet);
		}
		return sets;
	}

	public static List<String> createSubsetUsingTree(String str) {
		// take set if you want unique results
		List<String> result = new ArrayList<String>();
		result.add("[]");

		// If str is not null, then process further otherwise return empty set.
		if (str != null && str.length() > 0) {
			// Iterate each element of a set
			for (int i = 0; i < str.length(); i++) {
				// Working on str.charAt(i);
				// Store the result of subset of str.charAt(i) in tempList.
				List<String> tempList = new ArrayList<String>();

				// Add str.charAt(i) in each item of result.
				Iterator<String> iter = result.iterator();
				while (iter.hasNext()) {
					String val = iter.next();

					// If val is [], it means str.charAt(i) is not included, So
					// include it in result.
					if (val.equals("[]")) {
						tempList.add("[" + str.charAt(i) + "]");
						// System.out.println(tempList);''
					} else {
						// For each item, there will be 2 subset, one including
						// it and one without including it.
						// If val is not [], it means it already contain some
						// subset without str.charAt(i), So just include it.
						tempList.add("[" + val.substring(1, val.length() - 1) + ", " + str.charAt(i) + "]");
					}
				}
				// Add all subsets present in tempList to final result.
				result.addAll(tempList);
			}
		}
		return result;
	}

}