package OCR.JAVA;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Studio {
	public static final String STUDIOS_LIST = "OCR/studios.csv";

	public String name;
	public String category;
	
	public Studio(String name, String category) {
		this.name = name;
		this.category = category;
	}

	public Studio(String name) throws IOException {
		this.name = name;
		this.category = getCategory(name);
	}

	private static String getCategory(String studioName) throws IOException {
		File f = new File(STUDIOS_LIST);
		BufferedReader r = new BufferedReader(new FileReader(f));
		String l = "";

		while ((l = r.readLine()) != null) {
			if(l.split(",")[0].equals(studioName)) {
				r.close();
				return (l.split(",")[1]);
			}
		}
		r.close();
		throw new IOException("Unexpected studio found: \"" + studioName + "\" in the film metadata.");
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this)
            return true;
        if (!(obj instanceof Studio))
            return false;

        Studio studio = (Studio)obj;
        return this.name.equals(studio.name);
	}
	
}
