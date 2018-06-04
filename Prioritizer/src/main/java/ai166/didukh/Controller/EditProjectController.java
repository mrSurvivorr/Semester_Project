package ai166.didukh.Controller;

import static ai166.didukh.Main.model;

import ai166.didukh.Model.DBController;
import ai166.didukh.Model.DBException;
import ai166.didukh.Model.Project;
import java.io.IOException;
import java.sql.SQLException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class EditProjectController extends UIController{

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

  @FXML
  public void initialize(){

    titleField.setText(model.getProject().getProjectTitle());
    descField.setText(model.getProject().getProjectDesc());
    dueField.setValue(model.getProject().getProjectDeadline().toLocalDate());
  }

  public void handleProjectEditConfirm(ActionEvent actionEvent) throws Exception {

    Project project = new Project();
    DBController dbaccess = new DBController();

    if(titleField.getText().isEmpty() || descField.getText().isEmpty() || dueField.getValue()==null){

      errorMessage.setText("Error: Fill in every field!");
    }
    else{

      project.setProjectID(model.getProject().getProjectID());
      project.setProjectTitle(titleField.getText());
      project.setProjectDesc(descField.getText());
      project.setUserReference(model.getUser().getUserID());
      project.setProjectDeadline(java.sql.Date.valueOf(dueField.getValue()));

      try{

        dbaccess.updateProject(project);

        model.setProject(dbaccess.pullProject(project));

        for (int i = 0; i < model.getUser().getProjects().size(); i++) {

          if(model.getUser().getProjects().get(i).getProjectID() == model.getProject().getProjectID()){
            model.getUser().getProjects().set(i,model.getProject());
          }
        }
        model.getProject().setTasks(dbaccess.pullTasks(model.getProject().getProjectID()));

        changeWindow(confirmButton,"/View/MainWindow.fxml",800, 600);

      }catch(SQLException e){

        errorMessage.setText("Error: Database connection failed!");
      }catch(DBException e){

        errorMessage.setText("Error: A project with such title is already assigned to this user!");
      }
    }
  }

  public void handleProjectEditDiscard(ActionEvent actionEvent) throws Exception{

    changeWindow(discardButton,"/View/MainWindow.fxml",800, 600);
  }

}
