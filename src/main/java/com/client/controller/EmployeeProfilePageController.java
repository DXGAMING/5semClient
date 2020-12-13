package com.client.controller;

import com.client.connection.RequestParameter;
import com.client.connection.ServerConnection;
import com.client.connection.ServerResponse;
import com.client.connection.Session;
import com.client.connection.UserRequest;
import com.client.entity.Employee;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;


public class EmployeeProfilePageController extends AbstractController implements Initializable
{
	public JFXButton homeBackBtn;
	public JFXButton editProfileBtn;
	public JFXButton saveBtn;
	public JFXTextField empIdText;
	public JFXTextField empNameText;
	public JFXTextField empUsernameText;
	public JFXTextField empPassText;
	public JFXTextField empDesText;
	public JFXTextField empEmailText;
	public JFXTextField empContactText;

	public Label confirmationMsg;

	public void setEmpInfo() throws SQLException
	{

		ServerConnection connection = ServerConnection.getInstance();
		UserRequest request = connection.getRequest();
		request.setAttribute(RequestParameter.COMMAND_NAME, "GET_EMPLOYEE_PROFILE");
		connection.sendRequest();
		ServerResponse response = connection.getResponse();
		String login = (String) response.getAttribute(RequestParameter.EMPLOYEE_EMAIL);
		String name = (String) response.getAttribute(RequestParameter.EMPLOYEE_NAME);
		String designation = (String) response.getAttribute(RequestParameter.DESIGNATION);
		String phone = (String) request.getAttribute(RequestParameter.EMPLOYEE_CONTACT);
		String email = (String) request.getAttribute(RequestParameter.EMPLOYEE_EMAIL);
		String pass = (String) request.getAttribute(RequestParameter.PASSWORD);

		empNameText.setText(name);
		empIdText.setText(String.valueOf(login));
		empDesText.setText(designation);
		empEmailText.setText(email);
		empContactText.setText(phone);
		empUsernameText.setText(login);
		empPassText.setText(pass);
	}


	@Override
	public void initialize(final URL location, final ResourceBundle resources)
	{
		saveBtn.setVisible(false);
		saveBtn.setDisable(true);

		empNameText.setEditable(false);
		empPassText.setEditable(false);
		empDesText.setEditable(false);
		empContactText.setEditable(false);
		empEmailText.setEditable(false);
	}

	public void homeBackBtnAction(final ActionEvent event) throws IOException
	{
		if (event.getSource() == homeBackBtn)
		{
			introPageEmployee();
		}
	}


	public void editProfileBtnAction(ActionEvent event)
	{
		if (event.getSource() == editProfileBtn)
		{
			saveBtn.setDisable(false);
			saveBtn.setVisible(true);

			empNameText.setEditable(true);
			empPassText.setEditable(true);
			empEmailText.setEditable(true);
			empContactText.setEditable(true);
		}
	}

	public void saveBtnAction(ActionEvent event) throws IOException
	{
		if (event.getSource() == saveBtn)
		{
			String empName = empNameText.getText();
			String empPass = empPassText.getText();
			String empDes = empDesText.getText();
			String empEmail = empEmailText.getText();
			String empContact = empContactText.getText();

			if (empName.trim().isEmpty() || empPass.trim().isEmpty() || empDes.trim().isEmpty())
			{
				confirmationMsg.setText("Please fill up the form correctly.");
				return;
			}

			Session session = Session.getInstance();

			ServerConnection connection = ServerConnection.getInstance();
			UserRequest request = connection.getRequest();
			request.setAttribute(RequestParameter.COMMAND_NAME, "UPDATE_EMPLOYEES");
			request.setAttribute(RequestParameter.EMPLOYEE_NAME, empName);
			request.setAttribute(RequestParameter.DESIGNATION, empDes);
			request.setAttribute(RequestParameter.EMPLOYEE_EMAIL, empEmail);
			request.setAttribute(RequestParameter.EMPLOYEE_CONTACT, empContact);
			request.setAttribute(RequestParameter.PASSWORD, empPass);
			request.setAttribute(RequestParameter.LOGIN, session.getAttribute(RequestParameter.LOGIN));
			connection.sendRequest();
			saveBtn.setDisable(true);
			saveBtn.setVisible(false);
			empNameText.setEditable(false);
			empPassText.setEditable(false);
			empDesText.setEditable(false);
			empEmailText.setEditable(false);
			empContactText.setEditable(false);

			confirmationMsg.setStyle("-fx-text-fill: #24bb71");
			confirmationMsg.setText("Saved Successfully!");


		}
	}

	private void introPageEmployee() throws IOException
	{
		Stage newStage = createStage("/view/intropageemployee.fxml");
		newStage.showAndWait();
	}
}
