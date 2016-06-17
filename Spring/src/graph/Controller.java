package graph;

import java.util.concurrent.atomic.AtomicLong;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    @RequestMapping("/getGraphData")
    public GraphData getGraphData() {
    	Node[] nodes = new Node[2];
    	Node n1 = new Node();
    	Node n2 = new Node();
    	NodeData nd1 = new NodeData();
    	NodeData nd2 = new NodeData();
    	nd1.setId("1");
    	nd2.setId("2");
    	nd1.setName("Batu");
    	nd2.setName("Hasan");
    	n1.setData(nd1);
    	n2.setData(nd2);
    	nodes[0]=n1;
    	nodes[1]=n2;
        GraphData gd = new GraphData();
        gd.setNodes(nodes);
        return gd;
        
        
        
    }
}
