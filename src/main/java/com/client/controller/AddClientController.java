package com.client.controller;

import com.client.connection.RequestParameter;
import com.client.connection.ServerConnection;
import com.client.connection.UserRequest;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;


public class AddClientController extends AbstractController implements Initializable
{
	public JFXButton homeBackBtn;
	public JFXButton AddClientBtn;
	public JFXButton viewAllClient;
	public JFXButton addNewClientBtn;
	public JFXTextField addressField;
	public JFXTextField phoneField;
	public JFXTextField clientNameField;
	public JFXTextField contactPersonField;
	public Label confirmationMsg;

	public void homeBackBtnAction(ActionEvent event) throws IOException
	{
		if (event.getSource() == homeBackBtn)
		{
			adminIntroPage();
		}
	}

	public void AddClientBtnAction(ActionEvent event)
	{
	}

	public void viewAllClientAction(ActionEvent event) throws IOException
	{
		if (event.getSource() == viewAllClient)
		{
			allClientPage();
		}

	}

	public void addNewClientAction(ActionEvent event)
	{
		if (event.getSource() == addNewClientBtn)
		{
			String clientName = clientNameField.getText();
			String contactPerson = contactPersonField.getText();
			String phone = phoneField.getText();
			String address = addressField.getText();

			if (clientName.trim().isEmpty() || contactPerson.trim().isEmpty() || phone.trim().isEmpty() || address.trim().isEmpty())
			{
				confirmationMsg.setText("Please fillup the form correctly.");
				return;
			}
			ServerConnection connection = ServerConnection.getInstance();
			UserRequest request = connection.getRequest();
			request.setAttribute(RequestParameter.COMMAND_NAME, "ADD_CLIENT");
			request.setAttribute(RequestParameter.CLIENT_NAME, clientName);
			request.setAttribute(RequestParameter.CLIENT_CONTACT, contactPerson);
			request.setAttribute(RequestParameter.CLIENT_PHONE, phone);
			request.setAttribute(RequestParameter.CLIENT_ADDRESS, address);
			connection.sendRequest();
			confirmationMsg.setStyle("-fx-text-fill: #24bb71");
			confirmationMsg.setText("A client named " + clientName + " has been added.");


		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
		int id = 0;

		Image imageDecline = new Image(getClass().getResourceAsStream("/icons/home-icon.png"));
		ImageView cameraIconView = new ImageView(imageDecline);
		cameraIconView.setFitHeight(25);
		cameraIconView.setFitWidth(25);
		homeBackBtn.setGraphic(cameraIconView);

		AddClientBtn.setStyle("-fx-background-color: #5d2664");
	}

	private void adminIntroPage() throws IOException
	{
		Stage newStage = createStage("/view/intropageadmin.fxml");
		newStage.showAndWait();
	}

	private void allClientPage() throws IOException
	{
		Stage newStage = createStage("/view/allclient.fxml");
		newStage.showAndWait();
	}
}
