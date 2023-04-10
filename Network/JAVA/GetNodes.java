package Network.JAVA;

import java.io.IOException;
import java.util.ArrayList;

import OCR.JAVA.Film;

public class GetNodes {
	protected static final boolean KEEP_ORGANIZATION_NAMES = false;

	private static final String META = "metadata-staff_plot.csv", EXTRA = "metadata-extra.csv", 
			EDGES = "edges.csv", NODES = "nodes.csv";

	private static ArrayList<Film> films;
	private static int globalCounter = 0;

	public static void main(String[] args) {
		try {
			films = Film.initAllFilms();
			//for(int i=1949; i<1967; i++)
				getAllNodesInYear(1949);
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

	private static void addAffiliationsToNode(Node node, ArrayList<Film> filmRange) throws IOException {
		for (Film film : filmRange) {
			if(film.hasName(node.name)) {
				node.addAffiliation(film.production);
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

		System.out.println("\n\nTotal nodes: " + nodes.size() + " in year " + year + ".");

		for (Node node : nodes) {
			addAffiliationsToNode(node, filmsInYear);
			node.getMainAfiiliation();
		}
	}
	
}