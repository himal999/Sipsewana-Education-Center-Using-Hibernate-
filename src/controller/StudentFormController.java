package controller;/*
author :Himal
version : 0.0.1
*/

import com.jfoenix.controls.JFXButton;

import com.jfoenix.controls.JFXDatePicker;
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
import model.Student;
import model.TM.StudentTM;

import java.io.IOException;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.time.LocalDate;

import java.util.Calendar;
import java.util.Optional;


public class StudentFormController {
    public AnchorPane root;
    public TableView<StudentTM> tblStudent;
    public JFXTextField txtNIc;
    public JFXTextField txtName;
    public JFXTextField txtTel;
    public JFXButton btnDynamic;
    public Label lblHeader;
    public JFXButton btnAddNewStudent;
    public JFXTextField txtCity;
    public JFXDatePicker dpDob;

    public JFXTextField txtEmail;

    public void initialize() throws SQLException, ClassNotFoundException {
        txtNIc.setDisable(true);
        txtName.setDisable(true);
        txtCity.setDisable(true);
        dpDob.setDisable(true);

        txtTel.setDisable(true);
        btnDynamic.setDisable(true);
        txtEmail.setDisable(true);



        tblStudent.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("nic"));
        tblStudent.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("name"));
        tblStudent.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("address"));
        tblStudent.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("dob"));
        tblStudent.getColumns().get(4).setCellValueFactory(new PropertyValueFactory<>("tel"));
        tblStudent.getColumns().get(5).setCellValueFactory(new PropertyValueFactory<>("email"));
        tblStudent.getColumns().get(6).setCellValueFactory(new PropertyValueFactory<>("action"));

        loadDataFromDB();

    }

    public void navigateOnHome(MouseEvent mouseEvent) throws IOException {
        Parent root = FXMLLoader.load(this.getClass().getResource("../view/dashboardForm.fxml"));
        Scene mainScene = new Scene(root);
        Stage mainStage = (Stage)this.root.getScene().getWindow();
        mainStage.setScene(mainScene);

        TranslateTransition tt = new TranslateTransition(Duration.millis(300),mainScene.getRoot());
        tt.setFromY(mainScene.getWidth()-50);
        tt.setToY(0);
        tt.play();
    }

    public void mouseEnter(MouseEvent mouseEvent) {
        ImageView icon = (ImageView) mouseEvent.getSource();

        DropShadow shadow =  new DropShadow();
        shadow.setWidth(30);
        shadow.setHeight(30);
        shadow.setRadius(50);
        shadow.setColor(Color.BLUE);
        icon.setEffect(shadow);
    }

    public void mouseExit(MouseEvent mouseEvent) {
        ImageView icon  = (ImageView) mouseEvent.getSource();
        icon.setEffect(null);
    }

    public void addNewStudentOnAction(ActionEvent actionEvent) {

        lblHeader.setText("Add New Student");

        txtNIc.setDisable(false);
        txtName.setDisable(false);
        txtEmail.setDisable(false);
        txtCity.setDisable(false);
        dpDob.setDisable(false);

        txtTel.setDisable(false);
        btnDynamic.setDisable(false);
        btnDynamic.setText("Add");
    }

    public void dynamicOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {

        Student student =  new Student(txtNIc.getText(),txtName.getText(),txtCity.getText(), Date.valueOf(dpDob.getValue()),txtTel.getText(),txtEmail.getText());

        if(btnDynamic.getText().equalsIgnoreCase("Add")){
            PreparedStatement pst = DbConnection.getInstance().getConnection().prepareStatement("INSERT INTO `Student` VALUES (?,?,?,?,?,?)");
            execute(pst,"Student Added Success",student.getNic(),student.getName(),student.getAddress(),student.getDob(),student.getTel(),student.getEmail());
        }
        else if(btnDynamic.getText().equalsIgnoreCase("Update")){
            PreparedStatement pst = DbConnection.getInstance().getConnection().prepareStatement("UPDATE `Student` SET nic=?,`name`=?,address=?,dob=?,tel=?,email=? WHERE nic=?");
            execute(pst,"Student Update Success",student.getNic(),student.getName(),student.getAddress(),student.getDob(),student.getTel(),student.getEmail(),student.getNic());
        }
        else if(btnDynamic.getText().equalsIgnoreCase("Delete")){
            ButtonType yes = new ButtonType("Ok",ButtonBar.ButtonData.OK_DONE);
            ButtonType no = new ButtonType("Cancel",ButtonBar.ButtonData.CANCEL_CLOSE);

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"Are you sure Delete Date ?",yes,no);

            Optional<ButtonType> result = alert.showAndWait();

            if(result.orElse(no)==yes){
              PreparedStatement pst = DbConnection.getInstance().getConnection().prepareStatement("DELETE FROM `Student` WHERE nic=?");
              execute(pst,"Student Delete Success",student.getNic());
              return;
            }
            clearField();
        }
    }

    private void clearField(){

        lblHeader.setText("Student Panel");

        txtNIc.clear();
        txtNIc.setDisable(true);
        txtName.clear();
        txtName.setDisable(true);
        txtCity.clear();
        txtCity.setDisable(true);
        dpDob.setDisable(true);
        dpDob.setValue(null);
        txtTel.clear();
        txtTel.setDisable(true);
        txtEmail.clear();
        txtEmail.setDisable(true);
        btnDynamic.setDisable(true);
    }

    private void loadDataFromDB() throws SQLException, ClassNotFoundException {
        tblStudent.getItems().clear();
        PreparedStatement pst = DbConnection.getInstance().getConnection().prepareStatement("SELECT * FROM `Student`");
        ResultSet rst = pst.executeQuery();
        while (rst.next()) {
            Button update = new Button("Update");
            Button delete = new Button("Delete");
            HBox action = new HBox(update,delete);
            StudentTM student = new StudentTM(rst.getString(1), rst.getString(2), rst.getString(3),rst.getDate(4), rst.getString(5), rst.getString(6),action);
            tblStudent.getItems().add(student);

            update.setOnAction(event -> {


                loadTextFieldData(student,"Update Student","Update");
            });

            delete.setOnAction(event -> {
                loadTextFieldData(student,"Delete Student","Delete");
                return;
            });
        }
    }

    private void loadTextFieldData(StudentTM student,String header,String button){
        txtNIc.setDisable(false);
        txtNIc.setEditable(false);
        txtName.setDisable(false);
        txtCity.setDisable(false);
        dpDob.setDisable(false);
        txtTel.setDisable(false);
        txtEmail.setDisable(false);

        lblHeader.setText(header);

        btnDynamic.setDisable(false);
        btnDynamic.setText(button);

        txtNIc.setText(student.getNic());
        txtName.setText(student.getName());
        txtCity.setText(student.getAddress());


        Calendar cal = Calendar.getInstance();
        cal.setTime(student.getDob());
        LocalDate date = LocalDate.of(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH));
        dpDob.setValue(date);

        txtTel.setText(student.getTel());
        txtEmail.setText(student.getEmail());


    }


    private void execute(PreparedStatement pst,String msg,Object... args) throws SQLException, ClassNotFoundException {

        for(int i=0;i<args.length;i++){
            pst.setObject(i+1,args[i]);
        }

        if(pst.executeUpdate()>0){
            new Alert(Alert.AlertType.CONFIRMATION,msg).show();
            loadDataFromDB();
            clearField();
            return;
        }else{
            new Alert(Alert.AlertType.WARNING,"Try Again").show();
            return;
        }
    }


}
