import java.io.File;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.neo4j.cypher.internal.ExecutionEngine;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Result;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.kernel.GraphDatabaseQueryService;
import org.neo4j.logging.Log;
import org.neo4j.logging.LogProvider;

public class Operations {

	/**
	 * @param args
	 */

	public static void main(String[] args) {
		// TODO Auto-generated method stub
			GraphDatabaseFactory graphDbFactory = new GraphDatabaseFactory();
			File db = new File("/home/odayibas/Desktop/Staj/neo4j-community-3.0.3/data/databases/graph.db");
			GraphDatabaseService graphDb = graphDbFactory.newEmbeddedDatabase(db);
			
			Result result = graphDb.execute("MATCH (a)-[:ACTED_IN]->(m)<-[:DIRECTED]-(d) RETURN a,m,d LIMIT 10");
			
			Map<String, Object> map = result.next();
			
			
			Set<String> keys = map.keySet();
			
			for (String key : keys) {
				Node node = (Node) map.get(key);
				System.out.println("key " + key + " value " + node);
				
				Map<String, Object> valueMap = node.getAllProperties();
				
				Set<String> valueKeys = valueMap.keySet();
				
				for (String valueKey : valueKeys) {
					System.out.println("valueKey " + valueKey + " valueKeyValue " + valueMap.get(valueKey));
				}
				
				
			}
			
			
//			
//			LogProvider logProvider = new LogProvider() {
//				
//				@Override
//				public Log getLog(String arg0) {
//					// TODO Auto-generated method stub
//					return null;
//				}
//				
//				@Override
//				public Log getLog(Class arg0) {
//					// TODO Auto-generated method stub
//					return null;
//				}
//			};
			
			

//	      ExecutionEngine execEngine = new ExecutionEngine();
//	      ExecutionResult execResult = execEngine.execute("MATCH (java:JAVA) RETURN java");
//	      String results = execResult.dumpToString();
//	      System.out.println(results);
	}}

