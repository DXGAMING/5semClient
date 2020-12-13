package com.client.controller;

import com.client.information_window.InformationWindow;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;
import com.client.connection.AttributeKey;
import com.client.connection.RequestParameter;
import com.client.connection.ServerConnection;
import com.client.connection.ServerResponse;
import com.client.connection.Session;
import com.client.connection.UserRequest;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;


public class LoginController extends AbstractController
{
	@FXML
	private Button loginbutton;
	@FXML
	private JFXTextField textFieldLogin;
	@FXML
	private JFXPasswordField passwordField;
	@FXML
	private Label isConnected;

	public JFXRadioButton adminToggle;
	public JFXRadioButton employeeToggle;

	public String userRole;

	@FXML
	private void login(ActionEvent actionEvent) throws IOException
	{
		if (!employeeToggle.isSelected() && !adminToggle.isSelected())
		{
			isConnected.setText("Select Admin or Employee from above");
			return;
		}
		Session.getInstance().clear();

		String login = textFieldLogin.getText();
		String password = passwordField.getText();
		if (userRole.matches("ADMIN_AUTH"))
		{
			ServerConnection connection = createServerConnectionForLoginRequest(login, password, true);
			connection.sendRequest();
			ServerResponse response = connection.getResponse();
			boolean successfulAuthentication = (boolean) response.getAttribute(AttributeKey.SUCCESSFUL_AUTHENTICATION);
			if (successfulAuthentication)
			{
				Session session = Session.getInstance();
				session.setAttribute(RequestParameter.IS_ADMIN, true);
				session.setAttribute(RequestParameter.LOGIN, login);
				session.setAttribute(RequestParameter.PASSWORD, password);
				textFieldLogin.clear();
				passwordField.clear();
				adminIntroPage();
			}
			else
			{
				passwordField.clear();
				InformationWindow.showAuthorizationError();
			}
		}
		else
		{
			ServerConnection connection = createServerConnectionForLoginRequest(login, password, false);
			connection.sendRequest();
			ServerResponse response = connection.getResponse();
			boolean successfulAuthentication = (boolean) response.getAttribute(AttributeKey.SUCCESSFUL_AUTHENTICATION);
			if (successfulAuthentication)
			{
				Session session = Session.getInstance();
				session.setAttribute(RequestParameter.IS_ADMIN, false);
				session.setAttribute(RequestParameter.LOGIN, login);
				session.setAttribute(RequestParameter.PASSWORD, password);
				textFieldLogin.clear();
				passwordField.clear();
				employeeIntroPage();
			}
			else
			{
				passwordField.clear();
				InformationWindow.showAuthorizationError();
			}
		}
	}

	public void adminToggleOnAction(ActionEvent event)
	{
		if (adminToggle.isSelected())
		{
			employeeToggle.setSelected(false);
			userRole = "ADMIN_AUTH";
		}
	}

	public void employeeToggleOnAction(ActionEvent event)
	{
		if (employeeToggle.isSelected())
		{
			adminToggle.setSelected(false);
			userRole = "EMPLOYEE_AUTH";
		}
	}

	private ServerConnection createServerConnectionForLoginRequest(final String login, final String password, final boolean isAdmin)
	{
		ServerConnection connection = ServerConnection.getInstance();
		UserRequest request = connection.getRequest();
		request.setAttribute(RequestParameter.COMMAND_NAME, "AUTHENTICATION");
		request.setAttribute(RequestParameter.LOGIN, login);
		request.setAttribute(RequestParameter.PASSWORD, password);
		request.setAttribute(RequestParameter.IS_ADMIN, isAdmin);
		return connection;
	}

	private void adminIntroPage() throws IOException
	{
		Stage newStage = createStage("/view/intropageadmin.fxml");
		newStage.showAndWait();
	}

	private void employeeIntroPage() throws IOException
	{
		Stage newStage = createStage("/view/intropageemployee.fxml");
		newStage.showAndWait();
	}
}
