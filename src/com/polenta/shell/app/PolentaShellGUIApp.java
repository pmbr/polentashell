package com.polenta.shell.app;

import java.io.IOException;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

public class PolentaShellGUIApp extends Application implements PolentaShellApp {

	private Stage primaryStage;
	private BorderPane rootLayout;
	// private BorderPane rootLayout;

	@Override
	public void start(Stage primaryStage) throws Exception {
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("AddressApp");

		initRootLayout();

		showPersonOverview();
	}

	public void initRootLayout() {
		try {
			// Carrega o root layout do arquivo fxml.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(PolentaShellGUIApp.class.getResource("view/RootLayout.fxml"));
			rootLayout = (BorderPane) loader.load();
			//
			// Mostra a scene (cena) contendo o root layout.
			Scene scene = new Scene(rootLayout);
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void showPersonOverview() {
		try {
			// Carrega o person overview.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(PolentaShellGUIApp.class.getResource("view/StatementView.fxml"));
			AnchorPane personOverview = (AnchorPane) loader.load();

			// Define o person overview dentro do root layout.
			rootLayout.setCenter(personOverview);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void startApp(String[] args) {

		launch(args);
	}

}
