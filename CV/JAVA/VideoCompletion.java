package CV.JAVA;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class VideoCompletion {
	public static final boolean COUNT_MUSICAL_AS_FEATURE = true;

	public static final String SOURCE = "CV/video_collection.csv";
	public static final String META = "metadata.csv", EXTRA = "metadata-extra.csv";

	public static void main(String[] args) throws IOException {
		File source = new File(SOURCE);
		BufferedReader reader = new BufferedReader(new FileReader(source));
		reader.readLine();

		int collectedPrivate = 0, privateTotal = 0;
		int collectedFeature = 0, featureTotal = 0;
		int collectedStateFeature = 0, stateFeatureTotal = 0;
		int collectedDocumentary = 0, documentaryTotal = 0;
		int collectedPerformance = 0, performanceTotal = 0;
		int collectedOpera = 0, operaTotal = 0;
		
		String line = null;
		
		//Read all lines from the file and get csv values
		while((line = reader.readLine()) != null) {
			String[] values = line.split(",", -1);
			String key = values[0];

			//if(!isFeatureFilm(key)) System.out.println(getFilmType(key));
			System.out.println(values[1] + " is Feature: " + isFeatureFilm(key) + ", is Private:" + isPrivateFilm(values[3]));
			if(isFeatureFilm(key)) {
				featureTotal++;
				if(!values[4].isEmpty()) collectedFeature++;

				if(!isPrivateFilm(values[3])) {
					stateFeatureTotal++;
					if(!values[4].isEmpty()) collectedStateFeature++;
				}
			}
			if(isPrivateFilm(values[3])) {
				privateTotal++;
				if(!values[4].isEmpty()) collectedPrivate++;
			}

			if(getFilmType(key).equals("Artistic Documentary")) {
				documentaryTotal++;
				if(!values[4].isEmpty()) collectedDocumentary++;
			}

			if(getFilmType(key).equals("Performance")) {
				performanceTotal++;
				if(!values[4].isEmpty()) collectedPerformance++;
			}

			if(getFilmType(key).equals("Opera")) {
				operaTotal++;
				if(!values[4].isEmpty()) collectedOpera++;
			}
		}
		
		reader.close();

		System.out.println();
		System.out.println("Collected " + collectedPrivate + " out of " + privateTotal + " private films.");
		System.out.println("Collected " + collectedFeature + " out of " + featureTotal + " feature films.");
		System.out.println("Collected " + collectedStateFeature + " out of " + stateFeatureTotal + " state feature films.");
		System.out.println("Collected " + collectedDocumentary + " out of " + documentaryTotal + " documentaries.");
		System.out.println("Collected " + collectedPerformance + " out of " + performanceTotal + " performances.");
		System.out.println("Collected " + collectedOpera + " out of " + operaTotal + " operas.");
	}

	private static boolean isFeatureFilm(String key) throws IOException {
		File mFile = new File(META);
		File eFile = new File(EXTRA);
		BufferedReader mReader = new BufferedReader(new FileReader(mFile));
		BufferedReader eReader = new BufferedReader(new FileReader(eFile));

		String line = null;
		while((line = mReader.readLine()) != null) {
			String[] values = line.split(",");
			String mKey = values[0];
			if(mKey.equals(key)) {
				String type = OCR.JAVA.postprocessing.FilmTyping.getFilmType(line, false);
				mReader.close();
				eReader.close();
				return COUNT_MUSICAL_AS_FEATURE ? (type.equals("Feature") || type.equals("Musical")) : type.equals("Feature");
			}
		}
		while((line = eReader.readLine()) != null) {
			String[] values = line.split(",");
			String eKey = values[0];
			if(eKey.equals(key)) {
				String type = OCR.JAVA.postprocessing.FilmTyping.getFilmType(line, true);
				mReader.close();
				eReader.close();
				return COUNT_MUSICAL_AS_FEATURE ? (type.equals("Feature") || type.equals("Musical")) : type.equals("Feature");
			}
		}
		mReader.close();
		eReader.close();
		throw new IOException("Cannot find film with key: " + key);
	}

	private static boolean isPrivateFilm(String category) {
		return category.equals("Shanghai (private)");
	}

	private static String getFilmType(String key) throws IOException {
		File mFile = new File(META);
		File eFile = new File(EXTRA);
		BufferedReader mReader = new BufferedReader(new FileReader(mFile));
		BufferedReader eReader = new BufferedReader(new FileReader(eFile));

		String line = null;
		while((line = mReader.readLine()) != null) {
			String[] values = line.split(",");
			String mKey = values[0];
			if(mKey.equals(key)) {
				String type = OCR.JAVA.postprocessing.FilmTyping.getFilmType(line, false);
				mReader.close();
				eReader.close();
				return type;
			}
		}
		while((line = eReader.readLine()) != null) {
			String[] values = line.split(",");
			String eKey = values[0];
			if(eKey.equals(key)) {
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
	
}
