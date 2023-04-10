package Network.JAVA;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;

import OCR.JAVA.Film;

public class GetNodes {
	private static final String META = "metadata-staff_plot.csv", EXTRA = "metadata-extra.csv", 
			EDGES = "edges.csv", NODES = "nodes.csv";

	private static ArrayList<Film> films;

	public static void main(String[] args) {
		try {
			films = Film.initAllFilms();
			getAllNodesInYear(1949);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	private static void getAllNodesInYear(int year) throws IOException {
		ArrayList<Film> filmsInYear = new ArrayList<Film>();
		for (Film film : films) {
			if(film.year == year) {
				filmsInYear.add(film);
			}
		}

		for (Film film : filmsInYear) {
			String[] directors = film.getDirectorNameArray();
			String[] scriptwriters = film.getScriptwriterNameArray();
			String[] actors = film.getActingNameArray();
			String[] staff = film.getOtherStaffNameArray();
		}
	}
	
}