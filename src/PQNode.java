//Omar Mustafa Dalal 1180171
//PQNode used for priority queue
public class PQNode implements Comparable<PQNode> {

	//Attributes
	private float dist;
	private City city;
	
	//Constructor
	public PQNode(City city, float dist) {
		this.city = city;
		this.dist = dist;
	}
	
	//Setters and Getters
	public void setCity(City city) {
		this.city = city;
	}
	
	public City getCity() {
		return this.city;
	}
	
	public void setDist(float dist) {
		this.dist = dist;
	}
	
	public float getDist() {
		return this.dist;
	}

	//Compare Method
	@Override
	public int compareTo(PQNode node) {
		if (dist>node.getDist()) {
			return 1;
		} else if (dist<node.getDist()) {
			return -1;
		}
		return 0;
	}

}
