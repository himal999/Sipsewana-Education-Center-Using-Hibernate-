package controller;/*
author :Himal
version : 0.0.1
*/

import bo.StudentBO;
import bo.impl.StudentBOImpl;
import com.jfoenix.controls.*;

import entity.Student;
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
import model.TM.ProgramTM;
import model.TM.StudentTM;


import java.io.IOException;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
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

    public static  ArrayList<ProgramTM> programTMS;
    public  static  double amount;

    StudentBO studentBO= new StudentBOImpl();


    public void initialize() throws SQLException, ClassNotFoundException, IOException {
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
        mainStage.centerOnScreen();
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
        txtNIc.clear();
        txtNIc.setEditable(true);
        txtName.setDisable(false);
        txtName.clear();
        txtEmail.setDisable(false);
        txtEmail.clear();
        txtCity.setDisable(false);
        txtCity.clear();
        dpDob.setDisable(false);


        txtTel.setDisable(false);
        txtTel.clear();
        btnDynamic.setDisable(false);
        btnDynamic.setText("Add");

        chEnable.setDisable(false);
    }

    public void dynamicOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException, IOException {

        Student student =  new Student(txtNIc.getText(),txtName.getText(),txtCity.getText(), Date.valueOf(dpDob.getValue()),txtTel.getText(),txtEmail.getText(),"Pending");

        if(btnDynamic.getText().equalsIgnoreCase("Add")){
            if(studentBO.checkNewStudent(student.getNic())){
                if(studentBO.saveStudent(student)){
                    new Alert(Alert.AlertType.CONFIRMATION,"Student Added Success").show();
                    clearField();
                    loadDataFromDB();
                    return;
                }else{
                    new Alert(Alert.AlertType.WARNING,"Try Again").show();
                    return;
                }
            }
            return;
        }
        else if(btnDynamic.getText().equalsIgnoreCase("Update")){

            if(studentBO.updateStudent(student)){
                new Alert(Alert.AlertType.CONFIRMATION,"Student Update Success").show();
                clearField();
                loadDataFromDB();
                return;
            }else{
                new Alert(Alert.AlertType.WARNING,"Try Again").show();
                return;
            }

        }
        else if(btnDynamic.getText().equalsIgnoreCase("Delete")){
            ButtonType yes = new ButtonType("Ok",ButtonBar.ButtonData.OK_DONE);
            ButtonType no = new ButtonType("Cancel",ButtonBar.ButtonData.CANCEL_CLOSE);

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"Are you sure Delete Date ?",yes,no);

            Optional<ButtonType> result = alert.showAndWait();

            if(result.orElse(no)==yes){
                if(studentBO.deleteStudent(student)){
                  new Alert(Alert.AlertType.CONFIRMATION,"Student Delete Success").show();
                  loadDataFromDB();
                  clearField();
                  return;
              }else{
                  new Alert(Alert.AlertType.WARNING,"Try Again").show();
                  return;
              }
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

       chEnable.setSelected(false);
       btnStuCourseReg.setDisable(true);
       chEnable.setDisable(true);
    }

    private void loadDataFromDB() throws SQLException, ClassNotFoundException, IOException {

        tblStudent.getItems().clear();
        List<entity.Student> studentList =  studentBO.allStudent();
        for(entity.Student  temp : studentList ){
            Button update = new Button("Update");
            Button delete = new Button("Delete");
            HBox action = new HBox(update,delete);
            StudentTM student = new StudentTM(temp.getNic(),temp.getName(),temp.getAddress(),temp.getDob(),temp.getTel(),temp.getEmail(),temp.getStatus(),action);
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




        txtNIc.setText(student.getNic());
        txtName.setText(student.getName());
        txtCity.setText(student.getAddress());


        Calendar cal = Calendar.getInstance();
        cal.setTime(student.getDob());
        LocalDate date = LocalDate.of(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH));
        dpDob.setValue(date);

        txtTel.setText(student.getTel());
        txtEmail.setText(student.getEmail());
        chEnable.setSelected(false);


        if(student.getStatus().equalsIgnoreCase("Pending")){
            chEnable.setDisable(false);
            btnDynamic.setDisable(true);
            btnStuCourseReg.setDisable(false);

            txtNIc.setEditable(false);
            txtName.setEditable(false);
            txtCity.setEditable(false);
            txtTel.setEditable(false);
            txtEmail.setEditable(false);
            dpDob.setEditable(false);
            btnStuCourseReg.setDisable(true);
        }else{
            chEnable.setDisable(true);
            btnStuCourseReg.setDisable(true);
            btnDynamic.setDisable(false);
            btnDynamic.setText(button);

            txtNIc.setEditable(false);
            txtName.setEditable(true);
            txtCity.setEditable(true);
            txtTel.setEditable(true);
            txtEmail.setEditable(true);
            dpDob.setEditable(true);
        }

    }

    public void stuCourseRegOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException, IOException {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd ");
        Calendar cal = Calendar.getInstance();
        String date = dateFormat.format(cal.getTime());
        entity.Student student = new entity.Student(txtNIc.getText(), txtName.getText(), txtCity.getText(), Date.valueOf(dpDob.getValue()), txtTel.getText(), txtEmail.getText(), "Success");
        StuRegister register = new StuRegister(student.getNic(), programTMS, date, amount);

        if(tblStudent.getSelectionModel().isEmpty()){
            if (studentBO.checkNewStudent(student.getNic())) {
                if (studentBO.insertStudentData(student)) {
                    if(studentBO.insertStuRegisterData(register)){
                    new Alert(Alert.AlertType.CONFIRMATION,"Course & Student Register Success").show();
                    clearField();
                    loadDataFromDB();
                    return;
                    }else{
                    new Alert(Alert.AlertType.WARNING,"Try Again").show();
                    return;
                }
            }
        }
        }else if(tblStudent.getSelectionModel().getSelectedItem().getStatus().equalsIgnoreCase("Pending")){

            if(studentBO.updateStatus(student)){
                if(studentBO.insertStuRegisterData(register)){
                    new Alert(Alert.AlertType.CONFIRMATION,"Course Register Success").show();
                    clearField();
                    tblStudent.getSelectionModel().clearSelection();
                    loadDataFromDB();
                    return;
                }else{
                    tblStudent.getSelectionModel().clearSelection();
                    new Alert(Alert.AlertType.WARNING,"Try Again").show();
                    return;
                }
            }else {
                tblStudent.getSelectionModel().clearSelection();
                new Alert(Alert.AlertType.WARNING,"Try Again").show();
                clearField();
                return;
            }

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
