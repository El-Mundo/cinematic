package OCR.JAVA.romanizing.src;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import OCR.JAVA.Film;

public class NameRoman {
	private final static String JOBS = "OCR/JAVA/romanizing/translated-jobs.csv";
	private final static String NONHAN_NAMES = "OCR/JAVA/romanizing/roman-non-han_names.csv";
	private final static String TAR = "OCR/translated-names.tsv";

	private static HashMap<String, String> jobToEng = new HashMap<String, String>();
	private static HashMap<String, String> nonHanNameToEng = new HashMap<String, String>();

	public static void main(String[] args) throws IOException {
		File nonHanNames = new File(NONHAN_NAMES);
		File jobs = new File(JOBS);
		BufferedReader reader1 = new BufferedReader(new FileReader(nonHanNames));
		String line = reader1.readLine();
		while((line = reader1.readLine()) != null) {
			String[] parts = line.split(",");
			nonHanNameToEng.put(parts[0], parts[1]);
		}
		reader1.close();
		BufferedReader reader2 = new BufferedReader(new FileReader(jobs));
		line = "";
		while((line = reader2.readLine()) != null) {
			String[] parts = line.split(",");
			jobToEng.put(parts[0], parts[1]);
		}
		reader2.close();

		System.out.println("Finished reading indexes");

		int i = 0;

		ArrayList<Film> films = Film.initAllFilms();
		ArrayList<TranslateTemp> translated = new ArrayList<TranslateTemp>();
		for (Film film : films) {
			String[] acting = film.getActingNameArray();
			String[] directing = film.getDirectorNameArray();
			String[] writing = film.getScriptwriterNameArray();
			HashMap<String, String> other = film.getOtherStaffNameArrayWithRole();

			String act = "", dir = "", script = "", otherStaff = "";
			for (String name : acting) {
				String roman = "";
				if(nonHanNameToEng.containsKey(name))
				//Get specifically translated name (by Google Translate due to lack of appropriate method) if it is not applicable as a standard Han name
					roman = nonHanNameToEng.get(name);
				else
				//Otherwise, romanize the name as a standard Han name (pinyin)
					roman = RomanizeMain.romanizeStandardHanName(name);
				act += roman + ", ";
			}
			if(act.endsWith(", "))
				act = act.substring(0, act.length() - 2);
			for (String name : directing) {
				String roman = "";
				if(nonHanNameToEng.containsKey(name))
					roman = nonHanNameToEng.get(name);
				else
					roman = RomanizeMain.romanizeStandardHanName(name);
				dir += roman + ", ";
			}
			if(dir.endsWith(", "))
				dir = dir.substring(0, dir.length() - 2);
			for (String name : writing) {
				String roman = "";
				if(nonHanNameToEng.containsKey(name))
					roman = nonHanNameToEng.get(name);
				else
					roman = RomanizeMain.romanizeStandardHanName(name);
				script += roman + ", ";
			}
			if(script.endsWith(", "))
				script = script.substring(0, script.length() - 2);
			for (String name : other.keySet()) {
				String title = other.get(name);
				String roman = "";
				
				if(nonHanNameToEng.containsKey(name))
					roman = nonHanNameToEng.get(name);
				else
					roman = RomanizeMain.romanizeStandardHanName(name);

				String engTitle = "";
				if(jobToEng.containsKey(title)) {
					engTitle = jobToEng.get(title);
				} else {
					engTitle = title;
					throw new RuntimeException("Unrecognized job title: " + title);
				}

				roman += " (" + engTitle + ")";
				otherStaff += roman + ", ";
			}
			if(otherStaff.endsWith(", "))
				otherStaff = otherStaff.substring(0, otherStaff.length() - 2);

			translated.add(new TranslateTemp(film.key, dir, script, act, otherStaff));

			i++;
			System.out.println("Translated " + i + "/" + films.size());
		}
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(TAR));
		i = 0;
		for (TranslateTemp t : translated) {
			writer.write(t.toString());
			writer.newLine();
			i++;
			System.out.println("Wrote " + i + "/" + translated.size());
		}
		writer.close();
	}

	private static class TranslateTemp {
		public String act, dir, script, other;
		public String key;

		public TranslateTemp(String key, String dir, String script, String act, String other) {
			this.key = key;
			this.act = act;
			this.dir = dir;
			this.script = script;
			this.other = other;
		}

		@Override
		public String toString() {
			return key + ";" + dir + ";" + script + ";" + act + ";" + other;
		}
	}

}
