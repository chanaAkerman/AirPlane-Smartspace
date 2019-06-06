package smartspace.plugins;

import java.util.Date;
import java.util.HashMap;
import org.springframework.web.client.RestTemplate;


import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import smartspace.data.ActionEntity;
import smartspace.data.ElementEntity;
import smartspace.data.UserEntity;
import smartspace.data.util.EntityFactory;
import smartspace.data.util.EntityFactoryImpl;
import smartspace.layout.ActionBoundary;

public class ClientApplication extends Application {

	public static Stage window;
	public static Scene startScene;
	public static String url;
	public static  Airplane curPlane;
	public static EntityFactory factory = new EntityFactoryImpl();
	public static RestTemplate restTemplate = new RestTemplate();

	public static void main(String[] args) {
		// -Dplayground.host=10.0.0.7 -smartspace.port=8084
		String host = System.getProperty("smartspace.host");
		if (host == null) {
			host = "localhost";
		}
		int port;
		try {
			port = Integer.parseInt(System.getProperty("smartspace.port"));
		} catch (Exception e) {
			port = 8084;
		}
		init(host,port);
		launch(args);

	}
	
	public static void init(String host, int port) {
		url="http://" + host + ":" + port + "/smartspace";
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		window = primaryStage;
		BorderPane mainWindow = new BorderPane();
		mainWindow.getStyleClass().add("pane");
		VBox topLayout = new VBox(8);
		topLayout.setAlignment(Pos.CENTER);
		topLayout.paddingProperty().set(new Insets(10));;
		HBox buttonsLayout = new HBox(8);
		buttonsLayout.setAlignment(Pos.CENTER);

		Label welcome = new Label("Welcome to Airplain Smartspace!");
		welcome.setFont(Font.font("Verdana", FontWeight.BOLD, 22));
		Label whatToDo = new Label("What would you like to do?");
		whatToDo.setFont(Font.font("Verdana", FontPosture.ITALIC, 18));
		topLayout.getChildren().addAll(welcome, whatToDo);

		Button login = new MyButton("Login");
		login.setOnAction(e -> new LoginWindow());
		Button register = new MyButton("Register");
		register.setOnAction(e -> new RegisterWindow());
		buttonsLayout.getChildren().addAll(login, register);

		mainWindow.setTop(topLayout);
		mainWindow.setCenter(buttonsLayout);
		
		startScene = new Scene(mainWindow, 580, 150);
		startScene.getStylesheets().add("styles.css");
		window.setTitle("smartspace");
		window.getIcons().add(new Image("/airplane.png"));
		window.setResizable(false);
		window.setScene(startScene);
		window.show();
	}

	public static void afterLogin(UserEntity user) {
		BorderPane mainWindow = new BorderPane();
		mainWindow.requestLayout();
		mainWindow.getStyleClass().add("pane");
		VBox topTotal = new VBox(8);
		topTotal.setAlignment(Pos.CENTER);
		HBox topLayout1 = new HBox(8);
		topLayout1.setAlignment(Pos.CENTER);
		topLayout1.paddingProperty().set(new Insets(10,0,0,0));
		HBox topLayout2 = new HBox(8);
		topLayout2.setAlignment(Pos.CENTER);
		HBox buttonsLayout = new HBox(8);
		buttonsLayout.setAlignment(Pos.CENTER);
		
		String urlimg = user.getAvatar();
		Image img = new Image("/airplane.png");
		try {
			img = new Image(urlimg,30,30,false,false);
		} catch (IllegalArgumentException ex) {
			img = new Image("/airplane.png",30,30,false,false);
		}
		ImageView userImg = new ImageView();
		userImg.setImage(img);
		Label userWelcome = new Label("Welcome, " + user.getUserName() + "!");
		userWelcome.setFont(Font.font("Verdana", FontWeight.BOLD, 22));
		Label userInfo = new Label("(Role:"+user.getRole()+"/Points:"+user.getPoints()+")");
		userInfo.setFont(Font.font("Verdana", FontPosture.ITALIC, 18));
		Button logout = new MyButton("Logout");
		topLayout1.getChildren().addAll(userImg, userWelcome, logout);
		topLayout2.getChildren().add(userInfo);
		topTotal.getChildren().addAll(topLayout1,topLayout2);
		
		Button createAirPlane = new MyButton("Create AirPlane");
		Button buyTicket = new MyButton("Buy Ticket");
		Button updateTicket = new MyButton("Update Ticket");
		Button deleteTicket = new MyButton("Delete Ticket");
		Button showAirplain = new MyButton("Requests");
	
		buttonsLayout.getChildren().addAll(buyTicket, updateTicket,deleteTicket);

		if (!user.getRole().toString().equals("PLAYER")) {
			buttonsLayout.getChildren().addAll(showAirplain);
		}
		if (user.getRole().toString().equals("ADMIN") && curPlane==null) {
			buttonsLayout.getChildren().addAll(createAirPlane);
		}

		mainWindow.setTop(topTotal);
		mainWindow.setCenter(buttonsLayout);

		Scene scene = new Scene(mainWindow, 580, 120);
		scene.getStylesheets().add("styles.css");
		
		window.setScene(scene);
		window.setTitle(user.getUserName() + " - smartspace");
		
		createAirPlane.setOnAction(e->{
			curPlane = new Airplane( user,4, 20, 3, 4, 870);
			createAirPlane.setVisible(false);
		});
		buyTicket.setOnAction(e -> {
			new Ticket(window,scene,curPlane,user);
		});
		showAirplain.setOnAction(e -> {
			Ticket.showAirplain(window,scene,curPlane,user);
		});
		deleteTicket.setOnAction(e -> {
			Ticket.deleteTicket(window,scene,curPlane,user);
		});
		
		updateTicket.setOnAction(e -> {
			Ticket.updateTicketWindow(window,scene,curPlane,user);
		});
		logout.setOnAction(e -> {
			window.setScene(startScene);
		});

	}

	public static class MyButton extends Button {
		public MyButton(String text) {
			super(text);
			this.setMinSize(75, 25);
		}
	}
	
	public static Scene getStartScene() {
		return startScene;
	}
	
	public static void AddNewAction(UserEntity user,ElementEntity element,String type) {

		ActionEntity newAction = factory.createNewAction(
				element.getElementSmartspace(), element.getElementId(), user.getUserSmartspace(), user.getKey(),
				type , new Date(), new HashMap<>());
		
		ActionBoundary action = new ActionBoundary(newAction);
		ActionBoundary newUser = restTemplate.postForObject(url+"/actions",
				action,
				ActionBoundary.class);
	}
}
