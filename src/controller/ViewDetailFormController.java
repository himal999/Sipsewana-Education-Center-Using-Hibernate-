package controller;/*
author :Himal
version : 0.0.1
*/

import com.jfoenix.controls.JFXComboBox;
import db.DbConnection;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.TM.DetailTM;

import java.io.IOException;
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

    public Label lblClickHere;
    public ImageView imgIcon;
    public Label lblNoOfCourse;
    private  String selected;



    public void initialize() throws SQLException, ClassNotFoundException {
        lblClickHere.setDisable(true);
        lblNoOfCourse.setVisible(false);


        loadData();

        cmbNic.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            try {
                lblClickHere.setDisable(false);
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


    public void newProgramRegister(MouseEvent mouseEvent) throws IOException, SQLException, ClassNotFoundException {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(this.getClass().getClassLoader().getResource("view/newRegisterForm.fxml"));

        Parent root  = loader.load();
        Stage subStage = new Stage();
        subStage.setScene(new Scene(root));
        subStage.show();
        RegisterFormController rg  = loader.getController();
        rg.bool = false;
        rg.mainRoot = this.root;
        this.root.setDisable(true);
        rg.nic = selected;
        rg.initialize(selected);
        rg.initialize(0);
    }

    public void navigateOnHome(MouseEvent mouseEvent) throws IOException {
        Parent root  = FXMLLoader.load(getClass().getResource("../view/dashboardForm.fxml"));
        Scene scene = new Scene(root);
        Stage stage  = (Stage)this.root.getScene().getWindow();
        stage.setScene(scene);
        stage.show();

        TranslateTransition tt = new TranslateTransition(Duration.millis(350),scene.getRoot());
        tt.setFromY(scene.getWidth()-50);
        tt.setToY(0);
        tt.play();

    }

    public void mouseEnter(MouseEvent mouseEvent) {
        DropShadow shadow = new DropShadow();
        shadow.setColor(Color.BLUE);
        shadow.setHeight(30);
        shadow.setWidth(30);
        shadow.setRadius(50);
        imgIcon.setEffect(shadow);

    }

    public void mouseExit(MouseEvent mouseEvent) {
        imgIcon.setEffect(null);
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
    }

    public void loadTable(String id) throws SQLException, ClassNotFoundException {


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
        lblNoOfCourse.setVisible(true);
        lblNoOfCourse.setText(String.valueOf(tblDetail.getItems().size()));
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
