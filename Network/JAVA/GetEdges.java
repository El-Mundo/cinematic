package Network.JAVA;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import OCR.JAVA.Film;

public class GetEdges {
	protected static final String EDGES_ROOT = "Network/csv/edges";
	private static final boolean USE_WEIGHT = false, RUN_ALL_ONCE = true;

	private static ArrayList<Film> films;

	public static void main(String[] args) {
		try {
			films = Film.initAllFilms();
			if(!RUN_ALL_ONCE) {
				getAllEdgesInYear(1966);
			} else {
				getAllEdges();
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	private static void getAllEdges() throws IOException {
		if(USE_WEIGHT)
			getAllWeightedEdges();
		else
			getAllUnweightedEdges();
	}

	private static void getAllEdgesInYear(int year) throws IOException {
		if(USE_WEIGHT)
			getAllWeightedEdgesInYear(year);
		else
			getAllUnweightedEdgesInYear(year);
	}

	private static void getAllWeightedEdgesInYear(int year) throws IOException {
		ArrayList<Film> filmsInYear = new ArrayList<Film>();
		HashMap<String, Integer> estimatedEdges = new HashMap<String, Integer>();
		//Use a special format to represent an edge: "A->B"

		for (Film film : films) {
			if(film.year == year) {
				filmsInYear.add(film);
			}
		}

		int totalWeight = 0;
		int maxWeight = 1;
		String maxWeightRep = "";

		for (Film film : filmsInYear) {
			String[] allNames = film.getAllNamesArrayWithoutDuplication();
			ArrayList<String> estimatedEdgesInEntry = new ArrayList<String>();

			for (String name : allNames) {
				for (String name2 : allNames) {
					if(Film.isOrganisation(name) || Film.isOrganisation(name2)) continue; //Skip organisations
					if(name.equals(name2)) continue; //Skip the same node

					String rep = name + "->" + name2;
					String rev = name2 + "->" + name;
					//Use this to count the paired appearance of two nodes only once for one film
					if(!estimatedEdgesInEntry.contains(rep) && !estimatedEdgesInEntry.contains(rev)) {
						estimatedEdgesInEntry.add(rep);
						
						//The network is undirected, so we only need to add one edge for a pair of nodes
						if(!estimatedEdges.containsKey(rep) && !estimatedEdges.containsKey(rev)) {
							estimatedEdges.put(rep, 1);
							totalWeight++;
						} else {
							totalWeight++;
							int weight;
							//If the edge already exists, we need to increase the weight
							if(estimatedEdges.containsKey(rep)) {
								weight = estimatedEdges.get(rep) + 1;
								estimatedEdges.put(rep, weight);
							} else if(estimatedEdges.containsKey(rev)) {
								weight = estimatedEdges.get(rev) + 1;
								estimatedEdges.put(rev, weight);
							} else {
								throw new IOException("Edge: " + rep + " or " + rev + " not found!");
							}
							if(weight > maxWeight) {
								maxWeight = weight;
								maxWeightRep = rep;
							} 
						}
					}
				}
			}
		}

		ArrayList<WeightedEdge> edges = new ArrayList<WeightedEdge>();
		for (String edge : estimatedEdges.keySet()) {
			String[] names = edge.split("->");
			edges.add(new WeightedEdge(nameToId(names[0], year), nameToId(names[1], year), estimatedEdges.get(edge)));
		}

		System.out.println("Year " + year + " done. " + edges.size() + " edges found.");
		System.out.println("Total weight: " + totalWeight);
		System.out.println("Max weight: " + maxWeight + " (" + maxWeightRep + ")");
	
		writeAllWeightedEdges(edges, Integer.toString(year));
	}

	private static void getAllUnweightedEdges() throws IOException {
		ArrayList<Edge> edges = new ArrayList<Edge>();
		int n = 0;

		for (Film film : films) {
			String type = film.getFilmType();
			String category = formatCategories(film.getCategory());
			String production = film.productionToString();
			String key = film.key;
			String[] allNames = film.getAllNamesArrayWithoutDuplication();
			ArrayList<String> estimatedEdges = new ArrayList<String>();
			int year = film.year;
			
			for (String name : allNames) {
				for (String name2 : allNames) {
					if(Film.isOrganisation(name) || Film.isOrganisation(name2)) continue; //Skip organisations
					if(name.equals(name2)) continue; //Skip the same node

					String rep = name + "->" + name2;
					String rev = name2 + "->" + name;
					if(!estimatedEdges.contains(rep) && !estimatedEdges.contains(rev)) {
						//The network is undirected, so we only need to add one edge for a pair of nodes
						estimatedEdges.add(rep);
					}
				}
			}

			for (String edge : estimatedEdges) {
				String[] names = edge.split("->");
				edges.add(new Edge(nameToAllYearId(names[0]), nameToAllYearId(names[1]), year, production, category, type, key));
			}
			n++;
			System.out.println("Film (" + n + "/" + films.size() + ") " + film.title + " done. " + estimatedEdges.size() + " edges found.");
		}

		writeAllEdges(edges, "all-unweighted");
	}

	private static void getAllWeightedEdges() throws IOException {
		HashMap<String, Integer> estimatedEdges = new HashMap<String, Integer>();

		int totalWeight = 0;
		int maxWeight = 1;
		String maxWeightRep = "";

		for (Film film : films) {
			String[] allNames = film.getAllNamesArrayWithoutDuplication();
			ArrayList<String> estimatedEdgesInEntry = new ArrayList<String>();

			for (String name : allNames) {
				for (String name2 : allNames) {
					if(Film.isOrganisation(name) || Film.isOrganisation(name2)) continue; //Skip organisations
					if(name.equals(name2)) continue; //Skip the same node

					String rep = name + "->" + name2;
					String rev = name2 + "->" + name;
					//Use this to count the paired appearance of two nodes only once for one film
					if(!estimatedEdgesInEntry.contains(rep) && !estimatedEdgesInEntry.contains(rev)) {
						estimatedEdgesInEntry.add(rep);
						
						//The network is undirected, so we only need to add one edge for a pair of nodes
						if(!estimatedEdges.containsKey(rep) && !estimatedEdges.containsKey(rev)) {
							estimatedEdges.put(rep, 1);
							totalWeight++;
						} else {
							totalWeight++;
							int weight;
							//If the edge already exists, we need to increase the weight
							if(estimatedEdges.containsKey(rep)) {
								weight = estimatedEdges.get(rep) + 1;
								estimatedEdges.put(rep, weight);
							} else if(estimatedEdges.containsKey(rev)) {
								weight = estimatedEdges.get(rev) + 1;
								estimatedEdges.put(rev, weight);
							} else {
								throw new IOException("Edge: " + rep + " or " + rev + " not found!");
							}
							if(weight > maxWeight) {
								maxWeight = weight;
								maxWeightRep = rep;
							} 
						}
					}
				}
			}
		}

		ArrayList<WeightedEdge> edges = new ArrayList<WeightedEdge>();
		for (String edge : estimatedEdges.keySet()) {
			String[] names = edge.split("->");
			edges.add(new WeightedEdge(nameToAllYearId(names[0]), nameToAllYearId(names[1]), estimatedEdges.get(edge)));
		}

		System.out.println("Year 1949-1966 all done. " + edges.size() + " edges found.");
		System.out.println("Total weight: " + totalWeight);
		System.out.println("Max weight: " + maxWeight + " (" + maxWeightRep + ")");
	
		writeAllWeightedEdges(edges, "all");
	}

	@Deprecated
	private static void getAllUnweightedEdgesInYear(int year) throws IOException {
		ArrayList<Film> filmsInYear = new ArrayList<Film>();
		ArrayList<Edge> edges = new ArrayList<Edge>();

		for (Film film : films) {
			if(film.year == year) {
				filmsInYear.add(film);
			}
		}

		for (Film film : filmsInYear) {
			String type = film.getFilmType();
			String category = formatCategories(film.getCategory());
			String production = film.productionToString();
			String key = film.key;
			String[] allNames = film.getAllNamesArrayWithoutDuplication();
			ArrayList<String> estimatedEdges = new ArrayList<String>();
			
			for (String name : allNames) {
				for (String name2 : allNames) {
					if(Film.isOrganisation(name) || Film.isOrganisation(name2)) continue; //Skip organisations
					if(name.equals(name2)) continue; //Skip the same node

					String rep = name + "->" + name2;
					String rev = name2 + "->" + name;
					if(!estimatedEdges.contains(rep) && !estimatedEdges.contains(rev)) {
						//The network is undirected, so we only need to add one edge for a pair of nodes
						estimatedEdges.add(rep);
					}
				}
			}

			for (String edge : estimatedEdges) {
				String[] names = edge.split("->");
				edges.add(new Edge(nameToId(names[0], year), nameToId(names[1], year), year, production, category, type, key));
			}
		}

		System.out.println("Year " + year + " done. " + edges.size() + " edges found.");

		writeAllEdges(edges, Integer.toString(year));
	}

	private static String formatCategories(String[] categories) {
		String result = "";
		for (String category : categories) {
			result += category + " / ";
		}
		return result.substring(0, result.length() - 3);
	}

	private static void writeAllEdges(ArrayList<Edge> edges, String tag) throws IOException {
		File file = new File(EDGES_ROOT + "/edges-" + tag + ".csv");
		FileWriter fWriter;
		if (file.exists()) {
			fWriter = new FileWriter(file, false);
		} else {
			file.createNewFile();
			fWriter = new FileWriter(file);
		}
		BufferedWriter writer = new BufferedWriter(fWriter);
		writer.append("Source,Target,Label,Year,Production,Category,Film Type\n");
		for (Edge edge : edges) {
			writer.append(edge.toString() + "\n");
		}
		writer.close();
	}

	private static void writeAllWeightedEdges(ArrayList<WeightedEdge> edges, String tag) throws IOException {
		File file = new File(EDGES_ROOT + "/edges-" + tag + ".csv");
		FileWriter fWriter;
		if (file.exists()) {
			fWriter = new FileWriter(file, false);
		} else {
			file.createNewFile();
			fWriter = new FileWriter(file);
		}
		BufferedWriter writer = new BufferedWriter(fWriter);
		writer.append("Source,Target,Weight\n");
		for (WeightedEdge edge : edges) {
			writer.append(edge.toString() + "\n");
		}
		writer.close();
	}

	private static int nameToId(String name, int year) throws IOException {
		File nodeFile = new File(GetNodes.NODES_ROOT + "/nodes-" + year + ".csv");
		BufferedReader br = new BufferedReader(new FileReader(nodeFile));
		String line;
		while((line = br.readLine()) != null) {
			String[] parts = line.split(",");
			if(parts[1].replaceAll("\"", "").equals(name)) {
				br.close();
				return Integer.parseInt(parts[0]);
			}
		}
		br.close();
		throw new IOException("Node not found: " + name + " in year " + year);
	}

	private static int nameToAllYearId(String name) throws IOException {
		File nodeFile = new File(GetNodes.NODES_ROOT + "/nodes-all.csv");
		BufferedReader br = new BufferedReader(new FileReader(nodeFile));
		String line;
		while((line = br.readLine()) != null) {
			String[] parts = line.split(",");
			if(parts[1].replaceAll("\"", "").equals(name)) {
				br.close();
				return Integer.parseInt(parts[0]);
			}
		}
		br.close();
		throw new IOException("Node not found: " + name + " in all-year nodes.");
	}
	
}
