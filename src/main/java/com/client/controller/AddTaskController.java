package com.client.controller;

import com.client.connection.RequestParameter;
import com.client.connection.ServerConnection;
import com.client.connection.ServerResponse;
import com.client.connection.UserRequest;
import com.client.entity.Client;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXColorPicker;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import com.sun.org.apache.regexp.internal.RE;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.temporal.ChronoUnit;
import java.util.ResourceBundle;


public class AddTaskController extends AbstractController implements Initializable
{
	public JFXTextField getTask_name()
	{
		return Task_name;
	}

	public JFXTextField getAssignedto()
	{
		return Assignedto;
	}

	public JFXDatePicker getTask_start_date()
	{
		return task_start_date;
	}

	public JFXDatePicker getTask_end_date()
	{
		return task_end_date;
	}

	public JFXColorPicker getTask_color()
	{
		return task_color;
	}


	public JFXTextField getTaskProjectID()
	{
		return TaskProjectID;
	}

	public void setProjectName(JFXTextField projectName)
	{
		TaskProjectName.setText(projectName.getText());
	}

	public void setProject_id_task(JFXTextField project_id_task)
	{
		getTaskProjectID().setText(project_id_task.getText());
	}

	//Error messages used when changing the task_end_date value
	final private String NO_START_DATE_ERROR = "Start date cannot be empty";
	final private String INVALID_END_DATE = "End date must be equal or greater than start date";
	final private String NO_END_DATE_ERROR = "End date cannot be empty";

	@FXML
	private JFXTextField TaskProjectName;
	@FXML
	private JFXTextField TaskProjectID;
	@FXML
	private JFXTextField Task_name;
	@FXML
	private JFXTextField Assignedto;
	@FXML
	private JFXDatePicker task_start_date = new JFXDatePicker();
	@FXML
	private JFXDatePicker task_end_date = new JFXDatePicker();
	@FXML
	private JFXColorPicker task_color = new JFXColorPicker();
	@FXML
	private javafx.scene.control.Label invalid_date_label = new javafx.scene.control.Label();

	private ObservableList<String> dependencyTaskList = FXCollections.observableArrayList();
	private ObservableList<String> employeeList = FXCollections.observableArrayList();


	public void setDependency(ChoiceBox<String> dependency)
	{
		this.dependency = dependency;
	}

	public ChoiceBox<String> getDependency()
	{
		return dependency;
	}

	@FXML
	ChoiceBox<String> dependency = new ChoiceBox<String>();
	public ChoiceBox<String> employeeAssigned = new ChoiceBox<String>();

	/**
	 * Validates if start and end date have a valid range, that happens when start_date is lower or equals to end_date
	 *
	 * @return True if is valid, false if it isn't
	 */
	private boolean hasValidDateRange()
	{
		if (task_start_date.getValue() == null || task_end_date.getValue() == null)
		{
			return false;
		}
		final Date startDate = Date.valueOf(task_start_date.getValue());
		final Date endDate = Date.valueOf(task_end_date.getValue());

		//If endDate is greater than startDate return true (is valid) else return false (is invalid)
		return endDate.compareTo(startDate) >= 0;
	}

	public void getDependencyList()
	{

		ServerConnection connection = ServerConnection.getInstance();
		UserRequest request = connection.getRequest();
		request.setAttribute(RequestParameter.COMMAND_NAME, "GET_DEPENDENCIES");
		request.setAttribute(RequestParameter.TASK_NAME, getTask_name().getText());
		connection.sendRequest();
		ServerResponse response = connection.getResponse();
		int numberOfDependencies = (int) response.getAttribute(RequestParameter.SIZE);
		for (int i = 0; i < numberOfDependencies; i++)
		{
			String taskName = (String) response.getAttribute(RequestParameter.TASK_NAME + i);
			dependencyTaskList.add(taskName);
		}
		dependency.setItems(dependencyTaskList);

	}

	public void getEmployeeList()
	{

		ServerConnection connection = ServerConnection.getInstance();
		UserRequest request = connection.getRequest();
		request.setAttribute(RequestParameter.COMMAND_NAME, "GET_EMPLOYEES");
		request.setAttribute(RequestParameter.TASK_NAME, getTask_name().getText());
		connection.sendRequest();
		ServerResponse response = connection.getResponse();
		int numberOfEmployees = (int) response.getAttribute(RequestParameter.SIZE);
		for (int i = 0; i < numberOfEmployees; i++)
		{
			String login = (String) response.getAttribute(RequestParameter.LOGIN + i);
			String name = (String) response.getAttribute(RequestParameter.EMPLOYEE_NAME + i);
			employeeList.add(login + " - " + name);
		}
		employeeAssigned.setItems(employeeList);
	}

	@FXML
	private JFXButton AddTaskButton;


	private String calcDays(JFXDatePicker start_date, JFXDatePicker end_date) throws IOException
	{
		long intervalDays = (ChronoUnit.DAYS.between(start_date.getValue(), end_date.getValue()) + 1);
		return String.valueOf(intervalDays);
	}

	@FXML
	private void onDateChange(javafx.event.ActionEvent event)
	{
		if (hasValidDateRange())
		{
			invalid_date_label.setText(null);
		}
		else if (task_start_date.getValue() == null)
		{
			invalid_date_label.setText(NO_START_DATE_ERROR);
		}
		else if (task_end_date.getValue() != null)
		{
			invalid_date_label.setText(INVALID_END_DATE);
		}
		else if (event == null)
		{
			//This means the AddTaskButton button called this method and if so we must tell the user to specify the end_date
			invalid_date_label.setText(NO_END_DATE_ERROR);

		}

		if (invalid_date_label.getText() != null)
		{
			invalid_date_label.setVisible(true);
		}
		else
		{
			invalid_date_label.setVisible(false);
		}
	}

	@FXML
	private void AddTaskDetail(javafx.event.ActionEvent event) throws Exception
	{
		if (hasValidDateRange() && event.getSource() == AddTaskButton)
		{
			invalid_date_label.setVisible(false);
			insertProjectTask();
		}
		else
		{
			//We call the onDateChange to show the respective date error
			onDateChange(null);
		}

	}

	private void insertProjectTask() throws IOException
	{

		// getting the id of assigned employee
		String[] EmpArrayStr = employeeAssigned.getValue().split("-");
		int assignedEmployeeId = Integer.parseInt(EmpArrayStr[0].trim());

		if (assignedEmployeeId == 0)
		{
			invalid_date_label.setText("A valid employee must be selected.");
			return;
		}
		else
		{
			invalid_date_label.setText("");
		}

		ServerConnection connection = ServerConnection.getInstance();
		UserRequest request = connection.getRequest();
		request.setAttribute(RequestParameter.COMMAND_NAME, "INSERT_TASK");
		request.setAttribute(RequestParameter.TASK_NAME, getTask_name().getText());
		request.setAttribute(RequestParameter.TASK_TIME, calcDays(task_start_date, task_end_date));
		request.setAttribute(RequestParameter.TASK_START_DATE, task_start_date.getValue());
		request.setAttribute(RequestParameter.TASK_END_DATE, task_end_date.getValue());
		request.setAttribute(RequestParameter.TASK_PROGRESS, "0%");
		request.setAttribute(RequestParameter.TASK_ASSIGNED, assignedEmployeeId);
		request.setAttribute(RequestParameter.TASK_DEPENDENCY, dependency.getValue());
		connection.sendRequest();
	}



	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
		getEmployeeList();
		getDependencyList();
	}
}
