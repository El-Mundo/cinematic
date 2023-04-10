package OCR.JAVA;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/*
 * Use this finalised utility class to store information about a film.
 * A Film with reels as negative number is one with no recordance of such information.
 */
public class Film {
	public String key, title, translated, production, colour, special, director, scriptwriter, acting, staff, plot;
	public int year, reels;

	public static final String METADATA_PATH = "metadata.csv", EXTRA_METADATA_PATH = "metadata-extra.csv", STAFF_PLOT_DATA_PATH = "metadata-staff_plot.csv";

	public Film(String key, String title, int year, String translated, String production, String colour, int reels, String special, String director, String scriptwriter, String acting, String staff, String plot) {
		this.key = key;
		this.title = title;
		this.translated = translated;
		this.production = production;
		this.colour = colour;
		this.special = special;
		this.director = director;
		this.scriptwriter = scriptwriter;
		this.acting = acting;
		this.staff = staff;
		this.plot = plot;
		this.year = year;
		this.reels = reels;
	}

	public Film(String line, boolean isInMainMetadata) throws IOException {
		String[] values = line.split(",", -1);
		key = values[0];
		title = values[1];
		year = Integer.parseInt(values[2]);
		translated = values[3];
		production = values[4];
		colour = values[5];
		
		if(isInMainMetadata) {
			reels = Integer.parseInt(values[6]);
			special = values[7];
			loadStaffPlotInfoForMetadataFromKey(this.key);
		} else {
			reels = -1;
			special = values[6];
			director = values[7];
			scriptwriter = values[8];
			acting = values[9];
			staff = values[10];
			plot = values[11];
		}
	}

	private void loadStaffPlotInfoForMetadataFromKey(String key) throws IOException {
		File spFile = new File(STAFF_PLOT_DATA_PATH);
		BufferedReader reader = new BufferedReader(new FileReader(spFile));
		String line = null;
		while((line = reader.readLine()) != null) {
			String[] values = line.split(",");
			String spKey = values[0];
			if(spKey.equals(this.key)) {
				this.director = values[1];
				this.scriptwriter = values[2];
				this.acting = values[3];
				this.staff = values[4];
				this.plot = values[5];
				reader.close();
				return;
			}
		}
		reader.close();
		throw new IOException("Cannot find film with key: " + key);
	}

	public String getFilmType() throws IOException {
		File mFile = new File(METADATA_PATH);
		File eFile = new File(EXTRA_METADATA_PATH);
		BufferedReader mReader = new BufferedReader(new FileReader(mFile));
		BufferedReader eReader = new BufferedReader(new FileReader(eFile));
		String line = null;
		while((line = mReader.readLine()) != null) {
			String[] values = line.split(",");
			String mKey = values[0];
			if(mKey.equals(this.key)) {
				String type = OCR.JAVA.postprocessing.FilmTyping.getFilmType(line, false);
				mReader.close();
				eReader.close();
				return type;
			}
		}
		while((line = eReader.readLine()) != null) {
			String[] values = line.split(",");
			String eKey = values[0];
			if(eKey.equals(this.key)) {
				String type = OCR.JAVA.postprocessing.FilmTyping.getFilmType(line, true);
				mReader.close();
				eReader.close();
				return type;
			}
		}
		mReader.close();
		eReader.close();
		throw new IOException("Cannot find film with key: " + key);
	}

	private static String[] staffAttributeToArray(String attribute) {
		String[] staffArray = attribute.split("/");
		for(int i = 0; i < staffArray.length; i++) {
			staffArray[i] = staffArray[i].trim();
		}
		return staffArray;
	}

	public String[] getDirectorNameArray() {
		return staffAttributeToArray(this.director);
	}

	public String[] getScriptwriterNameArray() {
		return staffAttributeToArray(this.scriptwriter);
	}

	public String[] getActingNameArray() {
		return staffAttributeToArray(this.acting);
	}

	public String[] getOtherStaffNameArray() throws IOException {
		if(staff.isBlank()) return new String[0];

		String[] staffArray = this.staff.split("/");
		for(int i = 0; i < staffArray.length; i++) {
			if(!staffArray[i].contains("(")) {
				throw new IOException("A member of staff does not have a role: " + staffArray[i] + " in film: " + this.title + "(" + this.key + ")");
			}
			staffArray[i] = staffArray[i].substring(0, staffArray[i].indexOf("(")).trim();
		}
		return staffArray;
	}

	//Use this method to get an array of all entry films
	public static ArrayList<Film> initAllFilms() throws IOException {
		File mFile = new File(METADATA_PATH);
		File eFile = new File(EXTRA_METADATA_PATH);
		BufferedReader mReader = new BufferedReader(new FileReader(mFile));
		BufferedReader eReader = new BufferedReader(new FileReader(eFile));
		String line = null;
		ArrayList<Film> films = new ArrayList<Film>();

		mReader.readLine();
		eReader.readLine();

		while((line = mReader.readLine()) != null)
			films.add(new Film(line, true));
		while((line = eReader.readLine()) != null)
			films.add(new Film(line, false));
			
		mReader.close();
		eReader.close();
		return films;
	}
}
