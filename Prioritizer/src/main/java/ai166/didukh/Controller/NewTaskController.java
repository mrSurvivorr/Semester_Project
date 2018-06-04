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

public class NewTaskController extends UIController{

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

  public void handleNewTaskConfirm(ActionEvent actionEvent) throws Exception{

    Task task = new Task();
    DBController dbaccess = new DBController();

    if(titleField.getText().isEmpty() || descField.getText().isEmpty() || dueField.getValue()==null){

      errorMessage.setText("Error: Fill in every field!");
    }
    else{

      task.setTaskTitle(titleField.getText());
      task.setTaskDesc(descField.getText());
      task.setTaskPriority(parseInt(priorField.getText()));
      task.setProjectReference(model.getProject().getProjectID());
      task.setTaskDeadline(java.sql.Date.valueOf(dueField.getValue()));
      task.setStatus(needToDo);

      try{

        dbaccess.putTask(task);

        if(model.getProject().getTasks() == null)
          model.getProject().setTasks(new ArrayList<>());

        model.getProject().getTasks().add(task);

        changeWindow(confirmButton,"/View/MainWindow.fxml",800, 600);

      }catch(SQLException e){

        errorMessage.setText("Error: Database connection failed!");
      }catch(DBException e){

        errorMessage.setText("Error: A task with such title is already assigned to this project!");
      }
    }
  }

  public void handleNewTaskDiscard(ActionEvent actionEvent) throws Exception{

    changeWindow(discardButton,"/View/MainWindow.fxml",800, 600);
  }

}
