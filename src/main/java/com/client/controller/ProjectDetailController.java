package com.client.controller;

import com.client.connection.RequestParameter;
import com.client.connection.ServerConnection;
import com.client.connection.ServerResponse;
import com.client.connection.UserRequest;
import com.client.entity.Task;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ResourceBundle;


public class ProjectDetailController extends AbstractController implements Initializable
{
	private final Logger logger = LoggerFactory.getLogger(ProjectDetailController.class);
	public TableView<Task> tableview;
	public TableColumn<Task, String> TaskName;
	public TableColumn<Task, String> TaskTime;
	public TableColumn<Task, String> TaskStartDate;
	public TableColumn<Task, String> TaskEndDate;
	public TableColumn<Task, String> TaskProgress;
	public TableColumn<Task, String> TaskColor;
	public TableColumn<Task, String> TaskDependency;
	public TableColumn<Task, String> TaskAssinged;
	public JFXButton DeleteTaskButton;
	public Label isidexist;
	public JFXButton btnProjectDetail;
	public JFXButton searchproject;
	public JFXButton GanttChartButton;
	public JFXButton homeBackBtn;

	//Error messages used when changing the end_date value
	final private String NO_START_DATE_ERROR = "Start date cannot be empty";
	final private String INVALID_END_DATE = "End date must be equal or greater than start date";




	@FXML
	JFXTextField ProjectID;
	@FXML
	JFXTextField ProjectName;
	@FXML
	JFXTextField esti_time;
	@FXML
	JFXDatePicker start_date = new JFXDatePicker();
	LocalDate start_dateValue = start_date.getValue();
	@FXML
	JFXDatePicker end_date = new JFXDatePicker();
	LocalDate end_dateValue = end_date.getValue();
	@FXML
	Label invalid_date_label = new Label();
	@FXML
	private JFXButton btnAddTask;
	@FXML
	private JFXButton LoadTaskButton;
	@FXML
	private JFXButton allproject;

	@Override
	public void initialize(URL url, ResourceBundle rb)
	{

		TaskName.setCellValueFactory(new PropertyValueFactory<>("TaskName"));
		TaskTime.setCellValueFactory(new PropertyValueFactory<>("TaskTime"));
		TaskStartDate.setCellValueFactory(new PropertyValueFactory<>("TaskStartDate"));
		TaskEndDate.setCellValueFactory(new PropertyValueFactory<>("TaskEndDate"));
		TaskProgress.setCellValueFactory(new PropertyValueFactory<>("TaskProgress"));
		TaskColor.setCellValueFactory(new PropertyValueFactory<>("TaskColor"));
		TaskDependency.setCellValueFactory(new PropertyValueFactory<>("TaskDependency"));
		TaskAssinged.setCellValueFactory(new PropertyValueFactory<>("TaskAssinged"));

		tableview.setEditable(true);
		TaskProgress.setCellFactory(TextFieldTableCell.forTableColumn());

		Image imageDecline = new Image(getClass().getResourceAsStream("/icons/home-icon.png"));
		ImageView cameraIconView = new ImageView(imageDecline);
		cameraIconView.setFitHeight(25);
		cameraIconView.setFitWidth(25);
		homeBackBtn.setGraphic(cameraIconView);

		ClientNameText.setDisable(true);
	}

	/**
	 * Validates if start and end date have a valid range, that happens when start_date is lower or equals to end_date
	 *
	 * @return True if is valid, false if it isn't
	 */
	private boolean hasValidDateRange()
	{
		if (start_date.getValue() == null || end_date.getValue() == null)
		{
			return false;
		}
		final Date startDate = Date.valueOf(start_date.getValue());
		final Date endDate = Date.valueOf(end_date.getValue());

		//If endDate is greater than startDate return true (is valid) else return false (is invalid)
		return endDate.compareTo(startDate) >= 0;
	}

	private void insertProjectInfo()
	{
		getEsti_time();
		String[] ClientArrayStr = projectClient.getValue().split("-");
		int projectClientId = Integer.parseInt(ClientArrayStr[0].trim());

		if (projectClientId == 0)
		{
			invalid_date_label.setText("A valid employee must be selected.");
			return;
		}
		else
		{
			invalid_date_label.setText("");
		}

		try
		{
			ServerConnection connection = ServerConnection.getInstance();
			UserRequest request = connection.getRequest();
			request.setAttribute(RequestParameter.COMMAND_NAME, "INSERT_PROJECT_INFO");
			request.setAttribute(RequestParameter.PROJECT_NAME, getProjectName());
			request.setAttribute(RequestParameter.PROJECT_START_DATE, getStart_dateValue());
			request.setAttribute(RequestParameter.PROJECT_END_DATE, getEnd_dateValue());
			request.setAttribute(RequestParameter.CLIENT_NAME, getClientNameText());
			connection.sendRequest();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		getProjectName().setDisable(true);
		getProjectID().setDisable(true);
		getStart_date().setEditable(false);
		getEnd_date().setEditable(false);
	}


	@FXML
	private String calcDays(JFXDatePicker start_date, JFXDatePicker end_date)
	{
		long intervalDays = (ChronoUnit.DAYS.between(start_date.getValue(), end_date.getValue()) + 1);
		return String.valueOf(intervalDays);
	}

	@FXML
	public JFXTextField getEsti_time()
	{
		esti_time.setText(" " + calcDays(start_date, end_date) + " Days");
		return esti_time;
	}

	// When end date of the project is chosen, this function works
	@FXML
	private void showDays(ActionEvent event)
	{
		if (hasValidDateRange())
		{
			invalid_date_label.setVisible(false);
			insertProjectInfo();
			return;
		}
		else if (start_date.getValue() == null)
		{
			end_date.setValue(null);
			invalid_date_label.setText(NO_START_DATE_ERROR);
		}
		else
		{
			invalid_date_label.setText(INVALID_END_DATE);
		}
		invalid_date_label.setVisible(true);
	}

	@FXML
	private void startDateChanged(ActionEvent event)
	{
		if (end_date.getValue() != null)
		{
			showDays(null);
		}
		else if (invalid_date_label.isVisible())
		{
			//This means we were showing the NO_START_DATE_ERROR label error
			invalid_date_label.setVisible(false);
		}
	}

	@FXML
	private void addTask(final ActionEvent event) throws IOException
	{
		if (event.getSource() == btnAddTask)
		{
			addTaskPage();
		}
	}

	public void getTableData()
	{
		//Clear the all column data from table
		tableview.getItems().clear();

		ServerConnection connection = ServerConnection.getInstance();
		UserRequest request = connection.getRequest();
		request.setAttribute(RequestParameter.COMMAND_NAME, "GET_PROJECT_TABLE_DATA");
		ServerResponse response = connection.getResponse();

		int numberOfTasks = (int) response.getAttribute(RequestParameter.SIZE);
		for (int i = 0; i < numberOfTasks; i++)
		{
			String task_name = (String) response.getAttribute(RequestParameter.TASK_NAME + i);
			String time = (String) response.getAttribute(RequestParameter.TASK_TIME + i);
			String task_start_date = (String) response.getAttribute(RequestParameter.TASK_START_DATE + i);
			String task_end_date = (String) response.getAttribute(RequestParameter.TASK_END_DATE + i);
			String progress = (String) response.getAttribute(RequestParameter.TASK_PROGRESS + i);
			String dependency = (String) response.getAttribute(RequestParameter.TASK_DEPENDENCY + i);
			String assigned = (String) response.getAttribute(RequestParameter.TASK_ASSIGNED + i);

			Task task = new Task(task_name, time, task_start_date, task_end_date, progress, dependency, assigned);
			tableview.getItems().add(task);
		}
	}

	public void LoadTaskHandle(ActionEvent event)
	{
		if (event.getSource() == LoadTaskButton)
		{
			getTableData();
		}
	}

	public void onEditProgress(TableColumn.CellEditEvent<Task, String> taskStringCellEditEvent) throws SQLException
	{
		Task task = tableview.getSelectionModel().getSelectedItem();
		task.setTaskProgress(taskStringCellEditEvent.getNewValue());

		ServerConnection connection = ServerConnection.getInstance();
		UserRequest request = connection.getRequest();
		request.setAttribute(RequestParameter.COMMAND_NAME, "UPDATE_TASK_PROGRESS");
		request.setAttribute(RequestParameter.TASK_PROGRESS, task.getTaskProgress());
		request.setAttribute(RequestParameter.TASK_NAME, task.getTaskName());
		connection.sendRequest();
	}

	//CLient Choicebox

	public String getClientNameText()
	{
		return ClientNameText.getText();
	}

	public void setClientNameText(String clientNameText)
	{
		this.ClientNameText.setText(clientNameText);
	}

	public JFXTextField ClientNameText;
	private ObservableList<String> clientList = FXCollections.observableArrayList();


	public ChoiceBox<String> projectClient = new ChoiceBox<String>();

	public void getClientList()
	{
		ServerConnection connection = ServerConnection.getInstance();
		UserRequest request = connection.getRequest();
		request.setAttribute(RequestParameter.COMMAND_NAME, "GET_PROJECT_TABLE_DATA");
		ServerResponse response = connection.getResponse();

		int numberOfClients = (int) response.getAttribute(RequestParameter.SIZE);
		for (int i = 0; i < numberOfClients; i++)
		{
			clientList.add(response.getAttribute(RequestParameter.CLIENT_NAME+i) + " - " + response.getAttribute(RequestParameter.LOGIN+i));
		}
		projectClient.setItems(clientList);
	}

	public void userIsEmployee()
	{
		btnAddTask.setDisable(true);
		DeleteTaskButton.setDisable(true);
		btnProjectDetail.setDisable(true);
		tableview.setEditable(false);
	}

	public void DeleteTaskHandle(final ActionEvent event) throws SQLException
	{
		if (event.getSource() == DeleteTaskButton)
		{
			Task selectedItem = tableview.getSelectionModel().getSelectedItem();
			tableview.getItems().remove(selectedItem);
			ServerConnection connection = ServerConnection.getInstance();
			UserRequest request = connection.getRequest();
			request.setAttribute(RequestParameter.COMMAND_NAME, "DELETE_TASK");
			request.setAttribute(RequestParameter.TASK_NAME, selectedItem.getTaskName());
			connection.sendRequest();
		}
	}

	public void allproject(final ActionEvent event) throws Exception
	{
		if (event.getSource() == allproject)
		{
			projectSummaryPage();
		}
	}


	public void ProjectDetail(final ActionEvent event)
	{
		if (event.getSource() == btnProjectDetail)
		{
			FXMLLoader Loader = new FXMLLoader();
			ProjectDetailController projectDetailController = Loader.getController();
			projectDetailController.getClientList();
		}
	}

	public void SearchProjectAction(final ActionEvent event) throws IOException
	{
		if (event.getSource() == searchproject)
		{
			searchProjectPage();
		}
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

	private void adminIntroPage() throws IOException
	{
		Stage newStage = createStage("/view/intropageadmin.fxml");
		newStage.showAndWait();
	}

	private void addTaskPage() throws IOException
	{
		Stage newStage = createStage("/view/addtask.fxml");
		newStage.showAndWait();
	}

	private void projectSummaryPage() throws IOException
	{
		Stage newStage = createStage("/view/projectsummary.fxml");
		newStage.showAndWait();
	}

	private void searchProjectPage() throws IOException
	{
		Stage newStage = createStage("/view/searchproject.fxml");
		newStage.showAndWait();
	}
	public JFXTextField getProjectID()
	{
		return ProjectID;
	}

	public JFXTextField getProjectName()
	{
		return ProjectName;
	}

	public void setProjectID(String projectID)
	{
		ProjectID.setText(projectID);
	}

	public void setProjectName(String projectName)
	{
		ProjectName.setText(projectName);
	}

	public void setEsti_time(String esti_time)
	{
		this.esti_time.setText(esti_time);
	}

	public JFXDatePicker getStart_date()
	{
		return start_date;
	}

	public void setStart_date(LocalDate start_date)
	{
		this.start_date.setValue(start_date);
	}

	public LocalDate getStart_dateValue()
	{
		return start_dateValue;
	}

	public void setStart_dateValue(LocalDate start_dateValue)
	{
		this.start_dateValue = start_dateValue;
	}

	public JFXDatePicker getEnd_date()
	{
		return end_date;
	}

	public void setEnd_date(LocalDate end_date)
	{
		this.end_date.setValue(end_date);
	}

	public LocalDate getEnd_dateValue()
	{
		return end_dateValue;
	}

	public void setEnd_dateValue(LocalDate end_dateValue)
	{
		this.end_dateValue = end_dateValue;
	}

}
