package example.plot;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

import javax.imageio.*;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.*;


public class BarChartController implements Initializable {

    @FXML
    private TableColumn<Data, Character> asciiCharColumn;

    @FXML
    private TableColumn<Data, Character> asciiCharColumn1;

    @FXML
    private TableColumn<Data, Character> asciiCharColumn2;

    @FXML
    private TableColumn<Data, Character> asciiCharColumn3;

    @FXML
    private TableColumn<Data, Integer> asciiNumColumn;

    @FXML
    private TableColumn<Data, Integer> asciiNumColumn1;

    @FXML
    private TableColumn<Data, Integer> asciiNumColumn2;

    @FXML
    private TableColumn<Data, Integer> asciiNumColumn3;

    @FXML
    private BarChart<?, ?> barChart;

    @FXML
    private BarChart<?, ?> barChartBlue;

    @FXML
    private BarChart<?, ?> barChartGreen;

    @FXML
    private BarChart<?, ?> barChartRed;

    @FXML
    private Label label;

    @FXML
    private Label label1;

    @FXML
    private Label label2;

    @FXML
    private Label label3;

    @FXML
    private CategoryAxis labelX;

    @FXML
    private NumberAxis labelY;

    @FXML
    private TableColumn<Data, Integer> numColumn;

    @FXML
    private TableColumn<Data, Integer> numColumn1;

    @FXML
    private TableColumn<Data, Integer> numColumn2;

    @FXML
    private TableColumn<Data, Integer> numColumn3;

    @FXML
    private AnchorPane pane;

    @FXML
    private Button selectFileButton;

    @FXML
    private TableColumn<Data, Double> probabilityColumn;

    @FXML
    private TableColumn<Data, Double> probabilityColumn1;

    @FXML
    private TableColumn<Data, Double> probabilityColumn2;

    @FXML
    private TableColumn<Data, Double> probabilityColumn3;

    @FXML
    private TableView<Data> tableView;

    @FXML
    private TableView<Data> tableViewBlue;

    @FXML
    private TableView<Data> tableViewGreen;

    @FXML
    private TableView<Data> tableViewRed;

    @FXML
    private Label startLabel;

    @FXML
    private Label formatLabel;


    String fileName;
    String text;
    DecimalFormat df;
    Integer length = 0;
    Integer lengthRed = 0;
    Integer lengthGreen = 0;
    Integer lengthBlue = 0;
    double entropy = 0;
    double entropyRed = 0;
    double entropyGreen = 0;
    double entropyBlue = 0;
    HashMap<Integer, Integer> map;
    private Channel channel;
    XYChart.Series series;
    XYChart.Series seriesRed;
    XYChart.Series seriesGreen;
    XYChart.Series seriesBlue;

    GeneratorLSG lsg;
    GeneratorBBS bbs;
    GeneratorLFSR lfsr;
    List<Integer> res;

    VigenereCipher cipher = new VigenereCipher(1072, 32);


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        df = new DecimalFormat("#.######");

        barChartRed.setVisible(false);
        barChartGreen.setVisible(false);
        barChartBlue.setVisible(false);
        tableViewRed.setVisible(false);
        tableViewGreen.setVisible(false);
        tableViewBlue.setVisible(false);
        formatLabel.setVisible(false);
        label.setVisible(false);
        label1.setVisible(false);
        label2.setVisible(false);
        label3.setVisible(false);
        startLabel.setVisible(false);

        map = new HashMap<>();
        for (int i = 0; i <= 255; i++) {
            map.put(i, 0);
        }

        lsg = new GeneratorLSG();
        bbs = new GeneratorBBS();
        lfsr = new GeneratorLFSR();


        // Изменить генератор
//        try {
//            res = lsg.generate(1000);
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//        res = bbs.generate(1000);
//        res = lfsr.generate(1000);

//        System.out.println(res);


        // Изменить ключ
        try {
            res = cipher.getVigenereKeyInt(readUsingScanner("src/main/resources/textChipher.txt"), 1);
//            res = cipher.getVigenereKeyStr(readUsingScanner("src/main/resources/textChipher.txt"), "Родной куст и зайцу дорог.");
//            res = cipher.getVigenereKeyList(readUsingScanner("src/main/resources/textChipher.txt"), lfsr.generate(10));
        } catch (IOException e) {
            e.printStackTrace();
        }


        selectFileButton.setOnAction(actionEvent -> {
            startLabel.setVisible(false);
            formatLabel.setVisible(true);
            barChart.setVisible(false);
            barChartRed.setVisible(false);
            barChartGreen.setVisible(false);
            barChartBlue.setVisible(false);
            tableView.setVisible(false);
            tableViewRed.setVisible(false);
            tableViewGreen.setVisible(false);
            tableViewBlue.setVisible(false);
            label.setVisible(false);
            label1.setVisible(false);
            label2.setVisible(false);
            label3.setVisible(false);
            startLabel.setVisible(false);

            try {
                fileName = selectFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            formatLabel.setText("Расширение файла: \"" + getFileExtension(fileName) + "\"");

        });

        setData();
        try {
            showData();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private String selectFile() throws IOException {
        clearData();
        JFileChooser fileopen = new JFileChooser();
        File chooserFile = new File((new File("./src/main/resources").getCanonicalPath()));
        fileopen.setCurrentDirectory(chooserFile);
        int ret = fileopen.showDialog(null, "Открыть файл");
        File file = null;
        if (ret == JFileChooser.APPROVE_OPTION) {
            file = fileopen.getSelectedFile();
        }
        return file.getPath();
    }

    private static String readUsingScanner(String fileName) throws IOException {
        Scanner scanner = new Scanner(Paths.get(fileName), StandardCharsets.UTF_8.name());
        String data = scanner.useDelimiter("\\A").next();
        scanner.close();
        return data;
    }

    private static String getFileExtension(String name) {
        int index = name.indexOf('.');
        return index == -1 ? null : name.substring(index);
    }

    private void clearData() {
        barChart.getData().clear();
        barChartRed.getData().clear();
        barChartGreen.getData().clear();
        barChartBlue.getData().clear();
        tableView.getItems().clear();
        tableViewRed.getItems().clear();
        tableViewGreen.getItems().clear();
        tableViewBlue.getItems().clear();
        entropy = 0;
        entropyRed = 0;
        entropyGreen = 0;
        entropyBlue = 0;
    }

    public void showData() throws SQLException {
        asciiNumColumn.setCellValueFactory(new PropertyValueFactory<Data, Integer>("asciiNum"));
        numColumn.setCellValueFactory(new PropertyValueFactory<Data, Integer>("num"));
        probabilityColumn.setCellValueFactory(new PropertyValueFactory<Data, Double>("probability"));

        asciiNumColumn1.setCellValueFactory(new PropertyValueFactory<Data, Integer>("asciiNum"));
        numColumn1.setCellValueFactory(new PropertyValueFactory<Data, Integer>("num"));
        probabilityColumn1.setCellValueFactory(new PropertyValueFactory<Data, Double>("probability"));

        asciiNumColumn2.setCellValueFactory(new PropertyValueFactory<Data, Integer>("asciiNum"));
        numColumn2.setCellValueFactory(new PropertyValueFactory<Data, Integer>("num"));
        probabilityColumn2.setCellValueFactory(new PropertyValueFactory<Data, Double>("probability"));

        asciiNumColumn3.setCellValueFactory(new PropertyValueFactory<Data, Integer>("asciiNum"));
        numColumn3.setCellValueFactory(new PropertyValueFactory<Data, Integer>("num"));
        probabilityColumn3.setCellValueFactory(new PropertyValueFactory<Data, Double>("probability"));


        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableViewRed.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableViewGreen.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableViewBlue.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    private void setData() {
        if (!Objects.equals(res, "")) {
            barChart.setVisible(true);
            tableView.setVisible(true);
            label.setVisible(true);

            for (Integer ch : res) {
                map.merge(ch, 1, Integer::sum);
                length++;
            }

            series = new XYChart.Series();
            series.setName("Количество повторений");
            for (int i = 0; i <= 255; i++) {
                series.getData().add(new XYChart.Data(String.valueOf(i), map.get(i)));
            }

            barChart.getData().addAll(series);
            for (int i = 0; i <= 255; i++) {
                try {
                    tableView.getItems().add(new Data(new String(new byte[]{map.get(i).byteValue()}, "cp866").toCharArray()[0], i, map.get(i), df.format((double) map.get(i) / length)));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                if (map.get(i) != 0) {
                    entropy += -((double) map.get(i) / length) * ((Math.log((double) map.get(i) / length)) / Math.log(2.0));
                }
            }
        } else if (Objects.equals(getFileExtension(fileName), ".txt")) {
            barChart.setVisible(true);
            tableView.setVisible(true);
            label.setVisible(true);

            try {
                text = readUsingScanner(fileName);
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                byte[] bytes = Files.readAllBytes(Path.of(fileName));
                for (byte b : bytes) {
                    map.merge(Byte.toUnsignedInt(b), 1, Integer::sum);
                    length++;
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            series = new XYChart.Series();
            series.setName("Количество повторений");
            for (int i = 0; i <= 255; i++) {
                series.getData().add(new XYChart.Data(String.valueOf(i), map.get(i)));
            }

            barChart.getData().addAll(series);
            for (int i = 0; i <= 255; i++) {
                try {
                    tableView.getItems().add(new Data(new String(new byte[]{map.get(i).byteValue()}, "cp866").toCharArray()[0], i, map.get(i), df.format((double) map.get(i) / length)));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                if (map.get(i) != 0) {
                    entropy += -((double) map.get(i) / length) * ((Math.log((double) map.get(i) / length)) / Math.log(2.0));
                }
            }
        } else {
            barChartRed.setVisible(true);
            barChartGreen.setVisible(true);
            barChartBlue.setVisible(true);

            tableViewRed.setVisible(true);
            tableViewGreen.setVisible(true);
            tableViewBlue.setVisible(true);

            label.setVisible(true);
            label1.setVisible(true);
            label2.setVisible(true);
            label3.setVisible(true);

            channel = Channel.RED;
            startAnalysis();
            seriesRed = new XYChart.Series();
            seriesRed.setName("RED");
            for (int i = 0; i <= 255; i++) {
                seriesRed.getData().add(new XYChart.Data(String.valueOf(i), map.get(i)));

                tableViewRed.getItems().add(new Data((char) i, i, map.get(i), df.format((double) map.get(i) / lengthRed)));
                if (map.get(i) != 0)
                    entropyRed += -((double) map.get(i) / lengthRed) * ((Math.log((double) map.get(i) / lengthRed)) / Math.log(2.0));
            }
            map.clear();

            channel = Channel.GREEN;
            startAnalysis();
            seriesGreen = new XYChart.Series();
            seriesGreen.setName("GREEN");
            for (int i = 0; i <= 255; i++) {
                seriesGreen.getData().add(new XYChart.Data(String.valueOf(i), map.get(i)));

                tableViewGreen.getItems().add(new Data((char) i, i, map.get(i), df.format((double) map.get(i) / lengthGreen)));
                if (map.get(i) != 0)
                    entropyGreen += -((double) map.get(i) / lengthGreen) * ((Math.log((double) map.get(i) / lengthGreen)) / Math.log(2.0));
            }
            map.clear();

            channel = Channel.BLUE;
            startAnalysis();
            seriesBlue = new XYChart.Series();
            seriesBlue.setName("BLUE");
            for (int i = 0; i <= 255; i++) {
                seriesBlue.getData().add(new XYChart.Data(String.valueOf(i), map.get(i)));

                tableViewBlue.getItems().add(new Data((char) i, i, map.get(i), df.format((double) map.get(i) / lengthBlue)));
                if (map.get(i) != 0)
                    entropyBlue += -((double) map.get(i) / lengthBlue) * ((Math.log((double) map.get(i) / lengthBlue)) / Math.log(2.0));
            }
            map.clear();

            barChartRed.getData().addAll(seriesRed);
            barChartGreen.getData().addAll(seriesGreen);
            barChartBlue.getData().addAll(seriesBlue);

            entropy = entropyRed + entropyGreen + entropyBlue;
        }

        label.setText("Энтропия: " + entropy);
        label1.setText("Энтропия: " + entropyRed);
        label2.setText("Энтропия: " + entropyGreen);
        label3.setText("Энтропия: " + entropyBlue);
    }

    private void startAnalysis() {
        BufferedImage image = null;
        for (int i = 0; i <= 255; i++) {
            map.put(i, 0);
        }

        try {
            image = ImageIO.read(new File(fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                Integer rgb = image.getRGB(x, y);

                if (channel == Channel.RED) {
                    map.merge((rgb >> 16) & 0x000000FF, 1, Integer::sum);
                    lengthRed += 1;
                } else if (channel == Channel.GREEN) {
                    map.merge((rgb >> 8) & 0x000000FF, 1, Integer::sum);
                    lengthGreen += 1;
                } else if (channel == Channel.BLUE) {
                    map.merge((rgb) & 0x000000FF, 1, Integer::sum);
                    lengthBlue += 1;
                }
            }
        }
    }

    private enum Channel {
        RED,
        GREEN,
        BLUE
    }
}


