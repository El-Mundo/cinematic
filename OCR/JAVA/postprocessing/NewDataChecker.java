package OCR.JAVA.postprocessing;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import OCR.JAVA.Film;

public class NewDataChecker {
	private static final String SRC = "OCR/JAVA/postprocessing/dadian_entries.txt"; // Source path

	public static void main(String[] args) throws NumberFormatException, IOException {
		//checkEntryCompletionAgainstDadian();
		countDadianCompletion();
	}

	private static void countDadianCompletion() throws IOException {
		File source = new File(SRC);
		ArrayList<String> entriesNotInDadian = new ArrayList<String>();
		ArrayList<String> missingEntries = new ArrayList<String>();
		ArrayList<Film> films = Film.initAllFilms();
		ArrayList<Film> filmsInYear = new ArrayList<Film>();
		BufferedReader br = new BufferedReader(new FileReader(source));
		String line = "";
		int year = 0;
		int count = 0, total = 0;
		int privateCount = 0;
		//Read all lines from the file
		while ((line = br.readLine()) != null) {
			if(line.isEmpty()) {
				year = Integer.parseInt(br.readLine());
				System.out.println("Year: " + (year-1) + " with entry count: " + count);
				count = 0;

				if(filmsInYear.size() > 0) {
					//Print all films that were not found
					for (Film film : filmsInYear) {
						entriesNotInDadian.add(film.title);
					}
				}

				filmsInYear.clear();
				for (Film film : films) {
					//Add films in the year to array
					if(film.year == year) {
						filmsInYear.add(film);
					}
				}
				//System.out.println("Films in year: " + filmsInYear.size() + " in year " + year + ".");
				//System.in.read();
			}else{
				//This is a film in Dadian
				total++;
				String title = line;
				boolean found = false;
				for (Film film : filmsInYear) {
					if(film.title.equals(title)) {
						//This entry can be found in both Dadian and the project data
						String[] cats = film.getCategory();
						for (String s : cats) {
							if(s.equals("Shanghai (private)")) {
								//This is a private film
								privateCount++;
								break;
							}
						}
						filmsInYear.remove(film);
						found = true;
						break;
					}
				}
				if(!found) {
					//This entry is only in Dadian but not in the project data
					missingEntries.add(title);
					if(title.equals("警惕") || title.equals("逃不了")) privateCount++;
				}
				count++;
			}
		}
		br.close();

		int thisProjPrivateCount = 0;
		for (Film film : films) {
			String[] cats = film.getCategory();
			for (String s : cats) {
				if(s.equals("Shanghai (private)")) {
					//This is a private film
					thisProjPrivateCount++;
					break;
				}
			}
		}

		int bianmuPrivateCount = 0;
		ArrayList<Film> bianmuFilms = Film.initAllFilmsInMainMetadata();
		for (Film film : bianmuFilms) {
			String[] cats = film.getCategory();
			for (String s : cats) {
				if(s.equals("Shanghai (private)")) {
					//This is a private film
					bianmuPrivateCount++;
					break;
				}
			}
		}

		//A film will be counted as "private" if one of its GeoCategories is "Shanghai (private)"
		System.out.println();
		System.out.println("Missing entries from Dadian: " + missingEntries.size());
		System.out.println("Entries not in Dadian: " + entriesNotInDadian.size());
		System.out.println("Total entries in Dadian: " + total);
		System.out.println("Private-studio entries in Dadian: " + privateCount);
		System.out.println("Total entries in this porject: " + films.size());
		System.out.println("Private-studio entries in this project: " + thisProjPrivateCount);
		System.out.println("Total entries in bianmu: " + bianmuFilms.size());
		System.out.println("Private-studio entries in bianmu: " + bianmuPrivateCount);
	}

	@SuppressWarnings("unused")
	private static void checkEntryCompletionAgainstDadian() throws IOException {
		File source = new File(SRC);
		ArrayList<String> entriesNotInDadian = new ArrayList<String>();
		ArrayList<String> missingEntries = new ArrayList<String>();
		ArrayList<Film> films = Film.initAllFilms();
		ArrayList<Film> filmsInYear = new ArrayList<Film>();
		ArrayList<String> duplicateInYear = new ArrayList<String>();
		BufferedReader br = new BufferedReader(new FileReader(source));
		String line = "";
		int year = 0;
		int count = 0;
		//Read all lines from the file
		while ((line = br.readLine()) != null) {
			if(line.isEmpty()) {
				year = Integer.parseInt(br.readLine());
				duplicateInYear.clear();
				System.out.println("Year: " + (year-1) + " with entry count: " + count);
				count = 0;

				if(filmsInYear.size() > 0) {
					//Print all films that were not found
					for (Film film : filmsInYear) {
						entriesNotInDadian.add(film.title);
					}
				}

				filmsInYear.clear();
				for (Film film : films) {
					//Add films in the year to array
					if(film.year == year) {
						filmsInYear.add(film);
					}
				}
				//System.out.println("Films in year: " + filmsInYear.size() + " in year " + year + ".");
				//System.in.read();
			}else{
				String title = line;
				if(!duplicateInYear.contains(title))duplicateInYear.add(title);
					else {System.out.println(title + " is a duplicate in year " + (year) + "."); System.in.read();}
				//Iterate through filmsInYear to check if this title appears in that year
				boolean found = false;
				for (Film film : filmsInYear) {
					if(film.title.equals(title)) {
						filmsInYear.remove(film);
						found = true;
						break;
					}
				}
				if(!found) {
					missingEntries.add(title);
					System.out.println(title + " not found in year " + year + ".");
					System.in.read();
				}
				count++;
			}
		}
		br.close();

		System.out.println("Missing entries: " + missingEntries.size());
	}


}