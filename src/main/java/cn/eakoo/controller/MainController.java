package cn.eakoo.controller;

import cn.eakoo.CodeGeneratorApplication;
import cn.eakoo.data.DataSource;
import cn.eakoo.data.GlobalConfig;
import cn.eakoo.data.PackageConfig;
import cn.eakoo.util.AlertUtils;
import cn.eakoo.veiw.AboutView;
import cn.eakoo.veiw.SettingView;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.TemplateType;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import de.felixroske.jfxsupport.FXMLController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.net.URL;
import java.util.Collections;
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

    private static final String DEFAULT_AUTHOR = "rui.zhou";
    private static final String DEFAULT_PATH = "D://CodeGenerator";
    private static final String DEFAULT_XML_PATH = "D://CodeGenerator//resources";
    private static final String DEFAULT_PARENT_PACKAGE = "com.cignacmb";
    private static final String DEFAULT_MODULE_NAME = "ibss";

    @Resource
    private SettingController settingController;

    @SneakyThrows
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // 预加载配置
        ApplicationContext applicationContext = SpringContextHolder.getApplicationContext();
        SettingView settingView = applicationContext.getBean(SettingView.class);
        CodeGeneratorApplication.getStage().setScene(new Scene(settingView.getView()));
    }

    @PostConstruct
    public void init() {

    }

    @FXML
    public void aboutClick(ActionEvent actionEvent) {
        CodeGeneratorApplication.showView(AboutView.class, Modality.APPLICATION_MODAL);
    }

    @FXML
    public void createCode(ActionEvent actionEvent) {
        try {
            // 获取配置的数据源
            DataSource dataSource = settingController.getDataSource();
            DataSourceConfig.Builder dataSourceConfigBuilder = new DataSourceConfig.Builder(dataSource.getUrl(),
                    dataSource.getUser(), dataSource.getPassword());
            FastAutoGenerator fastAutoGenerator = FastAutoGenerator.create(dataSourceConfigBuilder);

            // 获取基本信息
            String[] tableArray = tables.getText().split(",");
            String[] tablePrefixArray = tablePrefix.getText().split(",");
            String codeCreatePath = StringUtils.isBlank(codePath.getText()) ? DEFAULT_PATH : codePath.getText();
            String mapperXmlPath = StringUtils.isBlank(xmlPath.getText()) ? DEFAULT_XML_PATH : xmlPath.getText();
            String authorStr = StringUtils.isBlank(author.getText()) ? DEFAULT_AUTHOR : author.getText();
            String parent = StringUtils.isBlank(groupId.getText()) ? DEFAULT_PARENT_PACKAGE : groupId.getText();
            String moduleName = StringUtils.isBlank(artifactId.getText()) ? DEFAULT_MODULE_NAME : artifactId.getText();

            // 设置全局配置
            GlobalConfig globalConfig = settingController.getGlobalConfig();
            log.info("globalConfig : {}", globalConfig);
            fastAutoGenerator.globalConfig(builder -> {
                builder.author(authorStr);
                builder.commentDate(globalConfig.getCommentDate());
                builder.dateType(globalConfig.getDateType());
                builder.outputDir(codeCreatePath);
                if (globalConfig.isEnableSwagger()) {
                    builder.enableSwagger();
                }
                if (globalConfig.isEnableKotlin()) {
                    builder.enableKotlin();
                }
                if (globalConfig.isFileOverride()) {
                    builder.fileOverride();
                }
                if (globalConfig.isDisableOpenDir()) {
                    builder.disableOpenDir();
                }
            });

            // 设置包配置
            PackageConfig packageConfig = settingController.getPackageConfig();
            log.info("packageConfig : {}", packageConfig);
            fastAutoGenerator.packageConfig(builder -> {
                builder.parent(parent);
                builder.moduleName(moduleName);
                builder.entity(packageConfig.getEntity());
                builder.service(packageConfig.getService());
                builder.serviceImpl(packageConfig.getServiceImpl());
                builder.mapper(packageConfig.getMapper());
                builder.xml(packageConfig.getMapperXml());
                builder.controller(packageConfig.getController());
                builder.other(packageConfig.getOther());
                builder.pathInfo(Collections.singletonMap(OutputFile.mapperXml, mapperXmlPath));
            });

            // 设置策略配置
            fastAutoGenerator.strategyConfig(builder -> {
                builder.addInclude(tableArray);
                builder.addTablePrefix(tablePrefixArray);
            });

            // 设置模板配置
            fastAutoGenerator.templateEngine(new FreemarkerTemplateEngine());
            fastAutoGenerator.templateConfig(builder -> {
                builder.disable(TemplateType.ENTITY);
                builder.entity("/templates/entity.java");
                builder.service("/templates/service.java");
                builder.serviceImpl("/templates/serviceImpl.java");
                builder.mapper("/templates/mapper.java");
                builder.mapperXml("/templates/mapper.xml");
                builder.controller("/templates/controller.java");
            });

            // 设置注入配置
            fastAutoGenerator.injectionConfig(builder -> {
                    builder.beforeOutputFile((tableInfo, objectMap) ->
                            log.info("tableInfo : {}, objectMap : {}", tableInfo.getEntityName(), objectMap.size()));
//                    builder.customMap(Collections.singletonMap("test", "baomidou"));
//                    builder.customFile(Collections.singletonMap("test.txt", "/templates/test.vm"));
            });
            fastAutoGenerator.execute();

        } catch (Exception e) {
            log.error("create code error", e);
            AlertUtils.error(e.getMessage(), e);
        }
    }

    public void settingClick(ActionEvent actionEvent) {
        CodeGeneratorApplication.showView(SettingView.class, Modality.APPLICATION_MODAL);
    }
}
