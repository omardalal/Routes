//Omar Mustafa Dalal 1180171
import java.util.ArrayList;

//AStar class containing the algorithm for finding a path using A*
public class AStar {

	//Attributes
	private City[] cities;
	private MinPQ pq;
	private ArrayList<City> visited;
	
	//Constructor
	public AStar(City[] cities) {
		this.cities = cities;
		pq = new MinPQ(cities.length);
		visited = new ArrayList<>();
	}
	
	//Algorithm to find path between two cities, calculate the distance, time and space complexities
	public float findPath(int src, int dest, ArrayList<City> route, int[] complexity) {
		clearVars();
		if (src==dest) {
			route.add(cities[src]);
			return 0;
		}
		int time = 0;
		int space = 0;
		pq.insert(new PQNode(cities[src], heuristic(cities[src], cities[dest])));
		cities[src].setCost(0);
		while (!pq.empty()) {
			City city = pq.dequeue().getCity();
			if (city==cities[dest]) {
				break;
			}
			visited.add(city);
			for (int i=0; i<city.getNeighbours().size(); i++) {
				CNode neighbour = city.getNeighbours().get(i);
				City nCity = neighbour.getCity();
				float nCost = city.getCost()+neighbour.getDistance();
				if (!visited.contains(nCity)) {
					if (!pq.contains(nCity)) {
						nCity.setCost(nCost);
						nCity.setParent(city);
						pq.insert(new PQNode(nCity, nCost+heuristic(cities[dest], nCity)));
						time++;
					} else if (pq.contains(nCity)&&nCost<nCity.getCost()) {
						nCity.setCost(nCost);
						nCity.setParent(city);
						pq.decreaseKey(nCity, nCost+heuristic(cities[dest], nCity));
						time++;
					}
				}
			}
			if (pq.size()>space) {
				space = pq.size();
			}
		}
		City currentCity = cities[dest];
		route.add(currentCity);
		while (!(currentCity = currentCity.getParent()).equals(cities[src])) {
			route.add(currentCity);
		}
		route.add(currentCity);
		complexity[0] = time;
		complexity[1] = space;
		return cities[dest].getCost();
	}
	
	//Reset variables
	private void clearVars() {
		pq = new MinPQ(cities.length);
		for (int i=0; i<cities.length; i++) {
			cities[i].clearVars();
		}
		visited.clear();
	}

	//Method to calculate heuristic between two cities using Euclidean distance
	public static float heuristic(City city1, City city2) {
		float x1 = city1.getX(), y1 = city1.getY();
		float x2 = city2.getX(), y2 = city2.getY();
		return (float)Math.sqrt(Math.pow(x2-x1, 2)+Math.pow(y2-y1, 2));
	}
	
	//Find city in array by name
	public int findCity(String cityName) {
		for (int i=0; i<cities.length; i++) {
			if (cityName.equals(cities[i].getName())) {
				return i;
			}
		}
		return -1;
	}
}
