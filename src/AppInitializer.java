import db.FactoryConfig;
import entity.Program;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class AppInitializer extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root =  FXMLLoader.load(this.getClass().getResource("view/dashboardForm.fxml"));
        Scene scene = new Scene(root);
        primaryStage.setTitle("Sipsewana Education Center");
        primaryStage.setScene(scene);
        primaryStage.setFullScreen(false);
        primaryStage.show();

        Session session = FactoryConfig.getInstance().getSession();
        Transaction transaction = session.beginTransaction();

   /*  Program p = new Program();
        p.setId("c002");
        p.setName("nimal");
        p.setDuration("sfsdf");
        p.setFee(454);
        session.save(p);*/

        transaction.commit();

        session.close();
    }
}
