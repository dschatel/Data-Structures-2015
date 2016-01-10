package apps;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;

import structures.Graph;
import structures.Vertex;

public class MSTdriver {

	static BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));
	
	public static void main(String[] args) throws IOException {
		System.out.print("Enter document file name => ");
		String document = keyboard.readLine();
		
		Graph graph = new Graph(document);
		
		
		PartialTreeList ptlist = MST.initialize(graph);
		PartialTree p1;
		p1 = ptlist.remove();
		PartialTree.Arc arc1 = (PartialTree.Arc)p1.getArcs().deleteMin();
		p1 = ptlist.removeTreeContaining(arc1.v2);
		
		Iterator <PartialTree> it = ptlist.iterator();
		
		while (it.hasNext()) {
			System.out.println(it.next());
		}	
		
		p1 = ptlist.remove();
		arc1 = (PartialTree.Arc)p1.getArcs().deleteMin();
		p1 = ptlist.removeTreeContaining(arc1.v2);
		
		it = ptlist.iterator();
		
		while (it.hasNext()) {
			System.out.println(it.next());
		}	
		
		
		//graph.print();
		
		/*PartialTreeList treelist = MST.initialize(graph);
		
		Iterator <PartialTree> it = treelist.iterator();
		
		while (it.hasNext()) {
			System.out.println(it.next());
		}*/
		
		//ArrayList<PartialTree.Arc> mst = new ArrayList<PartialTree.Arc>();
		
		//mst = MST.execute(graph, treelist);
		
		//System.out.println("The MST of this Graph is: " + mst);
		
		
		
		
	}

}
