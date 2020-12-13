package com.client.controller;

import com.client.connection.AttributeKey;
import com.client.connection.RequestParameter;
import com.client.connection.ServerConnection;
import com.client.connection.ServerResponse;
import com.client.connection.Session;
import com.client.connection.UserRequest;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;

import java.util.ResourceBundle;


public class AdminProfilePageController extends AbstractController implements Initializable
{
	private final Logger logger = LoggerFactory.getLogger(AdminProfilePageController.class);
	public JFXButton homeBackBtn;
	public JFXButton editProfileBtn;
	public JFXButton saveBtn;
	public JFXTextField adminNameText;
	public JFXTextField adminUsernameText;
	public JFXTextField adminPassText;
	public JFXTextField adminDesText;
	public JFXTextField adminEmailText;
	public JFXTextField adminContactText;

	public Label confirmationMsg;


	@FXML
	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
		initProfileData();
		saveBtn.setVisible(false);
		saveBtn.setDisable(true);

		adminNameText.setEditable(false);
		adminPassText.setEditable(false);
		adminDesText.setEditable(false);
		adminContactText.setEditable(false);
		adminEmailText.setEditable(false);
	}

	public void homeBackBtnAction(ActionEvent actionEvent)
	{
		homeBackBtn.setOnAction(event -> {
					try
					{
						adminIntroPage();
					}
					catch (IOException e)
					{
						logger.error("error while opening adding car");
					}
				}
		);
	}


	public void editProfileBtnAction(ActionEvent actionEvent)
	{
		editProfileBtn.setOnAction(event -> {
			saveBtn.setDisable(false);
			saveBtn.setVisible(true);

			adminNameText.setEditable(true);
			adminPassText.setEditable(true);
			adminDesText.setEditable(true);
			adminEmailText.setEditable(true);
			adminContactText.setEditable(true);
		});
	}


	public void saveBtnAction(ActionEvent event)
	{
		if (event.getSource() == saveBtn)
		{
			String adminName = adminNameText.getText();
			String adminPass = adminPassText.getText();
			String adminDes = adminDesText.getText();
			String adminEmail = adminEmailText.getText();
			String adminContact = adminContactText.getText();

			if (adminName.trim().isEmpty() || adminPass.trim().isEmpty() || adminDes.trim().isEmpty())
			{
				confirmationMsg.setText("Please fill up the form correctly.");
				return;
			}
			ServerConnection connection = ServerConnection.getInstance();
			UserRequest request = connection.getRequest();
			request.setAttribute(RequestParameter.COMMAND_NAME, "UPDATE_ADMIN_PROFILE");
			request.setAttribute(RequestParameter.ADMIN_NAME, adminName);
			request.setAttribute(RequestParameter.PASSWORD, adminPass);
			request.setAttribute(RequestParameter.EMAIL, adminEmail);
			request.setAttribute(RequestParameter.DESIGNATION, adminDes);
			request.setAttribute(RequestParameter.CONTACT, adminContact);
			connection.sendRequest();
			ServerResponse response = connection.getResponse();
			boolean successfulChange = (boolean) response.getAttribute(AttributeKey.SUCCESSFUL_CHANGE);
			if (successfulChange)
			{
				saveBtn.setDisable(true);
				saveBtn.setVisible(false);
				adminNameText.setEditable(false);
				adminPassText.setEditable(false);
				adminDesText.setEditable(false);
				adminEmailText.setEditable(false);
				adminContactText.setEditable(false);
				confirmationMsg.setStyle("-fx-text-fill: #24bb71");
				confirmationMsg.setText("Saved Successfully!");
			}
		}

	}

	private void initProfileData()
	{
		ServerConnection connection = ServerConnection.getInstance();
		UserRequest request = connection.getRequest();
		request.setAttribute(RequestParameter.COMMAND_NAME, "GET_ADMIN_PROFILE");
		connection.sendRequest();
		ServerResponse response = connection.getResponse();

		final String name = (String) response.getAttribute(RequestParameter.ADMIN_NAME);
		final String email = (String) response.getAttribute(RequestParameter.EMAIL);
		final String desig = (String) response.getAttribute(RequestParameter.DESIGNATION);
		final String contact = (String) response.getAttribute(RequestParameter.CONTACT);
		adminNameText.setText(name);
		adminDesText.setText(desig);
		adminEmailText.setText(email);
		adminContactText.setText(contact);
		Session session = Session.getInstance();
		adminUsernameText.setText((String) session.getAttribute(RequestParameter.LOGIN));
		adminPassText.setText((String) session.getAttribute(RequestParameter.PASSWORD));
	}

	private void adminIntroPage() throws IOException
	{
		Stage newStage = createStage("/view/intropageadmin.fxml");
		newStage.showAndWait();
	}
}
