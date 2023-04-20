package GIS.JAVA;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import OCR.JAVA.Studio;

public class MapPlot {
	String name;
	int startYear, endYear;
	int id;
	boolean debutAtPrivate;
	String debutRegion;
	//Use a String formatted as "YEAR-REGION" as the key to get the time of appearance of this person in this region in the year
	HashMap<String, Integer> locations;
	
	public MapPlot(int id, String name, int startYear, boolean debutAtPrivate, String debutRegion) {
		this.id = id;
		this.name = name;
		this.startYear = startYear;
		this.endYear = -1;
		this.locations = new HashMap<String, Integer>();
		this.debutAtPrivate = debutAtPrivate;
		this.debutRegion = debutRegion;
	}

	@Override
	public String toString() {
		String str = id + "," + name + "," + startYear + "," + endYear + "," + debutAtPrivate + "," + debutRegion;
		for(int year=1949; year<1967; year++) {
			str = str.concat(",");
			boolean firstOfNode = true;
			for (String yearRegion : locations.keySet()) {
				if(yearRegion.startsWith(year + "-")) {
					if(!firstOfNode) {
						str += " & ";
					}
					firstOfNode = false;
					str += yearRegion.substring(yearRegion.indexOf("-") + 1, yearRegion.length()) + " [" + locations.get(yearRegion) + "]";
				}
			}
		}
		return str;
	}

	//Call this after all locations have been added to find the last appearance of this person
	public void setEndYearAfterLocated() {
		int maxYear = -1;
		for(String key : locations.keySet()) {
			//if(!key.contains("-")) System.err.println("Key " + key + " does not contain -");
			int year = Integer.parseInt(key.split("-")[0]);
			if(year > maxYear) {
				maxYear = year;
			}
		}
		this.endYear = maxYear;
	}

	public void addAppearance(String studio, int year, int count) throws IOException {
		String cat = Studio.getStudioCategory(studio);
		addLocation(cat, year, count);
	}

	private void addLocation(String location, int year, int count) {
		String key = year + "-" + location;
		if(locations.containsKey(key)) {
			locations.put(key, locations.get(key) + count);
		} else {
			locations.put(key, count);
		}
	}

	@Deprecated
	protected static class Location {
		String category;
		int year;
		int count;
		
		public Location(String category, int year) {
			this.category = category;
			this.year = year;
			this.count = 0;
		}
	}

	@Deprecated
	protected static class Person {
		public ArrayList<Location> locations;
		public String name;
		private int id;

		public Person(String name, int id) {
			this.name = name;
			this.id = id;
			locations = new ArrayList<Location>();
		}

		@Override
		public boolean equals(Object obj) {
			if(obj == null)
				return false;
			
			if(obj instanceof Person) {
				Person other = (Person) obj;
				return other.id == this.id;
			} else {
				return false;
			}
		}

		@Override
		public int hashCode() {
			return id;
		}
	}
		
}
