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
	final static String FILEPATH="/home/odayibas/Desktop/Staj/neo4j-community-3.0.3/data/databases/output.txt";

	public static void main(String[] args) throws IOException {
		
			GraphDatabaseFactory graphDbFactory = new GraphDatabaseFactory();
			File db = new File(DBPATH);
			GraphDatabaseService service = graphDbFactory.newEmbeddedDatabase(db);
			//String str = "CREATE (WhatsonYourMindPureEnergyTommyBoyCD:Music {ASIN:\"B000000HFH\",title:\"What's on Your Mind (Pure Energy) [Tommy Boy CD5/12\")\",salesrank:135746}";
			//clearDB(service);
			//service.execute("CREATE (PatternsofPreachingASermonSampler:Book {ASIN:\"0827229534\",title:\"Patterns of Preaching: A Sermon Sampler\",salesrank:396585})");
			
			executeFile(FILEPATH, service);
			//returnAll(service);
			
	}
	static void executeQuery(String path, GraphDatabaseService service){
	      
		Result result = service.execute(path);
		
		while(result.hasNext()){
			Map<String, Object> map = result.next();//One row
			
			Set<String> keys = map.keySet();
			System.out.println();
			for (String key : keys) {
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
		String query = "LOAD CSV WITH HEADERS FROM "+
						"\"file://"+path+"\" +AS Line"+
						"WITH Line" +
						"Return Line";
		executeQuery(query, service);
//		BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
//		String strLine="";
//		System.out.println("Filo import started..");
//		while ((strLine = br.readLine()) != null){
//			service.execute(strLine);
//		}
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
