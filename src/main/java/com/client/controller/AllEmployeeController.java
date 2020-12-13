package com.client.controller;

import com.client.connection.RequestParameter;
import com.client.connection.ServerConnection;
import com.client.connection.ServerResponse;
import com.client.connection.UserRequest;
import com.client.entity.Employee;
import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;


public class AllEmployeeController extends AbstractController implements Initializable
{
	Stage stage;

	private String userRole;
	private int adminId;

	public String getUserRole()
	{
		return userRole;
	}

	public void setUserRole(String userRole)
	{
		this.userRole = userRole;
	}

	public int getAdminId()
	{
		return adminId;
	}

	public void setAdminId(int adminId)
	{
		this.adminId = adminId;
	}


	public JFXButton homeBackBtn;
	public JFXButton AddEmployeeBtn;
	public JFXButton viewAllEmployee;
	public TableView<Employee> employeeTableView;
	public Label employeeCountLabel;
	public TableColumn<Employee, String> empId;
	public TableColumn<Employee, String> empName;
	public TableColumn<Employee, String> empDept;
	public TableColumn<Employee, String> empDesignation;
	public TableColumn<Employee, String> empEmail;
	public TableColumn<Employee, String> empPhone;

	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
		empId.setCellValueFactory(new PropertyValueFactory<>("empId"));
		empName.setCellValueFactory(new PropertyValueFactory<>("empName"));
		empDept.setCellValueFactory(new PropertyValueFactory<>("empDept"));
		empDesignation.setCellValueFactory(new PropertyValueFactory<>("empDesignation"));
		empEmail.setCellValueFactory(new PropertyValueFactory<>("empEmail"));
		empPhone.setCellValueFactory(new PropertyValueFactory<>("empPhone"));
		employeeTableView.setEditable(true);

		getEmployeeTableData();

		Image imageDecline = new Image(getClass().getResourceAsStream("/icons/home-icon.png"));
		ImageView cameraIconView = new ImageView(imageDecline);
		cameraIconView.setFitHeight(25);
		cameraIconView.setFitWidth(25);
		homeBackBtn.setGraphic(cameraIconView);
	}

	private void getEmployeeTableData()
	{
		//Clear the all column data from table
		employeeTableView.getItems().clear();
		ServerConnection connection = ServerConnection.getInstance();
		UserRequest request = connection.getRequest();
		request.setAttribute(RequestParameter.COMMAND_NAME, "SHOW_EMPLOYEES");
		connection.sendRequest();
		ServerResponse response = connection.getResponse();
		int numberOfEmployees = (int) response.getAttribute(RequestParameter.SIZE);
		for (int i = 0; i < numberOfEmployees; i++)
		{
			String login = (String) response.getAttribute(RequestParameter.EMPLOYEE_EMAIL + i);
			String name = (String) response.getAttribute(RequestParameter.EMPLOYEE_NAME + i);
			String designation = (String) response.getAttribute(RequestParameter.DESIGNATION + i);
			String department = (String) request.getAttribute(RequestParameter.EMPLOYEE_DEPARTMENT + i);
			String phone = (String) request.getAttribute(RequestParameter.EMPLOYEE_CONTACT + i);
			String email = (String) request.getAttribute(RequestParameter.EMPLOYEE_EMAIL + i);
			Employee singleEmployee = new Employee(login, name, department, designation, phone, email);
			employeeTableView.getItems().add(singleEmployee);
		}
		employeeCountLabel.setText(String.valueOf(numberOfEmployees));

		employeeCountLabel.setText("Currently you have " + numberOfEmployees + " employees.");
	}



	public void homeBackBtnAction(final ActionEvent event) throws IOException
	{
		if (event.getSource() == homeBackBtn)
		{
			adminIntroPage();
		}
	}

	public void AddEmployeeBtnAction(final ActionEvent event) throws IOException
	{
		if (event.getSource() == AddEmployeeBtn)
		{
			addEmployeePage();
		}
	}

	public void viewAllEmployeeAction(ActionEvent event)
	{
	}

	private void adminIntroPage() throws IOException
	{
		Stage newStage = createStage("/view/intropageadmin.fxml");
		newStage.showAndWait();
	}

	private void addEmployeePage() throws IOException
	{
		Stage newStage = createStage("/view/addemployee.fxml");
		newStage.showAndWait();
	}
}
