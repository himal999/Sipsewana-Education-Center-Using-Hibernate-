package controller;/*
author :Himal
version : 0.0.1
*/

import com.jfoenix.controls.*;

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

import model.StuRegister;
import model.Student;
import model.TM.ProgramTM;
import model.TM.StudentTM;


import java.io.IOException;
import java.sql.*;

import java.text.DateFormat;

import java.text.SimpleDateFormat;
import java.time.LocalDate;


import java.util.ArrayList;
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




    public JFXCheckBox chEnable;
    public JFXButton btnStuCourseReg;


    private Connection con;

    public static  ArrayList<ProgramTM> programTMS;
    public  static  double amount;


    public void initialize() throws SQLException, ClassNotFoundException {
        txtNIc.setDisable(true);
        txtName.setDisable(true);
        txtCity.setDisable(true);
        dpDob.setDisable(true);

        txtTel.setDisable(true);
        btnDynamic.setDisable(true);
        txtEmail.setDisable(true);

        chEnable.setDisable(true);

        btnStuCourseReg.setDisable(true);




        tblStudent.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("nic"));
        tblStudent.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("name"));
        tblStudent.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("address"));
        tblStudent.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("dob"));
        tblStudent.getColumns().get(4).setCellValueFactory(new PropertyValueFactory<>("tel"));
        tblStudent.getColumns().get(5).setCellValueFactory(new PropertyValueFactory<>("email"));
        tblStudent.getColumns().get(6).setCellValueFactory(new PropertyValueFactory<>("status"));
        tblStudent.getColumns().get(7).setCellValueFactory(new PropertyValueFactory<>("action"));

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
        txtNIc.setEditable(true);
        txtName.setDisable(false);
        txtEmail.setDisable(false);
        txtCity.setDisable(false);
        dpDob.setDisable(false);

        txtTel.setDisable(false);
        btnDynamic.setDisable(false);
        btnDynamic.setText("Add");

        chEnable.setDisable(false);
    }

    public void dynamicOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {

        Student student =  new Student(txtNIc.getText(),txtName.getText(),txtCity.getText(), Date.valueOf(dpDob.getValue()),txtTel.getText(),txtEmail.getText(),"Pending");

        if(btnDynamic.getText().equalsIgnoreCase("Add")){

            con = DbConnection.getInstance().getConnection();
            PreparedStatement pst = con.prepareStatement("INSERT INTO `student` VALUES (?,?,?,?,?,?,?)");
            execute(pst,"Student Added Success",student.getNic(),student.getName(),student.getAddress(),student.getDob(),student.getTel(),student.getEmail(),student.getStatus());
        }
        else if(btnDynamic.getText().equalsIgnoreCase("Update")){
            con = DbConnection.getInstance().getConnection();
            PreparedStatement pst = con.prepareStatement("UPDATE `student` SET nic=?,`name`=?,address=?,dob=?,tel=?,email=? WHERE nic=?");
            execute(pst,"Student Update Success",student.getNic(),student.getName(),student.getAddress(),student.getDob(),student.getTel(),student.getEmail(),student.getNic());
        }
        else if(btnDynamic.getText().equalsIgnoreCase("Delete")){
            ButtonType yes = new ButtonType("Ok",ButtonBar.ButtonData.OK_DONE);
            ButtonType no = new ButtonType("Cancel",ButtonBar.ButtonData.CANCEL_CLOSE);

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"Are you sure Delete Date ?",yes,no);

            Optional<ButtonType> result = alert.showAndWait();

            if(result.orElse(no)==yes){
              con = DbConnection.getInstance().getConnection();
              PreparedStatement pst = con.prepareStatement("DELETE FROM `student` WHERE nic=?");
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
        con = DbConnection.getInstance().getConnection();
        PreparedStatement pst = con.prepareStatement("SELECT * FROM `student`");
        ResultSet rst = pst.executeQuery();
        while (rst.next()) {
            Button update = new Button("Update");
            Button delete = new Button("Delete");
            HBox action = new HBox(update,delete);
            StudentTM student = new StudentTM(rst.getString(1), rst.getString(2), rst.getString(3),rst.getDate(4), rst.getString(5), rst.getString(6),rst.getString(7),action);
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

        chEnable.setDisable(true);


    }




    private void execute(PreparedStatement pst,String msg,Object... args) throws SQLException, ClassNotFoundException {


        for(int i=0;i<args.length;i++){
            pst.setObject(i+1,args[i]);
        }

       if(pst.executeUpdate()>0){

           new Alert(Alert.AlertType.CONFIRMATION,msg).show();
           clearField();
           loadDataFromDB();
           return ;
        }else{
           new Alert(Alert.AlertType.WARNING,"Try Again").show();
           clearField();
           return;
        }

    }










    public void stuCourseRegOnAction(ActionEvent actionEvent) throws SQLException {

        Student student = new Student(txtNIc.getText(),txtName.getText(),txtCity.getText(),Date.valueOf(dpDob.getValue()),txtTel.getText(),txtEmail.getText(),"Success");

        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd ");
        Calendar cal = Calendar.getInstance();
        String date  = dateFormat.format(cal.getTime());

        StuRegister register = new StuRegister(student.getNic(),programTMS,date,amount);
        try {
            con.setAutoCommit(false);
            PreparedStatement pst = null;
            pst = con.prepareStatement("INSERT INTO `student` VALUES (?,?,?,?,?,?,?)");
            pst.setObject(1,student.getNic());
            pst.setObject(2,student.getName());
            pst.setObject(3,student.getAddress());
            pst.setObject(4,student.getDob());
            pst.setObject(5,student.getTel());
            pst.setObject(6,student.getEmail());
            pst.setObject(7,student.getStatus());

            if(pst.executeUpdate()>0){

                boolean bool=false;
                for(ProgramTM temp:register.getPrograms()){
                    pst = con.prepareStatement("INSERT INTO `stu course register` VALUES (?,?)");
                    pst.setObject(1,register.getNic());
                    pst.setObject(2,temp.getId());
                    if(pst.executeUpdate()>0){
                        bool=true;
                    }else{
                        bool=false;
                        con.rollback();
                        con.setAutoCommit(true);
                    }

                }
                if(bool) {
                    pst = con.prepareStatement("INSERT INTO `stu course register detail` VALUES (?,?,?)");
                    pst.setObject(1,register.getNic());
                    pst.setObject(2,date);
                    pst.setObject(3,amount);

                    if(pst.executeLargeUpdate()>0){
                        con.commit();
                        con.setAutoCommit(true);
                    }else{
                        con.rollback();
                        con.setAutoCommit(true);
                    }
                }


            }else{
                con.rollback();
                con.setAutoCommit(true);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        finally {
            con.setAutoCommit(true);
        }
    }

    public void enableRegisterField(MouseEvent mouseEvent) throws IOException, SQLException, ClassNotFoundException {
           if(chEnable.isSelected()){
               btnStuCourseReg.setDisable(true);
               btnDynamic.setDisable(true);

               FXMLLoader loader = new FXMLLoader();
               loader.setLocation(this.getClass().getClassLoader().getResource("view/registerForm.fxml"));
               Parent root = loader.load();
               Stage stage = new Stage();
               stage.setScene(new Scene(root));
               stage.show();

               RegisterFormController registerFormController = loader.getController();
               registerFormController.bool=true;
               registerFormController.mainRoot = this.root;
               registerFormController.submit = this.btnStuCourseReg;
               registerFormController.initialize(0);
               this.root.setDisable(true);

           }else{
               btnStuCourseReg.setDisable(true);
               btnDynamic.setDisable(false);
           }
    }
}
