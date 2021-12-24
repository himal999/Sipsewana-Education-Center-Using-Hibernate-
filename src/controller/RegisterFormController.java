package controller;/*
author :Himal
version : 0.0.1
*/

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import db.DbConnection;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import model.Program;
import model.TM.ProgramTM;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class RegisterFormController {
    public JFXComboBox cmbCode;
    public JFXButton btnAdd;
    public JFXButton btnOk;
    public Label lblNoOfProgram;
    public Label lblFullAmount;
    public JFXTextField txtName;
    public JFXTextField txtFee;
    public JFXTextField txtDuration;
    public TableView<ProgramTM> tblAdd;
    public AnchorPane subRoot;

    private ArrayList<Program> programArrayList = new ArrayList<>();
    private String select;

    /*Scene data*/

    public AnchorPane mainRoot;
    public Button submit;
    public static  ArrayList<ProgramTM> tempPr = new ArrayList<>();

    /*second adding data to db */

    public String nic;
    public  boolean bool;

    public void initialize()  {

    }

    public void initialize(int id) throws SQLException, ClassNotFoundException {


        btnOk.setDisable(true);
        btnAdd.setDisable(true);

        txtFee.setEditable(false);

        txtName.setEditable(false);

        txtDuration.setEditable(false);



        if(bool){

            loadData();
        }
        if(!bool){

            newLoadData();
        }

        cmbCode.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            setData(select=newValue.toString());
        });

        tblAdd.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("id"));
        tblAdd.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("name"));
        tblAdd.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("duration"));
        tblAdd.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("fee"));
        tblAdd.getColumns().get(4).setCellValueFactory(new PropertyValueFactory<>("action"));

        calData();
    }

    public void initialize(String nic) throws SQLException, ClassNotFoundException {


        ArrayList<String> cCid = new ArrayList<>();
        PreparedStatement pst = DbConnection.getInstance().getConnection().prepareStatement("SELECT c_id FROM `stu course register` WHERE nic=?");
        pst.setObject(1,nic);
        ResultSet rst = pst.executeQuery();
        while (rst.next()){
            cCid.add(rst.getString("c_id"));
        }

        pst = DbConnection.getInstance().getConnection().prepareStatement("SELECT c_id FROM `course`");
        rst = pst.executeQuery();
      L1:  while (rst.next()){
            for(String temp:cCid){
                if(rst.getString("c_id").equalsIgnoreCase(temp)){
                    continue  L1;
                }
            }
            cmbCode.getItems().add(rst.getString("c_id"));
        }
    }

    public void addToTable(ActionEvent actionEvent) {


     boolean exit =  tblAdd.getItems().stream().anyMatch(programTM -> programTM.getId().equalsIgnoreCase(select));

     if(!exit){
      Button delete = new Button("Drop");
      HBox action = new HBox(delete);
      tblAdd.getItems().add(new ProgramTM(select,txtName.getText(),txtDuration.getText(),Double.parseDouble(txtFee.getText()),action));
      calData();

      if(!tblAdd.getItems().isEmpty()){
             btnOk.setDisable(false);
         }
      delete.setOnAction(event -> {

          tblAdd.getItems().removeAll(tblAdd.getSelectionModel().getSelectedItem());
          calData();

          if(tblAdd.getItems().isEmpty()){
              btnOk.setDisable(true);
          }
      });

      return;
     }

     new Alert(Alert.AlertType.WARNING,"This Program Register Already Exists").show();
     return;
 }


    public void finishToAdd(ActionEvent actionEvent) {

        for(ProgramTM temp:tblAdd.getItems()){
             tempPr.add(temp);
         }

          FXMLLoader loader = new FXMLLoader();
          loader.setLocation(getClass().getClassLoader().getResource("view/studentForm.fxml"));
          StudentFormController st =  loader.getController();
          st.programTMS = tempPr;

          st.amount = Double.parseDouble(lblFullAmount.getText());
          mainRoot.setDisable(false);
          submit.setDisable(false);
          Stage stage = (Stage)subRoot.getScene().getWindow();
          stage.close();
    }

    private void loadData() throws SQLException, ClassNotFoundException {
        PreparedStatement pst = DbConnection.getInstance().getConnection().prepareStatement("SELECT * FROM `course`");
        ResultSet rst  = pst.executeQuery();
        while (rst.next()){

            programArrayList.add(new Program(rst.getString(1),rst.getString(2),rst.getString(3),rst.getDouble(4)));
        }

        for(Program temp:programArrayList) {

            cmbCode.getItems().add(temp.getId());

        }
    }

    private void newLoadData() throws SQLException, ClassNotFoundException {
        PreparedStatement pst = DbConnection.getInstance().getConnection().prepareStatement("SELECT * FROM `course`");
        ResultSet rst  = pst.executeQuery();
        while (rst.next()){

            programArrayList.add(new Program(rst.getString(1),rst.getString(2),rst.getString(3),rst.getDouble(4)));
        }
    }

    private void setData(String id){
        btnAdd.setDisable(false);
        for(Program temp : programArrayList){
            if(id.equalsIgnoreCase(temp.getId())){
                txtName.setText(temp.getName());
                txtDuration.setText(temp.getDuration());
                txtFee.setText(String.valueOf(temp.getFee()));
                return;
            }
        }
    }

    private void calData(){
        double amount=0.0;
        lblNoOfProgram.setText(String.valueOf(tblAdd.getItems().size()));
        for(ProgramTM temp:tblAdd.getItems()){
            amount+=temp.getFee();
        }
        lblFullAmount.setText(String.valueOf(amount));
    }

    public void newFinishToAdd(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {

        for (ProgramTM temp : tblAdd.getItems()){
            PreparedStatement pst = DbConnection.getInstance().getConnection().prepareStatement("INSERT INTO `stu course register` VALUES(?,?)");
            pst.setObject(1,nic);
            pst.setObject(2,temp.getId());
            if(pst.executeUpdate()>0){

            }else{
                new Alert(Alert.AlertType.WARNING,"Try Again").show();
                return;
            }
        }
        mainRoot.setDisable(false);
        Stage stage = (Stage)subRoot.getScene().getWindow();
        stage.close();

    }
}
