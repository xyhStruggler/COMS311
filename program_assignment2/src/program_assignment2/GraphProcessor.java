package program_assignment2;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * Graph processor.
 */
public class GraphProcessor {

    private Map<String, LinkedList<String>> graph;

    // Visited set used in SCCAlgo.
    private Set<String> visited;

    // Finish time used in SCCAlgo.
    private Map<String, Integer> finishTime;

    // The counter used in ComputeOrderAlgo.
    private int counter;

    // Set S used in SCCAlgo.
    private Set<String> S;

    /**
     * Reads the directed graph from the input file.
     *
     * @param graphData the absolute path of a file that stores a directed graph.
     */
    public GraphProcessor(String graphData) {

        // Create an empty graph.
        graph = new LinkedHashMap<String, LinkedList<String>>();

        Scanner input = null;

        try {
            // Open the input file.
            input = new Scanner(new FileInputStream(graphData));

            // Read the number of vertices.
            int num = input.nextInt();

            // Read the edges.
            while (input.hasNextLine()) {
                String line = input.nextLine().trim();
                if (line.length() > 0) {
                    String[] tokens = line.split("\\s+");
                    String src = tokens[0];
                    String dst = tokens[1];
                    if (!graph.containsKey(src)) {
                        graph.put(src, new LinkedList<String>());
                    }
                    if (!graph.containsKey(dst)) {
                        graph.put(dst, new LinkedList<String>());
                    }
                    graph.get(src).add(dst);
                }
            }

        } catch (FileNotFoundException e) {
            System.out.println("Error: cannot read file " + graphData);

        } finally {
            // Close the file.
            if (input != null) {
                input.close();
            }
        }
    }

    /**
     * Returns the out degree of v.
     */
    public int outDegree(String v) {
        return graph.get(v).size();
    }

    /**
     * Returns true if u and v belong to the same SCC; otherwise returns false.
     */
    public boolean sameComponent(String u, String v) {

        // Find all the SCC in the graph.
        ArrayList<Set<String>> scc = SCCAlgo();

        // For each component in G,
        for (Set<String> component : scc) {

            // If both u and v is in it, return true.
            if (component.contains(v)) {
                return component.contains(u);
            }
        }
        return false;
    }

    /**
     * Return all the vertices that belong to the same Strongly Connected Component of v (including v).
     */
    public ArrayList<String> componentVertices(String v) {

        // Find all the SCC in the graph.
        ArrayList<Set<String>> scc = SCCAlgo();

        // For each component in G,
        for (Set<String> component : scc) {

            // If v is in it, return it.
            if (component.contains(v)) {
                return new ArrayList<String>(component);
            }
        }
        return null;
    }

    /**
     * Returns the size of the largest component.
     */
    public int largestComponent() {

        // Find all the SCC in the graph.
        ArrayList<Set<String>> scc = SCCAlgo();

        int largestSize = 0;

        // For each component in G,
        for (Set<String> component : scc) {

            // Update the largest size.
            largestSize = Math.max(largestSize, component.size());
        }
        return largestSize;
    }

    /**
     * Returns the number of strongly connect components.
     */
    public int numComponents() {

        // Find all the SCC in the graph.
        ArrayList<Set<String>> scc = SCCAlgo();

        // Returns the number of components.
        return scc.size();
    }

    /**
     * Returns the BFS path from u to v.
     */
    public ArrayList<String> bfsPath(String u, String v) {

        // Create a path list.
        ArrayList<String> path = new ArrayList<String>();

        // Node for search.
        class Node {

            private String link;
            private Node prevLink;

            private Node(String link, Node prevLink) {
                this.link = link;
                this.prevLink = prevLink;
            }
        }

        // Initialize a Queue Q and a list visited.
        Queue<Node> Q = new LinkedList<Node>();
        Set<String> visited = new HashSet<String>();

        // Place root in Q and visited.
        Q.add(new Node(u, null));
        visited.add(u);

        // while Q is not empty Do
        while (!Q.isEmpty()) {

            // Let front be the first element of Q.
            Node front = Q.poll();

            // If the front link is v,
            if (front.link.equals(v)) {

                // Build the path.
                while (front != null) {
                    path.add(0, front.link);
                    front = front.prevLink;
                }
                break;
            }

            // For every edge <front, next> in E DO
            for (String next : graph.get(front.link)) {

                // If next is not visited,
                if (!visited.contains(next)) {

                    // add it to the end of Q, and add it to visited.
                    Q.add(new Node(next, front));
                    visited.add(next);
                }
            }
        }
        return path;
    }

    /**
     * Returns the vertex with the highest out degree.
     */
    private String vertexWithHighestOutDegree() {
        String vertex = null;
        int highestOutDegree = 0;

        // For every v in the graph,
        for (String v : graph.keySet()) {

            // Compute its out degree.
            int outDegree = outDegree(v);

            // If it has the highest degree, update the result.
            if (outDegree > highestOutDegree) {
                highestOutDegree = outDegree;
                vertex = v;
            }
        }
        return vertex;
    }

    /**
     * Returns a graph in which every edge direction is reversed.
     */
    private Map<String, LinkedList<String>> reverse(Map<String, LinkedList<String>> graph) {
        Map<String, LinkedList<String>> reversedGraph = new LinkedHashMap<String, LinkedList<String>>();

        for (Map.Entry<String, LinkedList<String>> entry : graph.entrySet()) {
            String u = entry.getKey();
            for (String v : entry.getValue()) {
                if (!reversedGraph.containsKey(u)) {
                    reversedGraph.put(u, new LinkedList<String>());
                }
                if (!reversedGraph.containsKey(v)) {
                    reversedGraph.put(v, new LinkedList<String>());
                }
                reversedGraph.get(v).add(u);
            }
        }
        return reversedGraph;
    }

    /**
     * Compute Order Algo
     */
    private void computeOrderAlgo(Map<String, LinkedList<String>> G) {

        // Compute Gr = (V,Er).
        Map<String, LinkedList<String>> Gr = reverse(G);

        // Unmark every vertex of V .
        visited.clear();

        // counter = 0;
        counter = 0;

        // For every v in G
        for (String v : G.keySet()) {

            // if v is unmarked, Call FinishDFS(Gr,v).
            if (!visited.contains(v)) {
                finishDFS(Gr, v);
            }
        }
    }

    /**
     * FinishDFS(G,v)
     */
    private void finishDFS(Map<String, LinkedList<String>> G, String v) {

        //  Mark v;
        visited.add(v);

        // For every u such that <v,u> in E
        for (String u : G.get(v)) {

            // if u is unmarked, DFS(G, u).
            if (!visited.contains(u)) {
                finishDFS(G, u);
            }
        }

        // counter++;
        counter++;

        // FinishTime[v] = counter;
        finishTime.put(v, counter);
    }

    /**
     * SccDFS(G,v)
     */
    private void sccDFS(Map<String, LinkedList<String>> G, String v) {

        // Mark v;
        visited.add(v);

        // S = S union {v}.
        S.add(v);

        // For every u such that <v,u> in E
        for (String u : G.get(v)) {

            // if u is unmarked, DFS(G, u)
            if (!visited.contains(u)) {
                sccDFS(G, u);
            }
        }
    }

    /**
     * SCCAlgo: Returns a list of all the strongly connect components.
     */
    private ArrayList<Set<String>> SCCAlgo() {

        // Initialization.
        ArrayList<Set<String>> SCCs = new ArrayList<Set<String>>();
        visited = new HashSet<String>();
        finishTime = new HashMap<String, Integer>();

        // Call ComputeOrder(G).
        computeOrderAlgo(graph);

        // Let v1, v2, ··· vn be the ordering vertices of v
        //      such that FinishTime(vi) > FinishTime(vi+1).
        ArrayList<String> V = new ArrayList<String>(graph.keySet());
        Collections.sort(V, new Comparator<String>() {
            @Override
            public int compare(String v1, String v2) {
                return finishTime.get(v1) > finishTime.get(v2) ? -1 : 1;
            }
        });

        // Unmark every vertex of V.
        visited.clear();

        // for i in range [1,···,n],
        for (String v : V) {

            // if vi is unmarked
            if (!visited.contains(v)) {

                // Set S = EMPTY SET.
                S = new HashSet<String>();

                // SccDFS(G, vi).
                sccDFS(graph, v);

                // Output S.
                SCCs.add(S);
            }
        }
        return SCCs;
    }

    public static void main(String[] args) {
        GraphProcessor gf = new GraphProcessor("WikiCS.txt");
        System.out.println("Vertex with highest out degree: " + gf.vertexWithHighestOutDegree());
        System.out.println("Number of components of the graph: " + gf.numComponents());
        System.out.println("Size of the largest component: " + gf.largestComponent());
        System.out.println("SCC of /wiki/Hal_Abelson: " + gf.componentVertices("/wiki/Hal_Abelson"));
        System.out.println("sameComponent(/wiki/Hal_Abelson, /wiki/Unsupervised_learning): "
                + gf.sameComponent("/wiki/Hal_Abelson", "/wiki/Unsupervised_learning"));
        System.out.println("Path from /wiki/David_Kahn_(writer) to /wiki/Unsupervised_learning: \n  "
                + gf.bfsPath("/wiki/David_Kahn_(writer)", "/wiki/Unsupervised_learning"));
    }
}
