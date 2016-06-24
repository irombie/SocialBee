package neo4j;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Result;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;


import scala.collection.immutable.List;
import scala.collection.mutable.HashSet;


//http://neo4j.com/developer/guide-import-csv/


public class Operation {

	final static String DBPATH="/home/odayibas/Desktop/Staj/neo4j-community-3.0.3/data/databases/graph.db";   
	final static String FILEPATH="/home/odayibas/Desktop/Staj/neo4j-community-3.0.3/data/databases/den.txt";
	static GraphDatabaseService service;
	public static void main(String[] args) throws IOException {
			
		
		
		
		
	}
	public static void initialize(){
		GraphDatabaseFactory graphDbFactory = new GraphDatabaseFactory();
		File db = new File(DBPATH);
		service = graphDbFactory.newEmbeddedDatabase(db);
	}
	public static GraphData shortestPath(String query,ArrayList<GraphNode> nodes,ArrayList<GraphEdge> edges){
		Result result = service.execute(query);
		while(result.hasNext()){
			Map<String,Object> map = result.next();
			Set<String> keys = map.keySet();
			for (String key : keys){
				
			}
		}
		return null;
	}
	public static GraphData pullMovies (String query,ArrayList<GraphNode> nodes){
		Result result = service.execute(query);
		while(result.hasNext()){
			Map<String,Object> map = result.next();
			Set<String> keys = map.keySet();
			
			for (String key : keys){
				Node node = (Node) map.get(key);			
				int id = Integer.parseInt(String.valueOf(node.getId()));

				Map<String, Object> valueMap = node.getAllProperties();
				Set<String> valueMapKeys = valueMap.keySet();
				
				Movie movie = new Movie();
				MovieData dt = new MovieData();
				dt.setId(id);
				dt.setType("movie");
				for (String valueMapKey : valueMapKeys) {
					if(valueMapKey.equals("tagline"))
						dt.setTagLine((String) valueMap.get(valueMapKey));
					if(valueMapKey.equals("title"))
						dt.setTitle((String) valueMap.get(valueMapKey));
					else
						dt.setReleased(String.valueOf(valueMap.get(valueMapKey)));
				}
				movie.setData(dt);
				nodes.add(movie);
			}
			
		}
		GraphData data = new GraphData();
		data.setNodes(nodes);
		return data;
	}
	public static GraphData movieNetwork(String query,ArrayList<GraphNode> nodes,ArrayList<GraphEdge> edges,String firstRelation,String secondRelation ,String nameOfMovie) throws IOException{
		Result result = service.execute(query);
		HashSet<Integer> ids = new HashSet<Integer>();//Node ids set for dupplication check
		
		while(result.hasNext()){
			Map<String, Object> map = result.next();//One row
			Set<String> keys = map.keySet();
			
			for (String key : keys) {
				Node node = (Node) map.get(key);		
				int id = Integer.parseInt(String.valueOf(node.getId()));
				
				if(!ids.contains(id))
					ids.add(id);
				else
					continue;//Newly coming node is a dupplicate
				
				Map<String, Object> valueMap = node.getAllProperties();				
				Set<String> valueMapKeys = valueMap.keySet();
				NodeKey nodeKey = NodeKey.valueOf(key);
				switch (nodeKey){
				case a:
					Actor actor = new Actor();
					ActorData dt = new ActorData();
					dt.setId(id);
					dt.setType("actor");
					for (String valueMapKey : valueMapKeys) {
						if(valueMapKey.equals("name"))
							dt.setName((String) valueMap.get(valueMapKey));
						else
							dt.setBorn(String.valueOf(valueMap.get(valueMapKey))); 
					}
					actor.setData(dt);
					nodes.add(actor);
					break;
				case d:
					Director director = new Director();
					DirectorData dt2 = new DirectorData();
					dt2.setId(id);
					dt2.setType("director");
					for (String valueMapKey : valueMapKeys) {
						if(valueMapKey.equals("name"))
							dt2.setName((String) valueMap.get(valueMapKey));
						else
							dt2.setBorn(String.valueOf(valueMap.get(valueMapKey))); 
					}
					director.setData(dt2);
					nodes.add(director);
					break;
				case m:
					Movie movie = new Movie();
					MovieData dt3 = new MovieData();
					dt3.setId(id);
					dt3.setType("movie");
					for (String valueMapKey : valueMapKeys) {
						if(valueMapKey.equals("tagline"))
							dt3.setTagLine((String) valueMap.get(valueMapKey));
						if(valueMapKey.equals("title"))
							dt3.setTitle((String) valueMap.get(valueMapKey));
						else
							dt3.setReleased(String.valueOf(valueMap.get(valueMapKey)));
					}
					movie.setData(dt3);
					nodes.add(movie);
					break;
				default:
					IOException e = new IOException();
					throw e;
				}			
			}
		}
		
		//Add edges for nodes
		for(GraphNode node: nodes){
				if(node instanceof Actor){
					GraphEdge edge = new GraphEdge();
					GraphEdgeData dt = new GraphEdgeData();
					dt.setSource(((Actor) node).getData().getId());
					int idMovie = findMovie(nameOfMovie, nodes);
					if(idMovie!=-1){
						dt.setTarget(idMovie);
					}
					else{
						IOException e = new IOException();
						throw e;
					}
					dt.setType(firstRelation);
					edge.setData(dt);
					edges.add(edge);
				}
				if(node instanceof Director){
					GraphEdge edge = new GraphEdge();
					GraphEdgeData dt = new GraphEdgeData();
					dt.setSource(((Director) node).getData().getId());
					int idMovie = findMovie(nameOfMovie, nodes);
					if(idMovie!=-1){
						dt.setTarget(idMovie);
					}
					else{
						IOException e = new IOException();
						throw e;
					}
					dt.setType(secondRelation);
					edge.setData(dt);
					edges.add(edge);
				}
		}
		
		GraphData data = new GraphData();
		data.setNodes(nodes);
		data.setEdges(edges);
		return data;
	}
	static void clearDB(){		
		service.execute("MATCH (n) DETACH DELETE n");
		System.out.println("Database Cleared");
	}
	static int findMovie(String title,ArrayList<GraphNode> nodes){
		int id=-1;
		
		for (GraphNode node: nodes){
			if(node instanceof Movie)
				if(((Movie) node).getData().getTitle().equals(title))
					id = ((Movie) node).getData().getId();
		}
		return id;
		
	}
}
