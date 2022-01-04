package controller;
/*
author :Himal
version : 0.0.1
*/

import bo.ProgramBO;
import bo.impl.ProgramBOImpl;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import dao.impl.ProgrammingDAOImpl;
import db.FactoryConfig;
import entity.Program;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import model.TM.ProgramTM;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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


    ProgramBO programmingDAO =  new ProgramBOImpl();

    private ArrayList<entity.Program> programArrayList = new ArrayList<>();
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

    public void initialize(int id) throws SQLException, ClassNotFoundException, IOException {


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

    public void initialize(String nic) throws SQLException, ClassNotFoundException, IOException {

        Session session  = FactoryConfig.getInstance().getSession();
        Transaction transaction = session.beginTransaction();
        List<String> cCid = new ArrayList<>();
        String sql = "SELECT programs_id FROM student_program WHERE students_nic= ?";
        NativeQuery sqlQuery = session.createSQLQuery(sql);
        sqlQuery.setParameter(1,nic);
        cCid = sqlQuery.list();

        List<String> list = new ArrayList<>();
        String hql = "SELECT id FROM program";
        Query query = session.createQuery(hql);
        list = query.list();

        String tt= null;
    L1: for(String temp:list){

        for(String id:cCid){
            tt=id;
            if(id.equalsIgnoreCase(temp)){
                continue L1;
            }
        }
        cmbCode.getItems().add(tt);
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

    private void loadData() throws IOException {

        List<Program> programs =  programmingDAO.allProgram();
        for (Program temp:programs){
            programArrayList.add(new Program(temp.getId(),temp.getName(),temp.getDuration(),temp.getFee()));
        }

        for(Program temp:programArrayList){
            cmbCode.getItems().add(temp.getId());
        }
    }

    private void newLoadData() throws IOException {
        ProgrammingDAOImpl programmingDAO = new ProgrammingDAOImpl();
        List<entity.Program> programs = programmingDAO.allProgram();
        for(entity.Program temp:programs){
            programArrayList.add(temp);
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

    public void newFinishToAdd(ActionEvent actionEvent) throws SQLException, ClassNotFoundException, IOException {

        for(ProgramTM temp:tblAdd.getItems()){
            Session session = FactoryConfig.getInstance().getSession();
            Transaction transaction = session.beginTransaction();
            String sql = "INSERT INTO student_program VALUES(?,?)";
            Query query = session.createSQLQuery(sql);
            query.setParameter(1,temp.getId());
            query.setParameter(2,nic);
            if(query.executeUpdate()>0){
                transaction.commit();
                session.close();
            }else{
                new Alert(Alert.AlertType.WARNING,"Try Again ").show();
                session.close();
                return;

            }
        }
        mainRoot.setDisable(false);
        Stage stage = (Stage)subRoot.getScene().getWindow();
        stage.close();

    }
}
