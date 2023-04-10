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
	public HashMap<Studio, Integer> allAffiliatedCount; //This represents the number of films the person had made with each studio (in a specified period of time).
	public ArrayList<String> mainAffiliatedCategory; //This represents the categroy of the studios with which the person had made most films (in a specified period of time).
	//public NodeType type;

	public Node(String name) {
		this.name = name;
		this.allAffiliated = new ArrayList<Studio>();
		allAffiliatedCount = new HashMap<Studio, Integer>();
		this.mainAffiliatedCategory = new ArrayList<String>();
	}

	private void addAffiliationCount(Studio studio) {
		if (allAffiliatedCount.containsKey(studio)) {
			allAffiliatedCount.put(studio, allAffiliatedCount.get(studio) + 1);
		} else {
			allAffiliatedCount.put(studio, 1);
		}
	}

	public void addAffiliation(Studio studio) {
		allAffiliated.add(studio);
		addAffiliationCount(studio);
	}

	public void addAffiliation(Studio[] studio) {
		for (Studio s : studio) {
			allAffiliated.add(s);
			addAffiliationCount(s);
		}
	}

	public void getMainAffiliationStudio() {
		ArrayList<Studio> mostFrequent = mostFrequentStudio();
		if(mostFrequent.size() > 1) {
			System.out.println("Warning: " + name + " has more than one most frequent studio: ");
			for (Studio studio : allAffiliated) {
				System.out.println(studio.name);
			}
			System.out.println();
		}
	}

	public void getMainAffiatedCategory() {
		ArrayList<String> mostFrequent = mostFrequentCategory();
		if(mostFrequent.size() > 1) {
			System.out.println("Warning: " + name + " has more than one most frequent categories: ");
			for (Studio studio : allAffiliated) {
				System.out.println(studio.category);
			}
			System.out.println();
		}
		mainAffiliatedCategory = mostFrequent;
	}

	@Override
	public String toString() {
		String s = name + ",";
		for (int i=0; i<mainAffiliatedCategory.size(); i++) {
			s += mainAffiliatedCategory.get(i) + ((i < mainAffiliatedCategory.size() - 1) ? " & " : "");
		}
		s += ",";
		for (int i=0; i<allAffiliated.size(); i++) {
			s += allAffiliated.get(i).name + " (" + allAffiliatedCount.get(allAffiliated.get(i)) + ")" + ((i < allAffiliated.size() - 1) ? " & " : "");
		}
		return s;
	}

	private ArrayList<Studio> mostFrequentStudio() {
		HashMap<Studio, Integer> hs = new HashMap<Studio, Integer>();
		int freq = 0;
		Studio[] studios = allAffiliated.toArray(new Studio[allAffiliated.size()]);
 
		for (int i = 0; i < studios.length; i++) {
			int newFreq = 0;
			if (hs.containsKey(studios[i])) {
				newFreq = hs.get(studios[i]) + this.allAffiliatedCount.get(studios[i]);
				throw new RuntimeException("Unexpected studio duplication!");
			} else {
				newFreq = this.allAffiliatedCount.get(studios[i]);
			}
			hs.put(studios[i], newFreq);
			freq = Math.max(newFreq, freq);
		}
 
        Set<Map.Entry<Studio, Integer> > set = hs.entrySet();
        ArrayList<Studio> keys = new ArrayList<Studio>();
		for (Map.Entry<Studio, Integer> me : set) {
			if (me.getValue() == freq) {
				keys.add(me.getKey());
			}
		}

		return keys;
	}

	private ArrayList<String> mostFrequentCategory() {
		Studio[] studios = allAffiliated.toArray(new Studio[allAffiliated.size()]);
		String[] allCats = new String[studios.length];
		for (int i=0; i<studios.length; i++) {
			allCats[i] = studios[i].category;
		}

		HashMap<String, Integer> map = new HashMap<String, Integer>();
		int freq = 0;
 
		for (int i = 0; i < studios.length; i++) {
			int newFreq = 0;
			if (map.containsKey(studios[i].category)) {
				newFreq = map.get(studios[i].category) + this.allAffiliatedCount.get(studios[i]);
			} else {
				newFreq = this.allAffiliatedCount.get(studios[i]);
			}
			map.put(studios[i].category, newFreq);
			freq = Math.max(newFreq, freq);
		}
 
        Set<Map.Entry<String, Integer> > set = map.entrySet();
        ArrayList<String> keys = new ArrayList<String>();
		for (Map.Entry<String, Integer> me : set) {
			if (me.getValue() == freq) {
				keys.add(me.getKey());
			}
		}

		return keys;
	}

}
