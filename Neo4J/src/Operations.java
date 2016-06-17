import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.neo4j.cypher.internal.ExecutionEngine;
import org.neo4j.cypher.internal.ExecutionResult;
import org.neo4j.cypher.internal.compiler.v2_3.pipes.ExecuteUpdateCommandsPipe;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Result;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.kernel.GraphDatabaseQueryService;
import org.neo4j.logging.Log;
import org.neo4j.logging.LogProvider;


//http://neo4j.com/developer/guide-import-csv/



public class Operations {

	final static String DBPATH="/home/odayibas/Desktop/Staj/neo4j-community-3.0.3/data/databases/graph.db";   
	final static String FILEPATH="/home/odayibas/Desktop/Staj/neo4j-community-3.0.3/data/databases/den.txt";

	public static void main(String[] args) throws IOException {
		
			GraphDatabaseFactory graphDbFactory = new GraphDatabaseFactory();
			File db = new File(DBPATH);
			GraphDatabaseService service = graphDbFactory.newEmbeddedDatabase(db);
			
			//clearDB(service);
			String query = "MATCH (a)-[:ACTED_IN]->(m)<-[:DIRECTED]-(d) RETURN a,m,d LIMIT 10;";
			executeQuery(query, service);
			
			//executeFile(FILEPATH, service);
			//returnAll(service);
			
	}
	static void executeQuery(String query, GraphDatabaseService service){
	      
		Result result = service.execute(query);
		
		while(result.hasNext()){
			Map<String, Object> map = result.next();//One row
			Set<String> keys = map.keySet();
			System.out.println();
			for (String key : keys) {
				System.out.println(key);
				Node node = (Node) map.get(key);				
				Map<String, Object> valueMap = node.getAllProperties();				
				Set<String> valueMapKeys = valueMap.keySet();
				for (String valueMapKey : valueMapKeys) {
					System.out.println(valueMapKey + ": " + valueMap.get(valueMapKey));	
				}
			}
		}
	}
	static void executeFile(String path,GraphDatabaseService service) throws IOException{
		long startTime = System.currentTimeMillis();
		clearDB(service);
		FileInputStream fstream = new FileInputStream(FILEPATH);
//		String query = "LOAD CSV WITH HEADERS FROM "+
//						"\"file://"+path+"\" +AS Line"+
//						"WITH Line" +
//						"Return Line";
		//executeQuery(query, service);
		BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
		String strLine="";
		System.out.println("Filo import started..");
		while ((strLine = br.readLine()) != null){
			service.execute(strLine);
			//System.out.println(strLine);
		}
		long endTime = System.currentTimeMillis();
		long duration = (endTime - startTime)/1000;//Seconds
		
		System.out.println("File is imported into Database within: "+duration+" seconds!");
	}
	static void clearDB(GraphDatabaseService service){
		service.execute("MATCH (n) DETACH DELETE n");
		System.out.println("Database Cleared");
	}
	static void returnAll(GraphDatabaseService service){
		executeQuery("MATCH (a) RETURN a", service);
	}
}
