package cs311.hw8.graphalgorithms;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import cs311.hw8.graph.Graph;
import cs311.hw8.graph.IGraph.Edge;
import cs311.hw8.graph.IGraph.Vertex;

import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.io.*;

/**
 * 
 * @author yuhanxiao
 *
 * @param <Location>
 * @param <Road>
 */
public class OSMMap<Location,Road> {

	/**
	 * Main2 for total distance
	 */
	public static void main2(String[] args){
		OSMMap<Location,Road> o = new OSMMap<Location,Road>();
		o.LoadMap("AmesMap.txt");
		double TotalDistance = o.TotalDistance();
		System.out.println(TotalDistance);
	}
	/**
	 * Main3 for routes 
	 * @param args
	 */
	@SuppressWarnings("resource")
	public static void main3(String[] args){
		List<String> vertaxNames = new LinkedList<String>();
		List<Location> locations = new LinkedList<Location>();
		List<String> Route = new LinkedList<String>();
		OSMMap<Location,Road> o = new OSMMap<Location,Road>();
		String mapFilename = args[0];
		String routFilename = args[1];
		o.LoadMap(mapFilename);
		Scanner input = new Scanner(System.in);
        File file = new File(routFilename);
        try {
			input = new Scanner(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
        while (input.hasNextLine()) {
            String line = input.nextLine();
            String[] parts = line.split("\\s+");
            //for test
            System.out.println(parts[0] + " " + parts[1]);
            Location l = new Location(Double.parseDouble(parts[0]),Double.parseDouble(parts[1]));
            locations.add(l);
        }
        //end reading the file
        input.close();
        int size = locations.size()-1;
        for(int i=0;i<size;i++){

        		List<String> names = new LinkedList<String>();
        		names = ShortestRoute(locations.get(i), locations.get(i+1));
        		for(String s : names){
        			if(s!=null){
        				vertaxNames.add(s);
        			}
        		}

        }
        Route = StreetRoute(vertaxNames);
        //print out the Street Route
        for(String s : Route){
        	System.out.println(s);
        }
	}

	private static Graph<Location,Road> g;
	public static Graph<Location,Road> getGraph(){
		return g;
	}


	/**
	 * represents map data
	 */
	public OSMMap(){
		OSMMap.g = new Graph<Location,Road>();
	}

	/**
	 *  loads an XML OSM file into a graph
	 *  @param filename
	 */
	public void LoadMap(String filename){
		try{
			File fXmlFile = new File(filename);
			DocumentBuilderFactory Factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder Builder = Factory.newDocumentBuilder();
			Document doc = Builder.parse(fXmlFile);
			doc.getDocumentElement().normalize();

			//clean previous map data if exist
			if(!check(g)){
				g = new Graph<Location,Road>();
			}


			//Load vertexes
			NodeList List = doc.getElementsByTagName("node");
			int length = List.getLength();
			for (int temp = 0; temp < length; temp++) {
				Node node = List.item(temp);
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					Element element = (Element) node;
					String name = element.getAttribute("id");
					double lat = Double.parseDouble(element.getAttribute("lat"));
					double lon = Double.parseDouble(element.getAttribute("lon"));
					Location location = new Location(lat,lon);
					g.addVertex(name,  location);
					//for test. Showing process
					Location l = (Location) g.getVertex(name).getVertexData();
					System.out.println("Adding vertex: " + name + " "+ l.getLatitude() + " " + l.getLongitude());

				}
			}

			//load edges
			List = doc.getElementsByTagName("way");
			//for each "way"
			for (int temp = 0; temp < List.getLength(); temp++) {
				Node node = List.item(temp);
				String name = "";
				boolean highway = false;
				boolean hasname = false;
				boolean oneway = false;
				List<String> vertexes = new ArrayList<String>();
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					Element element = (Element) node;
					//for each tag in each "way"
					int len = element.getChildNodes().getLength();
					for(int i = 0; i < len; i++){

						if(element.getChildNodes().item(i).getAttributes() != null){
							//for each attributes in each tag
							int le = element.getChildNodes().item(i).getAttributes().getLength();
							for(int j = 0; j < le; j++){
								if(element.getChildNodes().item(i) != null && element.getChildNodes().item(i).getAttributes().item(j)!=null){
									Node check = element.getChildNodes().item(i).getAttributes().item(j);

									//add vertex name if it begins with "ref="
									if(check.toString().substring(0, 4).equals("ref=")){
										String vname = check.toString().substring(5,check.toString().length()-1);
										vertexes.add(vname);

									}
									//check if it is highway and has a name
									else if(check.toString().substring(0, 2).equals("k=")){
//										System.out.println(check.toString().substring(3,check.toString().length()-1));
										//check if it is highway
										if(check.toString().substring(3,check.toString().length()-1).equals("highway")){
											highway = true;
										}
										//check if it has a name
										if(check.toString().substring(3,check.toString().length()-1).equals("name")){
											hasname = true;
											String tem = element.getChildNodes().item(i).getAttributes().item(j+1).toString();
											name = tem.substring(3,tem.length()-1);
										}
										//check if it is one way
										if(check.toString().substring(3,check.toString().length()-1).equals("oneway")){
											String tem = element.getChildNodes().item(i).getAttributes().item(j+1).toString();
											String s = tem.substring(3,tem.length()-1);
											if(s.equals("yes")) oneway = true;
										}
									}
								}
							}
						}
					}
				}
				if(highway && hasname){
					int size = vertexes.size()-1;
					for(int i = 0; i< size; i++){
						Vertex<Location> one = g.getVertex(vertexes.get(i));
						Vertex<Location> two = g.getVertex(vertexes.get(i+1));
						double lat1 = ((Location) one.getVertexData()).getLatitude();
						double lon1 = ((Location) one.getVertexData()).getLongitude();
						double lat2 = ((Location) two.getVertexData()).getLatitude();
						double lon2 = ((Location) two.getVertexData()).getLongitude();
						double distance = distance(lat1,lon1,lat2,lon2,"M");
						if(oneway){
							g.addEdge(vertexes.get(i), vertexes.get(i+1),  new Road(name,distance));
						}
						else{
							g.addEdge(vertexes.get(i), vertexes.get(i+1),  new Road(name,distance));
							g.addEdge(vertexes.get(i+1), vertexes.get(i),  new Road(name,distance));
						}
						//for test. Showing process
						System.out.println("Adding Edge: "+name+"  vertex1:  "+ vertexes.get(i) + "  vertex2:  "+vertexes.get(i+1)
						+ "  distance: "+distance);
					}
				}
			}
		}
		catch (Exception e){
			e.printStackTrace();
		}
	}

	/**
	 * outputs the approximate miles of roadway in the map
	 */
	public double TotalDistance(){
		double tDistance = 0.0;
		List<Edge<Road>> edges = new ArrayList<Edge<Road>>(g.getEdges());
		for(Edge<Road> edge : edges){
			double distance = ((Road) edge.getEdgeData()).getDistance();
			tDistance += distance;
		}
		tDistance = tDistance/2;
		return tDistance;
	}


	/**
	 * returns the vertex ID that is closest to the specified
	 * latitude and longitude.
	 * @param location
	 * @return
	 */
	public static String ClosestRoad(Location location){
		double min = Double.MAX_VALUE;
		String name = "";
		List<Vertex<Location>> nodes = new ArrayList<Vertex<Location>>(findVertexes(g.getEdges()));
		for(Vertex<Location> vertex : nodes){
			double lat = ((Location) vertex.getVertexData()).getLatitude();
			double lon = ((Location) vertex.getVertexData()).getLongitude();
			double distance = distance(location.getLatitude(),location.getLongitude(),lat,lon,"M");
			if(min>=distance){
				min = distance;
				name = vertex.getVertexName();
			}
		}
		return name;
	}

	/**
	 * returns a list of String types that is the sequence of vertex ID names that gives the path
	 * @param fromLocation
	 * @param toLocation
	 * @return
	 */
	public static List<String> ShortestRoute(Location fromLocation, Location toLocation){
		//get closest vertex from the locations
		String vertexStart = ClosestRoad(fromLocation);
		String vertexEnd = ClosestRoad(toLocation);

		//setup
		List<Edge<Road>> edges = new ArrayList<Edge<Road>>(g.getEdges());
		Set<Vertex<Location>> settledNodes = new HashSet<Vertex<Location>>();
		Set<Vertex<Location>> unSettledNodes = new HashSet<Vertex<Location>>();
		Map<Vertex<Location>, Vertex<Location>> predecessors = new HashMap<Vertex<Location>, Vertex<Location>>();
		Map<Vertex<Location>, Double> distance = new HashMap<Vertex<Location>, Double>();

        //execute
        Vertex<Location> source = g.getVertex(vertexStart);
        distance.put(source, 0.0);
        unSettledNodes.add(source);
        while (unSettledNodes.size() > 0) {
            Vertex<Location> node = null;
            Vertex<Location> minimum = null;
            for (Vertex<Location> vertex : unSettledNodes) {
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
            Graph<Location,Road> graph = OSMMap.getGraph();
            List<Vertex<Location>> adjacentNodes = graph.getNeighbors(node.getVertexName());
            for (Vertex<Location> target : adjacentNodes) {
            	Double t = distance.get(target);
            	Double n = distance.get(node);
            	if(t == null ){
            		t = (double) Integer.MAX_VALUE;
            	}
            	if(n == null ){
            		n = (double) Integer.MAX_VALUE;
            	}
            	Double d = 0.0;
            	for (Edge<Road> edge : edges) {
                    if (edge.getVertexName1().equals(node.getVertexName()) && edge.getVertexName2().equals(target.getVertexName())) {
                            d = ((Road) edge.getEdgeData()).getDistance();
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
        LinkedList<Vertex<Location>> path = new LinkedList<Vertex<Location>>();
        Vertex<Location> step = g.getVertex(vertexEnd);
        if (predecessors.get(step) == null) {
                return null;
        }
        path.add(step);
        while (predecessors.get(step) != null) {
                step = predecessors.get(step);
                path.add(step);
        }
        Collections.reverse(path);
        List<String> pathName = new LinkedList<String>();
        for(Vertex<Location> vertex : path){
        	pathName.add(vertex.getVertexName());
        }
        return pathName;
	}


	/**
	 * return the list of vertexes that has edges
	 */
	private static List<Vertex<Location>> findVertexes(List<Edge<Road>> edges){
		List<Vertex<Location>> list = new LinkedList<Vertex<Location>>();
		for(Edge<Road> e : edges){
			list.add(g.getVertex(e.getVertexName1()));
		}
		return list;
	}

	/**
	 * returns a List<String> of street names from the beginning location to the end location.
	 * @param VertexNames
	 * @return
	 */
	public static List<String> StreetRoute(List<String> VertexNames){
		List<String> StreetRoute = new LinkedList<String>();
		LinkedList<Edge<Road>> sp = new LinkedList<Edge<Road>>();
		List<Edge<Road>> edges = new ArrayList<Edge<Road>>(g.getEdges());
		int size = VertexNames.size()-1;
        for(int i=0;i<size-1;i++){
        	for(Edge<Road> e : edges){
        		if(e.getVertexName1().equals(VertexNames.get(i))
        				&& e.getVertexName2().equals(VertexNames.get(i+1))){
        			sp.add(e);
        		}
        	}
        }
        for(Edge<Road> e : sp){
        	String name = ((Road) e.getEdgeData()).getName();
        	if(!StreetRoute.contains(name)){
        		StreetRoute.add(name);
        	}
        }
		return StreetRoute;
	}
	/**
	 * a sequence of
	 * vertices where the first node is the start node, and every other node is visited
	 * exactly once and ends back at the start node.
	 * @param list
	 * @return
	 */
	public static List<Vertex<Location>> ApproximateTSP(List<String> list){
		List<Vertex<Location>> nodes = new ArrayList<Vertex<Location>>();
		List<Vertex<Location>> output = new ArrayList<Vertex<Location>>();
		int i = 0;
		for(String s : list){
			nodes.add(g.getVertex(s));
		}
		for(Vertex<Location> vertex : nodes){
			if(i == 0){
				output.add(vertex);
				nodes.remove(vertex);
				i++;
			}
			else{
				double min = Double.MAX_VALUE;
				int temp = 0;
				Location l = output.get(output.size()).getVertexData();
				for(int j=0;j<nodes.size();j++){
					double dis = distance(l.getLatitude(),l.getLongitude(),nodes.get(j)
							.getVertexData().getLatitude(),nodes.get(j).getVertexData().getLongitude(),"M");
					if(min<dis){
						min = dis;
						temp = j;
					}
				}
				output.add(nodes.get(temp));
				nodes.remove(temp);
			}
		}
		return output;
	}




	/**
	 * Calculate the distance between two point on earth with latitude and longitude
	 *
	 * Copied from http://www.geodatasource.com/developers/java
	 * @param lat1
	 * @param lon1
	 * @param lat2
	 * @param lon2
	 * @param unit
	 * @return
	 */
	static double distance(double lat1, double lon1, double lat2, double lon2, String unit) {
		double theta = lon1 - lon2;
		double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
		dist = Math.acos(dist);
		dist = rad2deg(dist);
		dist = dist * 60 * 1.1515;
		if (unit == "K") {
			dist = dist * 1.609344;
		} else if (unit == "N") {
			dist = dist * 0.8684;
		}

		return (dist);
	}
	/**
	 * This function converts decimal degrees to radians
	 *
	 * Copied from http://www.geodatasource.com/developers/java
	 * @param deg
	 * @return
	 */
	private static double deg2rad(double deg) {
		return (deg * Math.PI / 180.0);
	}
	/**
	 * This function converts radians to decimal degrees
	 *
	 * Copied from http://www.geodatasource.com/developers/java
	 * @param rad
	 * @return
	 */
	private static double rad2deg(double rad) {
		return (rad * 180 / Math.PI);
	}

	/**
	 * check if the data is empty
	 * @param g
	 * @return
	 */
	private Boolean check(Graph<Location,Road> g){
		List<cs311.hw8.graph.IGraph.Vertex<Location>> nodes = g.getVertices();
		List<cs311.hw8.graph.IGraph.Edge<Road>> edges = g.getEdges();
		if(!(nodes.isEmpty() || edges.isEmpty())){
			return false;
		}
		else return true;
	}




	//inner class location present latitude and longitude
	public static class Location
	{
		private double latitude;
		private double longitude;

		public Location(double la, double lo){
			latitude = la;
			longitude = lo;
		}
		// returns the latitude of the location
		public double getLatitude(){
			return latitude;
		}
		// returns the longitude of the location
		public double getLongitude(){
			return longitude;
		}
	}

	//inner class road present the length and name of the road
	public static class Road
	{
		private String name;
		private double distance;

		public Road(String name, double distance){
			this.name = name;
			this.distance = distance;
		}
		// returns the latitude of the location
		public String getName(){
			return name;
		}
		// returns the longitude of the location
		public double getDistance(){
			return distance;
		}
	}
}
