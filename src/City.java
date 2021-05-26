//Omar Mustafa Dalal 1180171
import java.util.ArrayList;

public class City {

	//Attributes
	private String name;
	private float x;
	private float y;
	private ArrayList<CNode> neighbours; //ArrayList that contains all neighboring cities
	private float cost; //Cost from start city to current city
	private City parent; //Previous city (used for displaying the path)
	
	//Constructor
	public City(String name, float x, float y) {
		this.name = name;
		this.x = x;
		this.y = y;
		this.cost = -1;
		neighbours = new ArrayList<>();
	}
	
	//Setters and Getters
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public ArrayList<CNode> getNeighbours() {
		return neighbours;
	}

	public void setNeighbours(ArrayList<CNode> neighbours) {
		this.neighbours = neighbours;
	}

	public float getCost() {
		return cost;
	}

	public void setCost(float cost) {
		this.cost = cost;
	}

	public City getParent() {
		return parent;
	}

	public void setParent(City parent) {
		this.parent = parent;
	}
	
	//Reset cost and parent variables
	public void clearVars() {
		cost = -1;
		parent = null;
	}

	//return city as a string
	@Override
	public String toString() {
		return "Name: "+name+", ("+x+", "+y+")\n";
	}
	
	//Check if two cities are equal by name
	public boolean equals(City city) {
		return this.name.equals(city.getName());
	}
}
