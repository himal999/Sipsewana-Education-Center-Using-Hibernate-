package controller;/*
author :Himal
version : 0.0.1
*/

import com.jfoenix.controls.JFXComboBox;
import dao.impl.ProgrammingDAOImpl;

import dao.impl.ViewDetailDAOImpl;
import db.FactoryConfig;
import entity.Program;
import javafx.animation.TranslateTransition;
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
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.NativeQuery;


import java.io.IOException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
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



    public void initialize() throws IOException {
        lblClickHere.setDisable(true);
        lblNoOfCourse.setVisible(false);


        loadData();

        cmbNic.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            try {
                lblClickHere.setDisable(false);
                selected = newValue.toString();
                loadTable(selected);

            } catch (IOException e) {
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
        stage.centerOnScreen();
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

    private void loadData() throws  IOException {
      ArrayList<String> nic = new ArrayList<>();

      String sql = "SELECT students_nic FROM `student_program`";
      Session session = FactoryConfig.getInstance().getSession();
      Transaction transaction = session.beginTransaction();
      NativeQuery sqlQuery = session.createSQLQuery(sql);
      List<String> list = sqlQuery.list();
      L1: for(String temp:list){
          for(String te:nic){
              if(te.equalsIgnoreCase(temp)){
                  continue L1;
              }
          }
          nic.add(temp);
          cmbNic.getItems().add(temp);
      }

        ArrayList<String> name = new ArrayList<>();
        for(String temp:nic){
            sql = "SELECT `name` FROM `student` WHERE nic=?";

            List<String> nList = session.createSQLQuery(sql).setParameter(1,temp).list();

            for(String te:nList){
                name.add(te);
            }
        }
        transaction.commit();
        session.close();
    }

    public void loadTable(String id) throws IOException {

        ArrayList<String> cId = new ArrayList<>();
        tblDetail.getItems().clear();

         ViewDetailDAOImpl viewDetailDAO = new ViewDetailDAOImpl();
         ArrayList<String> registerList;
          viewDetailDAO.getViewData(id);

        /* for(String temp:registerList){
             System.out.println("1");
             cId.add(temp);
         }*/
        ProgrammingDAOImpl programmingDAO = new ProgrammingDAOImpl();
         for (String te:cId){
            Program program = programmingDAO.getProgram(te);
             Button drop = new Button("Drop");
             HBox action = new HBox(drop);
             tblDetail.getItems().add(new DetailTM(program.getId(),program.getName(),program.getDuration(),action));
             drop.setOnAction(event -> {
                 try {
                     dropCourse(tblDetail.getSelectionModel().getSelectedItem().getId());
                     lblNoOfCourse.setVisible(true);
                     lblNoOfCourse.setText(String.valueOf(tblDetail.getItems().size()));
                 } catch (SQLException throwables) {
                     throwables.printStackTrace();
                 } catch (ClassNotFoundException e) {
                     e.printStackTrace();
                 } catch (IOException e) {
                     e.printStackTrace();
                 }
             });
         }
    }

    private void dropCourse(String select) throws SQLException, ClassNotFoundException, IOException {
        ButtonType yes = new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE);
        ButtonType no = new ButtonType("Cancel",ButtonBar.ButtonData.CANCEL_CLOSE);

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"Are you Sure Drop Data",yes,no);
        Optional<ButtonType> result  = alert.showAndWait();

        if(result.orElse(no)==yes){

            ViewDetailDAOImpl viewDetailDAO = new ViewDetailDAOImpl();

            if(viewDetailDAO.deleteRegisterDetail(select)){
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
