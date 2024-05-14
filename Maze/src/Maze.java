import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Maze {

	
//	instance variables you may need
	
	
	//Used for the first 4 part of txt
	private int S;
	private int A;
	private int L;
	private int k;
	
	
//	a variable storing the graph, a variable storing the id of the starting node, a variable storing the id of the end node
	//A graph variable, and two graphNodes to represent the start and end
	private Graph graph;
	private GraphNode start;
	private GraphNode end;
	
	
//	a variable storing the read number of coins, maybe even a variable storing the path so far so that you don't perform accidental 
//	(and unnecessary cycles).
//	if you maintain nodes on a path in a list, be careful to make a list of GraphNodes, 
//	otherwise removal from the list is going to behave in a weird way. 
//	REMEMBER your nodes have a field mark.. maybe that field could be useful to avoid cycles...
	
	//method to store path to reach end
	private ArrayList<GraphNode> path;
	

	public Maze(String inputFile) throws MazeException {
//		initialize your graph variable by reading the input file!
//		to maintain your code as clean and easy to debug as possible use the provided private helper method
		try
		{
			//make a Buffered reader, if cannot read then exception will be caught
			BufferedReader reader = new BufferedReader(new FileReader(inputFile));
			//If the input file is safe, initialise the path arrayList
			path = new ArrayList<GraphNode>();
			//Put the reader into the private method
			readInput(reader);
		}
		//catch method seperated into two parts to make messages clear
		catch(GraphException e)
		{
			throw new MazeException(e.getMessage());
		}
		catch(IOException w)
		{
			throw new MazeException("IO problems");
		}
	}
		
//Accessor method for graph
	public Graph getGraph() {
//		return your graph
		return graph;
	}

	//returns a path from start to end, returns null if no such path exists
	public Iterator<GraphNode> solve() throws GraphException {
//		simply call your private DFS. If you come up with a different approach that's ok too.
//		remember to always return an Iterator or null
		return DFS(k, start);
	}

	
	//Private method to use DFS to get a path from go to end
	private Iterator<GraphNode> DFS(int k, GraphNode go) throws GraphException {
//		perform a DFS of your graph. Reduce your k which represents the remaining coins
//		start with the base case
//		remember to return null if you didn't find a path
		
		//mark go and add it into our ArrayList
		go.mark(true);
		path.add(go);
		
		
		//get interator of all incidences of go
		Iterator<GraphEdge> temp = graph.incidentEdges(go);
		
		//if we have null, return null because there's no paths
		if(temp == null)
			return null;
		
		
		
		//otherwise, while we have edge in the iterator
		while(temp.hasNext())
		{
			//get the edge
			GraphEdge theEdge = temp.next();
			//get the end point of the edge
			GraphNode secondPoint = theEdge.secondEndpoint();
			
			
			//if the endpoint of the path is our end(same node), return the path
			if(secondPoint==end)
			{
				//CHECK COSTS
				int cost = theEdge.getType();
				
				
				//if costs are smaller than or equal to what we have
				if(k>=cost)
				{
					//add end node to path
					path.add(secondPoint);
					//return array as an iterator
					return path.iterator();
				}
			}
			else
			{
				//otherwise
				
				//if the endpoint of our edge is marked
				if(secondPoint.isMarked())
				{
					//if it's not in our path already (to avoid cycles)
					if(!path.contains(secondPoint))
					{
						//this means there's a potential path we might miss, unmark the node
						secondPoint.mark(false);
					}
					//otherwise no nothing about it
					
				}
				//now check for all points we need to go through(unmarked)
				if(!secondPoint.isMarked())
				{
					//At the end, we have all unmarked nodes, first check for type
					int cost = theEdge.getType();
					//check if we can go into the point with our coins
					if(k>=cost)
					{
						//recursion
						Iterator<GraphNode> check = DFS(k-cost, secondPoint);
						
						
						//if recursion returns things other than null, return it cause we found a path
						if(check != null)
							return check;
						
					}
				}
				
			}
		}
		//at the end of the while loop, our attempts to find a path is unfruitful, delete node from array
		path.remove(go);
		//return null when it's unfruitful
		return null;
		
		
	}


	private void readInput(BufferedReader inputReader) throws IOException, GraphException {
//		Read the values S, A, L, and k
//		pay attention when iterating over the input.. All testing input will be correctly formatted
//		remember to identify the starting and ending rooms
//		The input will have size A + A-1 and L + L-1 because every pair of nodes has its relationship inbetween them in the textual representation!
//		To maintain this method cleaner, you may use the private helper method insertEdge
		
		
		//read S, A, L, K. I didn't use read method because I've never used it before and it's confusing
		S = Integer.parseInt(inputReader.readLine());
		A = Integer.parseInt(inputReader.readLine());
		L = Integer.parseInt(inputReader.readLine());
		k = Integer.parseInt(inputReader.readLine());
		//IO exception if anything goes wrong,I assumed parseInt wouldn't give exception
		
		
		
		//create the graph of A*L (AxL)
		graph = new Graph(A*L);
		
		/*
		 * After we create the graph, it becomes necessary to read all the rooms and connections.
		 * In order to do this, I first note that in the correct input lines
		 * file will always start with RW and end with RW, this means that
		 * the file lines will always be odd:
		 */
		
		
		//make a first and second line
		String first = inputReader.readLine();
		String second = inputReader.readLine();
		
		
		//make a counter to count the nodes ONLY
		int counter = 0;
		
		
		//while the second line is not null, and thus we have not reached our end
		while(second != null)
		{
			//we have at least 3 lines
			
			
			//We look into every first of 3 nodes in the odd lines, After going through the node, counter++ to represent the node we are in
			for(int x = 0; x<first.length()-2;x+=2,counter+=1)
			{
				//For each node, check for starting and exit room, x will be used to go through charaters and counter be used for nodes ONLY
				//starting room
				if(first.charAt(x)=='s')
				{
					// if it is start, record it, identify the node as "start"
					//in the instance where there's already an start, throw exception
					//Start and end are declared in the class and not as local variable, so by default they are null
					if(start != null)
						throw new GraphException("More than one starting nodes");
					start = graph.getNode(counter);
				}
				//exit room
				else if (first.charAt(x)=='x')
				{
					//if it's an exit, record it
					//if there's more than one end, throw exception
					if(end != null)
						throw new GraphException("More than one exit");
					end = graph.getNode(counter);
				}
				//If it's neither, it's either room o or an unknown charater
				else if(first.charAt(x)!='o')
				{
					//if unknown charater, throw exception
					throw new GraphException("unknown charater in text file");
				}
				//we now know it's a valid room
				
				
				
				
				/* Now we check for the connection charaters between the first and second node,
				 * In order to do this, we first check if it's a corridor, if the first node is
				 * a wall, the second node must be a wall also*/
				//Check it's connections, and act corrispondingly
				//if it's right side is a corridor
				if(first.charAt(x+1)=='c')
				{
					//connect the rooms left and right of it using label "corridor"
					GraphNode leftRoom = graph.getNode(counter);
					GraphNode rightRoom = graph.getNode(counter+1);
					
					
					//insert Edge method
					graph.insertEdge(leftRoom,rightRoom,0,"corridor");
				}
				//if it's between 0-9
				else if(first.charAt(x+1)>='0' && first.charAt(x+1)<='9')
				{
					//do same thing as corridor but add value
					//connect the rooms left and right of it using label "corridor"
					GraphNode leftRoom = graph.getNode(counter);
					GraphNode rightRoom = graph.getNode(counter+1);
					
					
					//insert Edge method, parseInt the middle value into an int for the method to work
					graph.insertEdge(leftRoom,rightRoom,Integer.parseInt(first.substring(x+1,x+2)),"door");
				}
				//check if it's not a wall
				else if(first.charAt(x+1)!='w')
				{
					//throw exception
					throw new GraphException("Relationship contains an error");
				}
				//at the end, we know it's a wall, so we do nothing and go onto the next node (x+=2 and counter++)
				
				
			}
			//at the end of the loop, counter should be at the last node of the line
			
			
			//last node in list
			int last = first.length()-1;
			//For each node, check for starting and exit room, x will be used to go through charaters and counter be used for nodes ONLY
			if(first.charAt(last)=='s')
			{
				// if it is start, record it, identify the node as "start"
				//in the instance where there's already an start, throw exception
				//Start and end are declared in the class and not as local variable, so by default they are null
				if(start != null)
					throw new GraphException("More than one starting nodes");
				start = graph.getNode(counter);
			}
			//exit room
			else if (first.charAt(last)=='x')
			{
				//if it's an exit, record it
				//if there's more than one end, throw exception
				if(end != null)
					throw new GraphException("More than one exit");
				end = graph.getNode(counter);
			}
			//If it's neither, it's either room l or an unknown charater
			else if(first.charAt(last)!='o')
			{
				//if unknown charater, throw exception
				throw new GraphException("unknown charater in text file");
			}
			
			
			
			
			
			//AWADA
			//AWADA
			//counter++ to go to the next line 
			counter++;
			
			//for second line, it's way easier
			for(int x =0; x<second.length(); x+=2)
			{
				//if corridor
				if(second.charAt(x)=='c')
				{
					//get what's on up
					GraphNode top = graph.getNode(counter+(x/2)-A);
					//get what's down the corridor (our counter)
					GraphNode bottom = graph.getNode(counter+(x/2));
					
					
					//create an edge
					graph.insertEdge(top,bottom, 0, "corridor");
					
				}
				else if(second.charAt(x)>='0' && second.charAt(x)<='9')
				{
					//get what's on up
					GraphNode top = graph.getNode(counter+(x/2)-A);
					//get what's down the corridor (our counter)
					GraphNode bottom = graph.getNode(counter+(x/2));
					//create an edge with the correct type
					
					
					graph.insertEdge(top,bottom, Integer.parseInt(second.substring(x,x+1)), "door");
				}
				else if(second.charAt(x)!='w')
					throw new GraphException("Incorrect instructions with up/down");
				//if it's a wall, do nothing about it
			}
			
			//at the end of the second line, our counter is at the node of the third line, no need to modify it
			
			
			//refresh first and second line into our next line
			first = inputReader.readLine();
			second= inputReader.readLine();
			//loop
			
		}
		
		//At the end, we still have one last line
		
		//We look into every first of 3 nodes in the odd lines, After going through the node, counter++ to represent the node we are in
		//We look into every first of 3 nodes in the odd lines, After going through the node, counter++ to represent the node we are in
		for(int x = 0; x<first.length()-2;x+=2,counter+=1)
		{
			//For each node, check for starting and exit room, x will be used to go through charaters and counter be used for nodes ONLY
			//starting room
			if(first.charAt(x)=='s')
			{
				// if it is start, record it, identify the node as "start"
				//in the instance where there's already an start, throw exception
				//Start and end are declared in the class and not as local variable, so by default they are null
				if(start != null)
					throw new GraphException("More than one starting nodes");
				
				start = graph.getNode(counter);
			}
			//exit room
			else if (first.charAt(x)=='x')
			{
				//if it's an exit, record it
				//if there's more than one end, throw exception
				if(end != null)
					throw new GraphException("More than one exit");
				
				end = graph.getNode(counter);
			}
			//If it's neither, it's either room l or an unknown charater
			else if(first.charAt(x)!='o')
			{
				//if unknown charater, throw exception
				throw new GraphException("unknown charater in text file");
			}
			//we now know it's a valid room
			
			
			
			
			/* Now we check for the connection charaters between the first and second node,
			 * In order to do this, we first check if it's a corridor, if the first node is
			 * a wall, the second node must be a wall also*/
			//Check it's connections, and act corrispondingly
			//if it's right side is a corridor
			if(first.charAt(x+1)=='c')
			{
				//connect the rooms left and right of it using label "corridor"
				GraphNode leftRoom = graph.getNode(counter);
				GraphNode rightRoom = graph.getNode(counter+1);
				
				
				//insert Edge method
				graph.insertEdge(leftRoom,rightRoom,0,"corridor");
			}
			//if it's between 0-9
			else if(first.charAt(x+1)>='0' && first.charAt(x+1)<='9')
			{
				//do same thing as corridor but add value
				//connect the rooms left and right of it using label "corridor"
				GraphNode leftRoom = graph.getNode(counter);
				GraphNode rightRoom = graph.getNode(counter+1);
				
				
				//insert Edge method, parseInt the middle value into an int for the method to work
				graph.insertEdge(leftRoom,rightRoom,Integer.parseInt(first.substring(x+1,x+2)),"door");
			}
			//check if it's not a wall
			else if(first.charAt(x+1)!='w')
			{
				//throw exception
				throw new GraphException("Relationship contains an error");
			}
			//at the end, we know it's a wall, so we do nothing and go onto the next node (x+=2 and counter++)
			
			
		}
		
		
		
		//last node in list
		int last = first.length()-1;
		//For each node, check for starting and exit room, x will be used to go through charaters and counter be used for nodes ONLY
		if(first.charAt(last)=='s')
		{
			// if it is start, record it, identify the node as "start"
			//in the instance where there's already an start, throw exception
			//Start and end are declared in the class and not as local variable, so by default they are null
			if(start != null)
				throw new GraphException("More than one starting nodes");
			start = graph.getNode(counter);
		}
		//exit room
		else if (first.charAt(last)=='x')
		{
			//if it's an exit, record it
			//if there's more than one end, throw exception
			if(end != null)
				throw new GraphException("More than one exit");
			end = graph.getNode(counter);
		}
		//If it's neither, it's either room l or an unknown charater
		else if(first.charAt(last)!='o')
		{
			//if unknown charater, throw exception
			throw new GraphException("unknown charater in text file");
		}
		
		
		
		
		
	}

	//I would love to use this but I didn't realise it uses int for node 
	private void insertEdge(int node1, int node2, int linkType, String label) throws GraphException {
//		select the nodes and insert the appropriate edge.
		//just call the graph method
		
		//DO YOU KNOW WHICH LETTER IN THE ALPHABET IS THE MOST BEAUTIFUL? ITS U! (HOPE U HAVE A NICE END OF SEMESTER UNKNOWN STRANGER!)
	}

}
