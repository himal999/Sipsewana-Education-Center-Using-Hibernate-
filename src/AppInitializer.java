import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class AppInitializer extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root =  FXMLLoader.load(this.getClass().getResource("view/dashboardForm.fxml"));
        Scene scene = new Scene(root);
        primaryStage.setTitle("Sipsewana Education Center");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
