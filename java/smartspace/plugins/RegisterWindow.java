package smartspace.plugins;


import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import smartspace.data.UserEntity;
import smartspace.data.UserRole;
import smartspace.data.util.EntityFactory;
import smartspace.data.util.EntityFactoryImpl;
import smartspace.layout.UserBoundary;

public class RegisterWindow {

	private RestTemplate restTemplate = new RestTemplate();
	private String url = ClientApplication.url+"/users";
	private EntityFactory factory = new EntityFactoryImpl();
	
	private Stage stage;
	private Scene scene;
	private Label mail = new Label("Email");
	private TextField mailInput = new TextField();
	private Label userName = new Label("Username");
	private TextField userNameInput = new TextField();
	private Label avatar = new Label("Avatar");
	private TextField avatarInput = new TextField();
	private Label role = new Label("Role");
	private ComboBox<String> roleInput = new ComboBox<>();
	private Button registerBt = new Button("Register");
	
	public RegisterWindow() {

		stage = new Stage();
		stage.setTitle("Register");
		stage.getIcons().add(new Image("/airplane.png"));
		stage.initModality(Modality.APPLICATION_MODAL);
		GridPane mainLayout = new GridPane();
		mainLayout.setPadding(new Insets(10));
		mainLayout.setVgap(8);
		mainLayout.setHgap(10);

		// Design inputs
		mailInput.setPromptText("example@email.com");
		userNameInput.setPromptText("myUser");
		roleInput.getItems().addAll(UserRole.ADMIN.toString(),UserRole.MANAGER.toString(),UserRole.PLAYER.toString());
		avatarInput.setPromptText("myAvatar");

		GridPane.setConstraints(mail, 0, 0);
		GridPane.setConstraints(mailInput, 1, 0);
		GridPane.setConstraints(userName, 0, 1);
		GridPane.setConstraints(userNameInput, 1, 1);
		GridPane.setConstraints(role, 0, 2);
		GridPane.setConstraints(roleInput, 1, 2);
		GridPane.setConstraints(avatar, 0, 3);
		GridPane.setConstraints(avatarInput, 1, 3);
		GridPane.setConstraints(registerBt, 1, 4);

		mainLayout.getChildren().addAll(mail, mailInput, userName, userNameInput, role, roleInput, avatar, avatarInput,
				registerBt);
		
		registerBt.setOnAction(e -> {
			if (!mailInput.getText().isEmpty() && !userName.getText().isEmpty()
					&& !roleInput.getValue().isEmpty() && !avatarInput.getText().isEmpty()) {
				UserRole role=UserRole.PLAYER;
				if(roleInput.getValue().toString()=="MANAGER")
				{
					role = UserRole.MANAGER;
				}
				if(roleInput.getValue().toString()=="ADMIN")
				{
					role = UserRole.ADMIN;
				}
				UserEntity user = factory.createNewUser(mailInput.getText(), userNameInput.getText(),avatarInput.getText(),
						role,new Long(0));
				try {
					UserBoundary newUser = restTemplate.postForObject(url,
							new UserBoundary(user.getKey(), user.getUserName(), user.getAvatar(), user.getRole().toString(), user.getPoints()),
							UserBoundary.class);
					newUser.setRole(role.toString());
					new AlertBox("Registered successfully",
							"You have successfully registered!");
					ClientApplication.afterLogin(newUser.toEntity());
					stage.close();
				} catch (HttpServerErrorException ex) {
					new AlertBox("Email already in use",
							"please try to login or use new Email address");
				} catch (Exception ex) {
					new AlertBox("Error", ex.getCause().getMessage());
				}

			}
		});

		registerBt.setDefaultButton(true);
		scene = new Scene(mainLayout);
		stage.setResizable(false);
		stage.setScene(scene);
		stage.showAndWait();

	}
}
