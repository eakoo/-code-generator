package cn.eakoo.controller;

import cn.eakoo.CodeGeneratorApplication;
import cn.eakoo.veiw.AboutView;
import cn.eakoo.veiw.SettingView;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import de.felixroske.jfxsupport.FXMLController;
import javafx.event.ActionEvent;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.PostConstruct;
import java.net.URL;
import java.util.Collections;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * @author rui.zhou
 * @date 2021/11/15 09:21
 **/
@Slf4j
@FXMLController
public class MainController implements Initializable {

    public TextField author;
    public TextField groupId;
    public TextField artifactId;
    public TextField tables;
    public TextField tablePrefix;
    public TextField codePath;
    public TextField xmlPath;

    @FXML
    private SettingController settingController;


    private static final String USER_NAME = "ncm_uat";
    private static final String PASS_WORD = "ncm_uat123";
    private static final String URL = "jdbc:oracle:thin:@10.142.141.197:1521:dborgbk01";

    @SneakyThrows
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        /*TabPane load = FXMLLoader.load(Objects.requireNonNull(this.getClass().getResource("/fxml/setting.fxml")));
        log.info("load : {}", load);
        load.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            log.info("oldValue : {}, newValue : {}", oldValue, newValue);
        });*/
    }

    /**
     * 启动完成初始化
     *
     * @return void
     */
    @SneakyThrows
    @PostConstruct
    public void init(){
        TabPane load = FXMLLoader.load(Objects.requireNonNull(this.getClass().getResource("/fxml/setting.fxml")));
        log.info("load : {}", load);
        load.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            log.info("oldValue : {}, newValue : {}", oldValue, newValue);
        });
    }

    @FXML
    public void aboutClick(ActionEvent actionEvent) {
//        CodeGeneratorApplication.getStage().close();
        CodeGeneratorApplication.showView(AboutView.class, Modality.APPLICATION_MODAL);
    }

    @FXML
    public void createCode(ActionEvent actionEvent) {

        String[] tableArray = tables.getText().split(",");
        log.info("tableArray : {}", tableArray[0]);
        String[] tablePrefixArray = tablePrefix.getText().split(",");
        log.info("tablePrefixArray : {}", tablePrefixArray[0]);
        String codeCreatePath = StringUtils.isBlank(codePath.getText()) ? "D://CodeGenerator" : codePath.getText();
        log.info("codeCreatePath : {}", codeCreatePath);
        String mapperXmlPath = StringUtils.isBlank(xmlPath.getText())? "D://CodeGenerator" : xmlPath.getText();
        log.info("mapperXmlPath : {}", mapperXmlPath);
        String authorStr = StringUtils.isBlank(author.getText()) ? "rui.zhou" : author.getText();
        log.info("authorStr : {}", authorStr);

        FastAutoGenerator.create(new DataSourceConfig.Builder(URL, USER_NAME, PASS_WORD))
                .globalConfig(builder -> {
                    builder.author(authorStr) // 设置作者
                            .commentDate("yyyy-MM-dd")
                            .dateType(DateType.TIME_PACK)
                            .enableSwagger() // 开启 swagger 模式
                            .fileOverride() // 覆盖已生成文件
                            .outputDir(codeCreatePath); // 指定输出目录
                })
                .packageConfig(builder -> {
                    builder.parent(groupId.getText()) // 设置父包名
                            .moduleName(artifactId.getText()) // 设置父包模块名
                            .pathInfo(Collections.singletonMap(OutputFile.mapperXml, mapperXmlPath)); // 设置mapperXml生成路径
                })
                .strategyConfig(builder -> {
                    builder.addInclude(tableArray) // 设置需要生成的表名
                            .addTablePrefix(tablePrefixArray); // 设置过滤表前缀
                })
                .templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                /*.templateConfig(builder -> builder.disable(TemplateType.ENTITY)
                        .entity("/templates/entity.java")
                        .service("/templates/service.java")
                        .serviceImpl("/templates/serviceImpl.java")
                        .mapper("/templates/mapper.java")
                        .mapperXml("/templates/mapper.xml")
                        .controller("/templates/controller.java")
                        .build())*/
                /*.injectionConfig(builder -> builder.beforeOutputFile((tableInfo, stringObjectMap) -> {

                })
                        .customMap(Collections.singletonMap("test", "testValue"))
                        .customFile(Collections.singletonMap("test.txt", "/templates/test.vm"))
                        .build())*/
                .execute();
    }

    @FXML
    public void testDataBase1(ActionEvent actionEvent) {

    }

    public void settingClick(ActionEvent actionEvent) {
        CodeGeneratorApplication.showView(SettingView.class, Modality.WINDOW_MODAL);
    }
}
