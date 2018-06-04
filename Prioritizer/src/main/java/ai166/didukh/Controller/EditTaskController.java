package ai166.didukh.Controller;

import static ai166.didukh.Main.model;
import static ai166.didukh.Main.needToDo;
import static java.lang.Integer.parseInt;

import ai166.didukh.Model.DBController;
import ai166.didukh.Model.DBException;
import ai166.didukh.Model.Task;
import java.sql.SQLException;
import java.util.ArrayList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class EditTaskController extends UIController{

  @FXML
  private TextField titleField;

  @FXML
  private TextArea descField;

  @FXML
  private DatePicker dueField;

  @FXML
  private TextField priorField;

  @FXML
  private Button confirmButton;

  @FXML
  private Button discardButton;

  @FXML
  private Label errorMessage;

  @FXML
  public void initialize(){

    titleField.setText(model.getTask().getTaskTitle());
    descField.setText(model.getTask().getTaskDesc());
    dueField.setValue(model.getTask().getTaskDeadline().toLocalDate());
    priorField.setText(String.valueOf(model.getTask().getTaskPriority()));
  }


  public void handleTaskEditConfirm(ActionEvent actionEvent) throws Exception{

    Task task = new Task();
    DBController dbaccess = new DBController();

    if(titleField.getText().isEmpty() || descField.getText().isEmpty() || dueField.getValue()==null){

      errorMessage.setText("Error: Fill in every field!");
    }
    else{

      task.setTaskID(model.getTask().getTaskID());
      task.setTaskTitle(titleField.getText());
      task.setTaskDesc(descField.getText());
      task.setTaskPriority(parseInt(priorField.getText()));
      task.setProjectReference(model.getProject().getProjectID());
      task.setTaskDeadline(java.sql.Date.valueOf(dueField.getValue()));

      try{

        dbaccess.updateTask(task);

        model.setTask(dbaccess.pullTask(task));

        for (int i = 0; i < model.getProject().getTasks().size(); i++) {

          if(model.getProject().getTasks().get(i).getTaskID() == model.getTask().getTaskID()){

            model.getProject().getTasks().set(i, model.getTask());
          }

        }

        changeWindow(confirmButton,"/View/MainWindow.fxml",800, 600);

      }catch(SQLException e){

        errorMessage.setText("Error: Database connection failed!");
      }catch(DBException e){

        errorMessage.setText("Error: A task with such title is already assigned to this project!");
      }
    }
  }

  public void handleTaskEditDiscard(ActionEvent actionEvent) throws Exception{

    changeWindow(discardButton,"/View/MainWindow.fxml",800, 600);
  }
}
