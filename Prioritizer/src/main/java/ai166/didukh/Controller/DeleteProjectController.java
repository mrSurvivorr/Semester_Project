package ai166.didukh.Controller;

import static ai166.didukh.Main.model;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class DeleteProjectController extends UIController{

  @FXML
  private Button confirmButton;

  @FXML
  private Button cancelButton;

  @FXML
  private Label warningMessage;

  @FXML
  public void initialize(){

    warningMessage.setText("Delete " + model.getProject().getProjectTitle() + "?");
  }

  public void handleDeleteConfirm(ActionEvent actionEvent) throws Exception{

    closeWindow(confirmButton);
  }

  public void handleDeleteCancel(ActionEvent actionEvent) throws Exception{

    closeWindow(cancelButton);
  }
}
