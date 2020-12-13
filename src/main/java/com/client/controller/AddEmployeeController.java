package com.client.controller;

import com.client.connection.RequestParameter;
import com.client.connection.ServerConnection;
import com.client.connection.UserRequest;
import com.client.information_window.AlertBox;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.net.URL;

import java.util.ResourceBundle;


public class AddEmployeeController extends AbstractController implements Initializable
{
	public JFXTextField getEmployee_name()
	{
		return employee_name;
	}

	public JFXTextField getEmployee_username()
	{
		return employee_username;
	}

	public JFXPasswordField getEmployee_password()
	{
		return employee_password;
	}

	public JFXTextField getEmployee_email()
	{
		return employee_email;
	}

	public JFXTextField getEmployee_contact()
	{
		return employee_contact;
	}

	public JFXTextField getEmployee_designation()
	{
		return employee_designation;
	}

	public JFXTextField getEmployee_department()
	{
		return employee_department;
	}


	@FXML
	private Button AddEmployeeButton;
	@FXML
	private JFXTextField employee_username;
	@FXML
	private JFXPasswordField employee_password;
	@FXML
	private JFXTextField employee_name;
	@FXML
	private JFXTextField employee_email;
	@FXML
	private JFXTextField employee_contact;
	@FXML
	private JFXTextField employee_designation;
	@FXML
	private JFXTextField employee_department;
	@FXML
	JFXComboBox<String> boxEmpGender = new JFXComboBox<>();
	@FXML
	private JFXTextField employee_id;
	private int empid;

	public Label errorMsg;

	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
		boxEmpGender.getItems().addAll("Male", "Female");
	}

	@FXML
	private void AddEmployee(javafx.event.ActionEvent event) throws Exception
	{
		insertEmployee();
	}

	private void insertEmployee()
	{

		String employeeName = getEmployee_name().getText();
		String department = getEmployee_department().getText();
		String password = getEmployee_password().getText();
		String username = getEmployee_username().getText();

		if (employeeName.trim().isEmpty() || department.trim().isEmpty() || password.trim().isEmpty() || username.trim().isEmpty())
		{
			errorMsg.setText("Please fillup the form correctly.");
			return;
		}
		ServerConnection connection = ServerConnection.getInstance();
		UserRequest request = connection.getRequest();
		request.setAttribute(RequestParameter.COMMAND_NAME, "INSERT_EMPLOYEE");
		request.setAttribute(RequestParameter.EMPLOYEE_NAME, getEmployee_name().getText());
		request.setAttribute(RequestParameter.EMPLOYEE_EMAIL, getEmployee_email().getText());
		request.setAttribute(RequestParameter.EMPLOYEE_CONTACT, getEmployee_contact().getText());
		request.setAttribute(RequestParameter.DESIGNATION, getEmployee_designation().getText());
		request.setAttribute(RequestParameter.EMPLOYEE_DEPARTMENT, getEmployee_department().getText());
		request.setAttribute(RequestParameter.EMPLOYEE_GENDER, boxEmpGender.getValue());
		request.setAttribute(RequestParameter.LOGIN, getEmployee_name().getText());
		request.setAttribute(RequestParameter.PASSWORD,getEmployee_password().getText());
		connection.sendRequest();
		AlertBox.display("Confirmation", "Employee named " + getEmployee_name().getText() + " has been added.");
	}


}
