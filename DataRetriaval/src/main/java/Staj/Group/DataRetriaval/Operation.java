package Staj.Group.DataRetriaval;

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
			
		Operation.initialize();
    	
    	ArrayList<GraphNode> nodes = new ArrayList();
    	
        String query = "MATCH (a)-[:ACTED_IN]->(m{title:\"Apollo 13\"})<-[:DIRECTED]-(d) RETURN a;";
        GraphData data = Operation.executeQuery(query,nodes);
        query =  "MATCH (a)-[:ACTED_IN]->(m{title:\"Apollo 13\"})<-[:DIRECTED]-(d) RETURN m;";
        data = Operation.executeQuery(query,nodes);
        query = "MATCH (a)-[:ACTED_IN]->(m{title:\"Apollo 13\"})<-[:DIRECTED]-(d) RETURN d;";
        data = Operation.executeQuery(query,nodes);
        for(GraphNode node : data.getNodes()){
        	System.out.println(node.toString());
        }
	}
	public static void initialize(){
		GraphDatabaseFactory graphDbFactory = new GraphDatabaseFactory();
		File db = new File(DBPATH);
		service = graphDbFactory.newEmbeddedDatabase(db);
	}
	public static GraphData executeQuery(String query,ArrayList<GraphNode> nodes ){
		Result result = service.execute(query);

		HashSet<Integer> ids = new HashSet<Integer>();//Node ids set for dupplication check
		while(result.hasNext()){
			Map<String, Object> map = result.next();//One row
			Set<String> keys = map.keySet();
		 
			
			for (String key : keys) {
				//System.out.println(key);
				Node node = (Node) map.get(key);			
				String idS = String.valueOf(node.getId());
				int id = Integer.parseInt(idS);
				
				if(!ids.contains(id))
					ids.add(id);
				else
					break;//Newly coming node is a dupplicate
				
				Map<String, Object> valueMap = node.getAllProperties();				
				Set<String> valueMapKeys = valueMap.keySet();
				NodeKey nodeKey = NodeKey.valueOf(key);
				switch (nodeKey){
				case a:
					Actor actor = new Actor();
					ActorData dt = new ActorData();
					actor.setId(id);
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
					director.setId(id);
					
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
					movie.setId(id);
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
					System.out.println("NO MATCH!!!");
				}			
			}
		}
		GraphData data = new GraphData();
		data.setNodes(nodes);
		return data;
	}
	static void executeFile(String path,GraphDatabaseService service) throws IOException{
//		long startTime = System.currentTimeMillis();
//		clearDB();
//		FileInputStream fstream = new FileInputStream(FILEPATH);
////		String query = "LOAD CSV WITH HEADERS FROM "+
////						"\"file://"+path+"\" +AS Line"+
////						"WITH Line" +
////						"Return Line";
//		//executeQuery(query, service);
//		BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
//		String strLine="";
//		System.out.println("Filo import started..");
//		while ((strLine = br.readLine()) != null){
//			service.execute(strLine);
//			//System.out.println(strLine);
//		}
//		long endTime = System.currentTimeMillis();
//		long duration = (endTime - startTime)/1000;//Seconds
//		
//		System.out.println("File is imported into Database within: "+duration+" seconds!");
	}
	static void clearDB(){		
		service.execute("MATCH (n) DETACH DELETE n");
		System.out.println("Database Cleared");
	}
}
