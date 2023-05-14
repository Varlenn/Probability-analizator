package example.plot;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class DiffieHellmanController implements Initializable {

    @FXML
    private Label changeALabel;

    @FXML
    private Label changeBLabel;

    @FXML
    private Button genButton;

    @FXML
    private Label openALabel;

    @FXML
    private Label openBLabel;

    @FXML
    private TextField pField;

    @FXML
    private TextField secretAField;

    @FXML
    private Label secretBLabel;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        genButton.setOnAction(actionEvent -> {
            DiffieHellman diffieHellman = new DiffieHellman(Integer.parseInt(pField.getText()), Integer.parseInt(secretAField.getText()));
            diffieHellman.dhAlgorithm();
            diffieHellman.dh();

            secretBLabel.setText("Секретный ключ участника В: " + diffieHellman.getY());
            openALabel.setText("Открытый ключ участника А: " + diffieHellman.getKx());
            openBLabel.setText("Открытый ключ участника B: " + diffieHellman.getKy());
            changeALabel.setText("Обменный ключ участника А: " + diffieHellman.getA());
            changeBLabel.setText("Обменный ключ участника B: " + diffieHellman.getB());
        });
    }

    public static void newWindow() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(StartApplication.class.getResource("/DiffieHellman.fxml"));
        Stage window  = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);

        Scene scene = new Scene(fxmlLoader.load(), 393, 523);
        window.setScene(scene);
        window.setTitle("Диффи-Хэллман");
        window.showAndWait();
    }
}

