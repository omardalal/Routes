//Omar Mustafa Dalal 1180171
import java.util.ArrayList;

//UCS class containing the Uniform Cost Search algorithm
public class UCS {

	//Attributes
	private City[] cities;
	private ArrayList<City> visited;
	private MinPQ pq;
	
	//Constructor
	public UCS(City[] cities) {
		this.cities = cities;
		pq = new MinPQ(cities.length);
		visited = new ArrayList<>();
	}
	
	//Algorithm to find path between two cities, calculate the distance, time and space complexities
	public float findPath(int src, int dest, ArrayList<City> route, int[] complexity) {
		clearVars();
		int time = 0;
		int space = 0;
		if (src==dest) {
			route.add(cities[src]);
			return 0;
		}
		pq.insert(new PQNode(cities[src], 0));
		cities[src].setCost(0);
		while (!pq.empty()) {
			PQNode cityNode = pq.dequeue();
			City city = cityNode.getCity();
			if (city==cities[dest]) {
				break;
			}
			visited.add(city);
			for (int i=0; i<city.getNeighbours().size(); i++) {
				CNode neighbour = city.getNeighbours().get(i);
				float nCost = city.getCost()+neighbour.getDistance();
				City nCity = neighbour.getCity();
				if (!visited.contains(nCity)&&!pq.contains(nCity)) {
					nCity.setParent(city);
					nCity.setCost(nCost);
					pq.insert(new PQNode(nCity, nCost));
					time++;
				} else if (pq.contains(nCity)&&nCost<nCity.getCost()) {
					nCity.setParent(city);
					nCity.setCost(nCost);
					pq.decreaseKey(nCity, nCost);
					time++;
				}
			}
			if (pq.size()>space) {
				space=pq.size();
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
		visited = new ArrayList<>();
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