package Network.JAVA;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import OCR.JAVA.Studio;

public class Node {
	/*public enum NodeType {
		DIRECTOR, SCRIPTWRITER, ACTOR_OR_ACTRESS, OTHER_STAFF
	}*/

	public String name; //For a node representing a filmmaker/actor-or-actress, name is the ID to identify the person, as duplicate names are extremely rare.
	public ArrayList<Studio> allAffiliated; //This represents all studios with which the person had made films (in a specified period of time; may duplicate if this person collaborated with a studio for several times).
	public Studio mainAffiliatedProduction; //This represents the studio with which the person had made most films (in a specified period of time).
	//public NodeType type;

	public Node(String name) {
		this.name = name;
		this.allAffiliated = new ArrayList<Studio>();
	}

	public void addAffiliation(Studio studio) {
		allAffiliated.add(studio);
	}

	public void addAffiliation(Studio[] studio) {
		for (Studio s : studio) {
			allAffiliated.add(s);
		}
	}

	public void getMainAfiiliation() {
		ArrayList<Studio> mostFrequent = mostFrequentStudio(allAffiliated.toArray(new Studio[allAffiliated.size()]));
		if(mostFrequent.size() > 1) {
			System.out.println("Warning: " + name + " has more than one most frequent studio: ");
			for (Studio studio : allAffiliated) {
				System.out.println(studio.name);
			}
			System.out.println();
		}
	}

	@Override
	public String toString() {
		return name + "," + mainAffiliatedProduction;
	}

	private static ArrayList<Studio> mostFrequentStudio(Studio[] studios) {
		HashMap<Studio, Integer> hs = new HashMap<Studio, Integer>();
		int freq = 0;
 
		for (int i = 0; i < studios.length; i++) {
			int newFreq = 0;
			if (hs.containsKey(studios[i])) {
				newFreq = hs.get(studios[i]) + 1;
			} else {
				newFreq = 1;
			}
			hs.put(studios[i], newFreq);
			freq = Math.max(newFreq, freq);
		}
 
        Set<Map.Entry<Studio, Integer> > set = hs.entrySet();
        ArrayList<Studio> keys = new ArrayList<Studio>();
		for (Map.Entry<Studio, Integer> me : set) {
			if (me.getValue() == freq) {
				freq = me.getValue();
				keys.add(me.getKey());
			}
		}

		return keys;
	}

}
