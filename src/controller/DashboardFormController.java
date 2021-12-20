package controller;
/*
author :Himal
version : 0.0.1
*/

import javafx.animation.TranslateTransition;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;


public class DashboardFormController {

    public ImageView imgStudent;
    public ImageView imgDetails;
    public ImageView imgProgram;
    public Label lblHeader;
    public Label lblDescription;
    public AnchorPane root;


    public void navigate(MouseEvent mouseEvent) throws IOException {
        if(mouseEvent.getSource() instanceof ImageView){
            ImageView icon =  (ImageView) mouseEvent.getSource();

            Parent root = null;

            switch (icon.getId()){
                case "imgStudent":
                    root = FXMLLoader.load(this.getClass().getResource("../view/studentForm.fxml"));
                    break;
                case "imgProgram" :
                    root = FXMLLoader.load(this.getClass().getResource("../view/programmingForm.fxml"));
                    break;
                case "imgDetails" :
                    root = FXMLLoader.load(this.getClass().getResource("../view/viewDetailForm.fxml"));

            }

            if(root != null){
                Scene subScene =  new Scene(root);
                Stage subStage = (Stage) this.root.getScene().getWindow();
                subStage.setScene(subScene);
                subStage.centerOnScreen();

                TranslateTransition  tt =  new TranslateTransition(Duration.millis(500),subScene.getRoot());
                tt.setFromX(-subScene.getWidth());
                tt.setToX(0);
                tt.play();
            }
        }
    }

    public void playMouseEnter(MouseEvent mouseEvent) {
        if (mouseEvent.getSource() instanceof ImageView) {
            ImageView icon = (ImageView) mouseEvent.getSource();
            DropShadow glow = new DropShadow();

            switch (icon.getId()) {
                case "imgStudent":
                    lblHeader.setText("Student");
                    lblDescription.setText("create,view,update,delete");
                    glow.setColor(Color.RED);
                    break;
                case "imgProgram":
                    lblHeader.setText("Program");
                    lblDescription.setText("create,view,update,delete as program");
                    glow.setColor(Color.BLUE);
                    break;
                case "imgDetails":
                    lblHeader.setText("View Detail");
                    lblDescription.setText("create,view,update,delete as Details");
                    glow.setColor(Color.FIREBRICK);
            }


            glow.setWidth(50);
            glow.setHeight(50);
            glow.setRadius(30);
            icon.setEffect(glow);


        }
    }

    public void playMouseExit(MouseEvent mouseEvent) {

        ImageView icon = (ImageView) mouseEvent.getSource();

        icon.setEffect(null);
        lblHeader.setText("Welcome");
        lblDescription.setText("Please select one of above main operations to proceed");

    }
}
