package GIS.JAVA;

import java.util.ArrayList;

public class MapPlot {
	protected static class Location {
		String category;
		int year;
		
		public Location(String category, int year) {
			this.category = category;
			this.year = year;
		}
	}

	public ArrayList<Location> locations;
	public String name;
	private int id;

	public MapPlot(String name, int id) {
		this.name = name;
		this.id = id;
		locations = new ArrayList<Location>();
	}

	@Override
	public boolean equals(Object obj) {
		if(obj == null)
			return false;
		
		if(obj instanceof MapPlot) {
			MapPlot other = (MapPlot) obj;
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
