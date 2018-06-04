package ai166.didukh.Controller;

import static ai166.didukh.Main.model;

import ai166.didukh.Model.DBController;
import ai166.didukh.Model.DBException;
import ai166.didukh.Model.Project;
import java.sql.Date;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;


public class NewProjectController extends UIController{

  @FXML
  private Label errorMessage;

  @FXML
  private TextField titleField;

  @FXML
  private TextArea descField;

  @FXML
  private DatePicker dueField;

  @FXML
  private Button confirmButton;

  @FXML
  private Button discardButton;

  public void handleNewProjectConfirm(ActionEvent actionEvent) throws Exception{

    Project project = new Project();
    DBController dbaccess = new DBController();

    if(titleField.getText().isEmpty() || descField.getText().isEmpty() || dueField.getValue()==null){

      errorMessage.setText("Error: Fill in every field!");
    }
    else{

      project.setProjectTitle(titleField.getText());
      project.setProjectDesc(descField.getText());
      project.setUserReference(model.getUser().getUserID());
      project.setProjectDeadline(java.sql.Date.valueOf(dueField.getValue()));

      try{

        dbaccess.putProject(project);

        if(model.getUser().getProjects() == null)
          model.getUser().setProjects(new ArrayList<>());

        model.getUser().getProjects().add(project);

        changeWindow(confirmButton,"/View/SavedProjectsWindow.fxml",800, 600);

      }catch(SQLException e){

        errorMessage.setText("Error: Database connection failed!");
      }catch(DBException e){

        errorMessage.setText("Error: A project with such title is already assigned to this user!");
      }
    }
  }

  public void handleNewProjectDiscard(ActionEvent actionEvent) throws Exception{

    changeWindow(discardButton,"/View/SavedProjectsWindow.fxml",800, 600);
  }
}
