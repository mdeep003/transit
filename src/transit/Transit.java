package transit;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


/**
 * This class contains methods which perform various operations on a layered linked
 * list to simulate transit
 * 
 * Dheeptha Meruva 
 * 
 * @author Ishaan Ivaturi
 * @author Prince Rawal
 */
public class Transit {
	private TNode trainZero; // a reference to the zero node in the train layer

	/* 
	 * Default constructor used by the driver and Autolab. 
	 * DO NOT use in your code.
	 * DO NOT remove from this file
	 */ 
	public Transit() { trainZero = null; }

	/* 
	 * Default constructor used by the driver and Autolab. 
	 * DO NOT use in your code.
	 * DO NOT remove from this file
	 */
	public Transit(TNode tz) { trainZero = tz; }
	
	/*
	 * Getter method for trainZero
	 *
	 * DO NOT remove from this file.
	 */
	public TNode getTrainZero () {
		return trainZero;
	}

	/**
	 * Makes a layered linked list representing the given arrays of train stations, bus
	 * stops, and walking locations. Each layer begins with a location of 0, even though
	 * the arrays don't contain the value 0. Store the zero node in the train layer in
	 * the instance variable trainZero.
	 * 
	 * @param trainStations Int array listing all the train stations
	 * @param busStops Int array listing all the bus stops
	 * @param locations Int array listing all the walking locations (always increments by 1)
	 */
	public void makeList(int[] trainStations, int[] busStops, int[] locations) {

		this.trainZero= new TNode();

		TNode zeroBus = new TNode();
		TNode zeroLoc = new TNode();
		
		this.trainZero.setDown(zeroBus);
		zeroBus.setDown(zeroLoc);
		
		TNode prevBus = zeroBus;
		TNode nextBus = null;

		TNode prevLoc = zeroLoc;
		TNode nextLoc = null;
		 
		TNode nextTrain = null;
		TNode prevTrain = this.trainZero;
		
		
		int bIndex= -1;
		int tIndex = -1;
		
	
		
		for(int i=0;i<locations.length;i++) {
			
			nextLoc= new TNode(locations[i]);
			prevLoc.setNext(nextLoc);

			bIndex = existingC(locations[i],busStops);

			if(bIndex > -1) 
			{
				
				nextBus = new TNode(busStops[bIndex]);
				
				prevBus.setNext(nextBus);
				nextBus.setDown(nextLoc);

				prevBus = nextBus;
				
				tIndex= existingC (locations[i],trainStations);

				if(tIndex > -1) 
				{
					nextTrain = new TNode(trainStations[tIndex]);
					
					prevTrain.setNext(nextTrain);
					nextTrain.setDown(nextBus);

					prevTrain = nextTrain;
					
				}	
			}
			
			prevLoc = nextLoc;
		}
		
	}
		
	int existingC(int value,int values[]) 
	{
		
		for(int j=0;j<values.length;j++) 
		{
			if(values[j]==value) 
			{
				return j;
			}
		}
		return -1;
	}		
	
	
	/**
	 * Modifies the layered list to remove the given train station but NOT its associated
	 * bus stop or walking location. Do nothing if the train station doesn't exist
	 * 
	 * @param station The location of the train station to remove
	 */
	public void removeTrainStation(int station) {
		TNode currents = trainZero.getNext();
		TNode prevStops=trainZero;

		while(currents!=null)
		{
			if (currents.getLocation()==station)
			{
				prevStops.setNext(currents.getNext());
			}
			prevStops = currents;
			currents = currents.getNext();
		}
	}

	/**
	 * Modifies the layered list to add a new bus stop at the specified location. Do nothing
	 * if there is no corresponding walking location.
	 * 
	 * @param busStop The location of the bus stop to add
	 */
	public void addBusStop(int busStop) {
		TNode zerosBusStop = trainZero.getDown();
		TNode current = zerosBusStop;
		TNode downStop = new TNode();

		while(current.getLocation()<busStop){
		if (current.getNext().getLocation()>busStop)
		{

		//tNode start, int end dwn = walkTo(current.getDown(), busStop);
		TNode currentLoc = current.getDown();
		for(; currentLoc!=null&&currentLoc.getLocation()<busStop;currentLoc=currentLoc.getNext());
		if (currentLoc.getLocation() == busStop)
		{
			downStop = currentLoc;
		}

		TNode newBus = new TNode(busStop, current.getNext(), downStop);
		current.setNext(newBus);
		}


		current=current.getNext();
}
	}
	
	/**
	 * Determines the optimal path to get to a given destination in the walking layer, and 
	 * collects all the nodes which are visited in this path into an arraylist. 
	 * 
	 * @param destination An int representing the destination
	 * @return
	 * 
	 * 
	 */
	public ArrayList<TNode> bestPath(int destination) {
		ArrayList<TNode> pathTracker = new ArrayList<>();


		//ArrayList<TNode> trains=mapTo(trainZero, destination);
		ArrayList<TNode> search1 = new ArrayList<>();
		TNode firstLocs = trainZero;
		for(; firstLocs!=null && firstLocs.getLocation() <= destination; firstLocs=firstLocs.getNext())
		{
			search1.add(firstLocs);
		}
		ArrayList<TNode> trains = search1; 

		//ArrayList<TNode> busses=mapTo(trains.get(trains.size()-1).getDown(), destination);

		ArrayList<TNode> search2 = new ArrayList<>();
		TNode secondLocs = trains.get(trains.size()-1).getDown();
		for(; secondLocs!=null && secondLocs.getLocation() <= destination; secondLocs =secondLocs.getNext())
		{
			search2.add(secondLocs);
		}
		ArrayList<TNode> busses = search2; 

		//ArrayList<TNode> locs=mapTo(busses.get(busses.size()-1).getDown(), destination);
		ArrayList<TNode> search3 = new ArrayList<>();
		TNode thirdLocs = busses.get(busses.size()-1).getDown(); 
		for(; thirdLocs!=null && thirdLocs.getLocation() <= destination; thirdLocs =thirdLocs.getNext())
		{
			search3.add(thirdLocs);
		}
		ArrayList<TNode> locs = search3; 


		
		pathTracker.addAll(trains);
		pathTracker.addAll(busses);
		pathTracker.addAll(locs);

		return pathTracker;
	   
	}

	/**
	 * Returns a deep copy of the given layered list, which contains exactly the same
	 * locations and connections, but every node is a NEW node.
	 * 
	 * @return A reference to the train zero node of a deep copy
	 */
	public TNode duplicate() {

		TNode deepTrain = new TNode();
		TNode midtrainT= deepTrain; 

		for(TNode x=trainZero;x!=null;x=x.getNext())
		{
			midtrainT.setDown(x.getDown());
			if(x.getNext()!=null)
			{
				TNode supertrain = new TNode(x.getNext().getLocation());
				midtrainT.setNext(supertrain);
				midtrainT=supertrain; 
			}
		}

		TNode deepBus = new TNode();
		TNode midtrainB= deepBus; 

		for(TNode x=trainZero.getDown();x!=null;x=x.getNext())
		{
			midtrainB.setDown(x.getDown());
			if(x.getNext()!=null)
			{
				TNode supertrain = new TNode(x.getNext().getLocation());
				midtrainB.setNext(supertrain);
				midtrainB=supertrain; 
			}
		}

		TNode deepWalk= new TNode();
		TNode midtrainW=deepWalk; 

		for(TNode x=trainZero.getDown().getDown();x!=null;x=x.getNext())
		{
			midtrainW.setDown(x.getDown());
			if(x.getNext()!=null)
			{
				TNode supertrain = new TNode(x.getNext().getLocation());
				midtrainW.setNext(supertrain);
				midtrainW=supertrain; 
			}
		}

		deepTrain.setDown(deepBus);
		deepBus.setDown(deepWalk);

		for(TNode x=deepTrain;x!=null;x=x.getNext())
		{
			for(TNode b=deepBus;b!=null;b=b.getNext())
			{
				if(x.getLocation()==b.getLocation())
				{
					x.setDown(b);
				}
			}
		}

		for(TNode x=deepBus;x!=null;x=x.getNext())
		{
			for(TNode b=deepWalk;b!=null;b=b.getNext())
			{
				if(x.getLocation()==b.getLocation())
				{
					x.setDown(b);
				}
			}
		}

		return deepTrain; 
		
	}

	private TNode searchingN(TNode n,int location){
			
		TNode currNode = n;
		//Iterate list till next is null and location is less than search location
		while(currNode != null ) 
		{
			
			if(currNode.getLocation() == location) 
			{
				return currNode;
			}
			currNode = currNode.getNext();
		}
		return null;
	}

	/**
	 * Modifies the given layered list to add a scooter layer in between the bus and
	 * walking layer.
	 * 
	 * @param scooterStops An int array representing where the scooter stops are located
	 */
	public void addScooter(int[] scooterStops) {

		
		TNode zBus = this.trainZero.getDown();
		TNode lZero = this.trainZero.getDown().getDown();
		
		TNode zeroScoots = new TNode();
		zeroScoots.setDown(lZero);
		zBus.setDown(zeroScoots);
		
		TNode prevScoots= zeroScoots;
		
		
		
		
		for(int i=0 ;i <scooterStops.length;i++ ) 
		{
			
			TNode sNode = new TNode(scooterStops[i]);
			prevScoots.setNext(sNode);
			
			TNode lNode = searchingN(lZero,scooterStops[i]);
			sNode.setDown(lNode);
			
			TNode bNode = searchingN(zBus,scooterStops[i]);
			if(bNode != null) {
				bNode.setDown(sNode);
			}
			
			prevScoots = sNode;

		}

	}

	/**
	 * Used by the driver to display the layered linked list. 
	 * DO NOT edit.
	 */
	public void printList() {
		// Traverse the starts of the layers, then the layers within
		for (TNode vertPtr = trainZero; vertPtr != null; vertPtr = vertPtr.getDown()) {
			for (TNode horizPtr = vertPtr; horizPtr != null; horizPtr = horizPtr.getNext()) {
				// Output the location, then prepare for the arrow to the next
				StdOut.print(horizPtr.getLocation());
				if (horizPtr.getNext() == null) break;
				
				// Spacing is determined by the numbers in the walking layer
				for (int i = horizPtr.getLocation()+1; i < horizPtr.getNext().getLocation(); i++) {
					StdOut.print("--");
					int numLen = String.valueOf(i).length();
					for (int j = 0; j < numLen; j++) StdOut.print("-");
				}
				StdOut.print("->");
			}

			// Prepare for vertical lines
			if (vertPtr.getDown() == null) break;
			StdOut.println();
			
			TNode downPtr = vertPtr.getDown();
			// Reset horizPtr, and output a | under each number
			for (TNode horizPtr = vertPtr; horizPtr != null; horizPtr = horizPtr.getNext()) {
				while (downPtr.getLocation() < horizPtr.getLocation()) downPtr = downPtr.getNext();
				if (downPtr.getLocation() == horizPtr.getLocation() && horizPtr.getDown() == downPtr) StdOut.print("|");
				else StdOut.print(" ");
				int numLen = String.valueOf(horizPtr.getLocation()).length();
				for (int j = 0; j < numLen-1; j++) StdOut.print(" ");
				
				if (horizPtr.getNext() == null) break;
				
				for (int i = horizPtr.getLocation()+1; i <= horizPtr.getNext().getLocation(); i++) {
					StdOut.print("  ");

					if (i != horizPtr.getNext().getLocation()) {
						numLen = String.valueOf(i).length();
						for (int j = 0; j < numLen; j++) StdOut.print(" ");
					}
				}
			}
			StdOut.println();
		}
		StdOut.println();
	}
	
	/**
	 * Used by the driver to display best path. 
	 * DO NOT edit.
	 */
	public void printBestPath(int destination) {
		ArrayList<TNode> path = bestPath(destination);
		for (TNode vertPtr = trainZero; vertPtr != null; vertPtr = vertPtr.getDown()) {
			for (TNode horizPtr = vertPtr; horizPtr != null; horizPtr = horizPtr.getNext()) {
				// ONLY print the number if this node is in the path, otherwise spaces
				if (path.contains(horizPtr)) StdOut.print(horizPtr.getLocation());
				else {
					int numLen = String.valueOf(horizPtr.getLocation()).length();
					for (int i = 0; i < numLen; i++) StdOut.print(" ");
				}
				if (horizPtr.getNext() == null) break;
				
				// ONLY print the edge if both ends are in the path, otherwise spaces
				String separator = (path.contains(horizPtr) && path.contains(horizPtr.getNext())) ? ">" : " ";
				for (int i = horizPtr.getLocation()+1; i < horizPtr.getNext().getLocation(); i++) {
					StdOut.print(separator + separator);
					
					int numLen = String.valueOf(i).length();
					for (int j = 0; j < numLen; j++) StdOut.print(separator);
				}

				StdOut.print(separator + separator);
			}
			
			if (vertPtr.getDown() == null) break;
			StdOut.println();

			for (TNode horizPtr = vertPtr; horizPtr != null; horizPtr = horizPtr.getNext()) {
				// ONLY print the vertical edge if both ends are in the path, otherwise space
				StdOut.print((path.contains(horizPtr) && path.contains(horizPtr.getDown())) ? "V" : " ");
				int numLen = String.valueOf(horizPtr.getLocation()).length();
				for (int j = 0; j < numLen-1; j++) StdOut.print(" ");
				
				if (horizPtr.getNext() == null) break;
				
				for (int i = horizPtr.getLocation()+1; i <= horizPtr.getNext().getLocation(); i++) {
					StdOut.print("  ");

					if (i != horizPtr.getNext().getLocation()) {
						numLen = String.valueOf(i).length();
						for (int j = 0; j < numLen; j++) StdOut.print(" ");
					}
				}
			}
			StdOut.println();
		}
		StdOut.println();
	}
}
