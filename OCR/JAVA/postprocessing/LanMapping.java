import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

class LanMapping {
	private static final String input = "OCR/source/plain_text/1950.csv";
	private static final String FLAG = "{LINE_CUT}";

	public static void main(String[] args) throws IOException {
		File file = new File(input);
		BufferedReader reader = new BufferedReader(new FileReader(file));
		String line;
		boolean inQuote = false;
		int count = 0;

		while((line = reader.readLine()) != null) {
			if(line.contains("\"")) {
				System.out.println(line);
			}

			if(inQuote) {

			} else {
				
			}
		}

		System.out.println(count);
		reader.close();
	}
}