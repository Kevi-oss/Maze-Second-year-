
public class GraphEdge {

//	I need an origin and a destination
	private GraphNode start;
	private GraphNode end;
	
	
//	I need an int type variable
	private int type;
	
	
//	and I need a string label
	private String label;
	
	public GraphEdge(GraphNode u, GraphNode v, int type, String label) {
//		I should probably initialize everything
		start = u;
		end = v;
		this.type = type;
		this.label = label;
	}
	
//	I should probably fill in the bodies of those setters and getters
	public GraphNode firstEndpoint() {
		return start;
		
	}
//	Accessor method for second end point
	public GraphNode secondEndpoint() {
		return end;
		
	}
//	Accessor method for type
	public int getType() {
		return type;
		
	}
//	mutator method to change type
	public void setype(int type) {
		this.type = type;
		
	}
//	Accessor method to get Label
	public String getLabel() {
		return label;
		
	}
//	Mutator method to change label
	public void setLabel(String label) {
		this.label = label;
	}
	
}
