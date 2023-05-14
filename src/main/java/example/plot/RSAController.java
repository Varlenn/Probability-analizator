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

public class RSAController implements Initializable {

    @FXML
    private TextField cField;

    @FXML
    private Label dLabel;

    @FXML
    private Label deLabel;

    @FXML
    private Label deSignLabel;

    @FXML
    private TextField eField;

    @FXML
    private Label enLabel;

    @FXML
    private Label funLabel;

    @FXML
    private Button genButton;

    @FXML
    private TextField nField;

    @FXML
    private Label pLabel;

    @FXML
    private Label qLabel;

    @FXML
    private Label signLabel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        genButton.setOnAction(actionEvent -> {
            RSA rsa = new RSA(Integer.parseInt(nField.getText()), Integer.parseInt(cField.getText()), Integer.parseInt(eField.getText()));
            rsa.rsaAlgorithm();

            pLabel.setText("p: " + rsa.getP());
            qLabel.setText("q: " + rsa.getQ());
            funLabel.setText("Значение функции Эйлера: " + rsa.getEl());
            dLabel.setText("d: " + rsa.getD());
            deLabel.setText("Расшифрованное сообщение: " + rsa.getDe());
            enLabel.setText("Еще раз зашифрованное сообщение: " + rsa.getEn());
            signLabel.setText("Подпись: " + rsa.getCrs());
            deSignLabel.setText("Расшифрованная подпись: " + rsa.getDes());
        });
    }

    public static void newWindow() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(StartApplication.class.getResource("/RSA.fxml"));
        Stage window  = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);

        Scene scene = new Scene(fxmlLoader.load(), 732, 431);
        window.setScene(scene);
        window.setTitle("RSA");
        window.showAndWait();
    }
}
