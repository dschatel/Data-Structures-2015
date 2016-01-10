package apps;

import structures.*;
import java.util.ArrayList;

public class MST {
	
	/**
	 * Initializes the algorithm by building single-vertex partial trees
	 * 
	 * @param graph Graph for which the MST is to be found
	 * @return The initial partial tree list
	 */
	public static PartialTreeList initialize(Graph graph) {
	
		/* COMPLETE THIS METHOD */
		
		//Create empty PartialTreeList object
		//Iterate through vertices array
		//Create a PartialTree object, set root to the vertex at v
		//Iterate through list of neighbor objects attached to vertex
			//Create Arc objects consisting of vertex at v, vertex found in neighbor, and weight
			//Put Arc object into Minheap, repeat until at end of neighbors list
		//Append PartialTree object to PartialTreeList
		//Continue until end of vertices array
		//Return PartialTreeList

		PartialTreeList treelist = new PartialTreeList();
		
		for (int v = 0; v < graph.vertices.length; v++) {
			PartialTree p = new PartialTree(graph.vertices[v]);
			p.getRoot().parent = p.getRoot();
			
			for (Vertex.Neighbor ptr = graph.vertices[v].neighbors; ptr != null; ptr = ptr.next) {
				PartialTree.Arc arc = new PartialTree.Arc(p.getRoot(), ptr.vertex, ptr.weight);
				p.getArcs().insert(arc);
			}
			treelist.append(p);
		}
		
		return treelist;
	}

	/**
	 * Executes the algorithm on a graph, starting with the initial partial tree list
	 * 
	 * @param graph Graph for which MST is to be found
	 * @param ptlist Initial partial tree list
	 * @return Array list of all arcs that are in the MST - sequence of arcs is irrelevant
	 */
	public static ArrayList<PartialTree.Arc> execute(Graph graph, PartialTreeList ptlist) {
		
		ptlist = initialize(graph);
		
		ArrayList<PartialTree.Arc> result = new ArrayList<PartialTree.Arc>();
		
		while (ptlist.size() > 1) {
			PartialTree p = ptlist.remove();
			
			if(p.getArcs().isEmpty()) {} //Case for unconnected vertices -- removes from ptlist
			
			else {
			PartialTree.Arc arc = p.getArcs().deleteMin();
			
			while (arc.v2.getRoot().equals(p.getRoot())) {
				arc = p.getArcs().deleteMin();
			}
			
			result.add(arc);
			
			PartialTree ptwo = ptlist.removeTreeContaining(arc.v2);
			
			p.merge(ptwo);
						
			ptlist.append(p);
			}
					
		}

		return result;
	}
}
