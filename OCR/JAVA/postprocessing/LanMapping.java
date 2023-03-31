import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

class LanMapping {
	private static final String PARENT = "OCR/source/plain_text/csv/";
	private static final String MAIN_META = "metadata.csv";
	private static final String ORGANIZATIONS = "OCR/source/organizations.csv", NONHAN_NAMES = "OCR/source/non-han_chn_names_or_special_authorship.csv";
	private static final String JOB_TITLES = "OCR/source/jobs.csv";
	private static final String LINE_FLAG = "{LINE_CUT}", QUOTE_FLAG = "{QUOTE}";

	public static void main(String[] args) throws IOException {
		//mapEntriesToMainSheet();
		for(int i=1949; i<1967; i++)
			checkActingNames(i);
	}

	public static void mapEntriesToMainSheet() throws IOException {
		//int year = 1966;
		File mainFile = new File(MAIN_META);
		BufferedReader mReader = new BufferedReader(new FileReader(mainFile));
		mReader.readLine(); //Skip first line of titles
		//int mainLine = 1;
		int total = 0;

		for(int year = 1949; year<1967; year++) {
			File file = new File(PARENT + year + ".csv");
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line;
			int count = 0;

			System.out.println(file.getAbsolutePath() + "\n\n");

			while((line = reader.readLine()) != null) {
				String[] attrs = line.split(",");

				String expectedTitle = attrs[0].replace(" ", ""); //Remove unwanted spaces caused by OCR
				expectedTitle = expectedTitle.replace("#", ""); //Remove # mark used for multi-studio/special-format movies
				expectedTitle = expectedTitle.replace(QUOTE_FLAG, "\"");
				
				//mainLine++;
				String ma = "";
				while((ma = mReader.readLine()) != null) {
					String titleInMain = ma.split(",")[1];
					String mainYear = ma.split(",")[2];
					if(Integer.parseInt(mainYear) > year) {
						System.out.println(attrs[0]);
						System.out.println("No entry found in corresponding year for: " + expectedTitle);
						break;
					}

					if(titleInMain.equals(expectedTitle)) {
						System.out.println(expectedTitle + " = " + titleInMain);
						count++;
						break;
					}
				}
			}
			System.out.println("\n\n" + year + ": " + count + "\n\n");
			total += count;
			reader.close();
		}

		mReader.close();
		System.out.println("Total correct entries: " + total + ".\n\n");
	}

	public static void checkStaffNames(int year) throws IOException {
			File file = new File(PARENT + year + ".csv");
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line;
			int count = 0;

			System.out.println(file.getAbsolutePath() + "");

			while((line = reader.readLine()) != null) {
				String[] attrs = line.split(",");

				String staff = attrs[3].replace(" ", ""); //Remove unwanted spaces caused by OCR
				staff = staff.replace(QUOTE_FLAG, "\"");
				staff = staff.replace(LINE_FLAG, "\n");
				String[] staffNames = staff.split("\\n");

				for (String string : staffNames) {
					if(!string.contains("：")) {
						System.out.println("Error format found: \"" + string + "\" at movie " + attrs[0]);
						break;
					}
					String[] att = string.split("：");
					if(att.length > 2) {
						System.out.println("Error format found: \"" + string + "\" at movie " + attrs[0]);
						break;
					}

					String jobTitle = att[0];
					if(!isRecordedJob(jobTitle)) {
						System.out.println("New job title \"" + jobTitle + "\" found. Add to recordance? (-N/-Y)");
						String input = System.console().readLine();
						if(input.equalsIgnoreCase("y")) {
							appendJobTitle(jobTitle);
						} else {
							System.exit(0);
						}
					}
					
					String nameEntry = att[1];
					String[] names;
					if(nameEntry.contains("、")) {
						names = nameEntry.split("、");
					} else {
						names = new String[1];
						names[0] = nameEntry;
					}

					for (String name : names) {
						if(name.endsWith("等") || name.endsWith("执笔")) {
							System.out.println("Ambiguous authorship found: \"" + name + "\" at movie " + attrs[0]);
						}
						if(name.length() < 2) {
							System.out.println("Short name found : \"" + name + "\" at movie " + attrs[0]);
						}
						if(name.length() > 3) {
							//System.out.println("Long name found : \"" + name + "\" at movie " + attrs[0]);
							count++;
							if(!isRecorded(name)) {
								//System.out.println("New long name found. Add to organizations? (-N/-Y)");
								String input = System.console().readLine();
								if(input.equalsIgnoreCase("y")) {
									appendOrg(name);
								} else {
									appendNonHanOrSpecialAuthorship(name);
								}
							}
						}
					}
				}
			}
			System.out.println(year + ": " + count + "\n");
			reader.close();
	}

	public static void checkActingNames(int year) throws IOException {
		File file = new File(PARENT + year + ".csv");
		BufferedReader reader = new BufferedReader(new FileReader(file));
		String line;
		int count = 0;

		System.out.println(file.getAbsolutePath() + "");

		while((line = reader.readLine()) != null) {
			String[] attrs = line.split(",");

			String acting = attrs[4].replace(" ", ""); //Remove unwanted spaces caused by OCR
			if(acting.isBlank()) continue;

			acting = acting.replace(QUOTE_FLAG, "\"");
			acting = acting.replace(LINE_FLAG, "\n");
			String[] actingNames = acting.split("\\n");

			for (String string : actingNames) {
				if(!string.contains("-")) {
					System.out.println("Error format found: \"" + string + "\" at movie " + attrs[0]);
					break;
				}
				String[] att = string.split("-");
				if(att.length > 2) {
					System.out.println("Error format found: \"" + string + "\" at movie " + attrs[0]);
					break;
				}

				String nameEntry = att[0];
				String[] names;
				if(nameEntry.contains("、")) {
					names = nameEntry.split("、");
				} else {
					names = new String[1];
					names[0] = nameEntry;
				}

				for (String name : names) {
					if(name.endsWith("等") || name.endsWith("执笔")) {
						System.out.println("Ambiguous authorship found: \"" + name + "\" at movie " + attrs[0]);
					}
					if(name.length() < 2) {
						System.out.println("Short name found : \"" + name + "\" at movie " + attrs[0]);
					}
					if(name.length() > 3) {
						System.out.println("Long name found : \"" + name + "\" at movie " + attrs[0]);
						count++;
						if(!isRecorded(name)) {
							System.out.println("New long name found. Add to organizations? (-N/-Y)");
							String input = System.console().readLine();
							if(input.equalsIgnoreCase("y")) {
								appendOrg(name);
							} else {
								appendNonHanOrSpecialAuthorship(name);
							}
						}
					}
				}
			}
		}
		System.out.println(year + ": " + count + "\n");
		reader.close();
	}

	private static boolean isRecorded(String name) throws IOException {
		File f = new File(ORGANIZATIONS);
		File f2 = new File(NONHAN_NAMES);
		BufferedReader reader = new BufferedReader(new FileReader(f));
		String line;
		while((line = reader.readLine()) != null) {
			if(line.equals(name)) {
				reader.close();
				return true;
			}
		}
		reader.close();
		BufferedReader reader2 = new BufferedReader(new FileReader(f2));
		String line2;
		while((line2 = reader2.readLine()) != null) {
			if(line2.equals(name)) {
				reader2.close();
				return true;
			}
		}
		reader2.close();
		return false;
	}

	private static void appendOrg(String name) throws IOException {
		File org = new File(ORGANIZATIONS);
		BufferedWriter writer = new BufferedWriter(new FileWriter(org, true));
		writer.append(name + "\n");
		writer.close();
	}

	private static void appendNonHanOrSpecialAuthorship(String name) throws IOException {
		File nonhan = new File(NONHAN_NAMES);
		BufferedWriter writer = new BufferedWriter(new FileWriter(nonhan, true));
		writer.append(name + "\n");
		writer.close();
	}

	private static boolean isRecordedJob(String name) throws IOException {
		File f = new File(JOB_TITLES);
		BufferedReader reader = new BufferedReader(new FileReader(f));
		String line;
		while((line = reader.readLine()) != null) {
			if(line.equals(name)) {
				reader.close();
				return true;
			}
		}
		reader.close();
		return false;
	}
	
	private static void appendJobTitle(String name) throws IOException {
		File job = new File(JOB_TITLES);
		BufferedWriter writer = new BufferedWriter(new FileWriter(job, true));
		writer.append(name + "\n");
		writer.close();
	}

}