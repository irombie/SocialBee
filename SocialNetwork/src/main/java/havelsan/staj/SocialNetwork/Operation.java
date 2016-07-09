package havelsan.staj.SocialNetwork;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Result;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.kernel.impl.core.NodeProxy;

import scala.collection.Seq;
import scala.collection.convert.Wrappers.SeqWrapper;
import scala.collection.mutable.HashSet;


//http://neo4j.com/developer/guide-import-csv/


public class Operation {

	final static String DBPATH="/home/odayibas/Desktop/Staj/neo4j-community-3.0.3/data/databases/graph.db";
	static GraphDatabaseService service;
	public static void main(String[] args) throws IOException {
			
		
	}
	public static void initialize(){
		GraphDatabaseFactory graphDbFactory = new GraphDatabaseFactory();
		File db = new File(DBPATH);
		service = graphDbFactory.newEmbeddedDatabase(db);
	}
	public static AnalyzeData degreeAnalysis(String query){
		Result result = service.execute(query);
		
		
		AnalyzeData dt = new AnalyzeData();
		ArrayList<AnalyzeNode> data = new ArrayList<AnalyzeNode>();
		while(result.hasNext()){
			Map<String,Object> map = result.next();
			Set<String> keys = map.keySet();
			AnalyzeNode nd = new AnalyzeNode();
			for (String key : keys){
				if(key.equals("title")){
					nd.setTitle((String) map.get(key));
				}
				else{
					Object obj = map.get(key);
					String str = obj.toString();
					nd.setScore(Double.parseDouble(str));
				}
			}
			data.add(nd);
		}
		dt.setData(data);
		return dt;
	}
	public static GraphData shortestPath(String query) throws IOException{
		Result result = service.execute(query);
		ArrayList<GraphNode> nodes = new ArrayList<GraphNode>();
    	ArrayList<GraphEdge> edges = new ArrayList<GraphEdge>();
		ArrayList<String> types = new ArrayList<String>();
		
		while(result.hasNext()){
			Map<String,Object> map = result.next();
			
			//Get Labels
			SeqWrapper<?> obj = (SeqWrapper<?>) map.get("lbls");	
			Seq<?> seq = obj.underlying();
			for (scala.collection.Iterator<?> iterator = seq.iterator(); iterator.hasNext();) {
				SeqWrapper<?> node = (SeqWrapper<?>) iterator.next();
				Seq<?> seq2 = node.underlying();
				scala.collection.Iterator<?> iterator2 = seq2.iterator();
				types.add((String) iterator2.next());
			}
		
			//Get Nodes
			obj = (SeqWrapper<?>) map.get("nds");	
			seq = obj.underlying();
			Iterator<String> itTypes = types.iterator();
			for (scala.collection.Iterator<?> iterator = seq.iterator(); iterator.hasNext();) {
				NodeProxy node = (NodeProxy) iterator.next();
				int id = Integer.parseInt(String.valueOf(node.getId()));
				
				Map<String, Object> valueMap = node.getAllProperties();
				Set<String> valueMapKeys = valueMap.keySet();
				if(itTypes.next().equals("Movie")){
					Movie movie = new Movie();
					MovieData dt = new MovieData();
					dt.setId(id);
					dt.setType("Movie");
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
				else{
					Person person = new Person();
					PersonData dt1 = new PersonData();
					dt1.setId(id);
					dt1.setType("Person");
					for (String valueMapKey : valueMapKeys) {
						if(valueMapKey.equals("name"))
							dt1.setName((String) valueMap.get(valueMapKey));
						else
							dt1.setBorn(String.valueOf(valueMap.get(valueMapKey))); 
					}
					person.setData(dt1);
					nodes.add(person);
				}
					
			}				
			//Get Relation Types
			obj = (SeqWrapper<?>) map.get("types");	
			seq = obj.underlying();
			int index = 0;
			for (scala.collection.Iterator<?> iterator = seq.iterator(); iterator.hasNext();) {
				
				String relation = (String)iterator.next();
				
				GraphEdge edge = new GraphEdge();
				GraphEdgeData dt = new GraphEdgeData();
				
				String type = types.get(index);
				String typeNext = types.get(index+1);
				if(type.equals("Movie") && typeNext.equals("Person")){
					Movie mv = (Movie) nodes.get(index);
					dt.setTarget(mv.getData().getId());
					Person ps = (Person) nodes.get(index+1);
					dt.setSource(ps.getData().getId());
					dt.setType(relation);
					index++;
					edge.setData(dt);
					edges.add(edge);
				}
				if(type.equals("Person") && typeNext.equals("Movie")){
					Person ps = (Person) nodes.get(index);
					dt.setSource(ps.getData().getId());
					Movie mv = (Movie) nodes.get(index+1);
					dt.setTarget(mv.getData().getId());
					dt.setType(relation);
					index++;
					edge.setData(dt);
					edges.add(edge);
				}
				if(type.equals("Person") && typeNext.equals("Person")){
					Person ps = (Person) nodes.get(index);
					dt.setSource(ps.getData().getId());
					Person ps2 = (Person) nodes.get(index+1);
					dt.setTarget(ps2.getData().getId());
					dt.setType(relation);
					index++;
					edge.setData(dt);
					edges.add(edge);
				}
			}
		}
		GraphData data = new GraphData();
		data.setNodes(nodes);
		data.setEdges(edges);
		return data;
	}
	public static ArrayList<String> pullRelations(String query) {
		Result result = service.execute(query);
		ArrayList<String> list = new ArrayList<String>();
		while(result.hasNext()){
			Map<String,Object> map = result.next();
			Set<String> keys = map.keySet();
			for (String key : keys){
				String str = (String) map.get(key);
				list.add(str);
			}
		}
		return list;
	}
	public static GraphData pullMovies (String query){
		ArrayList<GraphNode> nodes = new ArrayList<GraphNode>();
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
				dt.setType("Movie");
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
	public static GraphData pullPeople (String query){
		Result result = service.execute(query);
    	ArrayList<GraphNode> nodes = new ArrayList<GraphNode>();
		while(result.hasNext()){
			Map<String,Object> map = result.next();
			Set<String> keys = map.keySet();
			
			for (String key : keys){
				Node node = (Node) map.get(key);			
				int id = Integer.parseInt(String.valueOf(node.getId()));

				Map<String, Object> valueMap = node.getAllProperties();
				Set<String> valueMapKeys = valueMap.keySet();
				
				Person person = new Person();
				PersonData dt = new PersonData();
				dt.setId(id);
				dt.setType("Person");
				for (String valueMapKey : valueMapKeys) {
					if(valueMapKey.equals("name"))
						dt.setName((String) valueMap.get(valueMapKey));
					else
						dt.setBorn(String.valueOf(valueMap.get(valueMapKey)));
				}
				person.setData(dt);
				nodes.add(person);
			}
			
		}
		GraphData data = new GraphData();
		data.setNodes(nodes);
		return data;
	}
	public static GraphData movieNetwork(String query,String firstRelation,String secondRelation ,String nameOfMovie) throws IOException{
		ArrayList<GraphNode> nodes = new ArrayList<GraphNode>();
    	ArrayList<GraphEdge> edges = new ArrayList<GraphEdge>();
		Result result = service.execute(query);
		HashSet<Integer> ids = new HashSet<Integer>();//Node id's set for duplication check
		
		while(result.hasNext()){
			Map<String, Object> map = result.next();//One row
			Set<String> keys = map.keySet();
			
			for (String key : keys) {
				Node node = (Node) map.get(key);		
				int id = Integer.parseInt(String.valueOf(node.getId()));
				
				if(!ids.contains(id))
					ids.add(id);
				else
					continue;//Newly coming node is a duplicate
				
				Map<String, Object> valueMap = node.getAllProperties();				
				Set<String> valueMapKeys = valueMap.keySet();
				NodeKey nodeKey = NodeKey.valueOf(key);
				switch (nodeKey){
				case m:
					Movie movie = new Movie();
					MovieData dt = new MovieData();
					dt.setId(id);
					dt.setType("Movie");
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
					break;	
				
				case a:
					Person actor = new Person();
					PersonData dt1 = new PersonData();
					dt1.setId(id);
					dt1.setType("Actor");
					for (String valueMapKey : valueMapKeys) {
						if(valueMapKey.equals("name"))
							dt1.setName((String) valueMap.get(valueMapKey));
						else
							dt1.setBorn(String.valueOf(valueMap.get(valueMapKey))); 
					}
					actor.setData(dt1);
					nodes.add(actor);
					break;
				case d:
					Person director = new Person();
					PersonData dt2 = new PersonData();
					dt2.setId(id);
					dt2.setType("Director");
					for (String valueMapKey : valueMapKeys) {
						if(valueMapKey.equals("name"))
							dt2.setName((String) valueMap.get(valueMapKey));
						else
							dt2.setBorn(String.valueOf(valueMap.get(valueMapKey))); 
					}
					director.setData(dt2);
					nodes.add(director);
					break;
				default:
					IOException e = new IOException();
					throw e;
				}
			}
		}
		
		//Add edges for nodes
		for(GraphNode node: nodes){
			if(node instanceof Person){
				GraphEdge edge = new GraphEdge();
				GraphEdgeData dt = new GraphEdgeData();
				dt.setSource(((Person) node).getData().getId());
				int idMovie = findMovie(nameOfMovie, nodes);
				if(idMovie!=-1){
					dt.setTarget(idMovie);
				}
				else{
					IOException e = new IOException();
					throw e;
				}
				if(((Person) node).getData().getType().equals("Actor"))
					dt.setType(firstRelation);
				else
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
