package neo4j;

public class Actor extends GraphNode{
	private ActorData data;

	public ActorData getData() {
		return data;
	}

	public void setData(ActorData data) {
		this.data = data;
	}

	
}