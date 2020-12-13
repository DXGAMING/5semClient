package com.client.controller;

import com.client.connection.RequestParameter;
import com.client.connection.ServerConnection;
import com.client.connection.ServerResponse;
import com.client.connection.UserRequest;
import com.client.entity.Client;
import com.client.entity.Employee;
import com.client.entity.Project;
import com.jfoenix.controls.JFXButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;

import java.sql.SQLException;
import java.util.ResourceBundle;


public class AdminIntroPageController extends AbstractController implements Initializable
{
	private final Logger logger = LoggerFactory.getLogger(AdminIntroPageController.class);
	private final ObservableList<Project> projectsData = FXCollections.observableArrayList();
	private final ObservableList<Employee> employeeData = FXCollections.observableArrayList();
	private final ObservableList<Client> clientData = FXCollections.observableArrayList();

	public JFXButton allProjectsBtn;
	public JFXButton addNewProjectBtn;
	public JFXButton searchProjectBtn;
	public JFXButton addClient;
	public JFXButton allClientBtn;
	public Label clientCountLabel;
	public JFXButton logoutBtn;
	public JFXButton addEmployeeBtn;
	public JFXButton allEmployeeBtn;
	public JFXButton profileBtn;

	public Label nameUser;
	public Label projectCountLabel;
	public Label employeeCountLabel;
	public TableView<Project> projectTableView;
	public TableView<Employee> employeeTableView;
	public TableView<Client> clientTableView;


	public TableColumn<Project, String> projectName;
	public TableColumn<Project, String> projectStartDate;
	public TableColumn<Project, String> projectEndDate;

	public TableColumn<Employee, String> employeeId;
	public TableColumn<Employee, String> employeeName;
	public TableColumn<Employee, String> employeeDesignation;

	public TableColumn<Client, String> clientId;
	public TableColumn<Client, String> clientName;
	public TableColumn<Client, String> contactPerson;


	public void getLogoutIcon()
	{
		Image imageDecline = new Image(getClass().getResourceAsStream("/icons/log-out.png"));
		ImageView cameraIconView = new ImageView(imageDecline);
		cameraIconView.setFitHeight(18);
		cameraIconView.setFitWidth(18);
		logoutBtn.setGraphic(cameraIconView);
	}

	@FXML
	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
		initData();

		projectName.setCellValueFactory(new PropertyValueFactory<>("projectName"));
		projectStartDate.setCellValueFactory(new PropertyValueFactory<>("projectStartDate"));
		projectEndDate.setCellValueFactory(new PropertyValueFactory<>("projectEndDate"));
		projectTableView.setEditable(false);

		employeeId.setCellValueFactory(new PropertyValueFactory<>("employeeId"));
		employeeName.setCellValueFactory(new PropertyValueFactory<>("employeeName"));
		employeeDesignation.setCellValueFactory(new PropertyValueFactory<>("designation"));
		employeeTableView.setEditable(false);

		clientId.setCellValueFactory(new PropertyValueFactory<>("clientId"));
		clientName.setCellValueFactory(new PropertyValueFactory<>("clientName"));
		contactPerson.setCellValueFactory(new PropertyValueFactory<>("contactPerson"));
		clientTableView.setEditable(false);

		getLogoutIcon();
		getAdminName();
	}

	public void getAdminName()
	{
		try
		{
			ServerConnection connection = ServerConnection.getInstance();
			UserRequest request = connection.getRequest();
			request.setAttribute(RequestParameter.COMMAND_NAME, "ADMIN_NAME");
			connection.sendRequest();
			ServerResponse response = connection.getResponse();
			String adminName = (String) response.getAttribute(RequestParameter.ADMIN_NAME);
			nameUser.setText(String.format("Welcome %s !", adminName));
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public void profileBtnAction(final ActionEvent event) throws SQLException, IOException
	{
		if (event.getSource() == profileBtn)
		{
			profilePage();
		}
	}

	public void allProjectsBtnAction(final ActionEvent event) throws IOException
	{
		if (event.getSource() == allProjectsBtn)
		{
			projectSummaryPage();
		}
	}

	public void addNewProjectBtnAction(final ActionEvent event) throws IOException
	{
		if (event.getSource() == addNewProjectBtn)
		{
			projectDetailPage();
		}
	}

	public void searchProjectBtnAction(final ActionEvent event) throws IOException
	{
		if (event.getSource() == searchProjectBtn)
		{
			searchProjectPage();
		}
	}

	public void allEmployeeBtnAction(final ActionEvent event) throws IOException
	{
		if (event.getSource() == allEmployeeBtn)
		{
			allEmployeePage();
		}
	}

	public void addEmployeeBtnAction(final ActionEvent event) throws IOException
	{
		if (event.getSource() == addEmployeeBtn)
		{
			addEmployeePage();
		}
	}

	public void addClientBtnAction(final ActionEvent event) throws IOException
	{
		if (event.getSource() == addClient)
		{
			addClientPage();
		}
	}

	public void allClientBtnAction(final ActionEvent event) throws IOException
	{
		if (event.getSource() == allClientBtn)
		{
			allClientPage();
		}
	}

	public void logoutBtnAction(final ActionEvent actionEvent)
	{
		logoutBtn.setOnAction(event -> {
			((Stage) ((Node) actionEvent.getSource()).getScene().getWindow()).close();
		});
	}

	private void initData()
	{
		initClients();
		initEmployees();
		initProjects();
	}

	private void initProjects()
	{
		projectsData.clear();
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
			projectsData.add(new Project(name, startDate, endDate));
		}
		projectCountLabel.setText(String.format("Currently you have %s projects", numberOfProjects));
	}

	private void initEmployees()
	{
		employeeData.clear();
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
			employeeData.add(new Employee(login, name, designation));
		}
		employeeCountLabel.setText(String.valueOf(numberOfEmployees));
	}

	private void initClients()
	{
		clientData.clear();
		ServerConnection connection = ServerConnection.getInstance();
		UserRequest request = connection.getRequest();
		request.setAttribute(RequestParameter.COMMAND_NAME, "SHOW_CLIENTS");
		connection.sendRequest();
		ServerResponse response = connection.getResponse();
		int numberOfClients = (int) response.getAttribute(RequestParameter.SIZE);
		for (int i = 0; i < numberOfClients; i++)
		{
			long login = (long) response.getAttribute(RequestParameter.LOGIN + i);
			String name = (String) response.getAttribute(RequestParameter.CLIENT_NAME + i);
			String contact = (String) response.getAttribute(RequestParameter.CLIENT_CONTACT + i);
			clientData.add(new Client(login, name, contact));
		}
		clientCountLabel.setText(String.valueOf(numberOfClients));
	}

	private void profilePage() throws IOException
	{
		Stage newStage = createStage("/view/adminprofile.fxml");
		newStage.showAndWait();
	}

	private void projectSummaryPage() throws IOException
	{
		Stage newStage = createStage("/view/projectsummary.fxml");
		newStage.showAndWait();
	}

	private void projectDetailPage() throws IOException
	{
		Stage newStage = createStage("/view/projectdetail.fxml");
		newStage.showAndWait();
	}

	private void searchProjectPage() throws IOException
	{
		Stage newStage = createStage("/view/searchproject.fxml");
		newStage.showAndWait();
	}

	private void allEmployeePage() throws IOException
	{
		Stage newStage = createStage("/view/allemployee.fxml");
		newStage.showAndWait();
	}

	private void addEmployeePage() throws IOException
	{
		Stage newStage = createStage("/view/addemployee.fxml");
		newStage.showAndWait();
	}

	private void addClientPage() throws IOException
	{
		Stage newStage = createStage("/view/addclient.fxml");
		newStage.showAndWait();
	}

	private void allClientPage() throws IOException
	{
		Stage newStage = createStage("/view/allclient.fxml");
		newStage.showAndWait();
	}
}
