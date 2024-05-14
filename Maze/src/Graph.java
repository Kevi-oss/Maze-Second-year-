import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Graph implements GraphADT {
	
//	Create an adjacency list or an adjacency matrix, a list is probably easier
	
	
	//array of vertices
	private GraphNode[] vertices;
	//array of arraylists of graphedges to represent the edges
	private ArrayList<GraphEdge>[] edges;
	
	
	/*When I check the nodes in the algorithm, I used == specifically to see
	 * if they represent the same nodes, I understood they represent address,
	 * it just doesn't matter if I check by types or address to represent the same node*/
	
	public Graph(int n) {
//		initialize your representation with empty adjacency lists
		
		
		//vertices intialization
		vertices = new GraphNode[n];	
		//make new node for the vertices
		for(int x = 0; x<n; x++)
		{
			vertices[x] = new GraphNode(x);
		}
		
		
		//Create an array of arrayLists to store edges
		edges = new ArrayList[n];
		//initalise all of them
		for(int x = 0; x<n; x++)
		{
			edges[x] = new ArrayList<GraphEdge>();
		}
		
	}
	
	@Override
	public void insertEdge(GraphNode nodeu, GraphNode nodev, int type, String label) throws GraphException {
		//Before we do anything, try to see if the node already exists
		try
		{
			//for each one of the vertices that is contained in this thing we want to add
			ArrayList<GraphEdge> first = edges[nodeu.getName()];
			//second is used to check if the other node exists
			ArrayList<GraphEdge> second = edges[nodev.getName()];
			
			
			//FYI: if there's nothing in the arraylist it returns 0 when size is called
			for(int x = 0; x<first.size(); x++)
			{
				//check the corrisponding nodes are the same in both edges
				//the edge inside the adjacency list for time purposes
				GraphEdge theFirstEdge = first.get(x);
				
				
				//if the the first point and the second point of the edge in the list are same nodes as what we wan't to add
				if(theFirstEdge.firstEndpoint()==nodeu && theFirstEdge.secondEndpoint() == nodev)
				{
					//throw an exception that will be caught and converted
					throw new Exception();
				}
				
			}	
			//Now let's attempt to add the edge into the list
			//Undirected graph so need to add one for each
			GraphEdge newEdgeOne = new GraphEdge(nodeu, nodev, type, label);
			GraphEdge newEdgeTwo = new GraphEdge(nodev, nodeu, type, label);
			//add them corrospondingly at both sides
			first.add(newEdgeOne);
			second.add(newEdgeTwo);
			
			/*Throughout this algorithm, if we try to access a node that doesn't exist, or that is
			 * out of bounds from our list, an exception is thrown. The catch function will capture
			 * these moments and throw the exception required want if any of these instances occur*/
		}
		catch(Exception e)
		{
			//if we catch any exceptions, slap it as a graph exception
			throw new GraphException("Either edge already exists or a node doesn't exist");
		}
	}

	@Override
	public GraphNode getNode(int u) throws GraphException {
//		Return the node with the appropriate name
		try
		{
			//try to access this node from our node list 
			GraphNode node = vertices[u];
			//return if accessing it is possible
			return node;
		}
		catch(Exception e)
		{
			//if accessing is not possible throw GraphException
			throw new GraphException("Node doesn't exist");
		}
		
	}

	@Override
	public Iterator<GraphEdge> incidentEdges(GraphNode u) throws GraphException {
//		Select from your adjacency list the appropriate Node and return an iterator over the collection.
//		Usually a call to .iterator() should work, unless you do something really exotic
		try
		{
			//attempt to retreive the node's edges, exception occurs if node doesn't exists
			ArrayList<GraphEdge> theEdges = edges[u.getName()];
			//check if the arraylist has 0 elements in it, this represents that it has no edges and we should return null
			if(theEdges.size()==0)
				return null;
			//otherwise return an iterator of the edges obtained from the iterator method
			return theEdges.iterator();
			
		}
		catch(Exception e)
		{
			//if an exception is thrown while trying to access the node, throw exception
			throw new GraphException("The node doesn't exists");
		}
	}

	@Override
	public GraphEdge getEdge(GraphNode u, GraphNode v) throws GraphException {
//		check if those nodes exist, then check if they have edges, then who has the least number of edges.
//		find the appropriate edge and return it, if no such edge exists remember to return null 
//		there are faster ways too ;)
		try
		{
			//attempt to see if the nodes exists, catch the exception if it doesn't
			ArrayList<GraphEdge> first = edges[u.getName()];
			ArrayList<GraphEdge> second = edges[v.getName()];
			
			
			//undirected graph, only need to check one side
			//for all edges starting with u
			for(int x =0; x<first.size();x++)
			{
				//check if the endpoints are the same
				if(first.get(x).secondEndpoint() == v)
					return first.get(x);
			}
			
			
			//else throw an exception
			throw new GraphException("Node doesn't exist or edge doesn't exist");
		}
		catch(Exception e)
		{
			throw new GraphException("Node doesn't exist or edge doesn't exist");
		}
		
		
	}

	@Override
	public boolean areAdjacent(GraphNode u, GraphNode v) throws GraphException {
//		maybe you could use a previously written method to solve this one quickly...
		try
		{
			getEdge(u,v);
			return true;
		}
		catch(Exception e)
		{
			return false;
		}
	}

}
