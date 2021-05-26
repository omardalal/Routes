//Omar Mustafa Dalal 1180171
public class MinPQ {
	
	//Attributes
	private PQNode[] heapArr;
	private int nodeCount;
	private int maxNodeCount;
	private final int START = 1;
	
	//Constructor
	public MinPQ(int maxCount) {
		this.heapArr = new PQNode[maxCount+1];
		this.maxNodeCount = maxCount+1;
		this.nodeCount = 1;
	}
	
	//Check if pq is empty
	public boolean empty() {
		return nodeCount==1;
	}
	
	//Check if pq contains a specific city
	public boolean contains(City city) {
		for (int i=START; i<nodeCount; i++) {
			if (heapArr[i].getCity().equals(city)) {
				return true;
			}
		}
		return false;
	}
	
	//Insert new node into the minheap
	public void insert(PQNode PQNode) {
		if (nodeCount==START) {
			heapArr[START] = PQNode;
			nodeCount++;
		} else if (nodeCount<maxNodeCount) {
			heapArr[nodeCount] = PQNode;
			int c = nodeCount;
			while (heapArr[c].compareTo(heapArr[parent(c)])<0) {
				swap(c, parent(c));
				if (parent(c)!=START) {
					c = parent(c);
				}
			}
			nodeCount++;
		} 
	}
	
	//Remove and return the root node
	public PQNode dequeue() {
		if (nodeCount>=1) {
			PQNode rootPQNode = swap(START, nodeCount-1);
			nodeCount--;
			fixHeap(START);
			return rootPQNode;
		}
		return null;
	}
	
	//Change the value for a city
	public void decreaseKey(City city, float newVal) {
		int index = findIndex(city);
		if (index>=START) {
			heapArr[index].setDist(newVal);
			int c = nodeCount-1;
			while (c != START && heapArr[c].compareTo(heapArr[parent(c)])<0){
				swap(c, parent(c));
				c = parent(c);
			}
		}
	}

	//Find index of a city in heap array
	private int findIndex(City city) {
		for (int i=START; i<nodeCount; i++) {
			if (heapArr[i].getCity().equals(city)) {
				return i;
			}
		}
		return -1;
	}

	//Return the current count
	public int size() {
		return this.nodeCount-1;
	}
	
	//Check if a given PQNode is a leaf
	private boolean leafNode(int index) {
		if (index<=nodeCount&&index>=nodeCount/2) {
			return true;
		} else {
			return false;
		}
	}
	
	//private method inside the minheap used to keep the minheap property of the property queue
	private void fixHeap(int index) {
		if (!leafNode(index)) {
			if (heapArr[index].compareTo(heapArr[left(index)])>=0||heapArr[index].compareTo(heapArr[right(index)])>=0) {
				if (heapArr[right(index)].compareTo(heapArr[left(index)])>=0) {
					swap(index, left(index));
					if (left(index)!=START) {
						fixHeap(left(index));
					}
				} else if (heapArr[left(index)].compareTo(heapArr[right(index)])>=0) {
					swap(index, right(index));
					if (right(index)!=START) {
						fixHeap(right(index));
					}
				}
			}
		}
		if ((nodeCount-1)==2) {
			if (heapArr[START].compareTo(heapArr[START+1])>=0) {
				swap(START, START+1);
			}
		}
	}
	
	public void print() {
		for (int i=START; i<nodeCount; i++) {
			System.out.print(heapArr[i].getCity().getName()+", ");
		}
		System.out.println();
	}
	
	//Method used to switch the positions of two nodes
	private PQNode swap(int index1, int index2) {
		PQNode tmp = heapArr[index1];
		heapArr[index1] =  heapArr[index2];
		heapArr[index2] = tmp;
		return tmp;
	}
	
	//Get parent of index
	private int parent(int index) {
		return index/2;
	}
	
	//Private method that returns the left node for a given parent
	private int left(int index) {
		return index*2;
	}
	
	//Private method that returns the right node for a given parent
	private int right(int index) {
		return index*2 + 1;
	}
}
