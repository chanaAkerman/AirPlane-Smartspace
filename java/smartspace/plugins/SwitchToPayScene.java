package smartspace.plugins;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import smartspace.data.UserEntity;

public class SwitchToPayScene {
	private Stage stage;
	private String firstName, lastName;
	private String fromCity="New York City";
	private String toCity="Tel Aviv";
	private String foodOptionChosen;
	
	private AirplaneSeat selectedSeat;
	private AirplaneSeat infoSeat;
	
	private Button confirmPay;
	private Scene paymentScene;
	
	private Label errorMessageLabel3 = new Label("");
	
	public SwitchToPayScene(Stage window,Scene sceneAirplane,AirplaneSeat selectedSeat, UserEntity user,boolean Paid){
		this.stage=window;    
		if(Paid) {
	        	createFinalScene();
	        }
	        else {
	    	this.selectedSeat=selectedSeat;
	        HBox layoutWindow3Main = new HBox(40);
	        layoutWindow3Main.setAlignment(Pos.CENTER);
	        VBox layoutWindow3Left = new VBox(15);
	        HBox layoutWindow3LeftInterrior = new HBox(10);
	        VBox interriorLeft = new VBox(29);
	        VBox interriorRight = new VBox(20);
	        
	        Label scene3Info = new Label("Flight Information");
	        scene3Info.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
	        scene3Info.setUnderline(true);
	        Label fromLab = new Label("From: ");
	        Label toLab = new Label("To: ");
	        Label categoryLab = new Label("Category: ");
	        Label seatLab = new Label("Seat: ");
	        Label foodLab = new Label("Food Option: "); 
	        Label priceLab = new Label("Price: ");
	        
	        TextField fromF = new TextField(fromCity);
	        TextField toF = new TextField(toCity);
	        TextField categoryF = new TextField(AirplaneSeat.getCategory(selectedSeat));
	        TextField seatF = new TextField(AirplaneSeat.getName(selectedSeat));
	        TextField foodF = new TextField(Ticket.getFoodOptionChosen());
	        TextField priceF = new TextField("$" + Double.toString(AirplaneSeat.getPrice(selectedSeat)));
	        
	        fromF.setEditable(false);
	        toF.setEditable(false);
	        categoryF.setEditable(false);
	        seatF.setEditable(false);
	        foodF.setEditable(false);
	        priceF.setEditable(false);
	        
	        Label scene3Info2 = new Label("Payment Information");
	        scene3Info2.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
	        scene3Info2.setUnderline(true);
	        Label namePayLab = new Label("Name (as it appears on your card)");
	        Label cardNumPayLab = new Label("Card Number (no dashes or spaces)");
	        Label expDatePayLab = new Label("Expiration Date");
	        Label securityPayLab = new Label("Security Code (cvc)");
	        
	        
	        TextField namePayF = new TextField(user.getUserName());
	        RestrictiveTextField cardNumPayF = new RestrictiveTextField();
	        cardNumPayF.setMaxLength(16);
	        cardNumPayF.setRestrict("[0-9]");
	        cardNumPayF.setMaxWidth(150.0);
	        
	        RestrictiveTextField securityPayF = new RestrictiveTextField();
	        securityPayF.setMaxLength(3);
	        securityPayF.setRestrict("[0-9]");
	        securityPayF.setMaxWidth(35.0);
	        
	        HBox dateHolderLayout = new HBox(10);
	        String[] monthList = {"January" , "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
	        String[] yearList = {"2019", "2020", "2021", "2022", "2023", "2024", "2025", "2026", "2027"};
	        ComboBox month = new ComboBox(FXCollections.observableArrayList(monthList));
	        ComboBox year = new ComboBox(FXCollections.observableArrayList(yearList));
	        dateHolderLayout.getChildren().addAll(month, year);
	        
	        VBox layoutWindow3Right = new VBox(13);
	        layoutWindow3Right.getChildren().addAll(scene3Info2, namePayLab, namePayF, cardNumPayLab, cardNumPayF, expDatePayLab, dateHolderLayout, securityPayLab, securityPayF/*, confirmPay*/);
	        
	        
	        //layoutWindow3Main.getChildren().addAll(layoutWindow3Left,
	        confirmPay = new Button("Confirm and Pay");
	        confirmPay.setOnAction(e -> 
	        {
	            try{
	                if((namePayF.getText()).length() >= 1 && cardNumPayF.getText().length() == 16 && securityPayF.getText().length() == 3 && month.getValue() != null && year.getValue() != null){
	                    errorMessageLabel3.setText("");
	                    createFinalScene();
	                }else{
	                    throw new PaymentNotFilledException();
	                }
	            }catch(PaymentNotFilledException e2){
	                errorMessageLabel3.setText("Please fill out ALL payment information");
	            }
	        }
	        );
	        
	        
	        interriorLeft.getChildren().addAll(fromLab, toLab, categoryLab, seatLab, foodLab, priceLab);
	        interriorRight.getChildren().addAll(fromF, toF, categoryF, seatF, foodF, priceF);
	        layoutWindow3LeftInterrior.getChildren().addAll(interriorLeft, interriorRight);
	        layoutWindow3Left.getChildren().addAll(scene3Info, layoutWindow3LeftInterrior/*, backToSelect*/);
	        layoutWindow3Left.setAlignment(Pos.CENTER);
	        
	        layoutWindow3Right.setAlignment(Pos.CENTER_LEFT);
	        
	        layoutWindow3Main.getChildren().addAll(layoutWindow3Left, layoutWindow3Right);
	        layoutWindow3Main.setPadding(new Insets(10, 10, 10, 10));
	        
	        VBox layoutWindow3OUTER = new VBox(20);
	        HBox buttonHolderWindow3 = new HBox(150);
	        buttonHolderWindow3.getChildren().addAll(confirmPay);
	        buttonHolderWindow3.setAlignment(Pos.CENTER);
	        layoutWindow3OUTER.getChildren().addAll(layoutWindow3Main, buttonHolderWindow3, errorMessageLabel3);
	        layoutWindow3OUTER.setAlignment(Pos.CENTER);
	        
	        paymentScene = new Scene(layoutWindow3OUTER, 600, 600);
	        stage.setScene(paymentScene);
	        stage.show();
	        }
	    }
	public void createFinalScene(){
        VBox layoutWindow4Main = new VBox(20);
        
        Label success = new Label("Your seat has been successfully reserved to Manager confirm");
        success.setFont(Font.font(Font.getFontNames().get(0),  FontWeight.EXTRA_BOLD, 20));
        success.setPadding(new Insets(10,10,10,10));
        Button closeApp, logout;
        closeApp = new Button("Exit");
        logout = new Button("Logout");
        closeApp.setOnAction(e -> Platform.exit());
        logout.setOnAction(e -> {
			stage.setScene(ClientApplication.getStartScene());
		});
        layoutWindow4Main.getChildren().addAll(success, closeApp,logout);
        layoutWindow4Main.setPadding(new Insets(10, 10, 10, 10));
        layoutWindow4Main.setAlignment(Pos.CENTER);
        closeApp.setAlignment( Pos.BOTTOM_CENTER);
        Scene scene4 = new Scene(layoutWindow4Main, 580, 200);
        stage.setScene(scene4);
        stage.show();
    }
	    
	}


