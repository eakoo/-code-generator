package cn.eakoo.controller;

import cn.eakoo.CodeGeneratorApplication;
import cn.eakoo.data.DataSource;
import de.felixroske.jfxsupport.FXMLController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.NodeOrientation;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.StageStyle;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * @author rui.zhou
 * @date 2021/11/18 09:55
 **/
@Slf4j
@FXMLController
public class SettingController implements Initializable {

    public TabPane parentTabPane;
    public TabPane dbTabPane;
    public Button db1Button;
    public CheckBox db1CheckBox;
    private static final String USER_CLASS = "user";
    private static final String PASS_WORD_CLASS = "password";
    private static final String URL_CLASS = "url";
    private DataSource dataSource;
    private CheckBox selectCheckBox;

    private final ObservableList<CheckBox> dataBaseCheckBoxList = FXCollections.observableArrayList();

    @SneakyThrows
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // 数据源配置测试按钮和选中按钮绑定事件
        dbTabPane.getTabs().forEach(tab -> {
            TabPane tabPane = tab.getTabPane();
            ObservableList<Tab> tabs = tabPane.getTabs();
            tabs.forEach(tab1 -> {
                Node node = tab1.getContent();
                AnchorPane anchorPane = (AnchorPane) node;
                anchorPane.getChildren().forEach(node1 -> {
                    if (node1 instanceof Button) {
                        Button button = (Button) node1;
                        button.setOnMouseClicked(event -> {
                            DataSource source = this.getDataSource(tab);
                            Exception exception = this.testConnection(source.getUrl(), source.getUser(), source.getPassword());
                            if (ObjectUtils.isNotEmpty(exception)) {
                                log.info("exception", exception);
                                Alert alert = new Alert(Alert.AlertType.ERROR);
                                alert.setTitle("connection exception");
                                alert.setHeaderText("Look, an Exception Dialog");
                                alert.setContentText(exception.getMessage());

                                StringWriter sw = new StringWriter();
                                PrintWriter pw = new PrintWriter(sw);
                                exception.printStackTrace(pw);
                                String exceptionText = sw.toString();

                                Label label = new Label("The exception stacktrace was:");
                                TextArea textArea = new TextArea(exceptionText);
                                textArea.setEditable(false);
                                textArea.setWrapText(true);

                                textArea.setMaxWidth(Double.MAX_VALUE);
                                textArea.setMaxHeight(Double.MAX_VALUE);
                                GridPane.setVgrow(textArea, Priority.ALWAYS);
                                GridPane.setHgrow(textArea, Priority.ALWAYS);

                                GridPane expContent = new GridPane();
                                expContent.setMaxWidth(Double.MAX_VALUE);
                                expContent.add(label, 0, 0);
                                expContent.add(textArea, 0, 1);
                                alert.getDialogPane().setExpandableContent(expContent);
                                alert.getDialogPane().getScene().get
                                alert.showAndWait();
                            } else {
                                log.info("exception", exception);
                            }
                        });
                    }
                    if (node1 instanceof CheckBox) {
                        CheckBox checkBox = (CheckBox) node1;
                        dataBaseCheckBoxList.add(checkBox);
                        checkBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
                            if (newValue) {
                                dataBaseCheckBoxList.stream()
                                        .filter(checkBox1 -> !checkBox1.equals(checkBox))
                                        .forEach(checkBox1 -> {
                                            checkBox1.setSelected(false);
                                        });
                                log.info("dataSource : {}", this.getDataSource(tab));
                            }
                        });
                    }
                });
            });
        });
    }

    /**
    * 测试数据库连接
    *
    * @param url 连接 url
    * @param user 用户
    * @param password 密码
    * @return Object
    */
    public Exception testConnection(String url, String user, String password){
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            Connection connection = DriverManager.getConnection(url, user, password);
            log.info("connection : {}", connection);
            connection.close();
        } catch (ClassNotFoundException | SQLException e) {
            return e;
        }
        return null;
    }

    /**
     * 获取选择的数据源信息
     *
     * @param tab 导航标签
     * @return DataSource
     */
    private DataSource getDataSource(Tab tab) {
        DataSource dataSource = new DataSource();
        TabPane tabPane = tab.getTabPane();
        Tab selectedItem = tabPane.getSelectionModel().getSelectedItem();
        Node content = selectedItem.getContent();
        AnchorPane anchorPane = (AnchorPane) content;
        anchorPane.getChildren().forEach(node -> {
            if (node instanceof GridPane) {
                GridPane gridPane = (GridPane) node;
                gridPane.getChildren().forEach(gridPaneNode -> {
                    if (gridPaneNode instanceof TextField) {
                        TextField textField = (TextField) gridPaneNode;
                        ObservableList<String> styleClass = textField.getStyleClass();
                        String text = textField.getText();
                        if (styleClass.contains(USER_CLASS)) {
                            dataSource.setUser(text);
                        }
                        if (styleClass.contains(PASS_WORD_CLASS)) {
                            dataSource.setPassword(text);
                        }
                        if (styleClass.contains(URL_CLASS)) {
                            dataSource.setUrl(text);
                        }
                    }
                });
            }
        });
        return dataSource;
    }

}
