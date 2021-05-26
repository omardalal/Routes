//Omar Mustafa Dalal 1180171
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
//Class containing methods used to read input files
public class ReadMethods {
	
	public static City[] readFiles(File cityFile, File connectionFile) {
		City[] cities = readCitiesFile(cityFile);
		readRoadsFile(connectionFile, cities);
		return cities;
	}
	
	//Method to read cities from a file
	private static City[] readCitiesFile(File file) {
		City[] cities;
		try {
			Scanner in = new Scanner(file);
			int V = in.nextInt();
			cities = new City[V];
			int cCount = 0;
			while (in.hasNextLine()&&cCount<V) {
				String cityName = in.next();
				float x = in.nextFloat();
				float y = in.nextFloat();
				City city = new City(cityName, x, y);
				cities[cCount++] = city;
			}
			in.close();
			return cities;
		} catch (FileNotFoundException ex) {
			System.out.println(ex);
		}
		return null;
	}
	
	//Method to read roads from a file
	private static void readRoadsFile(File file, City[] cities) {
		try {
			Scanner in = new Scanner(file);
			while (in.hasNextLine()) {
				String c1Name = in.next();
				String c2Name = in.next();
				float dist = in.nextFloat();
				City c1=null;
				City c2=null;
				for (int i=0; i<cities.length; i++) {
					if (c1Name.equals(cities[i].getName())) {
						c1 = cities[i];
					} else if (c2Name.equals(cities[i].getName())) {
						c2 = cities[i];
					}
					if (c1!=null&&c2!=null) {
						break;
					}
				}
				if (!checkExists(c1.getNeighbours(), c2)) {
					c1.getNeighbours().add(new CNode(c2, dist));
				}
				if (!checkExists(c2.getNeighbours(), c1)) {
					c2.getNeighbours().add(new CNode(c1, dist));
				}
			}
			in.close();
		} catch (FileNotFoundException ex) {
			System.out.println(ex);
		}
	}
	
	//method used to check if city already exists in ArrayList
	public static boolean checkExists(ArrayList<CNode> list, City city) {
		for (int i=0; i<list.size(); i++) {
			if (list.get(i).getCity().equals(city)) {
				return true;
			}
		}
		return false;
	}
}
