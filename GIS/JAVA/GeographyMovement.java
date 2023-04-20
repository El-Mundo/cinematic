package GIS.JAVA;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import OCR.JAVA.Film;

public class GeographyMovement {
	private static ArrayList<Film> films;
	private static final String ALL_NODES_LIST = "Network/csv/nodes/nodes-all.csv";
	private static final String NODES_DIRECTORY = "Network/csv/nodes/";
	private static final String TAR = "GIS/source/map_plots.csv";

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
		HashMap<String, MapPlot> mapPlots = new HashMap<String, MapPlot>();
		File nodeFile = new File(ALL_NODES_LIST);
		int lineNum = getFileLineNumber(nodeFile);
		BufferedReader reader = new BufferedReader(new FileReader(nodeFile));
		String line = reader.readLine();
		int n = 0;
		while((line = reader.readLine()) != null) {
			System.out.println("Initing map plots: " + (n) + "/" + lineNum);
			n++;

			String[] lineSplit = line.split(",");
			String name = lineSplit[1].replaceAll("\"", "");
			int id = Integer.parseInt(lineSplit[0]);
			int debutYear = Integer.parseInt(lineSplit[6]);
			boolean fromPrivateStudio = lineSplit[8].replaceAll("\"", "").equalsIgnoreCase("True");
			mapPlots.put(name, new MapPlot(id, name, debutYear, fromPrivateStudio, lineSplit[7].replaceAll("\"", "")));
		}
		reader.close();
		
		for(int year = 1949; year < 1967; year++) {
			File yearFile = new File(NODES_DIRECTORY + "nodes-" + year + ".csv");
			lineNum = getFileLineNumber(yearFile) - 2;
			reader = new BufferedReader(new FileReader(yearFile));
			line = reader.readLine();
			int j = 0;
			while((line = reader.readLine()) != null) {
				System.out.println("Loading plot appearances in " + (year) + ": " + j + "/" + lineNum);
				j++;
				if(line.isBlank()) continue;

				String[] lineSplit = line.split(",");
				String name = lineSplit[1].replaceAll("\"", "");
				String[] affiliations = lineSplit[3].replaceAll("\"", "").split("&");
				for(int i = 0; i < affiliations.length; i++) {
					affiliations[i] = affiliations[i].trim();
					String aff = affiliations[i];
					String studio = aff.substring(0, aff.lastIndexOf("(")).trim();
					int count = Integer.parseInt(aff.substring(aff.lastIndexOf("(") + 1, aff.lastIndexOf(")")));
					mapPlots.get(name).addAppearance(studio, year, count);
				}
			}
			System.out.println("Year " + year + " done.");
			reader.close();
		}

		int k = 0;
		File target = new File(TAR);
		BufferedWriter writer = new BufferedWriter(new FileWriter(target, false));
		writer.write("Network ID,Name,Debut Year,Last Appearance,Debut from Private Studio,Debut Region");
		for(int year = 1949; year < 1967; year++) {
			writer.write("," + year);
		}
		writer.newLine();
		MapPlot[] plots = new MapPlot[mapPlots.size()];
		plots = mapPlots.values().toArray(plots);
		System.out.println("Writing results...");
		for (MapPlot mapPlot : plots) {
			mapPlot.setEndYearAfterLocated();
			writer.append(mapPlot.toString() + "\n");

			System.out.println("Writing to file: " + k + "/" + mapPlots.size());
			k++;
		}
		writer.close();

		System.out.println("Done.");
	}

	private static int getFileLineNumber(File file) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(file));
		int lines = 0;
		while (reader.readLine() != null) lines++;
		reader.close();
		return lines;
	}
	
}