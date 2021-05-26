//Omar Mustafa Dalal 1180171
import java.io.File;
import java.util.ArrayList;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class Main extends Application {

	public static void main(String[] args) {
		launch();
	}
	
	//Variables for interface elements and other data
	private final String BOX_STYLE = "-fx-background-color:#fff;-fx-background-radius:10px;-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.16), 25px, 0, 0, 0);";
	private UCS ucs;
	private AStar astar;
	private Map map;
	private City[] citiesArr;
	
	private double windowHeight = 900;
	private double windowWidth = 1300;
	private Stage window;
	//Start method to run the program
	@Override
	public void start(Stage pS) throws Exception {
		Pane bgPane = new Pane();
		HBox root = new HBox(25);
		adjustHeight(bgPane);
		bgPane.getChildren().addAll(root);
		Scene scene = new Scene(bgPane, windowWidth, windowHeight);
		map = new Map(srcCB, destCB, scene);
		VBox right = getRightSide();
		root.getChildren().addAll(map, right);
		window = pS;
		pS.setScene(scene);
		pS.setResizable(false);
		pS.setTitle("Maps");
		pS.show();
	}

	private ComboBox<String> srcCB = new ComboBox<>();
	private ComboBox<String> destCB = new ComboBox<>();
	private int[] complexity;
	
	private Button runBtn;
	private Button clearBtn;
	private Button drawAllBtn;
	private FlowPane routePane;
	
	//Setting up the interface
	public VBox getRightSide() {
		VBox root = new VBox(15);
		root.setAlignment(Pos.CENTER);
		root.setStyle("-fx-font-size: 20px;");
		
		Label bigTitleLbl = new Label("Palestine Map");
		bigTitleLbl.setPadding(new Insets(0,0,15,0));
		bigTitleLbl.setStyle("-fx-font-weight:bold;-fx-font-size:35px");
		
		HBox topBox = new HBox(15);
		VBox fileBox = getFileBox();
		
		HBox columnsBox = new HBox(15);
		VBox columnOne = new VBox(20);
		columnOne.setPadding(new Insets(0, 0, 0, 15));
		columnOne.setAlignment(Pos.TOP_CENTER);
		
		VBox selectBox = getSelectPathBox();
		
		VBox routeBox = getRouteBox();
		routePane = new FlowPane();
		routePane.setVgap(15);
		ScrollPane routeScrollPane = getRouteScroll();
		routeScrollPane.setContent(routePane);
		routeBox.getChildren().addAll(routeScrollPane);
		
		VBox btnsBox = getBtnsBox();
		
		VBox complexityBox = getComplexityBox();
		columnOne.getChildren().addAll(selectBox, complexityBox);
		
		VBox columnTwo = new VBox(20);
		columnTwo.setAlignment(Pos.TOP_CENTER);
		columnTwo.getChildren().addAll(routeBox);
		
		topBox.getChildren().addAll(fileBox, btnsBox);
		topBox.setAlignment(Pos.CENTER);
		
		columnsBox.getChildren().addAll(columnOne, columnTwo);
		root.getChildren().addAll(bigTitleLbl, topBox, columnsBox);
		return root;
	}
	
	//Setting up the select file box
	private File citiesFile;
	private File roadsFile;
	private VBox getFileBox() {
		VBox root = new VBox(15);
		root.setPadding(new Insets(15));
		root.setAlignment(Pos.CENTER);
		root.setStyle(BOX_STYLE);
		root.setPrefWidth(610);
		
		Label titleLbl = new Label("Select Files");
		titleLbl.setStyle("-fx-font-size: 25px;-fx-font-weight:bold;");
		
		HBox citiesRow = new HBox(10);
		citiesRow.setAlignment(Pos.CENTER);
		Label citiesLbl = new Label("Towns File");
		TextField citiesField = new TextField();
		citiesField.setPrefWidth(350);
		citiesField.setDisable(true);
		citiesField.setPromptText("Towns File Location");
		Button cityBtn = new Button("Browse");
		cityBtn.setOnAction(e->{
			FileChooser chooser = new FileChooser();
			chooser.getExtensionFilters().add(new ExtensionFilter("Text File", "*.csv"));
			citiesFile = chooser.showOpenDialog(window);
			if (citiesFile!=null) {
				citiesField.setText(citiesFile.getAbsolutePath());
			} else {
				citiesField.setText("");
			}
		});
		citiesRow.getChildren().addAll(citiesLbl, citiesField, cityBtn);
		
		HBox roadsRow = new HBox(10);
		roadsRow.setAlignment(Pos.CENTER);
		Label roadsLbl = new Label("Roads File");
		TextField roadsField = new TextField();
		roadsField.setPrefWidth(350);
		roadsField.setDisable(true);
		roadsField.setPromptText("Roads File Location");
		Button roadsBtn = new Button("Browse");
		roadsBtn.setOnAction(e->{
			FileChooser chooser = new FileChooser();
			chooser.getExtensionFilters().add(new ExtensionFilter("Text File", "*.csv"));
			roadsFile = chooser.showOpenDialog(window);
			if (roadsFile!=null) {
				roadsField.setText(roadsFile.getAbsolutePath());
			} else {
				roadsField.setText("");
			}
		});
		roadsRow.getChildren().addAll(roadsLbl, roadsField, roadsBtn);
		
		Button readyBtn = new Button("Ready");
		readyBtn.setOnAction(e-> {
			try {
				citiesArr = ReadMethods.readFiles(citiesFile, roadsFile);
				ucs = new UCS(citiesArr);
				astar = new AStar(citiesArr);
				map.drawCities(citiesArr);
				for (int i=0; i<citiesArr.length; i++) {
					destCB.getItems().add(citiesArr[i].getName());
					srcCB.getItems().add(citiesArr[i].getName());
				}
				runBtn.setDisable(false);
				clearBtn.setDisable(false);
				drawAllBtn.setDisable(false);
				readyBtn.setDisable(true);
				cityBtn.setDisable(true);
				roadsBtn.setDisable(true);
			} catch (NullPointerException ex) {
				alert("Enter both files first!");
				roadsField.setText("");
				citiesField.setText("");
				citiesFile = null;
				roadsFile = null;
			} catch (Exception ex) {
				alert("Enter valid files!");
				roadsField.setText("");
				citiesField.setText("");
				citiesFile = null;
				roadsFile = null;
			}
		});
		
		root.getChildren().addAll(titleLbl, citiesRow, roadsRow, readyBtn);
		return root;
	}

	private Label timeLbl;
	private Label spaceLbl;
	
	//Setting up the box containing time and space complexity
	private VBox getComplexityBox() {
		VBox root = new VBox (5);
		root.setPadding(new Insets(15));
		root.setStyle(BOX_STYLE);
		root.setPrefSize(450, 100);
		root.setMaxWidth(450);
		
		VBox titleBox = new VBox();
		titleBox.setAlignment(Pos.CENTER);
		Label titleLbl = new Label("Complexity");
		titleLbl.setStyle("-fx-font-size: 25px;-fx-font-weight:bold;");
		titleBox.getChildren().add(titleLbl);
		
		timeLbl = new Label("Time: ");
		spaceLbl = new Label("Space: ");
		
		root.getChildren().addAll(titleBox, timeLbl, spaceLbl);
		return root;
	}
	
	//Styling the select path box
	private VBox getSelectPathBox() {
		VBox selectBox = new VBox(15);
		selectBox.setAlignment(Pos.CENTER);
		selectBox.setPadding(new Insets(15));
		selectBox.setStyle(BOX_STYLE);
		selectBox.setPrefWidth(450);
		
		Label titleLbl = new Label("Find Route");
		titleLbl.setStyle("-fx-font-size:30px; -fx-font-weight: bold;");
		titleLbl.setAlignment(Pos.CENTER);
		
		HBox srcBox = new HBox(15);
		srcBox.setAlignment(Pos.CENTER);
		Label srcLbl = new Label("Source City");
		srcLbl.setPrefWidth(150);
		srcCB.setValue("Select Source");
		srcCB.setPrefWidth(250);
		srcCB.setOnAction(e-> {
			map.select(srcCB.getValue(), citiesArr, true);
		});
		srcBox.getChildren().addAll(srcLbl, srcCB);
		
		HBox destBox = new HBox(15);
		destBox.setAlignment(Pos.CENTER);
		Label destLbl = new Label("Destination City");
		destLbl.setPrefWidth(150);
		destCB.setValue("Select Destination");
		destCB.setPrefWidth(250);
		destBox.getChildren().addAll(destLbl, destCB);
		destCB.setOnAction(e-> {
			map.select(destCB.getValue(), citiesArr, false);
		});
		
		HBox algorithmBox = new HBox(15);
		algorithmBox.setAlignment(Pos.CENTER);
		Label algorithmLbl = new Label("Algorithm");
		algorithmLbl.setPadding(new Insets(0,30,0,0));
		RadioButton ucsRB = new RadioButton("Uniform Cost Search");
		ucsRB.setSelected(true);
		ucsRB.setStyle("-fx-font-size:16px");
		RadioButton aStarRB = new RadioButton("A Star");
		aStarRB.setStyle("-fx-font-size:16px");
		ToggleGroup algTG = new ToggleGroup();
		ucsRB.setToggleGroup(algTG);
		aStarRB.setToggleGroup(algTG);
		algorithmBox.getChildren().addAll(algorithmLbl, ucsRB, aStarRB);
		
		runBtn = new Button("Find Route");
		runBtn.setDisable(true);
		runBtn.setAlignment(Pos.CENTER);
		runBtn.setOnAction(e->{
			runClicked(ucsRB.isSelected());
		});
		
		selectBox.getChildren().addAll(titleLbl, srcBox, destBox, algorithmBox, runBtn);
		return selectBox;
	}
	
	//Method called when find path is clicked
	private void runClicked(boolean ucsSelected) {
		try {
			complexity = new int[2];
			if (ucsSelected) {
				map.clearAll(citiesArr, false);
				int srcIndex = ucs.findCity(srcCB.getValue());
				int destIndex = ucs.findCity(destCB.getValue());
				ArrayList<City> route = new ArrayList<>();
				float dist = ucs.findPath(srcIndex, destIndex, route, complexity);
				distLbl.setText("Distance: "+String.format("%.2f", dist)+" Km");
				demonstrateRoute(routePane, route);
				timeLbl.setText("Time: "+complexity[0]+" (Rounds)");
				spaceLbl.setText("Space: "+complexity[1]+" (Maximun Node Count in PQ)");
				map.drawRoute(route);
			} else {
				map.clearAll(citiesArr, false);
				int srcIndex = astar.findCity(srcCB.getValue());
				int destIndex = astar.findCity(destCB.getValue());
				ArrayList<City> route = new ArrayList<>();
				float dist = astar.findPath(srcIndex, destIndex, route, complexity);
				distLbl.setText("Distance: "+String.format("%.2f", dist)+" Km");
				demonstrateRoute(routePane, route);
				timeLbl.setText("Time: "+complexity[0]+" (Rounds)");
				spaceLbl.setText("Space: "+complexity[1]+" (Maximun Node Count in PQ)");
				map.drawRoute(route);
			}
		} catch (NullPointerException ex) {
			alert("No routes connect "+srcCB.getValue()+" and "+destCB.getValue());
			map.clearAll(citiesArr, true);
		} catch (IndexOutOfBoundsException ex) {
			alert("Select source and destination cities first!");
			map.clearAll(citiesArr, true);
		}
	}

	//Styling the scroll pane used to display the path
	private ScrollPane getRouteScroll() {
		ScrollPane routeScrollPane = new ScrollPane();
		routeScrollPane.setHbarPolicy(ScrollBarPolicy.NEVER);
		routeScrollPane.setVbarPolicy(ScrollBarPolicy.NEVER);
		routeScrollPane.setStyle("-fx-background:#fff;-fx-background-color:#fff;-fx-border:none;");
		return routeScrollPane;
	}

	//Styling the map box containing show all roads and clear buttons
	private VBox getBtnsBox() {
		VBox btnsBox = new VBox(15);
		btnsBox.setAlignment(Pos.CENTER);
		btnsBox.setStyle(BOX_STYLE);
		btnsBox.setPrefSize(285, 125);
		btnsBox.setAlignment(Pos.CENTER);
		
		Label mapTitleLbl = new Label("Map");
		mapTitleLbl.setAlignment(Pos.CENTER);
		mapTitleLbl.setStyle("-fx-font-size:30px; -fx-font-weight: bold;");
		
		clearBtn = new Button("Clear Map");
		VBox.setMargin(clearBtn, new Insets(5,0,0,0));
		clearBtn.setDisable(true);
		clearBtn.setOnAction(e->{
			map.clearAll(citiesArr, true);
			routePane.getChildren().clear();
			distLbl.setText("Distance: ");
			timeLbl.setText("Time: ");
			spaceLbl.setText("Space: ");
		});
		drawAllBtn = new Button("Show All Roads");
		drawAllBtn.setDisable(true);
		drawAllBtn.setOnAction(e->{
			map.clearAll(citiesArr, false);
			map.drawAllRoutes(citiesArr);
		});
		btnsBox.getChildren().addAll(mapTitleLbl, clearBtn, drawAllBtn);
		return btnsBox;
	}
	
	//Route box which will be the content of the scroll pane to display path
	private Label distLbl;
	private VBox getRouteBox() {
		VBox root = new VBox (5);
		root.setAlignment(Pos.TOP_CENTER);
		root.setPadding(new Insets(15));
		root.setStyle(BOX_STYLE+"-fx-font-weight:bold;");
		root.setMinHeight(452.5);
		root.setMaxHeight(800);
		root.setPrefWidth(450);
		
		Label titleLbl = new Label("Route");
		titleLbl.setStyle("-fx-font-size: 25px");
		titleLbl.setAlignment(Pos.TOP_LEFT);
		
		distLbl = new Label("Distance: ");
		
		root.getChildren().addAll(titleLbl, distLbl);
		return root;
	}

	//Method to display alert in case of errors
	private void alert(String msg) {
		Alert alert = new Alert(AlertType.ERROR, msg, ButtonType.OK);
		alert.show();
	}
	
	//Method to adjust the height of the window for low resolution screens
	private void adjustHeight(Pane bgPane) {
		double screenHeight = Screen.getPrimary().getBounds().getHeight();
		if (screenHeight<=900) {
			windowHeight = 675;
			windowWidth = 1000;
			bgPane.setScaleX(0.75);
			bgPane.setScaleY(0.75);
			bgPane.setLayoutX(-(windowWidth-(windowWidth*0.75))/2);
			bgPane.setLayoutY(-(windowHeight-(windowHeight*0.75))/2);
			bgPane.setStyle("-fx-background-color:#fff");
		}		
	}

	
	//Add cities to the path and display them on screen
	private void demonstrateRoute(FlowPane pane, ArrayList<City> route) {
		pane.getChildren().clear();
		for (int i=route.size()-1; i>=0; i--) {
			StackPane rectPane = new StackPane();
			Label lbl = new Label(route.get(i).getName());
			lbl.setStyle("-fx-font-size:15px; -fx-font-weight:bold");
			lbl.setTextFill(Color.WHITE);
			Rectangle rect = new Rectangle(lbl.getText().length()*10, 40);
			rect.setFill(Color.web("55bbaa"));
			rect.setArcHeight(10);
			rect.setArcWidth(10);
			rectPane.getChildren().addAll(rect, lbl);
			pane.getChildren().add(rectPane);
			if (i!=0) {
				Rectangle arrow = new Rectangle(20,3);
				arrow.setFill(Color.web("55bbaa"));
				pane.getChildren().add(arrow);
			}
		}
	}
}
