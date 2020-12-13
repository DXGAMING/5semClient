package com.client.controller;

import com.client.connection.RequestParameter;
import com.client.connection.ServerConnection;
import com.client.connection.ServerResponse;
import com.client.connection.Session;
import com.client.connection.UserRequest;
import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import javafx.scene.control.Accordion;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;


public class AllProjectsPageController extends AbstractController implements Initializable
{
	private final Logger logger = LoggerFactory.getLogger(AllProjectsPageController.class);
	@FXML
	private JFXButton btnProjectDetail;
	@FXML
	private JFXButton allproject;
	@FXML
	private JFXButton searchproject;
	@FXML
	private Accordion accordion;
	@FXML
	private JFXButton homeBackBtn;

	@FXML
	private JFXButton btnAddEmployee;

	@Override
	public void initialize(final URL url, final ResourceBundle rb)
	{
		Image imageDecline = new Image(getClass().getResourceAsStream("/icons/home-icon.png"));
		ImageView cameraIconView = new ImageView(imageDecline);
		cameraIconView.setFitHeight(25);
		cameraIconView.setFitWidth(25);
		homeBackBtn.setGraphic(cameraIconView);
	}

	// When user clicks on Add new Project button
	@FXML
	private void projectDetail(final ActionEvent actionEvent) throws IOException
	{
		btnProjectDetail.setOnAction(event -> {
			try
			{
				projectDetailPage();
			}
			catch (IOException e)
			{
				logger.error("error while opening adding car");
			}
		});
	}

	// When "All project" button is clicked
	@FXML
	private void allproject(ActionEvent event) throws IOException
	{
		if (event.getSource() == allproject)
		{
			initializeProjects();
		}

	}

	// When "Search project" button is clicked
	@FXML
	private void SearchProjectAction(ActionEvent event) throws IOException
	{
		if (event.getSource() == searchproject)
		{
			searchProjectPage();
		}
	}

	public void initializeProjects() throws IOException
	{

		Session session = Session.getInstance();
		ServerResponse response;
		if ((boolean) session.getAttribute(RequestParameter.IS_ADMIN))
		{
			response = initProjectsAdmin();
		}
		else
		{
			response = initProjectsEmployee();
			btnProjectDetail.setDisable(true);
		}

		// Gerarating TitledPane taking project information from database
		TitledPane titledpane = new TitledPane();
		titledpane.setText((String) response.getAttribute(RequestParameter.PROJECT_NAME));

		// VBox is used to place content inside TitledPane
		VBox content = new VBox();

		// Variables to store information from Database
		String projectname = (String) response.getAttribute(RequestParameter.PROJECT_NAME);
		String startdate = (String) response.getAttribute(RequestParameter.PROJECT_START_DATE);
		String enddate = (String) response.getAttribute(RequestParameter.PROJECT_END_DATE);
		String estitime = (String) response.getAttribute(RequestParameter.ESTIMATED_TIME);
		String clientName = (String) response.getAttribute(RequestParameter.CLIENT_NAME);

		FXMLLoader Loader = new FXMLLoader();
		ProjectDetailController projectDetailController = Loader.getController();
		projectDetailController.getProjectID().setDisable(true);
		projectDetailController.setProjectName(projectname);
		projectDetailController.getProjectName().setEditable(false);
		projectDetailController.setStart_date(LocalDate.parse(startdate));
		projectDetailController.getStart_date().setDisable(true);
		projectDetailController.setEnd_date(LocalDate.parse(enddate));
		projectDetailController.getEnd_date().setDisable(true);
		projectDetailController.setEsti_time(estitime);
		projectDetailController.getEsti_time().setEditable(false);
		projectDetailController.getTableData();
		projectDetailController.getClientList();

		projectDetailController.ClientNameText.setDisable(false);
		projectDetailController.ClientNameText.setText(clientName);
		projectDetailController.projectClient.setVisible(false);
		// Save the information in VBox for TitledPane to display
		content.getChildren().add(new Label("Project Client: " + clientName));
		content.getChildren().add(new Label("Project Name: " + projectname));
		content.getChildren().add(new Label("Project Start Date: " + startdate));
		content.getChildren().add(new Label("Project End Date: " + enddate));
		content.getChildren().add(new Label("Estimated Time: " + estitime + " Days"));
		JFXButton showproject = new JFXButton("Show Project Detail");
		showproject.setStyle("-fx-background-color: linear-gradient(from 25% 25% to 100% 100%, #4a3f99, #42649d);");
		content.getChildren().add(showproject);

		titledpane.setContent(content);

		// accordion is an object of Accordion Class
		accordion.getPanes().add(titledpane);
		projectDetailPage();
	}



	//when Add Employee button is clicked
	@FXML
	private void addEmployeeAction(ActionEvent event) throws IOException
	{
		if (event.getSource() == btnAddEmployee)
		{
			Session session = Session.getInstance();
			if ((boolean) session.getAttribute(RequestParameter.IS_ADMIN))
			{
				addEmployeePage();
			}
		}
	}

	public void homeBackBtnAction(ActionEvent event) throws IOException
	{
		if (event.getSource() == homeBackBtn)
		{
			Session session = Session.getInstance();
			if ((boolean) session.getAttribute(RequestParameter.IS_ADMIN))
			{
				adminIntroPage();
			}
			else
			{
				employeeIntroPage();
			}
		}

	}

	private void projectDetailPage() throws IOException
	{
		Stage newStage = createStage("/view/projectdetail.fxml");
		newStage.showAndWait();
	}

	private void adminIntroPage() throws IOException
	{
		Stage newStage = createStage("/view/intropageadmin.fxml");
		newStage.showAndWait();
	}

	private void searchProjectPage() throws IOException
	{
		Stage newStage = createStage("/view/searchproject.fxml");
		newStage.showAndWait();
	}

	private void employeeIntroPage() throws IOException
	{
		Stage newStage = createStage("/view/intropageemployee.fxml");
		newStage.showAndWait();
	}

	private void addEmployeePage() throws IOException
	{
		Stage newStage = createStage("/view/addemployee.fxml");
		newStage.showAndWait();
	}

	private ServerResponse initProjectsAdmin()
	{
		ServerConnection connection = ServerConnection.getInstance();
		UserRequest request = connection.getRequest();
		request.setAttribute(RequestParameter.COMMAND_NAME, "GET_PROJECTS_ADMIN");
		connection.sendRequest();
		return connection.getResponse();
	}

	private ServerResponse initProjectsEmployee()
	{
		ServerConnection connection = ServerConnection.getInstance();
		UserRequest request = connection.getRequest();
		request.setAttribute(RequestParameter.COMMAND_NAME, "GET_PROJECTS_EMPLOYEE");
		connection.sendRequest();
		return connection.getResponse();
	}
}
