package Network.JAVA;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import OCR.JAVA.Film;
import OCR.JAVA.Studio;

public class GetNodes {
	protected static final boolean KEEP_ORGANIZATION_NAMES = false,
			RUN_ALL_YEARS_AT_ONCE = true;

	static final String META = "metadata-staff_plot.csv", EXTRA = "metadata-extra.csv", 
			EDGES = "Network/edges.csv", NODES = "Network/JAVA/nodes.csv";
	static int globalCounter = 0;

	protected static final String NODES_ROOT = "Network/csv/nodes";

	private static ArrayList<Film> films;

	public static void main(String[] args) {
		try {
			films = Film.initAllFilms();
			if(!RUN_ALL_YEARS_AT_ONCE) {
				for(int i=1949; i<1967; i++)
					getAllNodesInYear(i);
			} else {
				getAllNodes();
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	private static boolean hasNode(String name, ArrayList<Node> nodes) {
		for (Node node : nodes) {
			if(name.equals(node.name)) {
				return true;
			}
		}
		return false;
	}

	private static void addNodes(String[] names, ArrayList<Node> nodes, Film parent) throws IOException {
		for (String name : names) {
			if(!hasNode(name, nodes)) {
				if(KEEP_ORGANIZATION_NAMES) {
					nodes.add(new Node(name));
				} else {
					if(!Film.isOrganisation(name)) {
						nodes.add(new Node(name));
					}
				}
			}
		}
	}

	private static void addAffiliationsAndRolesToNode(Node node, ArrayList<Film> filmRange) throws IOException {
		for (Film film : filmRange) {
			if(film.hasName(node.name)) {
				node.addAffiliation(film.production);

				ArrayList<String> roles = film.getRolesOfAName(node.name);
				for (String role : roles) {
					node.addRole(role);
				}
			}
		}
	}

	private static void getAllNodesInYear(int year) throws IOException {
		ArrayList<Film> filmsInYear = new ArrayList<Film>();
		ArrayList<Node> nodes = new ArrayList<Node>();

		for (Film film : films) {
			if(film.year == year) {
				filmsInYear.add(film);
			}
		}

		for (Film film : filmsInYear) {
			//System.out.println(film.title);
			String[] directors = film.getDirectorNameArray();
			addNodes(directors, nodes, film);
			String[] scriptwriters = film.getScriptwriterNameArray();
			addNodes(scriptwriters, nodes, film);
			String[] actors = film.getActingNameArray();
			addNodes(actors, nodes, film);
			String[] staff = film.getOtherStaffNameArray();
			addNodes(staff, nodes, film);
		}

		int i = 0;
		for (Node node : nodes) {
			addAffiliationsAndRolesToNode(node, filmsInYear);
			node.getMainAffiliatedCategory();
			node.getMainRole();
			node.getFirstAppearanceCategory();
			node.assignLocationInList(i);
			i++;
		}

		System.out.println("\n\nTotal nodes: " + nodes.size() + " in year " + year + ".");
		writeAllNodes(nodes, Integer.toString(year));
		
		//DEBUG_checkStudioEquality(new Studio("长春电影制片厂", "Changchun"), new Studio("长春电影制片厂", "Changchun"));
		//DEBUG_checkStudioEquality(new Studio("北京电影制片厂", "Beijing"), new Studio("八一电影制片厂", "Beijing"));
		//DEBUG_writeAllNodes(nodes);
	}

	private static void writeAllNodes(ArrayList<Node> nodes, String tag) throws IOException {
		File file = new File(NODES_ROOT + "/nodes-" + tag + ".csv");
		FileWriter fWriter;
		if (file.exists()) {
			fWriter = new FileWriter(file, false);
		} else {
			file.createNewFile();
			fWriter = new FileWriter(file);
		}
		BufferedWriter writer = new BufferedWriter(fWriter);
		writer.append("Id,Label,GeoCategory,Affilations,Main Role,All Roles,Debut Year,Debut Region,Debut at Private Studio\n");
		for (Node node : nodes) {
			writer.append(node.toString() + "\n");
		}
		writer.close();
	}

	@SuppressWarnings("unused")
	private static void DEBUG_writeAllNodes(ArrayList<Node> nodes) throws IOException {
		File targetPath = new File(NODES);
		BufferedWriter writer = new BufferedWriter(new FileWriter(targetPath, true));
		for (Node node : nodes) {
			writer.append(node.toString() + "\n");
		}
		writer.close();
	}

	@SuppressWarnings("unused")
	private static void DEBUG_checkStudioEquality(Studio a, Studio b) {
		//Checks if the equals function overriden in Studio works.
		System.out.println("Studio a: " + a.name + " and b: " + b.name + " are equal?");
		if(a.equals(b)) {
			System.out.println("EQUAL");
		} else {
			System.out.println("NOT EQUAL");
		}
	}

	private static void getAllNodes() throws IOException {
		ArrayList<Node> nodes = new ArrayList<Node>();

		for (Film film : films) {
			//System.out.println(film.title);
			String[] directors = film.getDirectorNameArray();
			addNodes(directors, nodes, film);
			String[] scriptwriters = film.getScriptwriterNameArray();
			addNodes(scriptwriters, nodes, film);
			String[] actors = film.getActingNameArray();
			addNodes(actors, nodes, film);
			String[] staff = film.getOtherStaffNameArray();
			addNodes(staff, nodes, film);
		}

		int i = 0;
		for (Node node : nodes) {
			addAffiliationsAndRolesToNode(node, films);
			node.getMainAffiliatedCategory();
			node.getMainRole();
			node.getFirstAppearanceCategory();
			node.assignLocationInList(i);
			i++;
		}

		System.out.println("\n\nTotal nodes: " + nodes.size() + ".");
		writeAllNodes(nodes, "all");
	}
	
}