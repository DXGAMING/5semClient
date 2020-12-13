package com.client.controller;

import com.client.connection.RequestParameter;
import com.client.connection.ServerConnection;
import com.client.connection.ServerResponse;
import com.client.connection.UserRequest;
import com.client.entity.Client;
import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class AllClientController extends AbstractController implements Initializable
{
	public JFXButton homeBackBtn;
	public JFXButton AddClientBtn;
	public JFXButton viewAllClient;
	public TableView<Client> clientTableView;
	public Label clientCountLabel;
	public TableColumn<Client, String> clientId;
	public TableColumn<Client, String> clientName;
	public TableColumn<Client, String> contactPerson;
	public TableColumn<Client, String> phone;
	public TableColumn<Client, String> address;

	private void getClientTableData()
	{
		//Clear the all column data from table
		clientTableView.getItems().clear();
		ServerConnection connection = ServerConnection.getInstance();
		UserRequest request = connection.getRequest();
		request.setAttribute(RequestParameter.COMMAND_NAME, "SHOW_CLIENTS");
		connection.sendRequest();
		ServerResponse response = connection.getResponse();
		int numberOfClients = (int) response.getAttribute(RequestParameter.SIZE);
		for (int i = 0; i < numberOfClients; i++)
		{
			String login = (String) response.getAttribute(RequestParameter.LOGIN + i);
			String name = (String) response.getAttribute(RequestParameter.CLIENT_NAME + i);
			String contact = (String) response.getAttribute(RequestParameter.CLIENT_CONTACT + i);
			String addressStr = (String) response.getAttribute(RequestParameter.CLIENT_ADDRESS + i);
			String phoneStr = (String) response.getAttribute(RequestParameter.CLIENT_PHONE + i);
			Client singleClient = new Client(login, name, contact, phoneStr, addressStr);
			clientTableView.getItems().add(singleClient);
		}

		clientCountLabel.setText("Currently you have " + numberOfClients + " clients.");
	}

	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
		clientId.setCellValueFactory(new PropertyValueFactory<>("clientId"));
		clientName.setCellValueFactory(new PropertyValueFactory<>("clientName"));
		contactPerson.setCellValueFactory(new PropertyValueFactory<>("contactPerson"));
		phone.setCellValueFactory(new PropertyValueFactory<>("phone"));
		address.setCellValueFactory(new PropertyValueFactory<>("address"));
		clientTableView.setEditable(true);

		getClientTableData();

		Image imageDecline = new Image(getClass().getResourceAsStream("/icons/home-icon.png"));
		ImageView cameraIconView = new ImageView(imageDecline);
		cameraIconView.setFitHeight(25);
		cameraIconView.setFitWidth(25);
		homeBackBtn.setGraphic(cameraIconView);

		viewAllClient.setStyle("-fx-background-color: #5d2664");
	}

	public void homeBackBtnAction(ActionEvent event) throws IOException
	{
		if (event.getSource() == homeBackBtn)
		{
			adminIntroPage();
		}
	}

	public void AddClientBtnAction(ActionEvent event) throws IOException
	{
		addClientPage();
	}

	public void viewAllClientAction(ActionEvent event)
	{
	}

	private void adminIntroPage() throws IOException
	{
		Stage newStage = createStage("/view/intropageadmin.fxml");
		newStage.showAndWait();
	}

	private void addClientPage() throws IOException
	{
		Stage newStage = createStage("/view/addclient.fxml");
		newStage.showAndWait();
	}
}
