//Omar Mustafa Dalal 1180171
import java.util.ArrayList;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

//Map class which is a modified version of the StackPane that contains multiple layers to display the map
public class Map extends StackPane {

	//Attributes
	private Pane CBPane;
	private Pane LBPane;
	private Canvas canvas;
	private int selectedCount = 0;
	private int src = -1;
	private int dest = -1;
	private ComboBox<String> srcCB;
	private ComboBox<String> destCB;
	private Scene scene;

	//Constructor
	public Map(ComboBox<String> srcCB, ComboBox<String> destCB, Scene scene) {
		super();
		this.srcCB = srcCB;
		this.scene = scene;
		this.destCB = destCB;
		
		Image img = new Image("palMap.jpg", 300, 900, false, false);
		ImageView imgView = new ImageView(img);
		
		canvas = new Canvas(img.getWidth(),img.getHeight());
		
		CBPane = new Pane();
		LBPane = new Pane();
		CBPane.setMaxWidth(300);
		LBPane.setMaxWidth(300);
		
		super.getChildren().addAll(imgView, canvas, LBPane, CBPane);
	}
	
	//Draw all roads between cities
	public void drawAllRoutes(City[] cities) {
		GraphicsContext GC = canvas.getGraphicsContext2D();
		for (int i=0; i<cities.length; i++) {
			GC.setStroke(Color.web("55bb55"));
			for (int j=0; j<cities[i].getNeighbours().size();j++) {
				City neighbour = cities[i].getNeighbours().get(j).getCity();
				GC.strokeLine(cities[i].getX(), cities[i].getY(), neighbour.getX(), neighbour.getY());
			}
			GC.setStroke(Color.RED);
			GC.fillOval(cities[i].getX(), cities[i].getY(), 5, 5);
		}
	}
	
	//Clear map
	public void clearAll(City[] cities, boolean redraw) {
		clearRoutes();
		clearCB(cities, redraw);
		clearLb();
	}
	
	//Clear Routes
	public void clearRoutes() {
		GraphicsContext CG = canvas.getGraphicsContext2D();
		CG.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
	}
	
	//Clear selected cities
	public void clearCB(City[] cities, boolean redraw) {
		CBPane.getChildren().clear();
		if (redraw) {
			drawCities(cities);
		}
		src = -1;
		dest = -1;
		selectedCount = 0;
	}
	
	//clear LBPane which contains labels that display city names when drawing a path
	public void clearLb() {
		LBPane.getChildren().clear();
	}
	
	public int getSrcIndex() {
		return this.src;
	}
	
	public int getDestIndex() {
		return this.dest;
	}
	
	//Draw a route on the map
	public void drawRoute(ArrayList<City> cities) {
		GraphicsContext GC = canvas.getGraphicsContext2D();
		City cF = cities.get(0);
		Label fLbl = new Label(cF.getName());
		fLbl.setStyle("-fx-font-weight:bold");
		GC.fillOval(cF.getX(), cF.getY(), 5, 5);
		fLbl.setTextFill(Color.RED);
		fLbl.setLayoutX(cF.getX()+5);
		fLbl.setLayoutY(cF.getY()-5);
		LBPane.getChildren().add(fLbl);
		for (int i=0; i<cities.size()-1; i++) {
			City c1 = cities.get(i);
			City c2 = cities.get(i+1);
			GC.setStroke(Color.web("55bbaa"));
			GC.fillOval(c1.getX(), c1.getY(), 5, 5);
			GC.strokeLine(c1.getX(), c1.getY(), c2.getX(), c2.getY());
			GC.strokeLine(c1.getX()-1, c1.getY()-1, c2.getX()-1, c2.getY()-1);
			GC.strokeLine(c1.getX()+1, c1.getY()+1, c2.getX()+1, c2.getY()+1);
			if (i!=0) {
				Label lbl = new Label(c1.getName());
				lbl.setLayoutX(c1.getX()+5);
				lbl.setLayoutY(c1.getY()-5);
				LBPane.getChildren().add(lbl);
			}
		}
		City c = cities.get(cities.size()-1);
		Label lbl = new Label(c.getName());
		lbl.setStyle("-fx-font-weight:bold");
		GC.fillOval(c.getX(), c.getY(), 5, 5);
		lbl.setLayoutX(c.getX()+5);
		lbl.setLayoutY(c.getY()-5);
		lbl.setTextFill(Color.RED);
		LBPane.getChildren().add(lbl);
	}
	
	//Select a city on map (used when choosing a city from a combo box)
	public void select(String cityName, City[] cities, boolean isSrc) {
		try {
			RadioButton newRb = (RadioButton)scene.lookup("#"+cityName);
			int index = findCity(cityName, cities);
			int ogVal=isSrc?src:dest;
			if (ogVal!=-1) {
				RadioButton oldRb = (RadioButton)scene.lookup("#"+cities[ogVal].getName());
				oldRb.setSelected(false);
				oldRb.setStyle(smFont+blackColor+bold);
			}
			newRb.setTextFill(Color.WHITE);
			if (isSrc) {
				src = index;
			} else {
				dest = index;
			}
			newRb.setSelected(true);
			CBPane.getChildren().remove(newRb);
			CBPane.getChildren().add(newRb);
			newRb.setStyle(lgFont+greenBG+bold+"-fx-color:white;");
			newRb.setText(cityName+" ");
		} catch (NullPointerException ex) {}
	}
	
	//Styles for radio buttons
	private final String smFont = "-fx-font-size:8px;";
	private final String lgFont = "-fx-font-size:15px;";
	private final String bold = "-fx-font-weight:bold;";
	private final String blackColor = "-fx-color:#555;";
	private final String greenColor = "-fx-color:#55bbaa;";
	private final String greenBG = "-fx-background-color:#55bbaa;";
	public void drawCities(City[] cities) {
		for (City city: cities) {
			RadioButton rb = new RadioButton(city.getName());
			rb.setTextFill(Color.BLACK);
			rb.setId(city.getName());
			rb.setStyle(smFont+blackColor+bold);
			rb.setOnMouseEntered(e-> {
				if (rb.isSelected()) {
					rb.setStyle(lgFont+greenBG+bold);
				} else {
					rb.setTextFill(Color.WHITE);
					rb.setStyle(lgFont+greenBG+bold);
				}
			});
			rb.setOnMouseExited(e-> {
				if (rb.isSelected()) {
					rb.setStyle(lgFont+greenBG+bold);
				} else {
					rb.setTextFill(Color.BLACK);
					rb.setStyle(smFont+blackColor+bold);
				}
			});
			rb.setOnAction(e-> {
				if (rb.isSelected()&&selectedCount<2) {
					selectedCount++;
					int index = findCity(rb.getId(), cities);
					if (src==-1&&dest==-1) {
						src=index;
						srcCB.getSelectionModel().select(index);
					} else if (src==-1) {
						src=index;
						srcCB.getSelectionModel().select(index);
					} else if (dest==-1) {
						dest=index;
						destCB.getSelectionModel().select(index);
					}
				} else if (rb.isSelected()&&selectedCount>=2) { 
					rb.setSelected(false);
				} else {
					selectedCount--;
					int index = findCity(rb.getId(), cities);
					if (selectedCount==1) {
						if (index==dest) {
							dest=-1;
						} else if (src==index) {
							src=-1;
						}
					} else {
						dest=-1;
						src=-1;
					}
				}
				if (rb.isSelected()) {
					CBPane.getChildren().remove(rb);
					CBPane.getChildren().add(rb);
					rb.setStyle(lgFont+greenColor+greenBG+bold);
					rb.setText(city.getName()+" ");
				} else {
					rb.setStyle(smFont+blackColor+bold);
				}
			});
			rb.setLayoutX(city.getX());
			rb.setLayoutY(city.getY());
			Tooltip tooltip = new Tooltip(city.getName());
			tooltip.setStyle("-fx-font-size:15px");
			rb.setTooltip(tooltip);
			CBPane.getChildren().add(rb);
		}
	}
	//Look for city in array by name
	public int findCity(String cityName, City[] cities) {
		for (int i=0; i<cities.length; i++) {
			if (cityName.equals(cities[i].getName())) {
				return i;
			}
		}
		return -1;
	}
}	
