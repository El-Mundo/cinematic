import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

class LanMapping {
	private static final String PARENT = "OCR/source/plain_text/";
	private static final String FLAG = "{LINE_CUT}";

	public static void main(String[] args) throws IOException {
		for(int year = 1949; year<1967; year++) {
			File file = new File(PARENT + year + ".csv");
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line;
			boolean inQuote = false;
			int count = 0;

			while((line = reader.readLine()) != null) {
				if(line.contains("\"")) {
					count++;
					System.out.println(year+": "+line+"\n\n");
				}

				if(inQuote) {

				} else {
					
				}
			}
			System.out.println(year+": "+count);
			reader.close();
		}
	}
}