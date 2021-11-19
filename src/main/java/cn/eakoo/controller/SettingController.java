package cn.eakoo.controller;
import com.baomidou.mybatisplus.generator.config.OutputFile;

import java.io.IOException;
import java.util.HashMap;
import cn.eakoo.data.PackageConfig;
import com.baomidou.mybatisplus.generator.config.rules.DateType;

import cn.eakoo.data.DataSource;
import cn.eakoo.data.GlobalConfig;
import cn.eakoo.data.Result;
import cn.eakoo.util.AlertUtils;
import de.felixroske.jfxsupport.FXMLController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.annotation.Order;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
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
    public CheckBox db1CheckBox;
    private static final String URL_CLASS = "url";
    private static final String USER_CLASS = "user";
    private static final String PASS_WORD_CLASS = "password";
    private static final String MYSQL_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String ORACLE_DRIVER = "oracle.jdbc.driver.OracleDriver";
    public TextField commentDateTextField;
    public CheckBox fileOverrideCheckBox;
    public CheckBox disableOpenDirCheckBox;
    public CheckBox kotlinCheckBox;
    public CheckBox swaggerCheckBox;
    public ChoiceBox<DateType> dateTypeChoiceBox;
    public TextField serviceTextField;
    public TextField entityTextField;
    public TextField serviceImplTextField;
    public TextField mapperTextField;
    public TextField mapperXmlTextField;
    public TextField controllerTextField;
    public TextField customTextField;
    private DataSource dataSource;

    @Resource
    private MainController mainController;

    private final ObservableList<CheckBox> dataBaseCheckBoxList = FXCollections.observableArrayList();

    @SneakyThrows
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // 数据源配置测试按钮和选中按钮绑定事件
        this.dataBaseBindButton();

        // 加载默认数据源
        Tab selectedItem = dbTabPane.getSelectionModel().getSelectedItem();
        this.setDataSource(this.getDataSource(selectedItem));

        // 加载全局配置
        dateTypeChoiceBox.setValue(DateType.TIME_PACK);
        dateTypeChoiceBox.setItems(FXCollections.observableArrayList(DateType.ONLY_DATE, DateType.SQL_PACK, DateType.TIME_PACK));

    }

    @PostConstruct
    public void init() {
    }

    /**
     * 数据源配置测试按钮和选中按钮绑定事件
     */
    private void dataBaseBindButton() {
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
                            Tooltip tooltip = new Tooltip();
                            DataSource source = this.getDataSource(tab);
                            Result result = this.testConnection(source.getUrl(), source.getUser(), source.getPassword());
                            if (!result.isSuccess()) {
                                AlertUtils.error("connection exception", result.getException());
                            } else {
                                tooltip.setText("Succeeded\n" + result.getMessage());
                                tooltip.setAutoHide(true);
                                tooltip.show(button.getScene().getWindow());
                            }
                        });
                    }
                    if (node1 instanceof CheckBox) {
                        CheckBox checkBox = (CheckBox) node1;
                        dataBaseCheckBoxList.add(checkBox);
                        checkBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
                            if (newValue) {
                                this.setDataSource(this.getDataSource(tab));
                                dataBaseCheckBoxList.stream()
                                        .filter(checkBox1 -> !checkBox1.equals(checkBox))
                                        .forEach(checkBox1 -> checkBox1.setSelected(false));
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
    public Result testConnection(String url, String user, String password){
        try {
            Result result = new Result();
            result.setCode(0);
            String driver = url.contains("oracle") ? ORACLE_DRIVER : MYSQL_DRIVER;
            Class.forName(driver);
            Connection connection = DriverManager.getConnection(url, user, password);
            String databaseProductVersion = connection.getMetaData().getDatabaseProductVersion();
            result.setMessage(databaseProductVersion);
            connection.close();
            return result;
        } catch (ClassNotFoundException | SQLException e) {
            return new Result(99, e.getMessage(), e);
        }
    }

    /**
     * 获取选择的数据源连接信息
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

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * 获取全局配置
     *
     * @return GlobalConfig
     */
    public GlobalConfig getGlobalConfig() {
        GlobalConfig globalConfig = new GlobalConfig();
        String commentDate = commentDateTextField.getText();
        globalConfig.setCommentDate(StringUtils.isBlank(commentDate) ? "yyyy-MM-dd" : commentDate);
        globalConfig.setEnableKotlin(kotlinCheckBox.isSelected());
        globalConfig.setEnableSwagger(swaggerCheckBox.isSelected());
        globalConfig.setFileOverride(fileOverrideCheckBox.isSelected());
        globalConfig.setDisableOpenDir(disableOpenDirCheckBox.isSelected());
        globalConfig.setDateType(dateTypeChoiceBox.getValue());
        return globalConfig;
    }

    /**
     * 获取包配置
     *
     * @return PackageConfig
     */
    public PackageConfig getPackageConfig() {
        PackageConfig packageConfig = new PackageConfig();
        packageConfig.setEntity(StringUtils.isBlank(entityTextField.getText()) ? "entity" : entityTextField.getText());
        packageConfig.setService(StringUtils.isBlank(serviceTextField.getText()) ? "service" : serviceTextField.getText());
        packageConfig.setServiceImpl(StringUtils.isBlank(serviceImplTextField.getText()) ? "service.impl" : serviceImplTextField.getText());
        packageConfig.setMapper(StringUtils.isBlank(mapperTextField.getText()) ? "mapper" : mapperTextField.getText());
        packageConfig.setMapperXml(StringUtils.isBlank(mapperXmlTextField.getText()) ? "mapper.xml" : mapperXmlTextField.getText());
        packageConfig.setController(StringUtils.isBlank(controllerTextField.getText()) ? "controller" : controllerTextField.getText());
        packageConfig.setOther(StringUtils.isBlank(customTextField.getText()) ? "custom" : customTextField.getText());
        return packageConfig;
    }
}
