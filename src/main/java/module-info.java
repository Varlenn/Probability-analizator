module example.plot {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;
    requires java.desktop;
    requires java.sql;

    opens example.plot to javafx.fxml;
    exports example.plot;
}