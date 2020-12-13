package com.client.controller;

import com.client.connection.RequestParameter;
import com.client.connection.ServerConnection;
import com.client.connection.ServerResponse;
import com.client.connection.UserRequest;
import com.client.entity.Project;
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


public class EmployeeIntroPageController extends AbstractController implements Initializable
{
	public JFXButton allProjectsBtn;
	public JFXButton searchProjectBtn;
	public JFXButton logoutBtn;
	public Label nameUser;
	public Label projectCountLabel;
	public TableView<Project> projectTableView;

	public TableColumn<Project, String> projectName;
	public TableColumn<Project, String> projectStartDate;
	public TableColumn<Project, String> projectEndDate;


	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
		projectName.setCellValueFactory(new PropertyValueFactory<>("projectName"));
		projectStartDate.setCellValueFactory(new PropertyValueFactory<>("projectStartDate"));
		projectEndDate.setCellValueFactory(new PropertyValueFactory<>("projectEndDate"));
		projectTableView.setEditable(false);

		getLogoutIcon();
	}

	public void getProjectTableData()
	{
		//Clear the all column data from table
		projectTableView.getItems().clear();

		ServerConnection connection = ServerConnection.getInstance();
		UserRequest request = connection.getRequest();
		request.setAttribute(RequestParameter.COMMAND_NAME, "SHOW_PROJECTS");
		connection.sendRequest();
		ServerResponse response = connection.getResponse();
		int numberOfProjects = (int) response.getAttribute(RequestParameter.SIZE);
		for (int i = 0; i < numberOfProjects; i++)
		{
			String name = (String) response.getAttribute(RequestParameter.PROJECT_NAME + i);
			String startDate = (String) response.getAttribute(RequestParameter.PROJECT_START_DATE + i);
			String endDate = (String) response.getAttribute(RequestParameter.PROJECT_END_DATE + i);
			Project singleProject = new Project(name, startDate, endDate);
			projectTableView.getItems().add(singleProject);
		}
		projectCountLabel.setText("Currently you have " + String.valueOf(numberOfProjects) + " projects");
	}

	public void getEmployeeName(int id)
	{
		String employeeName = "USER";
		ServerConnection connection = ServerConnection.getInstance();
		UserRequest request = connection.getRequest();
		request.setAttribute(RequestParameter.COMMAND_NAME, "GET_EMPLOYEE_NAME");
		connection.sendRequest();
		ServerResponse response = connection.getResponse();
		employeeName = (String) response.getAttribute(RequestParameter.EMPLOYEE_NAME);
		nameUser.setText("Welcome  " + employeeName + " !");
	}


	public void getLogoutIcon()
	{
		Image imageDecline = new Image(getClass().getResourceAsStream("/icons/log-out.png"));
		ImageView cameraIconView = new ImageView(imageDecline);
		cameraIconView.setFitHeight(18);
		cameraIconView.setFitWidth(18);
		logoutBtn.setGraphic(cameraIconView);
	}

	public JFXButton profileBtn;

	public void ProfileBtnAction(final ActionEvent event) throws IOException
	{
		if (event.getSource() == profileBtn)
		{
			employeePage();
		}
	}

	public void allProjectsBtnAction(final ActionEvent event) throws IOException
	{
		if (event.getSource() == allProjectsBtn)
		{
			allProjectsPage();
		}
	}


	public void searchProjectBtnAction(ActionEvent event) throws IOException
	{
		if (event.getSource() == searchProjectBtn)
		{
			searchProjectPage();
		}
	}


	public void logoutBtnAction(ActionEvent event) throws IOException
	{
		if (event.getSource() == logoutBtn)
		{
			loginPage();
		}
	}

	private void employeePage() throws IOException
	{
		Stage newStage = createStage("/view/employeeprofile.fxml");
		newStage.showAndWait();
	}

	private void allProjectsPage() throws IOException
	{
		Stage newStage = createStage("/view/projectsummary.fxml");
		newStage.showAndWait();
	}

	private void searchProjectPage() throws IOException
	{
		Stage newStage = createStage("/view/searchproject.fxml");
		newStage.showAndWait();
	}
	private void loginPage() throws IOException
	{
		Stage newStage = createStage("/view/loginpage.fxml");
		newStage.showAndWait();
	}
}
