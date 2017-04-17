package cs311.hw8.graphalgorithms;

import cs311.hw8.graph.IGraph;
import cs311.hw8.graph.IGraph.Edge;
import cs311.hw8.graph.IGraph.Vertex;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author yuhanxiao
 * @param <V>
 * @param <E>
 */
public class GraphAlgorithms
{


	public static <V, E extends IWeight> List<Edge<E>> ShortestPath(
			IGraph<V, E> g, String vertexStart, String vertexEnd){
			//setup
			List<Edge<E>> edges = new ArrayList<Edge<E>>(g.getEdges());
			Set<Vertex<V>> settledNodes = new HashSet<Vertex<V>>();
			Set<Vertex<V>> unSettledNodes = new HashSet<Vertex<V>>();
			Map<Vertex<V>, Vertex<V>> predecessors = new HashMap<Vertex<V>, Vertex<V>>();
			Map<Vertex<V>, Double> distance = new HashMap<Vertex<V>, Double>();

	        //execute
	        Vertex<V> source = g.getVertex(vertexStart);
	        distance.put(source, 0.0);
	        unSettledNodes.add(source);
	        while (unSettledNodes.size() > 0) {
                Vertex<V> node = null;
                Vertex<V> minimum = null;
                for (Vertex<V> vertex : unSettledNodes) {
                        if (minimum == null) {
                                minimum = vertex;
                        } else {
                        		Double d = distance.get(vertex);
                        		Double m = distance.get(minimum);
                                if (d < m) {
                                        minimum = vertex;
                                }
                        }
                }
                node = minimum;
                settledNodes.add(node);
                unSettledNodes.remove(node);
                List<Vertex<V>> adjacentNodes = g.getNeighbors(node.getVertexName());
                for (Vertex<V> target : adjacentNodes) {
                	Double t = distance.get(target);
                	Double n = distance.get(node);
                	if(t == null ){
                		t = (double) Integer.MAX_VALUE;
                	}
                	if(n == null ){
                		n = (double) Integer.MAX_VALUE;
                	}
                	Double d = 0.0;
                	for (Edge<E> edge : edges) {
                        if (edge.getVertexName1().equals(node.getVertexName()) && edge.getVertexName2().equals(target.getVertexName())) {
                                d = (Double) edge.getEdgeData().getWeight();
                                break;
                        }
                	}
                    if (t > n + d) {
                    	distance.put(target, n + d);
                    	predecessors.put(target, node);
                    	unSettledNodes.add(target);
                        }
                }
	        }
	        LinkedList<Vertex<V>> path = new LinkedList<Vertex<V>>();
            Vertex<V> step = g.getVertex(vertexEnd);
            if (predecessors.get(step) == null) {
                    return null;
            }
            path.add(step);
            while (predecessors.get(step) != null) {
                    step = predecessors.get(step);
                    path.add(step);
            }
            Collections.reverse(path);

            //End
            LinkedList<Edge<E>> sp = new LinkedList<Edge<E>>();
	        for(int i=0;i<path.size()-1;i++){
	        	for(Edge<E> e : edges){
	        		if(e.getVertexName1().equals(path.get(i).getVertexName())
	        				&& e.getVertexName2().equals(path.get(i+1).getVertexName())){
	        			sp.add(e);
	        		}
	        	}
	        }
	        for(Edge<E> e : sp){
	        	System.out.println(e.getVertexName1() + "  " + e.getVertexName2());
	        }
	        System.out.println("");

			return sp;
	}
}
