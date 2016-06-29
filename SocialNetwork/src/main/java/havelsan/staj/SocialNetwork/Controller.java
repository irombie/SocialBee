package havelsan.staj.SocialNetwork;

import java.io.IOException;
import java.util.ArrayList;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class Controller {

    private boolean isInitialized = false;
    
    @RequestMapping("/GraphData")
    public GraphData GraphData(@RequestParam(value="movieName", defaultValue="V for Vendetta") String movieName) {

    	if(!isInitialized)
    	{
    		Operation.initialize();
    		isInitialized = true;
    	}
    	
    	ArrayList<GraphNode> nodes = new ArrayList<GraphNode>();
    	ArrayList<GraphEdge> edges = new ArrayList<GraphEdge>();
    	String firstRelation = "ACTED_IN";
    	String secondRelation = "DIRECTED";
        String query = "MATCH (a)-[:"+firstRelation+"]->(m{title:\""+movieName+"\"})<-[:"+secondRelation+"]-(d) RETURN a,m,d;";
        GraphData data = null;
        
		try {
			data = Operation.movieNetwork(query,nodes,edges,firstRelation,secondRelation,movieName);
		} catch (IOException e) {
			System.out.println("PROBLEM!!!");
			e.printStackTrace();
		}
		return data;  
    }
    //Degree centrality is simply the number of connections that a node has in the the network. 
    @RequestMapping("/DegreeCentralityP")
    public AnalyzeData DegreeCentralityP(@RequestParam(value="relation", defaultValue="") String relation) {

    	if(!isInitialized)
    	{
    		Operation.initialize();
    		isInitialized = true;
    	}
    	
    	if(!relation.isEmpty())
    		relation=":"+relation;
        String query = "MATCH (p:Person) RETURN p.name AS title, size( (p)-["+relation+"]-() ) AS degree ORDER BY degree DESC";
       
       	return Operation.centrality(query);
    }
    @RequestMapping("/DegreeCentralityM")
    public AnalyzeData DegreeCentralityM(@RequestParam(value="relation", defaultValue="") String relation) {

    	if(!isInitialized)
    	{
    		Operation.initialize();
    		isInitialized = true;
    	}
    	
    	if(!relation.isEmpty())
    		relation=":"+relation;
        String query = "MATCH (m:Movie) RETURN m.title AS title, size( (m)-["+relation+"]-() ) AS degree ORDER BY degree DESC";
       
       	return Operation.centrality(query);
    }
    @RequestMapping("/Relations")
    public ArrayList<String> Relations()
    {
    	if(!isInitialized)
    	{
    		Operation.initialize();
    		isInitialized = true;
    	}
    	
    	String query = ("match ()-[r]-() return distinct type(r) as types");
    	return Operation.pullRelations(query);
    }
    @RequestMapping("/Movies")
    public GraphData Movies()
    {
    	if(!isInitialized)
    	{
    		Operation.initialize();
    		isInitialized = true;
    	}
    	ArrayList<GraphNode> nodes = new ArrayList<GraphNode>();
    	
    	String query = ("match(m:Movie) return m ");
    	return Operation.pullMovies(query, nodes);
    }
    @RequestMapping("/People")
    public GraphData People()
    {
    	if(!isInitialized)
    	{
    		Operation.initialize();
    		isInitialized = true;
    	}
    	ArrayList<GraphNode> nodes = new ArrayList<GraphNode>();
    	
    	String query = ("match(p:Person) return p ");
    	return Operation.pullPeople(query, nodes);
    }
    @RequestMapping("/ShortestPathM2M")
    public GraphData ShortestPathM2M(@RequestParam(value="from", defaultValue="V for Vendetta") String from,@RequestParam(value="to", defaultValue="The Replacements") String to){
    	
    	if(!isInitialized)
    	{
    		Operation.initialize();
    		isInitialized = true;
    	}
    	ArrayList<GraphNode> nodes = new ArrayList<GraphNode>();
    	ArrayList<GraphEdge> edges = new ArrayList<GraphEdge>();
    	
    	String query = ("Match  (m1:Movie {title:\""+from+"\"}),(m2:Movie {title:\""+to+"\"}),p = shortestPath((m1)-[*]-(m2)) "+
    					"With p, extract(rel in rels(p) | type(rel)) as types, nodes(p) as nds "+
    					"return nds, types,extract (x in nds | labels(x))as lbls;");
    	GraphData data=null;
		try {
			data = Operation.shortestPath(query, nodes,edges);
		} catch (IOException e) {
			System.out.println("PROBLEM!!!");
			e.printStackTrace();
		}
    	return data;
    }
    @RequestMapping("/ShortestPathM2P")
    public GraphData ShortestPathM2P(@RequestParam(value="from", defaultValue="V for Vendetta") String from,@RequestParam(value="to", defaultValue="Tom Cruise") String to){
    	
    	if(!isInitialized)
    	{
    		Operation.initialize();
    		isInitialized = true;
    	}
    	ArrayList<GraphNode> nodes = new ArrayList<GraphNode>();
    	ArrayList<GraphEdge> edges = new ArrayList<GraphEdge>();
    	
    	String query = ("Match  (m1:Movie {title:\""+from+"\"}),(m2:Person {name:\""+to+"\"}),p = shortestPath((m1)-[*]-(m2)) "+
    					"With p, extract(rel in rels(p) | type(rel)) as types, nodes(p) as nds "+
    					"return nds, types,extract (x in nds | labels(x))as lbls;");
    	GraphData data=null;
		try {
			data = Operation.shortestPath(query, nodes,edges);
		} catch (IOException e) {
			System.out.println("PROBLEM!!!");
			e.printStackTrace();
		}
    	return data;
    }
    @RequestMapping("/ShortestPathP2P")
    public GraphData ShortestPathP2P(@RequestParam(value="from", defaultValue="Tom Cruise") String from,@RequestParam(value="to", defaultValue="Lana Wachowski") String to){
    	
    	if(!isInitialized)
    	{
    		Operation.initialize();
    		isInitialized = true;
    	}
    	ArrayList<GraphNode> nodes = new ArrayList<GraphNode>();
    	ArrayList<GraphEdge> edges = new ArrayList<GraphEdge>();
    	
    	String query = ("Match  (m1:Person {name:\""+from+"\"}),(m2:Person {name:\""+to+"\"}),p = shortestPath((m1)-[*]-(m2)) "+
    					"With p, extract(rel in rels(p) | type(rel)) as types, nodes(p) as nds "+
    					"return nds, types,extract (x in nds | labels(x))as lbls;");
    	GraphData data=null;
		try {
			data = Operation.shortestPath(query, nodes,edges);
		} catch (IOException e) {
			System.out.println("PROBLEM!!!");
			e.printStackTrace();
		}
    	return data;
    }
    
}
