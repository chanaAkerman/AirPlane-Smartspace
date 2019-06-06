package smartspace.plugins;


import java.time.LocalDate;
import org.springframework.web.client.RestTemplate;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import smartspace.data.UserEntity;
import smartspace.layout.UserBoundary;

public class Ticket {

	private RestTemplate restTemplate = new RestTemplate();
	private String url = ClientApplication.url+"/users";
	private static boolean ManagerWatch=false;
	private static boolean deleteWatch=false;
	private static boolean updateUser=false;
	private Image plane = new Image("/Layout2.png");
	private ImageView backgroundPlane = new ImageView(plane);
    private Scene scene;
    private static Stage window;
	
	private Button fromInfoToSeat;
	public Button confirmSeatButton=new Button("Confirm Options");
	public Button confirmManagerSeatButton=new Button("Confirm");
	public Button NotConfirmManagerSeatButton=new Button("Not Confirm");
	public Button yesButton=new Button("YES");
	public Button noButton=new Button("NO");
	private String firstName, lastName, fromCity,toCity;
	private TextField lastNm, firstNm;
	private LocalDate selectedDate;
	private DatePicker flightDate;
	
	public  Airplane curPlane;
	private AirplaneSeat selectedSeat;
	private AirplaneSeat infoSeat;
	private BorderPane layoutWindow2Main;
	private static String foodOptionChosen;
	public static String getFoodOptionChosen() {
		return foodOptionChosen;
	}

	private Button[][] seats;
	private Button back= new Button("Back");
	private int r=-1;
	private int c=-1;
	
	// don't use
	private int x=-1;
	private int y=-1;
	
	private  Label errorMessageLabel1 = new Label("");
	private  Label errorMessageLabel2 = new Label("");
	
	private RadioButton vegan = new RadioButton("Vegan");
	private RadioButton vegetarian = new RadioButton("Vegetarian");
	private RadioButton standard = new RadioButton("Standard");
	private RadioButton kosher = new RadioButton("Kosher");
	 
	private TextField seatPrice;
	private TextField seatCategory;
	private TextField seatPosition;
	//private TextField details;
	private TextArea details = new TextArea();
	private GridPane seatsPaneTop;
    private GridPane seatsPaneBot;
	public VBox clickedItemInformation;
	public static UserEntity curUser;
	public Scene backScene;
	public VBox buttonBottom;
	public VBox YesNoBottom;
	
	public Ticket(Stage stage,Scene start,Airplane air,UserEntity user) {
		if(air==null) {
			new AlertBox("Error","No available Airplane");
		}else {
		this.backScene=start;
		curUser = user;
		System.err.println("cur user: "+curUser.toString());
		this.curPlane=air;
		
		if(deleteWatch) {
			ShowAirplaneToDelete(stage);
		}
		else if(updateUser) {
			updateTicket(stage);
		}
		else if(ManagerWatch) {
			ShowAirplaneToConfirm(stage);
		}
		else if(!ManagerWatch) {
			ShowAirplaneToCostumer(stage);
		}
		}
	}
	
	public void updateTicket(Stage stage) {
		this.window=stage;
		for(int r = 1; r <= curPlane.numRows; r++){
            for(int c = 1; c <= curPlane.numSeatsPerRow; c++){
            	if(curPlane.userListArray[r-1][c-1]!=null && curUser.getKey().equals(curPlane.userListArray[r-1][c-1].getKey())){
            		x=r-1; y=c-1;
            	}
            }
		}
		if(x==-1 && y==-1) {
			new AlertBox("No seat Found","You have no resevation");
			ManagerWatch=false;
			deleteWatch=false;
			updateUser=false;
	    	window.setScene(backScene);
		}else {
		BorderPane layoutWindow2Main = new BorderPane();
        clickedItemInformation = new VBox(9);
        
        seatPrice = new TextField("Price   ");
        seatPrice.setEditable(false);
        seatCategory = new TextField("Category");
        seatCategory.setEditable(false);
        seatPosition = new TextField("Position");
        seatPosition.setEditable(false);
        
        Label seatPriceLabel = new Label("Price:");
        Label seatCategoryLabel = new Label("Category:");
        Label seatPositionLabel = new Label("Position:");
        Label foodOptionLabel = new Label("Food Options:");
        
        seatPrice.setText("$"+curPlane.seatListArray[x][y].getPrice());
        if(curPlane.seatListArray[x][y].getCategory()==1) {
        	seatCategory.setText("Economy class");
        }
        if(curPlane.seatListArray[x][y].getCategory()==2) {
        	seatCategory.setText("Business class");
        }
        if(curPlane.seatListArray[x][y].getCategory()==3) {
        	seatCategory.setText("First class");
        }
    	seatPosition.setText(curPlane.seatListArray[x][y].getSeatName()+"");
        
        
        clickedItemInformation.getChildren().addAll(seatPriceLabel, seatPrice, seatCategoryLabel, seatCategory, seatPositionLabel, seatPosition);
        
        clickedItemInformation.setAlignment(Pos.CENTER_LEFT);
        clickedItemInformation.setPadding(new Insets(10, 10, 10, 30));
        layoutWindow2Main.setLeft(clickedItemInformation);
        
        //Radiobuttons for food options
        ToggleGroup main = new ToggleGroup();
        vegan.setToggleGroup(main);
        vegetarian.setToggleGroup(main);
        standard.setToggleGroup(main);
        kosher.setToggleGroup(main);
        
        VBox foodLayout = new VBox(10);
        foodLayout.getChildren().addAll(foodOptionLabel, vegan, vegetarian, standard, kosher);
        foodLayout.setAlignment(Pos.CENTER_LEFT);
        foodLayout.setPadding(new Insets(10, 30, 10, 10));
        layoutWindow2Main.setRight(foodLayout);
        
        confirmSeatButton.setOnAction(e->{
        	handle(e);
        });
        
        if(curPlane.seatListArray[x][y].getMeal().equals(Meal.Kosher)) {
    		kosher.setSelected(true);
    	}
        if(curPlane.seatListArray[x][y].getMeal().equals(Meal.Vegan)) {
    		vegan.setSelected(true);
    	}
        if(curPlane.seatListArray[x][y].getMeal().equals(Meal.Standard)) {
    		standard.setSelected(true);
    	}
        if(curPlane.seatListArray[x][y].getMeal().equals(Meal.Vegetarian)) {
    		vegetarian.setSelected(true);
    	}
        // delete old Details
        curPlane.Unconfirm(x, y, curUser);
        
        VBox buttonBottom = new VBox(20);
        
        buttonBottom.setPadding(new Insets(20, 20, 20, 20));
        buttonBottom.setAlignment(Pos.CENTER);
        buttonBottom.getChildren().addAll(errorMessageLabel2, confirmSeatButton);
        layoutWindow2Main.setBottom(buttonBottom);
        
        Button[][] seats = new Button[curPlane.numRows][curPlane.numSeatsPerRow];
        
        //creating image object for seat display
        Image emptySeat = new Image("/EmptySeat.png");
        Image occupiedSeat = new Image("/OccupiedSeat.png");
        
        seatsPaneTop = new GridPane();
        seatsPaneBot = new GridPane();
        VBox seatsPaneMain = new VBox(30);
        seatsPaneMain.setPadding(new Insets(0, 0, 0, 60));
        //Places empty space inbetween seat rows to simulate leg room
        for(int i = 0; i < curPlane.numRows; i++){
            seatsPaneTop.getColumnConstraints().add(new ColumnConstraints(38));
            seatsPaneBot.getColumnConstraints().add(new ColumnConstraints(38));
        }
        int count = 0;
        for (int i = 0; i < curPlane.numRows; i++){
            for (int j = 0; j < curPlane.numSeatsPerRow; j++) {
                boolean flag = AirplaneSeat.isReserved(curPlane.seatList.get(count));
                if(flag){
                    ImageView occupiedView = new ImageView(occupiedSeat);
                    occupiedView.setFitHeight(16);
                    occupiedView.setFitWidth(16);
                    seats[i][j] = new Button("", occupiedView);
                    //reason it wasnt working earlier is possible ea
                }else{
                	ImageView emptyView = new ImageView(emptySeat);
                    emptyView.setFitHeight(16);
                    emptyView.setFitWidth(16);
                    seats[i][j]=new Button("", emptyView);
                }
                	seats[i][j].setOnAction(e->{
                		seatHandler(e,seats);
                	});
                
                count++;
                //adds seat to specific gridpane to allow for empty space to represent aisle
                if(j < (curPlane.numSeatsPerRow)/2){
                    seatsPaneTop.add(seats[i][j], i, j, 1, 1);
                }else if(j >= (curPlane.numSeatsPerRow)/2){
                    seatsPaneBot.add(seats[i][j],i,j- (curPlane.numSeatsPerRow)/2, 1, 1);
                }
            }
        }
        seatsPaneTop.setAlignment(Pos.CENTER);
        seatsPaneBot.setAlignment(Pos.CENTER);
        seatsPaneMain.getChildren().addAll(seatsPaneTop, seatsPaneBot);
        seatsPaneMain.setAlignment(Pos.CENTER);
        
        StackPane stackPane = new StackPane(); 
        layoutWindow2Main.setCenter(stackPane);
        
        //Retrieving the observable list of the Stack Pane 
        ObservableList list = stackPane.getChildren(); 
      
        //Adding all the nodes to the pane  
        list.addAll(backgroundPlane, seatsPaneMain);
        
        scene = new Scene(layoutWindow2Main, 1280, 350);
        window.setResizable(false);
		window.setScene(scene);
		window.show();
		}
				
		
	}
	
	public void ShowAirplaneToDelete(Stage stage) {
		confirmSeatButton.setVisible(false);
		for(int r = 1; r <= curPlane.numRows; r++){
            for(int c = 1; c <= curPlane.numSeatsPerRow; c++){
            	if(curPlane.userListArray[r-1][c-1]!=null && curUser.getKey().equals(curPlane.userListArray[r-1][c-1].getKey())){
            		x=r-1; y=c-1;
            		ShowAirplaneToDeleteCostumer(stage);
            	}
            }
		}
		if(x==-1 || y==-1) {
			new AlertBox("No seat Found","You have no resevation");
			backHandler(new ActionEvent());
		}
		back.setOnAction(e->{
			backHandler(e);
	        	
	        });
		errorMessageLabel2.setText("Are you sure you want to delete your resevation?");
		
		yesButton.setOnAction(e->{
			ClientApplication.AddNewAction(curUser, curPlane.elements[x][y], "Delete Ticket "+curPlane.seatListArray[x][y].getSeatName());
			curPlane.cancleReservation(x, y,curUser);
			deleteWatch=false;
			backHandler(e);
		});
		noButton.setOnAction(e->{
			deleteWatch=false;
			backHandler(e);
		});
		
		
	}
	
	private void ShowAirplaneToDeleteCostumer(Stage stage) {
		this.window=stage;
		BorderPane layoutWindow2Main = new BorderPane();
        clickedItemInformation = new VBox(9);
        
        seatPrice = new TextField("Price   ");
        seatPrice.setEditable(false);
        seatCategory = new TextField("Category");
        seatCategory.setEditable(false);
        seatPosition = new TextField("Position");
        seatPosition.setEditable(false);
        
        seatPrice.setText("$"+curPlane.seatListArray[x][y].getPrice());
        if(curPlane.seatListArray[x][y].getCategory()==1) {
        	seatCategory.setText("Economy class");
        }
        if(curPlane.seatListArray[x][y].getCategory()==2) {
        	seatCategory.setText("Business class");
        }
        if(curPlane.seatListArray[x][y].getCategory()==3) {
        	seatCategory.setText("First class");
        }
    	seatPosition.setText(curPlane.seatListArray[x][y].getSeatName()+"");
    	
        
        Label seatPriceLabel = new Label("Price:");
        Label seatCategoryLabel = new Label("Category:");
        Label seatPositionLabel = new Label("Position:");
        Label foodOptionLabel = new Label("Food Options:");
        
        
        
        clickedItemInformation.getChildren().addAll(seatPriceLabel, seatPrice, seatCategoryLabel, seatCategory, seatPositionLabel, seatPosition);
        
        clickedItemInformation.setAlignment(Pos.CENTER_LEFT);
        clickedItemInformation.setPadding(new Insets(10, 10, 10, 30));
        layoutWindow2Main.setLeft(clickedItemInformation);
        
        //Radiobuttons for food options
        ToggleGroup main = new ToggleGroup();
        vegan.setToggleGroup(main);
        vegetarian.setToggleGroup(main);
        standard.setToggleGroup(main);
        kosher.setToggleGroup(main);
        
        if(curPlane.seatListArray[x][y].getMeal().equals(Meal.Kosher)) {
    		kosher.setSelected(true);
    	}
        if(curPlane.seatListArray[x][y].getMeal().equals(Meal.Vegan)) {
    		vegan.setSelected(true);
    	}
        if(curPlane.seatListArray[x][y].getMeal().equals(Meal.Standard)) {
    		standard.setSelected(true);
    	}
        if(curPlane.seatListArray[x][y].getMeal().equals(Meal.Vegetarian)) {
    		vegetarian.setSelected(true);
    	}
        
        VBox foodLayout = new VBox(10);
        foodLayout.getChildren().addAll(foodOptionLabel, vegan, vegetarian, standard, kosher);
        foodLayout.setAlignment(Pos.CENTER_LEFT);
        foodLayout.setPadding(new Insets(10, 30, 10, 10));
        
        layoutWindow2Main.setRight(foodLayout);
        
        VBox YesNoButton= new VBox(20);
		YesNoButton.setPadding(new Insets(20, 20, 20, 20));
		YesNoButton.setAlignment(Pos.CENTER);
		YesNoButton.getChildren().addAll(errorMessageLabel2, yesButton,noButton,back);
		back.setVisible(false);
        layoutWindow2Main.setBottom(YesNoButton);
        
        Button[][] seats = new Button[curPlane.numRows][curPlane.numSeatsPerRow];
        
        //creating image object for seat display
        Image emptySeat = new Image("/EmptySeat.png");
        Image occupiedSeat = new Image("/OccupiedSeat.png");
        
        seatsPaneTop = new GridPane();
        seatsPaneBot = new GridPane();
        VBox seatsPaneMain = new VBox(30);
        seatsPaneMain.setPadding(new Insets(0, 0, 0, 60));
        //Places empty space inbetween seat rows to simulate leg room
        for(int i = 0; i < curPlane.numRows; i++){
            seatsPaneTop.getColumnConstraints().add(new ColumnConstraints(38));
            seatsPaneBot.getColumnConstraints().add(new ColumnConstraints(38));
        }
        int count = 0;
        for (int i = 0; i < curPlane.numRows; i++){
            for (int j = 0; j < curPlane.numSeatsPerRow; j++) {
                if(x==i && y==j && AirplaneSeat.isReserved(curPlane.seatList.get(count))) {
                    ImageView occupiedView = new ImageView(occupiedSeat);
                    occupiedView.setFitHeight(16);
                    occupiedView.setFitWidth(16);
                    seats[i][j] = new Button("", occupiedView);
                }else {
                	ImageView emptyView = new ImageView(emptySeat);
                    emptyView.setFitHeight(16);
                    emptyView.setFitWidth(16);
                    seats[i][j]=new Button("", emptyView);
                }

                
                count++;
                //adds seat to specific gridpane to allow for empty space to represent aisle
                if(j < (curPlane.numSeatsPerRow)/2){
                    seatsPaneTop.add(seats[i][j], i, j, 1, 1);
                }else if(j >= (curPlane.numSeatsPerRow)/2){
                    seatsPaneBot.add(seats[i][j],i,j- (curPlane.numSeatsPerRow)/2, 1, 1);
                }
            }
        }
        seatsPaneTop.setAlignment(Pos.CENTER);
        seatsPaneBot.setAlignment(Pos.CENTER);
        seatsPaneMain.getChildren().addAll(seatsPaneTop, seatsPaneBot);
        seatsPaneMain.setAlignment(Pos.CENTER);
        
        StackPane stackPane = new StackPane(); 
        layoutWindow2Main.setCenter(stackPane);
        
        //Retrieving the observable list of the Stack Pane 
        ObservableList list = stackPane.getChildren(); 
      
        //Adding all the nodes to the pane  
        list.addAll(backgroundPlane, seatsPaneMain);
        
        scene = new Scene(layoutWindow2Main, 1280, 350);
        window.setResizable(false);
		window.setScene(scene);
		window.show();
		
	}
	
	private void backHandler(ActionEvent e) {
		ManagerWatch=false;
		deleteWatch=false;
    	window.setScene(backScene);
		
	}
	
	public void ShowAirplaneToCostumer(Stage stage) {
		this.window=stage;
		BorderPane layoutWindow2Main = new BorderPane();
        clickedItemInformation = new VBox(9);
        
        seatPrice = new TextField("Price   ");
        seatPrice.setEditable(false);
        seatCategory = new TextField("Category");
        seatCategory.setEditable(false);
        seatPosition = new TextField("Position");
        seatPosition.setEditable(false);
        
        Label seatPriceLabel = new Label("Price:");
        Label seatCategoryLabel = new Label("Category:");
        Label seatPositionLabel = new Label("Position:");
        Label foodOptionLabel = new Label("Food Options:");
        
        
        
        clickedItemInformation.getChildren().addAll(seatPriceLabel, seatPrice, seatCategoryLabel, seatCategory, seatPositionLabel, seatPosition);
        
        clickedItemInformation.setAlignment(Pos.CENTER_LEFT);
        clickedItemInformation.setPadding(new Insets(10, 10, 10, 30));
        layoutWindow2Main.setLeft(clickedItemInformation);
        
        //Radiobuttons for food options
        ToggleGroup main = new ToggleGroup();
        vegan.setToggleGroup(main);
        vegetarian.setToggleGroup(main);
        standard.setToggleGroup(main);
        kosher.setToggleGroup(main);
        
        VBox foodLayout = new VBox(10);
        foodLayout.getChildren().addAll(foodOptionLabel, vegan, vegetarian, standard, kosher);
        foodLayout.setAlignment(Pos.CENTER_LEFT);
        foodLayout.setPadding(new Insets(10, 30, 10, 10));
        layoutWindow2Main.setRight(foodLayout);
        
        confirmSeatButton.setOnAction(e->{
        	handle(e);
        });
        VBox buttonBottom = new VBox(20);
        
        buttonBottom.setPadding(new Insets(20, 20, 20, 20));
        buttonBottom.setAlignment(Pos.CENTER);
        buttonBottom.getChildren().addAll(errorMessageLabel2, confirmSeatButton);
        layoutWindow2Main.setBottom(buttonBottom);
        
        Button[][] seats = new Button[curPlane.numRows][curPlane.numSeatsPerRow];
        
        //creating image object for seat display
        Image emptySeat = new Image("/EmptySeat.png");
        Image occupiedSeat = new Image("/OccupiedSeat.png");
        
        seatsPaneTop = new GridPane();
        seatsPaneBot = new GridPane();
        VBox seatsPaneMain = new VBox(30);
        seatsPaneMain.setPadding(new Insets(0, 0, 0, 60));
        //Places empty space inbetween seat rows to simulate leg room
        for(int i = 0; i < curPlane.numRows; i++){
            seatsPaneTop.getColumnConstraints().add(new ColumnConstraints(38));
            seatsPaneBot.getColumnConstraints().add(new ColumnConstraints(38));
        }
        int count = 0;
        for (int i = 0; i < curPlane.numRows; i++){
            for (int j = 0; j < curPlane.numSeatsPerRow; j++) {
                boolean flag = AirplaneSeat.isReserved(curPlane.seatList.get(count));
                if(flag){
                    ImageView occupiedView = new ImageView(occupiedSeat);
                    occupiedView.setFitHeight(16);
                    occupiedView.setFitWidth(16);
                    seats[i][j] = new Button("", occupiedView);
                    //reason it wasnt working earlier is possible ea
                }else{
                	ImageView emptyView = new ImageView(emptySeat);
                    emptyView.setFitHeight(16);
                    emptyView.setFitWidth(16);
                    seats[i][j]=new Button("", emptyView);
                }
                	seats[i][j].setOnAction(e->{
                		seatHandler(e,seats);
                	});
                
                count++;
                //adds seat to specific gridpane to allow for empty space to represent aisle
                if(j < (curPlane.numSeatsPerRow)/2){
                    seatsPaneTop.add(seats[i][j], i, j, 1, 1);
                }else if(j >= (curPlane.numSeatsPerRow)/2){
                    seatsPaneBot.add(seats[i][j],i,j- (curPlane.numSeatsPerRow)/2, 1, 1);
                }
            }
        }
        seatsPaneTop.setAlignment(Pos.CENTER);
        seatsPaneBot.setAlignment(Pos.CENTER);
        seatsPaneMain.getChildren().addAll(seatsPaneTop, seatsPaneBot);
        seatsPaneMain.setAlignment(Pos.CENTER);
        
        StackPane stackPane = new StackPane(); 
        layoutWindow2Main.setCenter(stackPane);
        
        //Retrieving the observable list of the Stack Pane 
        ObservableList list = stackPane.getChildren(); 
      
        //Adding all the nodes to the pane  
        list.addAll(backgroundPlane, seatsPaneMain);
        
        scene = new Scene(layoutWindow2Main, 1280, 350);
        window.setResizable(false);
		window.setScene(scene);
		window.show();
		
	}
	
	
	public void ShowAirplaneToConfirm(Stage stage) {
		 this.window=stage;
			BorderPane layoutWindow2Main = new BorderPane();
	        clickedItemInformation = new VBox(9);
	        
	        
			Label userLabel = new Label("Passenger");
			details = new TextArea();
			details.setPrefRowCount(10);
			details.setPrefColumnCount(100);
			details.setWrapText(true);
			details.setPrefWidth(150);

			 clickedItemInformation.getChildren().addAll(userLabel, details,confirmManagerSeatButton,NotConfirmManagerSeatButton);
			 confirmManagerSeatButton.setVisible(false);
			 NotConfirmManagerSeatButton.setVisible(false);
			 clickedItemInformation.setAlignment(Pos.CENTER_LEFT);
		     clickedItemInformation.setPadding(new Insets(10, 10, 10, 30));
		     layoutWindow2Main.setLeft(clickedItemInformation);

	        
	        seatsPaneTop = new GridPane();
	        seatsPaneBot = new GridPane();
	        VBox seatsPaneMain = new VBox(30);
	        seatsPaneMain.setPadding(new Insets(0, 0, 0, 60));
	        //Places empty space inbetween seat rows to simulate leg room
	        for(int i = 0; i < curPlane.numRows; i++){
	            seatsPaneTop.getColumnConstraints().add(new ColumnConstraints(38));
	            seatsPaneBot.getColumnConstraints().add(new ColumnConstraints(38));
	        }
	        seats=new Button[curPlane.numRows][curPlane.numSeatsPerRow];
	        clickedItemInformation.getChildren().add(back);
	        back.setOnAction(e->{
	       backHandler(e);
	        	
	        });
	        showAirPlane();

	        seatsPaneTop.setAlignment(Pos.CENTER);
	        seatsPaneBot.setAlignment(Pos.CENTER);
	        seatsPaneMain.getChildren().addAll(seatsPaneTop, seatsPaneBot);
	        seatsPaneMain.setAlignment(Pos.CENTER);
	        
	        StackPane stackPane = new StackPane(); 
	        layoutWindow2Main.setCenter(stackPane);
	        
	        //Retrieving the observable list of the Stack Pane 
	        ObservableList list = stackPane.getChildren(); 
	      
	        //Adding all the nodes to the pane  
	        list.addAll(backgroundPlane, seatsPaneMain);
	        
	        scene = new Scene(layoutWindow2Main, 1280, 350);
	        window.setResizable(false);
			window.setScene(scene);
			window.show();
		}
		
	
	
	private void showAirPlane() {
		//creating image object for seat display
        Image emptySeat = new Image("/EmptySeat.png");
        Image reservedSeat = new Image("/ReservedSeat.png");
        Image occupiedSeat = new Image("/OccupiedSeat.png");
        
		 int count = 0;
	        for (int i = 0; i < curPlane.numRows; i++){
	            for (int j = 0; j < curPlane.numSeatsPerRow; j++) {
	                boolean flag = AirplaneSeat.isConfirmed(curPlane.seatList.get(count));
	                UserEntity u = curPlane.userListArray[i][j];
	                if(flag && u!=null){
	                    ImageView occupiedView = new ImageView(occupiedSeat);
	                    occupiedView.setFitHeight(16);
	                    occupiedView.setFitWidth(16);
	                    seats[i][j] = new Button("", occupiedView);
	                    //reason it wasnt working earlier is possible ea
	                }else if(!flag && u!=null){
	                	ImageView reservedView = new ImageView(reservedSeat);
	                	reservedView.setFitHeight(16);
	                    reservedView.setFitWidth(16);
	                    seats[i][j]=new Button("", reservedView);
	                }else {
	                	ImageView emptyView = new ImageView(emptySeat);
	                    emptyView.setFitHeight(16);
	                    emptyView.setFitWidth(16);
	                    seats[i][j]=new Button("", emptyView);
	                }
	                	seats[i][j].setOnAction(e->{
	                		seatHandler(e,seats);
	                	});
	                
	                count++;
	                //adds seat to specific gridpane to allow for empty space to represent aisle
	                if(j < (curPlane.numSeatsPerRow)/2){
	                    seatsPaneTop.add(seats[i][j], i, j, 1, 1);
	                }else if(j >= (curPlane.numSeatsPerRow)/2){
	                    seatsPaneBot.add(seats[i][j],i,j- (curPlane.numSeatsPerRow)/2, 1, 1);
	                }
	            }
	        }
		
	}
	
	
	public void seatHandler(ActionEvent event,Button[][]mySeats) {
		int count = 0;
	        for(int r = 0; r < curPlane.numRows; r++){
	            for(int c = 0; c < curPlane.numSeatsPerRow; c++){
	                if(event.getSource() == mySeats[r][c]){
	                	this.r=r;
	                	this.c=c;
	                    if(ManagerWatch)
	                    {
	                    	ShowSeatDetails(r,c,curPlane.seatListArray[r][c].getSeatName(),curPlane.seatListArray[r][c].getCategory()+"",curPlane.seatListArray[r][c].getPrice(),curPlane.userListArray[r][c]);
	                    }
	                    else {
	                    	infoSeat = curPlane.seatList.get(count);
		                    seatPosition.setText(AirplaneSeat.getName(infoSeat));
		                    seatCategory.setText(AirplaneSeat.getCategory(infoSeat));
		                    seatPrice.setText("$" +Double.toString(AirplaneSeat.getPrice(infoSeat)));
	                    }
	                    
	                }
	                count++;
	            }
	        }
	}
	
	
	public void ShowSeatDetails(int x, int y,String name, String category, double price,UserEntity myUser) {
		if(category.equals("1")) { //1== economy
			category="Economy";
		}
		if(category.equals("2")) { //2 == business
			category="Business";
		}
		if(category.equals("3")) { //3 == first
			category="First";
		}
		
		if(myUser!=null && !curPlane.seatListArray[x][y].isConfirmed()){
			confirmManagerSeatButton.setVisible(true);
			NotConfirmManagerSeatButton.setVisible(true);
			details.setText("Ticket : "+name +" "+category+" "+price+" Order By "+myUser.getUserName()+" From Smartspace: "+myUser.getUserSmartspace()+
				" Email: "+myUser.getUserEmail());	
			
			confirmManagerSeatButton.setOnAction(e->{
				curPlane.Confirm(x, y,myUser);
				confirmManagerSeatButton.setVisible(false);
				NotConfirmManagerSeatButton.setVisible(false);
				details.setText("");
				
				UserBoundary response= this.restTemplate.getForObject(
						this.url+"/login/{userSmartspace}/{userEmail}",
						UserBoundary.class,curUser.getUserSmartspace(),curUser.getUserEmail());
				// add 20 points for confirm
				response.setPoints(response.getPoints()+20);
				
				this.restTemplate.put(
						this.url+"/login/{userSmartspace}/{userEmail}", 
						response,response.getUserSmartspace(),response.getUserEmail());
		    	
				ClientApplication.AddNewAction(myUser, curPlane.elements[x][y], "Confirmed Ticket");
				
				showAirPlane();
			});
			NotConfirmManagerSeatButton.setOnAction(e->{
				curPlane.Unconfirm(x,y,myUser);
				confirmManagerSeatButton.setVisible(false);
				NotConfirmManagerSeatButton.setVisible(false);
				details.setText("");
				
				UserBoundary response= this.restTemplate.getForObject(
						this.url+"/login/{userSmartspace}/{userEmail}",
						UserBoundary.class,curUser.getUserSmartspace(),curUser.getUserEmail());
				// add 20 points for not confirm
				response.setPoints(response.getPoints()+20);
				
				this.restTemplate.put(
						this.url+"/login/{userSmartspace}/{userEmail}", 
						response,response.getUserSmartspace(),response.getUserEmail());
				
				ClientApplication.AddNewAction(myUser, curPlane.elements[x][y], "Not Confirmed Ticket");
				
				showAirPlane();
			});
		}
		if(myUser==null) {
			details.setText("No reservation was made");	
		}
	}
	
    public void handle(ActionEvent event) 
	    {
	        if(event.getSource() == fromInfoToSeat){
	            try {
	                firstName = firstNm.getText();
	                lastName = lastNm.getText();
	                fromCity = "New York City";
	                toCity = "Tel Aviv";
	                selectedDate = flightDate.getValue();
	                Label flightInfo = new Label("Available seating for " + lastName + ", " + firstName + "--from " + fromCity + " to " + toCity + " on " + selectedDate+ ":");
	                flightInfo.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
	                flightInfo.setPadding(new Insets(10, 10, 10, 10));
	                layoutWindow2Main.setAlignment(flightInfo, Pos.CENTER);
	                if(selectedDate == null){
	                    throw new NullPointerException();
	                }
	                layoutWindow2Main.setTop(flightInfo);
	                window.setScene(scene);
	                //errorMessageLabel.setText("");
	                window.show();
	            }catch(NullPointerException e){
	                errorMessageLabel1.setText("Please fill out all the text fields.");
	            }
	        }else if(event.getSource() == confirmSeatButton){
	            try{
	                selectedSeat = infoSeat;
	                boolean check = selectedSeat == null;
	                if(check || AirplaneSeat.isReserved(selectedSeat)){
	                    throw new NoSeatException();
	                }else{
	                    
	                    if(vegan.isSelected()){
	                        foodOptionChosen = "Vegan";
	                        curPlane.seatListArray[r][c].setMeal(Meal.Vegan);
	                    }else if(vegetarian.isSelected()){
	                        foodOptionChosen = "Vegetarian";
	                        curPlane.seatListArray[r][c].setMeal(Meal.Vegetarian);
	                    }else if(kosher.isSelected()){
	                        foodOptionChosen = "Kosher";
	                        curPlane.seatListArray[r][c].setMeal(Meal.Kosher);
	                    }else if(standard.isSelected()){
	                        foodOptionChosen = "Standard";
	                        curPlane.seatListArray[r][c].setMeal(Meal.Standard);
	                    }else{
	                        throw new NoFoodOptionException();
	                    }  
	                    if(updateUser) {
	                    	 ClientApplication.AddNewAction(curUser, curPlane.elements[r][c], "Update Ticket");
	                    }
	                    else {
	                    	 ClientApplication.AddNewAction(curUser, curPlane.elements[r][c], "Reserved Ticket");
	                    }
	                   curPlane.saveReservation(r,c,curUser);
	                   new SwitchToPayScene(window,scene,selectedSeat,curUser,updateUser);
	                   updateUser=false;
	                }
	                
	            }catch(NoSeatException e){
	                errorMessageLabel2.setText("Please select an available Seat");
	            }catch(NoFoodOptionException e){
	                errorMessageLabel2.setText("Please Select a Food Option");
	            }
	        }
	    }
	
	
    public static void showAirplain(Stage stage,Scene start,Airplane air,UserEntity ManagerUser) {
    	if(air==null) {
			new AlertBox("Error","No available Airplane");
		}else {
		ManagerWatch=true;
		deleteWatch=false;
		updateUser=false;
		curUser=ManagerUser;
		new Ticket(stage,start,air,curUser);
		}
		
	}
	
	
	public static void deleteTicket(Stage stage,Scene start,Airplane air,UserEntity userToDelete) {
		if(air==null) {
			new AlertBox("Error","No available Airplane");
		}else {
			System.err.println("Static delete function: "+userToDelete.toString());
		deleteWatch=true;
		new Ticket(stage,start,air,userToDelete);
		}
		
	}
	
	
	public static void updateTicketWindow(Stage stage, Scene start, Airplane air, UserEntity userToUpdate) {
		if(air==null) {
			new AlertBox("Error","No available Airplane");
		}
		else {
			System.err.println("Static update function: "+userToUpdate.toString());
		deleteWatch=false;
		updateUser=true;
		new Ticket(stage,start,air,userToUpdate);
		}
		
	}
		
	}

