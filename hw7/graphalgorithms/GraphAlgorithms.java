package cs311.hw7.graphalgorithms;

import cs311.hw7.graph.IGraph;
import cs311.hw7.graph.IGraph.Edge;
import cs311.hw7.graph.IGraph.Vertex;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author yuhanxiao
 */
public class GraphAlgorithms
{
	
	
    public static <V, E> List<Vertex<V>> TopologicalSort( IGraph<V, E> g)
    {
    	if(!g.isDirectedGraph()){
    		g.setDirectedGraph();
    	}
    	List<Vertex<V>> graph = g.getVertices();
    	int n = graph.size();
        boolean[] visited = new boolean[n];
        int[] degree = new int[n];
        List<Vertex<V>> res = new ArrayList<Vertex<V>>();
        for (int i = 0; i < n; i++){
          if (!visited[i]&&degree[i]==0){
        	  degree[i]--;
        	  dfs(g, graph, visited, res, i,degree);
          }
        }
        Collections.reverse(res);
        return res;
    }
    
    public static <V, E> List<List<Vertex<V>>> AllTopologicalSort( IGraph<V, E> g )
    {
    	List<List<Vertex<V>>> list = new ArrayList<List<Vertex<V>>>();
    	for(int i=0;i<g.getVertices().size();i++){
    		
    		if(!contain(list,TopologicalSort(g))){
    			list.add(TopologicalSort(g));
    		}
    	}
        return list;
    }
    
    public static <V, E extends IWeight> IGraph<V, E> Kruscal(IGraph<V, E> g )
    {
    	List<Edge<E>> edge = g.getEdges();
    	for(int i=0;i<edge.size()-1;i++){
    		for(int j=0;j<edge.size()-1-i;j++){
    			if(edge.get(j).hashCode()>edge.get(j+1).hashCode()){
    				Edge temp = edge.get(j);
    				edge.remove(j);
    				edge.add(j, edge.get(j+1));
    				edge.remove(j+1);
    				edge.add(j+1, temp);
    			}
    		}
    	}
    	
    	return g;
    }
    
    
	//depth-first search logic
    private static <V,E> void dfs(IGraph<V, E> g, List<Vertex<V>> graph, boolean[] used, List<Vertex<V>> res, int u, int[] degree) {
        used[u] = true;
        for(int i=0;i<graph.size();i++){
        	List<Vertex<V>> neighbors = g.getNeighbors(graph.get(i).getVertexName());
        	for(int j=0;j<neighbors.size();j++){
        		String name = neighbors.get(j).getVertexName();
        		int index = getIndex(graph,name);
        		if(!used[index]){
        			degree[index]--;
            		dfs(g, graph,used,res,index, degree);
            	}
        	}
        	
        }
        res.add(graph.get(u));
      }
    
    
    
    //find the index of a vertex
    private static <V> int getIndex(List<Vertex<V>> graph, String name){
    	for(int i=0;i<graph.size();i++){
    		if(name == graph.get(i).getVertexName()){
    			return i;
    		}
    	}
    	return 0;
    }
    //contain
    private static <V,E> Boolean contain( List<List<Vertex<V>>> list, List<Vertex<V>> v){
    	for(int i=0;i<list.size();i++){
    		if(list.get(i).equals(v)){
    			return true;
    		}
    	}
    	return false;
    }
    
}