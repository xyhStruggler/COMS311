package cs311.hw7.graph;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;


public class Graph<V,E> implements IGraph<V,E>{

	private boolean directed;
    protected List<IGraph.Edge<E>> edges = new LinkedList<IGraph.Edge<E>>();  //edge.get(i).getVertex1 = s and edge.get(i).getVertex2 = k then edge from s -> k
    protected List<IGraph.Vertex<V>> vertices = new LinkedList<IGraph.Vertex<V>>();
	
    //Create a graph
    public Graph(){
    	super();
    }
    
    //Create a graph with a given vertex
    @SuppressWarnings({ "rawtypes", "unchecked" })
	public Graph(Vertex V){
    	super();
    	vertices.add(V);
    }
    
    //Create a graph with two given vertex and a edge between them
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public Graph(Vertex V1, Vertex V2, E ed){
    	super();
    	vertices.add(V1);
    	vertices.add(V2);
    	Edge E = new Edge(V1.getVertexName(), V2.getVertexName(), ed);
    	edges.add(E);
    }
	
	/**
     * Set the graph to be a directed graph.  Edge (x, y) is different than edge (y, x)
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
	public void setDirectedGraph() {
		E data = (E) new Object();
		for(int i=0; i<edges.size(); i++){
			String name1 = edges.get(i).getVertexName1();
			String name2 = edges.get(i).getVertexName2();
			Edge E = new Edge(name2, name1, edges.get(i).getEdgeData());
			for(int j=0; j <edges.size(); j++){
				if(edges.get(j).equals(E)){
					try {
						this.setEdgeData(name2, name1, data);
					} catch (cs311.hw7.graph.IGraph.NoSuchVertexException e) {
						e.printStackTrace();
					} catch (cs311.hw7.graph.IGraph.NoSuchEdgeException e) {
						e.printStackTrace();
					}
				}
			}
		}
		directed = true;
	}

	/**
     * Set the graph to be an undirected graph.  Edge (x, y) is in the graph
     * if and only if edge (y, x) is in the graph.  Note that when implementing this
     * and there are already edges defined in the graph, care must be taken to 
     * resolve conflicts and inconsistencies in the overall implementation.
     */
	public void setUndirectedGraph() {
    	int count = 0;
		for(int i=0; i<edges.size(); i++){
			String name1 = edges.get(i).getVertexName1();
			String name2 = edges.get(i).getVertexName2();
			for(int j=0; j <edges.size(); j++){
				if(edges.get(j).getVertexName1() == name2 && edges.get(j).getVertexName2() == name1){
					if(edges.get(j).getEdgeData() != edges.get(i).getEdgeData()){
						try {
							this.setEdgeData(name2, name1, edges.get(i).getEdgeData());
						} catch (cs311.hw7.graph.IGraph.NoSuchVertexException e) {
							e.printStackTrace();
						} catch (cs311.hw7.graph.IGraph.NoSuchEdgeException e) {
							e.printStackTrace();
						}
					}
				}
				else{
					count++;
				}
				if(count != edges.size()){
					this.addEdge(name2, name1, edges.get(i).getEdgeData());
				}
			}
		}
		directed = false;
	}

	/**
     * 
     * @return true if the graph is directed.
     */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public boolean isDirectedGraph() {
//		for(int i=0; i<edges.size(); i++){
//			String name1 = edges.get(i).getVertexName1();
//			String name2 = edges.get(i).getVertexName2();
//			Edge E = new Edge(name2, name1, edges.get(i).getEdgeData());
//			for(int j=0; j <edges.size(); j++){
//				if(edges.get(j).equals(E)){
//					return false;
//				}
//			}
//		}
		return directed;
	}

	/**
     * Adds a vertex to the graph with name given by the vertexName.  vertexNames,
     * must be unique in the graph.
     * 
     * @param vertexName The unique name of the vertex.
     * 
     * @throws cs311.hw6.graph.IGraph.DuplicateVertexException 
     */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void addVertex(String vertexName) throws DuplicateVertexException {
		int count = 0;
//		V d = (V) new Object();
		for(int i=0; i<vertices.size();i++){
			if(vertices.get(i).getVertexName() == vertexName){
				throw new DuplicateVertexException();
			}
			else count++;
		}
		if(count == vertices.size()){
			Vertex v = new Vertex(vertexName, "aaa");
			vertices.add(v);
		}
		
	}

	/**
     * Adds a vertex to the graph with name given by the vertexName.  vertexNames,
     * must be unique in the graph.  The vertexData of generic type is associated with
     * this vertex.
     * 
     * @param vertexName
     * @param vertexData
     * @throws cs311.hw6.graph.IGraph.DuplicateVertexException 
     */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void addVertex(String vertexName, Object vertexData) throws DuplicateVertexException {
		int count = 0;
		for(int i=0; i<vertices.size();i++){
			if(vertices.get(i).getVertexName() == vertexName){
				throw new DuplicateVertexException();
			}
			else count++;
		}
		if(count == vertices.size()){
			Vertex v = new Vertex(vertexName, vertexData);
			vertices.add(v);
		}
	}

	/**
     * Adds an edge to the graph by specifying the two vertices that comprise the 
     * edge.  If the graph is undirected then edge (x, y) or edge (y, x) may be used
     * to add the single edge.  If the graph is directed and edge (x,y) is added
     * followed by a subsequent edge (y, x), the later add would throw a 
     * DuplicateEdgeException.
     * 
     * @param vertex1 The first vertex in the edge.
     * @param vertex2 The second vertex in the edge.
     * 
     * @throws cs311.hw6.graph.IGraph.DuplicateEdgeException
     * @throws cs311.hw6.graph.IGraph.NoSuchVertexException 
     */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void addEdge(String vertex1, String vertex2) throws DuplicateEdgeException, NoSuchVertexException {
		int count = 0;
		E e = (E) new Object();
		for(int i=0;i<vertices.size();i++){
			if(vertices.get(i).getVertexName() == vertex1 || vertices.get(i).getVertexName() == vertex2){
				count++;
			}
		}
		if(count != 2){
			throw new NoSuchVertexException();
		}
		else{
			if(this.isDirectedGraph()){
				count = 0;
				for(int i=0;i<edges.size();i++){
					if(!(edges.get(i).getVertexName1() == vertex1 && edges.get(i).getVertexName2() == vertex2)){
						count++;
					}
				}
				if(count == edges.size()){
					Edge E = new Edge(vertex1,vertex2,e);
					edges.add(E);
				}
				else throw new DuplicateEdgeException();
			}
			else{
				count = 0;
				for(int i=0;i<edges.size();i++){
					if((edges.get(i).getVertexName1() == vertex1 && edges.get(i).getVertexName2() == vertex2)
							|| (edges.get(i).getVertexName1() == vertex2 && edges.get(i).getVertexName2() == vertex1)){
						throw new DuplicateEdgeException();
					}
					else{
						count++;
					}
				}
				if(count == edges.size()){
					Edge E = new Edge(vertex1,vertex2,e);
					edges.add(E);
				}
			}
		}
		
	}

	/**
     * Adds an edge to the graph by specifying the two vertices that comprise the 
     * edge.  If the graph is undirected then edge (x, y) or edge (y, x) may be used
     * to add the single edge.  If the graph is undirected and edge (x,y) is added
     * followed by a subsequent edge (y, x), the later add would throw a 
     * DuplicateEdgeException.  The edgeDaa parameter is used to associate generic
     * edge data with the edge.
     * 
     * @param vertex1 The first vertex in the edge.
     * @param vertex2 The second vertex in the edge.
     * @param edgeData The generic edge data.
     * 
     * @throws cs311.hw6.graph.IGraph.DuplicateEdgeException
     * @throws cs311.hw6.graph.IGraph.NoSuchVertexException 
     */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void addEdge(String vertex1, String vertex2, Object edgeData)
			throws DuplicateEdgeException, NoSuchVertexException {
		int count = 0;
		for(int i=0;i<vertices.size();i++){
			if(vertices.get(i).getVertexName() == vertex1 || vertices.get(i).getVertexName() == vertex2){
				count++;
			}
		}
		if(count != 2){
			throw new NoSuchVertexException();
		}
		else{
			if(this.isDirectedGraph()){
				count = 0;
				for(int i=0;i<edges.size();i++){
					if(!(edges.get(i).getVertexName1() == vertex1 && edges.get(i).getVertexName2() == vertex2)){
						count++;
					}
				}
				if(count == edges.size()){
					Edge E = new Edge(vertex1,vertex2,edgeData);
					edges.add(E);
				}
			}
			else{
				count = 0;
				for(int i=0;i<edges.size();i++){
					if((edges.get(i).getVertexName1() == vertex1 && edges.get(i).getVertexName2() == vertex2)
							|| (edges.get(i).getVertexName1() == vertex2 && edges.get(i).getVertexName2() == vertex1)){
						throw new DuplicateEdgeException();
					}
					else{
						count++;
					}
				}
				if(count == edges.size()){
					Edge E = new Edge(vertex1,vertex2,edgeData);
					edges.add(E);
				}
			}
		}
		
	}

	/**
     * Returns the generic vertex data associated with the specified vertex.  If no
     * vertex data is associated with the vertex, then null is returned.
     * 
     * @param vertexName  Name of vertex to get data for
     * 
     * @return The generic vertex data
     * 
     * @throws cs311.hw6.graph.IGraph.NoSuchVertexException 
     */
	public V getVertexData(String vertexName) throws NoSuchVertexException {
		int count = 0;
		for(int i=0;i<vertices.size();i++){
			if(vertices.get(i).getVertexName() == vertexName){
				return vertices.get(i).getVertexData();
			}
			else count++;
		}
		if(count == vertices.size()){
			throw new NoSuchVertexException();
		}
		return null;
	}

	/**
     * Sets the generic vertex data of the specified vertex.
     * 
     * @param vertexName The name of the vertex.
     * 
     * @param vertexData The generic vertex data.
     * 
     * @throws cs311.hw6.graph.IGraph.NoSuchVertexException 
     */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void setVertexData(String vertexName, Object vertexData) throws NoSuchVertexException {
		int count = 0;
		for(int i=0;i<vertices.size();i++){
			if(vertices.get(i).getVertexName() == vertexName){
				if(!(vertices.get(i).getVertexData().equals(vertexData))){
					Vertex V = new Vertex(vertexName,vertexData);
					vertices.remove(i);
					vertices.add(i, V);
				}
			}
			else count++;
		}
		if(count == vertices.size()){
			throw new NoSuchVertexException();
		}
		
	}

	/**
     * Returns the generic edge data associated with the specified edge.  If no
     * edge data is associated with the edge, then null is returned.
     * 
     * @param vertex1 Vertex one of the edge.
     * @param vertex2 Vertex two of the edge.
     * 
     * @return The generic edge data.
     * 
     * @throws cs311.hw6.graph.IGraph.NoSuchVertexException
     * @throws cs311.hw6.graph.IGraph.NoSuchEdgeException 
     */
	public E getEdgeData(String vertex1, String vertex2) throws NoSuchVertexException, NoSuchEdgeException {
		int count = 0;
		for(int i=0;i<vertices.size();i++){
			if(vertices.get(i).getVertexName() == vertex1 || vertices.get(i).getVertexName() == vertex2){
				count++;
			}
		}
		if(count != 2){
			throw new NoSuchVertexException();
		}
		else{
			count = 0;
			for(int i=0;i<edges.size();i++){
				if(!this.isDirectedGraph()){
					if((edges.get(i).getVertexName1() == vertex1 && edges.get(i).getVertexName2()==vertex2) 
							||(edges.get(i).getVertexName1() == vertex2 && edges.get(i).getVertexName2()==vertex1)){
						return edges.get(i).getEdgeData();
					}
					else count++;
				}
				else{
					if((edges.get(i).getVertexName1() == vertex1 && edges.get(i).getVertexName2()==vertex2)){
						return edges.get(i).getEdgeData();
					}
					else count++;
				}
			}
			if(count == edges.size()) throw new NoSuchEdgeException();
		}
		return null;
	}

	/**
     * Sets the generic edge data of the specified edge.
     * 
     * @param vertex1  Vertex one of the edge.
     * @param vertex2  Vertex two of the edge.
     * 
     * @param edgeData The generic edge data.
     * 
     * @throws cs311.hw6.graph.IGraph.NoSuchVertexException
     * @throws cs311.hw6.graph.IGraph.NoSuchEdgeException 
     */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void setEdgeData(String vertex1, String vertex2, Object edgeData)
			throws NoSuchVertexException, NoSuchEdgeException {
		int count = 0;
		for(int i=0;i<vertices.size();i++){
			if(vertices.get(i).getVertexName() == vertex1 || vertices.get(i).getVertexName() == vertex2){
				count++;
			}
		}
		if(count != 2){
			throw new NoSuchVertexException();
		}
		else{
			count = 0;
			for(int i=0;i<edges.size();i++){
				if(edges.get(i).getVertexName1() == vertex1 && edges.get(i).getVertexName2()==vertex2){
					if(!edges.get(i).getEdgeData().equals(edgeData)){
						Edge E = new Edge(vertex1,vertex2,edgeData);
						edges.remove(i);
						edges.add(i,E);
					}
				}
				else count++;
			}
			if(count == edges.size()) throw new NoSuchEdgeException();
		}
	}

	/**
     * Returns an encapsulated Vertex data type based on the vertex name
     * 
     * @param VertexName The name of the vertex.
     * 
     * @return The encapsulated vertex. 
     */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Vertex getVertex(String VertexName) {
		for(int i=0;i<vertices.size();i++){
			if(vertices.get(i).getVertexName() == VertexName){
				return vertices.get(i);
			}
		}
		return null;
	}

	/**
     * Returns an encapsulated Edge data type based on the specified edge.
     * 
     * @param vertexName1 Vertex one of edge.
     * @param vertexName2 Vertex two of edge.
     * 
     * @return Encapsulated edge.
     */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Edge getEdge(String vertexName1, String vertexName2) {
		for(int i=0;i<edges.size();i++){
			if(edges.get(i).getVertexName1() == vertexName1 && edges.get(i).getVertexName2() == vertexName2){
				return edges.get(i);
			}
		}
		return null;
	}

	/**
     * Returns a list of all the vertices in the graph.
     * 
     * @return The List<Vertex> of vertices.
     */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List getVertices() {
		return vertices;
	}

	/**
     * Returns all the edges in the graph.
     * 
     * @return The List<Edge<E>> of edges. 
     */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List getEdges() {
		return edges;
	}

	 /**
     * Returns all the neighbors of a specified vertex.
     * 
     * @param vertex The vertex to return neighbors for.
     * 
     * @return The list of vertices that are the neighbors of the specified vertex.
     */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List getNeighbors(String vertex) {
		List<Vertex<V>> neighbors = new LinkedList<Vertex<V>>();
		for(int i=0;i<edges.size();i++){
			if(this.isDirectedGraph()){
				if(edges.get(i).getVertexName1() == vertex){
					neighbors.add(this.getVertex(edges.get(i).getVertexName2()));
				}
			}
			else{
				if(edges.get(i).getVertexName1() == vertex){
					neighbors.add(this.getVertex(edges.get(i).getVertexName2()));
				}
				if(edges.get(i).getVertexName2() == vertex){
					neighbors.add(this.getVertex(edges.get(i).getVertexName1()));
				}
			}
		}
		return neighbors;
	}
	
	public void removeEdge(int i){
		edges.remove(i);
	}
	public void removeVertex(int i){
		vertices.remove(i);
	}

}
