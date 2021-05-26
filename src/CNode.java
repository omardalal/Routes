//Omar Mustafa Dalal 1180171
//CNode class which is used for neighbour cities ArrayList
public class CNode {

	//Attributes
	private City city;
	private float distance;
	
	//Constructor
	public CNode (City city, float distance) {
		this.city = city;
		this.distance = distance;
	}
	
	//Setters and Getters
	public City getCity() {
		return this.city;
	}
	
	public void setCity(City city) {
		this.city = city;
	}
	
	public float getDistance() {
		return this.distance;
	}
	
	public void setDistance(float distance) {
		this.distance = distance;
	}
}
