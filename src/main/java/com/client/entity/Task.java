package com.client.entity;

import javafx.beans.property.SimpleStringProperty;


public class Task implements Entity
{

    private SimpleStringProperty TaskName;
    private SimpleStringProperty TaskTime;
    private SimpleStringProperty TaskStartDate;
    private SimpleStringProperty TaskEndDate;
    private SimpleStringProperty TaskProgress;
    private SimpleStringProperty TaskDependency;
    private SimpleStringProperty taskAssigned;

    public Task(String taskName, String taskTime, String taskStartDate, String taskEndDate, String taskProgress, String taskDependency, String assigned) {
        TaskName = new SimpleStringProperty(taskName);
        TaskTime = new SimpleStringProperty(taskTime);
        TaskStartDate = new SimpleStringProperty(taskStartDate);
        TaskEndDate = new SimpleStringProperty(taskEndDate);
        TaskProgress = new SimpleStringProperty(taskProgress);
        TaskDependency = new SimpleStringProperty(taskDependency);
        taskAssigned = new SimpleStringProperty(assigned);
    }

    public String getTaskName() {
        return TaskName.get();
    }

    public void setTaskName(String taskName) {
        TaskName = new SimpleStringProperty(taskName);
    }

    public String getTaskTime() {
        return TaskTime.get();
    }

    public void setTaskTime(String taskTime) {
        TaskTime = new SimpleStringProperty(taskTime);
    }

    public String getTaskStartDate() {
        return TaskStartDate.get();
    }

    public void setTaskStartDate(String taskStartDate) {
        TaskStartDate = new SimpleStringProperty(taskStartDate);
    }

    public String getTaskEndDate() {
        return TaskEndDate.get();
    }

    public void setTaskEndDate(String taskEndDate) {
        TaskEndDate = new SimpleStringProperty(taskEndDate);
    }

    public String getTaskProgress() {
        return TaskProgress.get();
    }

    public void setTaskProgress(String taskProgress) {
        this.TaskProgress = new SimpleStringProperty(taskProgress);
    }

    public String getTaskDependency() {
        return TaskDependency.get();
    }

    public void setTaskDependency(String taskDependency) {
        TaskDependency = new SimpleStringProperty(taskDependency);
    }

    public String getTaskAssigned() {
        return taskAssigned.get();
    }

    public void setTaskAssigned(String taskAssigned) {
        this.taskAssigned = new SimpleStringProperty(taskAssigned);
    }
}
