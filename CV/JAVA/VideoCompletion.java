package CV.JAVA;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class VideoCompletion {
	public static final String SOURCE = "OCR/video_collection.csv";
	public static final String META = "metadata.csv", EXTRA = "metadata-extra.csv";

	public static void main(String[] args) throws IOException {
		File source = new File(SOURCE);
		BufferedReader reader = new BufferedReader(new FileReader(source));
		
		String line = null;
		
		//Read all lines from the file and get csv values
		while((line = reader.readLine()) != null) {
			String[] values = line.split(",");
			String key = values[0];


		}
			
	}

	private static boolean isFeatureFilm(String key) {
		File mFile = new File(META);
		File eFile = new File(EXTRA);
		BufferedReader mReader = new BufferedReader(new FileReader(mFile));
		BufferedReader eReader = new BufferedReader(new FileReader(eFile));

		String line = null;
		while((line = mReader.readLine()) != null) {
			String[] values = line.split(",");
			String mKey = values[0];
			if(mKey.equals(key)) {
				String category = values[1];
				if(isPrivateFilm(category)) {
					return false;
				}
			}
		}
	}

	private static boolean isPrivateFilm(String category) {
		return category.equals("Shanghai (private)");
	}
	
}
