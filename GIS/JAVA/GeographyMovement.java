package GIS.JAVA;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import OCR.JAVA.Film;

public class GeographyMovement {
	private static ArrayList<Film> films;
	private static final String ALL_NODES_LIST = "Network/csv/nodes/nodes-all.csv";

	public static void main(String[] args) {
		try {
			//writeMetadataJSON();
			initAllNodesAsMapPlots();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unused")
	private static void writeMetadataJSON() throws IOException {
		films = Film.initAllFilms();
		ObjectMapper mapper = new ObjectMapper();
		String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(films);
		BufferedWriter writer = new BufferedWriter(new FileWriter("metadata-all.json"));
		writer.write(json);
		writer.close();
	}

	private static void initAllNodesAsMapPlots() throws IOException {
		ArrayList<MapPlot> mapPlots = new ArrayList<MapPlot>();
		File nodeFile = new File(ALL_NODES_LIST);
		BufferedReader reader = new BufferedReader(new FileReader(nodeFile));
		String line = reader.readLine();
		while((line = reader.readLine()) != null) {
			String[] lineSplit = line.split(",");
			String name = lineSplit[0];
			int id = Integer.parseInt(lineSplit[1]);
			mapPlots.add(new MapPlot(name, id));
		}
		reader.close();
	}
	
}