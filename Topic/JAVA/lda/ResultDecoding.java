package Topic.JAVA.lda;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class ResultDecoding {
	private static final String SRC = "Topic/results.txt";
	private static final String TAR = "Topic/results_decoded.csv";
	private static final String FILM_KEY_FLAG = "|||FILM:", TOPIC_CODE_FLAG = "|||TOPICS:[", TEXT_FLAG = "]|||TEXT:";

	public static void main(String[] args) {
		try {
			ArrayList<String> decoded = new ArrayList<String>();
			BufferedReader br = new BufferedReader(new FileReader(SRC));
			String line = null;
			while ((line = br.readLine()) != null) {
				//If no flag found, throw RunTimeException
				if (line.indexOf(FILM_KEY_FLAG) == -1 || line.indexOf(TOPIC_CODE_FLAG) == -1 || line.indexOf(TEXT_FLAG) == -1) {
					br.close();
					throw new RuntimeException("No flag found in line: " + line);
				}

				String filmKey = line.substring(line.indexOf(FILM_KEY_FLAG) + FILM_KEY_FLAG.length(), line.indexOf(TOPIC_CODE_FLAG)).trim();
				System.out.println(filmKey);
				String topicCode = line.substring(line.indexOf(TOPIC_CODE_FLAG) + TOPIC_CODE_FLAG.length(), line.indexOf(TEXT_FLAG)).trim();
				//System.out.println(topicCode);
				int topic = Integer.parseInt(topicCode);
				decoded.add(filmKey + "," + topic);
			}
			br.close();

			//Write to file
			BufferedWriter bw = new BufferedWriter(new java.io.FileWriter(TAR));
			bw.write("Film Key,Topic\n");
			for (String s : decoded) {
				bw.write(s);
				bw.newLine();
			}
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
