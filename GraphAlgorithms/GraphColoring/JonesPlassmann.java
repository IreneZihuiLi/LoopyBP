package test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;
import java.util.StringTokenizer;

import org.apache.tinkerpop.gremlin.structure.Direction;
import org.apache.tinkerpop.gremlin.structure.T;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.apache.tinkerpop.gremlin.tinkergraph.structure.TinkerGraph;

public class JonesPlassmann {

	// Colored Map: <ID,colorStateNumber>, 0 means non-colored
	public static final HashMap<Integer, Integer> coloredMap = new HashMap<Integer, Integer>();
	// The global graph variable
	public static TinkerGraph graph = null;

	public static void main(String[] args) throws IOException {

		// Load in the graph
		graph = TinkerGraph.open();
		// I loaded from a csv file
		String filename = "data/test0_3_58_1355.csv";
		load(filename);
		// show a brief info of the graph
		System.out.println("\nGraph:" + graph.toString() + "\n"
				+ coloredMap.toString());

		// Do Jones-Plassmann Graph Coloring
		JonesPlassmann();

		// show a brief info of the graph
		System.out.println("Graph:" + graph.toString() + "\n"
				+ coloredMap.toString());

	}

	public static void load(String filename) {

		try {
			BufferedReader br = new BufferedReader(new FileReader(new File(
					filename)));
			String line;
			line = br.readLine();
			while ((line = br.readLine()) != null) {
				StringTokenizer st = new StringTokenizer(line, ", \t\n\r\f");
				if (st.countTokens() != 5) {
					System.out.println("Error reading from file \"" + filename
							+ "\"");
					System.exit(1);
				}

				Integer v1Id = Integer.valueOf(st.nextToken());
				Double v1Att = Double.valueOf(st.nextToken());
				Integer v2Id = Integer.valueOf(st.nextToken());
				Double v2Att = Double.valueOf(st.nextToken());
				Double edgeAtt = Double.valueOf(st.nextToken());
				// Read in ids, attributes
				readInVertex(v1Id, v2Id, v1Att, v2Att, edgeAtt);

			}

			System.out.print("Read File Done!");
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(0);
		}

	}

	// The readin methods, to add vertices and edges to the global graph.
	public static void readInVertex(int id1, int id2, double prop1,
			double prop2, double propEdge) {
		Vertex vertex1;
		Vertex vertex2;
		// Read in vertex 1
		Iterator<Vertex> vertexIterator1 = graph.vertices(id1);
		if (!vertexIterator1.hasNext()) {
			vertex1 = graph.addVertex(T.label, "vertex", T.id, id1, "weight",
					prop1, "colored", false);
		} else {
			vertex1 = vertexIterator1.next();
		}
		// Initialize the colored Map
		coloredMap.put((Integer) vertex1.id(), 0);

		// Read in vertex 2
		Iterator<Vertex> vertexIterator2 = graph.vertices(id2);
		if (!vertexIterator2.hasNext()) {

			vertex2 = graph.addVertex(T.label, "vertex", T.id, id2, "weight",
					prop2, "colored", false);
		} else {
			vertex2 = vertexIterator2.next();
		}

		// add the Edge
		vertex1.addEdge("edge", vertex2, "properties", propEdge);

	}

	// Now starts JP algo
	public static void JonesPlassmann() {
		// starts from the first color
		int randomColor = 1;

		ArrayList<Integer> initRandom = getRandomVertices(randomColor, 40);
		// if graph is not empty
		while (initRandom.size() != 0) {
			// do coloring in a group
			Iterator<Vertex> vertexIterator = graph.vertices(initRandom
					.toArray());
			while (vertexIterator.hasNext()) {
				// Color the vertex
				Vertex vertex = vertexIterator.next();
				doColor((Integer) vertex.id(), randomColor);
				vertex.remove();
			}

			initRandom.clear();
			randomColor++;
			initRandom = getRandomVertices(randomColor, 40);

		}

	}

	// Randomly get a group of vertices by ID
	public static ArrayList<Integer> getRandomVertices(int colorNumber,
			int number) {

		ArrayList<Integer> chosen = new ArrayList<Integer>();
		ArrayList<Integer> list = new ArrayList<Integer>();

		Iterator<Vertex> vertexOfGraph = graph.vertices();
		int size = 0;
		while (vertexOfGraph.hasNext()) {
			list.add((int) vertexOfGraph.next().id());
			size++;
		}
		Random random = new Random();

		// deal with boundry
		if (list.size() < number) {
			// all in a group
			return list;
		} else if (list.size() == 0) {
			return chosen;
		} else {

			for (int i = 0; i < number; i++) {

				ArrayList<Integer> neighbours = new ArrayList<Integer>();

				int index = random.nextInt(list.size() - 1);
				// if it is not colored
				if (!isColored(list.get(index))) {
					chosen.add(list.get(index));

					list.remove(index);

					// get neighours
					Iterator<Vertex> vertexIterator = graph.vertices(list
							.get(index));
					while (vertexIterator.hasNext()) {
						Vertex vertex = vertexIterator.next();
						// System.out.println("Now is Vertex "+
						// vertex.id()+"\t");
						// Get adjacent vertices
						Iterator<Vertex> ajacent = vertex
								.vertices(Direction.OUT);
						while (ajacent.hasNext()) {
							Vertex neighbourVertex = ajacent.next();
							// add to neighbor list
							neighbours.add((Integer) neighbourVertex.id());
							// System.out.println("Adjacent:"+neighbourVertex.id());
						}
						// System.out.println(neighbours.toString());
					}

					// check existance
					for (int j = 0; j < neighbours.size(); j++) {
						for (int m = 0; m < list.size(); m++) {
							if (list.get(m).equals(neighbours.get(j))) {

								list.remove(m);
								break;
							}
						}

					}

				}
			}

		}
		// System.out.println(chosen.toString());
		return chosen;
	}

	public static boolean isColored(Integer id) {

		if (!coloredMap.get(id).equals(0)) {
			return true;
		}
		return false;
	}

	public static void doColor(Integer id, Integer colorNumber) {

		coloredMap.put(id, colorNumber);
	}

}

/**
 * PS I was trying to implement the coloring by setting a property: "colored",4
 * But I was following the tutorial here:
 * http://www.tinkerpop.com/docs/3.0.0.M1/#_mutating_the_graph in this line:
 * blueprints.property("created",2010) I was notified an AbstractMethodError.
 * 
 * I think I used a wrong API, but in my case, I set up another HashMap to store
 * the state of colors.
 * 
 */
