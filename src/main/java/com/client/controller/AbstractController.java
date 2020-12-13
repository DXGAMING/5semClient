package com.client.controller;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;


public abstract class AbstractController
{
	protected Stage createStage(final String resourcePath) throws IOException
	{
		Stage newStage = new Stage();
		AnchorPane anchorPanePopup = (AnchorPane) FXMLLoader.load(getClass().getResource(resourcePath));
		Scene scene = new Scene(anchorPanePopup);
		newStage.setScene(scene);
		newStage.initModality(Modality.APPLICATION_MODAL);
		newStage.setTitle("");
		newStage.setMaxHeight(730);
		newStage.setMaxWidth(900);
		return newStage;
	}

}
