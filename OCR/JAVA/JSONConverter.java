package OCR.JAVA;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JSONConverter {
	private static ArrayList<Film> films;
	
	public static void main(String[] args) throws IOException {
		convertTranslatedNames();
	}

	private static void writeMetadataJSON() throws IOException {
		films = Film.initAllFilms();
		ObjectMapper mapper = new ObjectMapper();
		String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(films);
		BufferedWriter writer = new BufferedWriter(new FileWriter("metadata-all.json"));
		writer.write(json);
		writer.close();
	}

	private static void convertTranslatedNames() throws IOException {
		
	}

}