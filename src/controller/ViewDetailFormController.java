package controller;/*
author :Himal
version : 0.0.1
*/

import com.jfoenix.controls.JFXComboBox;
import db.DbConnection;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

import model.TM.DetailTM;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;

public class ViewDetailFormController {
    public AnchorPane root;
    public TableView<DetailTM> tblDetail;
    public JFXComboBox cmbNic;
    public Label lblHeader;
    public JFXComboBox cmbName;
    public Label lblClickHere;
    private  String selected;



    public void initialize() throws SQLException, ClassNotFoundException {
        lblClickHere.setDisable(true);



        loadData();

        cmbNic.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            try {
                selected = newValue.toString();
                loadTable(selected);

            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        });

        tblDetail.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("id"));
        tblDetail.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("name"));
        tblDetail.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("duration"));
        tblDetail.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("action"));


    }


    public void newProgramRegister(MouseEvent mouseEvent) {
    }

    public void navigateOnHome(MouseEvent mouseEvent) {
    }

    public void mouseEnter(MouseEvent mouseEvent) {
    }

    public void mouseExit(MouseEvent mouseEvent) {
    }

    private void loadData() throws SQLException, ClassNotFoundException {
       ArrayList<String> nic = new ArrayList<>();
        PreparedStatement pst = DbConnection.getInstance().getConnection().prepareStatement("SELECT nic FROM `stu course register`");
        ResultSet rst = pst.executeQuery();
       L1:while (rst.next()){
            for (String te:nic){
                if(te.equalsIgnoreCase(rst.getString(1))){
                    continue  L1;
                }
            }
            nic.add(rst.getString(1));
            cmbNic.getItems().add(rst.getString(1));
        }

        ArrayList<String> name = new ArrayList<>();
        for(String temp:nic){
            pst = DbConnection.getInstance().getConnection().prepareStatement("SELECT `name` FROM `student` WHERE nic=?");
            pst.setObject(1,temp);
            rst = pst.executeQuery();

            rst.next();
            name.add(rst.getString("name"));

        }

        for(String temp:name){
            cmbName.getItems().add(temp);
        }

    }

    private void loadTable(String id) throws SQLException, ClassNotFoundException {


        ArrayList<String> cId = new ArrayList<>();
        tblDetail.getItems().clear();


        PreparedStatement pst = DbConnection.getInstance().getConnection().prepareStatement("SELECT * FROM `stu course register` WHERE nic=?");
        pst.setObject(1,id);

        ResultSet rst = pst.executeQuery();

        while (rst.next()){

            cId.add(rst.getString("c_id"));

        }


        for(String temp : cId){

            pst =DbConnection.getInstance().getConnection().prepareStatement("SELECT c_id,c_name,duration FROM `course`  WHERE c_id=? ");
            pst.setObject(1,temp);

            rst = pst.executeQuery();

            Button drop = new Button("Drop");
            HBox action = new HBox(drop);

            rst.next();
            tblDetail.getItems().add(new DetailTM(rst.getString("c_id"),rst.getString("c_name"),rst.getString("duration"),action));

            drop.setOnAction(event -> {

                try {

                    dropCourse(tblDetail.getSelectionModel().getSelectedItem().getId());

                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            });

        }
    }
    private void dropCourse(String select) throws SQLException, ClassNotFoundException {
        ButtonType yes = new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE);
        ButtonType no = new ButtonType("Cancel",ButtonBar.ButtonData.CANCEL_CLOSE);

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"Are you Sure Drop Data",yes,no);
        Optional<ButtonType> result  = alert.showAndWait();

        if(result.orElse(no)==yes){

              PreparedStatement pst = DbConnection.getInstance().getConnection().prepareStatement("DELETE FROM `stu course register` WHERE c_id = ?");
              pst.setObject(1,select);

              if(pst.executeUpdate()>0){
                  new Alert(Alert.AlertType.CONFIRMATION,"Delete Success").show();
                 loadTable(selected);
                  return;
              }else{
                  new Alert(Alert.AlertType.WARNING,"Try Again").show();
                  return;
              }
        }
    }


}
