package controller;/*
author :Himal
version : 0.0.1
*/

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import db.DbConnection;
import javafx.animation.TranslateTransition;

import javafx.event.ActionEvent;
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
import model.Program;
import model.TM.ProgramTM;


import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class ProgrammingFormController {
    public TableView<ProgramTM> tblProgram;
    public JFXTextField txtProgramId;
    public JFXTextField txtProgramName;
    public JFXTextField txtFee;
    public JFXComboBox cmbDuration;
    public JFXButton btnDynamic;
    public Label lblHeader;
    public JFXButton btnAddNewProgram;
    public AnchorPane root;


    public void initialize() throws SQLException, ClassNotFoundException {

        txtProgramId.setDisable(true);
        txtProgramName.setDisable(true);
        txtFee.setDisable(true);
        cmbDuration.setDisable(true);
        btnDynamic.setDisable(true);

        cmbDuration.getItems().addAll("3-Months","6-Months","1-year","2-year","Custom");


        tblProgram.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("id"));
        tblProgram.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("name"));
        tblProgram.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("duration"));
        tblProgram.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("fee"));
        tblProgram.getColumns().get(4).setCellValueFactory(new PropertyValueFactory<>("action"));

        loadDataFromDatabase();


    }

    public void dynamicOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {

            Program program = new Program(txtProgramId.getText(), txtProgramName.getText(), cmbDuration.getSelectionModel().getSelectedItem().toString(), Double.parseDouble(txtFee.getText()));
            PreparedStatement pst=null;

            if(btnDynamic.getText().equalsIgnoreCase("Add")){
                if(checkProgramId(txtProgramId.getText())){
                pst = DbConnection.getInstance().getConnection().prepareStatement("INSERT INTO `course` VALUES (?,?,?,?)");
                execute(pst,"Program Added Success",program.getId(),program.getName(),program.getDuration(),program.getFee());
                return;
                }
            }

            else if(btnDynamic.getText().equalsIgnoreCase("Update")){
                 pst = DbConnection.getInstance().getConnection().prepareStatement("UPDATE `course` SET c_id=?,c_name=?,duration=?,fee=? WHERE c_id=?");
                 execute(pst,"Program Update Success",program.getId(),program.getName(),program.getDuration(),program.getFee(),program.getId());
                 return;
            }
            else if(btnDynamic.getText().equalsIgnoreCase("Delete")){

                ButtonType yes =  new ButtonType("OK",ButtonBar.ButtonData.OK_DONE);
                ButtonType no  = new ButtonType("Cancel",ButtonBar.ButtonData.CANCEL_CLOSE);

                Alert alert = new Alert(Alert.AlertType.INFORMATION,"Are you Sure Delete Data ?",yes,no);

                Optional<ButtonType> result = alert.showAndWait();

                if(result.orElse(no)==yes){
                    pst = DbConnection.getInstance().getConnection().prepareStatement("DELETE FROM `course` WHERE c_id=?");
                    execute(pst,"Delete Success",program.getId());
                    return;
                }
                clearField();
                return;
            }

    }

    public void addNewProgramOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {

        lblHeader.setText("New Program");

        txtProgramId.setDisable(false);
        txtProgramId.setEditable(true);
        txtProgramId.clear();
        txtProgramName.setDisable(false);
        txtProgramName.clear();
        txtFee.setDisable(false);
        txtFee.clear();
        cmbDuration.setDisable(false);
        cmbDuration.getSelectionModel().clearSelection();

        btnDynamic.setText("Add");
        btnDynamic.setDisable(false);
    }

    private void clearField(){

        lblHeader.setText("Program Panel");
        txtProgramId.clear();
        txtProgramId.setDisable(true);
        txtProgramName.clear();
        txtProgramName.setDisable(true);
        txtFee.clear();
        txtFee.setDisable(true);
        cmbDuration.getSelectionModel().clearSelection();
        cmbDuration.setDisable(true);
        btnDynamic.setDisable(true);
    }

    private  void loadDataFromDatabase() throws SQLException, ClassNotFoundException {
        tblProgram.getItems().clear();
        PreparedStatement pst = DbConnection.getInstance().getConnection().prepareStatement("SELECT * FROM `course`");
        ResultSet rst = pst.executeQuery();
        while (rst.next()){
            Button update =  new Button("Update");
            Button delete = new Button("Delete");
            HBox hBox = new HBox(update,delete);
            ProgramTM programTM = new ProgramTM(rst.getString(1),rst.getString(2),rst.getString(3),rst.getDouble(4),hBox);
            tblProgram.getItems().add(programTM);

            update.setOnAction(event -> {
                setData(programTM,"Update","Update Program");
            });

            delete.setOnAction(event -> {
               setData(programTM,"Delete","Delete Program");
            });
        }

        return;
    }

    private void setData(ProgramTM programTM,String text,String header){
        txtProgramId.setDisable(false);
        txtProgramId.setEditable(false);
        txtProgramId.setText(programTM.getId());

        txtProgramName.setDisable(false);
        txtProgramName.setText(programTM.getName());

        txtFee.setDisable(false);
        txtFee.setText(String.valueOf(programTM.getFee()));

        cmbDuration.setDisable(false);
        cmbDuration.valueProperty().setValue(programTM.getDuration());

        btnDynamic.setDisable(false);
        btnDynamic.setText(text);

        lblHeader.setText(header);

    }

    private boolean checkProgramId(String id) throws SQLException, ClassNotFoundException {
        PreparedStatement pst = DbConnection.getInstance().getConnection().prepareStatement("SELECT c_id FROM `course`");
        ResultSet rst = pst.executeQuery();

        while (rst.next()){
            if(rst.getString(1).equalsIgnoreCase(id)){
                txtProgramId.requestFocus();
                new Alert(Alert.AlertType.ERROR,"Sorry,This Program Id Already Exists.").show();
                return false;
            }
        }
        return true;
    }
    private void execute(PreparedStatement pst,String msg,Object... args) throws SQLException, ClassNotFoundException {

       for (int i=0;i<args.length;i++){
           pst.setObject(i+1,args[i]);
       }
         if (pst.executeUpdate() > 0) {
            new Alert(Alert.AlertType.CONFIRMATION, msg).show();
            clearField();
            loadDataFromDatabase();
            return ;
        } else {
            new Alert(Alert.AlertType.ERROR, "Try Again").show();
            return ;
        }
    }

    public void navigateOnHome(MouseEvent mouseEvent) throws IOException {

        Parent root = FXMLLoader.load(this.getClass().getResource("../view/dashboardForm.fxml"));
        Scene mainScene = new Scene(root);
        Stage mainStage =  (Stage)this.root.getScene().getWindow();
        mainStage.setScene(mainScene);

        TranslateTransition tt =  new TranslateTransition(Duration.millis(300),mainScene.getRoot());
        tt.setFromY(mainScene.getWidth()-50);
        tt.setToY(0);
        tt.play();

    }

    public void mouseEnter(MouseEvent mouseEvent) {

            ImageView icon = (ImageView) mouseEvent.getSource();
            DropShadow glow = new DropShadow();
            glow.setColor(Color.BLUE);
            glow.setWidth(50);
            glow.setHeight(50);
            glow.setRadius(30);
            icon.setEffect(glow);

     }

    public void mouseExit(MouseEvent mouseEvent) {
        ImageView icon = (ImageView) mouseEvent.getSource();
        icon.setEffect(null);
    }
}
