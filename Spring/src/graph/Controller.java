package graph;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicLong;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import neo4j.*;;

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
    	
    	ArrayList<GraphNode> nodes = new ArrayList<>();
    	ArrayList<GraphEdge> edges = new ArrayList<>();
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
    
    @RequestMapping("/Movies")
    public GraphData Movies()
    {
    	if(!isInitialized)
    	{
    		Operation.initialize();
    		isInitialized = true;
    	}
    	ArrayList<GraphNode> nodes = new ArrayList<>();
    	
    	String query = ("match(m:Movie) return m ");
    	GraphData data = Operation.pullMovies(query, nodes);
    	return data;
    }
    @RequestMapping("/ShortestPath")
    public GraphData ShortestPath(){
    	
    	if(!isInitialized)
    	{
    		Operation.initialize();
    		isInitialized = true;
    	}
    	ArrayList<GraphNode> nodes = new ArrayList<>();
    	ArrayList<GraphEdge> edges = new ArrayList<>();
    	
    	String query = ("Match  (m1:Movie {title:\"V for Vendetta\"}),(m2:Movie {title:\"The Replacements\"}),p = shortestPath((m1)-[*]-(m2))"+
    					"With p, extract(rel in rels(p) | type(rel)) as types, nodes(p) as nds"+
    					"return nds, types,extract (x in nds | labels(x));");
    	GraphData data = Operation.shortestPath(query, nodes,edges);
    	return data;
    }
}
