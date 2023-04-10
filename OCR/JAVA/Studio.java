package OCR.JAVA;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Studio {
	public static final String STUDIOS_LIST = "OCR/studios.csv";

	public String name;
	public String category;
	private int orderInList; //Recorded to be Hashcode
	
	public Studio(String name, String category, int orderInList) {
		this.name = name;
		this.category = category;
		this.orderInList = orderInList;
	}

	public Studio(String name) throws IOException {
		this.name = name;
		getCategoryAndOrder();
	}

	private void getCategoryAndOrder() throws IOException {
		File f = new File(STUDIOS_LIST);
		BufferedReader r = new BufferedReader(new FileReader(f));
		String l = "";
		int line = 0;

		while ((l = r.readLine()) != null) {
			if(l.split(",")[0].equals(name)) {
				this.category = l.split(",")[1];
				this.orderInList = line;
				r.close();
				return;
			}
			line++;
		}
		r.close();
		throw new IOException("Unexpected studio found: \"" + name + "\" in the film metadata.");
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

	@Override
	public int hashCode() {
		return orderInList;
	}
	
}
