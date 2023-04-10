package OCR.JAVA;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/*
 * Use this finalised utility class to store information about a film.
 * A Film with reels as negative number is one with no recordance of such information.
 */
public class Film {
	public String key, title, translated, colour, special, director, scriptwriter, acting, staff, plot;
	public int year, reels;
	public Studio[] production;

	public static final String METADATA_PATH = "metadata.csv", EXTRA_METADATA_PATH = "metadata-extra.csv", STAFF_PLOT_DATA_PATH = "metadata-staff_plot.csv";
	public static final String ORGANISATION_LIST_PATH = "OCR/organizations.csv", LONG_PERSON_NAME_LIST_PATH = "OCR/non-han_chn_names_or_special_authorship.csv";

	public Film(String key, String title, int year, String translated, String production, String colour, int reels, String special, String director, String scriptwriter, String acting, String staff, String plot) throws IOException {
		this.key = key;
		this.title = title;
		this.translated = translated;
		this.production = productionAttributeToStudioNameArray(production);
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
		production = productionAttributeToStudioNameArray(values[4]);
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
		if(attribute.isBlank()) return new String[0];

		String[] staffArray = attribute.split("/");
		for(int i = 0; i < staffArray.length; i++) {
			staffArray[i] = staffArray[i].trim();
		}
		return staffArray;
	}
	
	private static Studio[] productionAttributeToStudioNameArray(String production) throws IOException {
		if(production.isBlank()) return new Studio[0];

		String[] proArray = production.split("&");
		Studio[] proList = new Studio[proArray.length];
		for(int i = 0; i < proArray.length; i++) {
			proArray[i] = proArray[i].trim();
			proList[i] = new Studio(proArray[i]);
		}
		return proList;
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
			staffArray[i] = staffArray[i].substring(0, staffArray[i].indexOf("("));
			staffArray[i] = staffArray[i].trim();
		}
		return staffArray;
	}

	public HashMap<String, String> getOtherStaffNameArrayWithRole() throws IOException {
		HashMap<String, String> staffMap = new HashMap<String, String>();
		if(staff.isBlank()) return staffMap; //Return an empty map if there is no staff

		String[] staffArray = this.staff.split("/");
		for(int i = 0; i < staffArray.length; i++) {
			if(!staffArray[i].contains("(")) {
				throw new IOException("A member of staff does not have a role: " + staffArray[i] + " in film: " + this.title + "(" + this.key + ")");
			}
			String name = staffArray[i].substring(0, staffArray[i].indexOf("("));
			name = name.trim();
			String role = staffArray[i].substring(staffArray[i].indexOf("(") + 1, staffArray[i].indexOf(")"));
			staffMap.put(name, role);
		}
		return staffMap;
	}

	//Only returns true if a name is found in the director, scriptwriter, or other staff fields
	public boolean hasFilmmaker(String name) throws IOException {
		String[] directorArray = getDirectorNameArray();
		String[] scriptwriterArray = getScriptwriterNameArray();
		String[] otherStaffArray = getOtherStaffNameArray();
		for(String director : directorArray) {
			if(director.equals(name)) return true;
		}
		for(String scriptwriter : scriptwriterArray) {
			if(scriptwriter.equals(name)) return true;
		}
		for(String otherStaff : otherStaffArray) {
			if(otherStaff.equals(name)) return true;
		}
		return false;
	}

	//Only returns true if a name is found in the acting attribute
	public boolean hasActorOrActress(String name) throws IOException {
		String[] actingArray = getActingNameArray();
		for(String acting : actingArray) {
			if(acting.equals(name)) return true;
		}
		return false;
	}

	//Returns true if a name is found in the film
	public boolean hasName(String name) throws IOException {
		String[] directorArray = getDirectorNameArray();
		String[] scriptwriterArray = getScriptwriterNameArray();
		String[] otherStaffArray = getOtherStaffNameArray();
		String[] actingArray = getActingNameArray();
		for(String director : directorArray) {
			if(director.equals(name)) return true;
		}
		for(String scriptwriter : scriptwriterArray) {
			if(scriptwriter.equals(name)) return true;
		}
		for(String otherStaff : otherStaffArray) {
			if(otherStaff.equals(name)) return true;
		}
		for(String acting : actingArray) {
			if(acting.equals(name)) return true;
		}
		return false;
	}

	//Returns a list of roles that a name has in a film
	public ArrayList<String> getRolesOfAName(String name) throws IOException {
		String[] directorArray = getDirectorNameArray();
		String[] scriptwriterArray = getScriptwriterNameArray();
		HashMap<String, String> otherStaffArrayWithRole = getOtherStaffNameArrayWithRole();
		String[] otherStaffArray = otherStaffArrayWithRole.keySet().toArray(new String[0]);
		String[] actingArray = getActingNameArray();
		ArrayList<String> roles = new ArrayList<String>();
		for(String director : directorArray) {
			if(director.equals(name)) roles.add("Director");
		}
		for(String scriptwriter : scriptwriterArray) {
			if(scriptwriter.equals(name)) roles.add("Scriptwriter");
		}
		for(String otherStaff : otherStaffArray) {
			if(otherStaff.equals(name)) roles.add(otherStaffArrayWithRole.get(name));
		}
		for(String acting : actingArray) {
			if(acting.equals(name)) roles.add("Acting");
		}
		return roles;
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

	//Use this function to check if a name signed in a certain role is an organisation or a person
	public static boolean isOrganisation(String name) throws IOException {
		if(name.length() <= 3) return false;

		File orgFile = new File(ORGANISATION_LIST_PATH);
		File pFile = new File(LONG_PERSON_NAME_LIST_PATH);
		BufferedReader orgReader = new BufferedReader(new FileReader(orgFile));
		BufferedReader pReader = new BufferedReader(new FileReader(pFile));
		String line = null;

		while((line = orgReader.readLine()) != null) {
			if(line.equals(name)) {
				pReader.close();
				orgReader.close();
				return true;
			}
		}

		while((line = pReader.readLine()) != null) {
			if(line.equals(name)) {
				pReader.close();
				orgReader.close();
				return false;
			}
		}
			
		pReader.close();
		orgReader.close();
		throw new IOException("Unexpected filmmaker name: \"" + name + "\"");
	}
	
}
