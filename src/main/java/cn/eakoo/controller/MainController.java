package cn.eakoo.controller;

import cn.eakoo.*;
import cn.eakoo.config.SpringContextHolder;
import cn.eakoo.data.*;
import cn.eakoo.util.AlertUtils;
import cn.eakoo.veiw.AboutView;
import cn.eakoo.veiw.SettingView;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.IFill;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.TemplateType;
import com.baomidou.mybatisplus.generator.config.po.LikeTable;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import de.felixroske.jfxsupport.AbstractJavaFxApplicationSupport;
import de.felixroske.jfxsupport.FXMLController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.net.URL;
import java.util.Collections;
import java.util.List;
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
        Application.getStage().setScene(new Scene(settingView.getView()));
    }

    @PostConstruct
    public void init() {

    }

    @FXML
    public void aboutClick(ActionEvent actionEvent) {
        Application.showView(AboutView.class, Modality.APPLICATION_MODAL);
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
            cn.eakoo.data.StrategyConfig strategyConfig = settingController.getStrategyConfig();
            fastAutoGenerator.strategyConfig(builder -> {
                //1. 通用策略
                this.setCommonStrategy(tableArray, tablePrefixArray, builder, strategyConfig);

                //2. Entity策略
                this.setEntityStrategy(builder, strategyConfig.getEntityConfig());

                //3. Mapper策略
                this.setMapperStrategy(builder, strategyConfig.getMapperConfig());

                //4. Service策略
                this.setServiceStrategy(builder, strategyConfig.getServiceConfig());

                //5. Controller策略
                this.setControllerStrategy(builder, strategyConfig.getControllerConfig());

            });

            // 设置模板配置
            this.setTemplate(fastAutoGenerator);

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

    /**
     * 设置模板配置
     *
     * @param fastAutoGenerator FastAutoGenerator对象
     */
    private void setTemplate(FastAutoGenerator fastAutoGenerator) {
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
    }

    /**
     * 通用策略
     *
     * @param tableArray 生成表列表
     * @param tablePrefixArray 前缀集合
     * @param builder 策略配置构建
     */
    private void setCommonStrategy(String[] tableArray, String[] tablePrefixArray, StrategyConfig.Builder builder, cn.eakoo.data.StrategyConfig strategyConfig) {
        if (strategyConfig.isEnableCapitalMode()) {
            builder.enableCapitalMode();
        }
        if (strategyConfig.isEnableSkipView()) {
            builder.enableSkipView();
        }
        if (strategyConfig.isDisableSqlFilter()) {
            builder.disableSqlFilter();
        }
        if (strategyConfig.isEnableSchema()) {
            builder.enableSchema();
        }
        LikeTable likeTable = strategyConfig.getLikeTable();
        if (ObjectUtils.isNotEmpty(likeTable)) {
            builder.likeTable(likeTable);
        }
        LikeTable notLikeTable = strategyConfig.getNotLikeTable();
        if (ObjectUtils.isNotEmpty(notLikeTable)) {
            builder.notLikeTable(notLikeTable);
        }
        if (tableArray != null && tableArray.length > 0) {
            builder.addInclude(tableArray);
        }
        String[] addExclude = strategyConfig.getAddExclude();
        if (addExclude != null && addExclude.length > 0) {
            builder.addExclude(addExclude);
        }
        if (tablePrefixArray != null && tablePrefixArray.length > 0) {
            builder.addTablePrefix(tablePrefixArray);
        }
        String[] addTableSuffix = strategyConfig.getAddTableSuffix();
        if (addTableSuffix != null && addTableSuffix.length > 0) {
            builder.addTableSuffix(addTableSuffix);
        }
        String[] addFieldPrefix = strategyConfig.getAddFieldPrefix();
        if (addFieldPrefix != null && addFieldPrefix.length > 0) {
            builder.addFieldPrefix(addFieldPrefix);
        }
        String[] addFieldSuffix = strategyConfig.getAddFieldSuffix();
        if (addFieldSuffix != null && addFieldSuffix.length > 0) {
            builder.addFieldSuffix(addFieldSuffix);
        }
    }

    /**
     * Entity策略
     *
     * @param builder 策略配置构建
     * @param entityConfig 策略配置实体类
     */
    private void setEntityStrategy(StrategyConfig.Builder builder, EntityConfig entityConfig) {
        if (StringUtils.isNotBlank(entityConfig.getSuperClassName())) {
            builder.entityBuilder().superClass(entityConfig.getSuperClassName());
        }
        if (entityConfig.isDisableSerialVersionUID()) {
            builder.entityBuilder().disableSerialVersionUID();
        }
        if (entityConfig.isEnableColumnConstant()) {
            builder.entityBuilder().enableColumnConstant();
        }
        if (entityConfig.isEnableChainModel()) {
            builder.entityBuilder().enableChainModel();
        }
        if (entityConfig.isEnableLombok()) {
            builder.entityBuilder().enableLombok();
        }
        if (entityConfig.isEnableRemoveIsPrefix()) {
            builder.entityBuilder().enableRemoveIsPrefix();
        }
        if (entityConfig.isEnableTableFieldAnnotationEnable()) {
            builder.entityBuilder().enableTableFieldAnnotation();
        }
        if (entityConfig.isEnableActiveRecord()) {
            builder.entityBuilder().enableActiveRecord();
        }
        if (StringUtils.isNotBlank(entityConfig.getVersionColumnName())) {
            builder.entityBuilder().versionColumnName(entityConfig.getVersionColumnName());
        }
        if (StringUtils.isNotBlank(entityConfig.getVersionPropertyName())) {
            builder.entityBuilder().versionPropertyName(entityConfig.getVersionPropertyName());
        }
        if (StringUtils.isNotBlank(entityConfig.getLogicDeleteColumnName())) {
            builder.entityBuilder().logicDeleteColumnName(entityConfig.getLogicDeleteColumnName());
        }
        if (StringUtils.isNotBlank(entityConfig.getLogicDeletePropertyName())) {
            builder.entityBuilder().logicDeletePropertyName(entityConfig.getLogicDeletePropertyName());
        }
        if (ObjectUtils.isNotEmpty(entityConfig.getNaming())) {
            builder.entityBuilder().naming(entityConfig.getNaming());
        }
        if (ObjectUtils.isNotEmpty(entityConfig.getColumnNaming())) {
            builder.entityBuilder().columnNaming(entityConfig.getColumnNaming());
        }
        String[] superEntityColumns = entityConfig.getAddSuperEntityColumns();
        if (superEntityColumns != null && superEntityColumns.length > 0) {
            builder.entityBuilder().addSuperEntityColumns(superEntityColumns);
        }
        String[] addIgnoreColumns = entityConfig.getAddIgnoreColumns();
        if (addIgnoreColumns != null && addIgnoreColumns.length > 0) {
            builder.entityBuilder().addIgnoreColumns(addIgnoreColumns);
        }
        List<IFill> addTableFills = entityConfig.getAddTableFills();
        if (addTableFills != null && !addTableFills.isEmpty()) {
            builder.entityBuilder().addTableFills(addTableFills);
        }
        builder.entityBuilder().idType(ObjectUtils.isEmpty(entityConfig.getIdType())? IdType.AUTO : entityConfig.getIdType());
        if (StringUtils.isNotBlank(entityConfig.getFormatFileName())) {
            builder.entityBuilder().formatFileName(entityConfig.getFormatFileName());
        }
    }

    /**
     * Mapper策略
     *
     * @param builder 策略配置构建
     * @param mapperConfig 策略配置实体类
     */
    private void setMapperStrategy(StrategyConfig.Builder builder, MapperConfig mapperConfig) {
        if (StringUtils.isNotBlank(mapperConfig.getSuperClassName())) {
            builder.mapperBuilder().superClass(mapperConfig.getSuperClassName());
        }
        if (mapperConfig.isEnableMapperAnnotation()) {
            builder.mapperBuilder().enableMapperAnnotation();
        }
        if (mapperConfig.isEnableBaseResultMap()) {
            builder.mapperBuilder().enableBaseResultMap();
        }
        if (mapperConfig.isEnableBaseColumnList()) {
            builder.mapperBuilder().enableBaseColumnList();
        }
        if (ObjectUtils.isNotEmpty(mapperConfig.getCache())) {
            builder.mapperBuilder().cache(mapperConfig.getCache());
        }
        if (StringUtils.isNotBlank(mapperConfig.getFormatMapperFileName())) {
            builder.mapperBuilder().formatMapperFileName(mapperConfig.getFormatMapperFileName());
        }
        if (StringUtils.isNotBlank(mapperConfig.getFormatXmlFileName())) {
            builder.mapperBuilder().formatXmlFileName(mapperConfig.getFormatXmlFileName());
        }
    }

    /**
     * Service 策略配置
     *
     * @param builder 策略配置构建
     * @param serviceConfig 策略配置实体类
     */
    private void setServiceStrategy(StrategyConfig.Builder builder, ServiceConfig serviceConfig) {
        if (StringUtils.isNotBlank(serviceConfig.getSuperServiceClass())) {
            builder.serviceBuilder().superServiceClass(serviceConfig.getSuperServiceClass());
        }
        if (StringUtils.isNotBlank(serviceConfig.getSuperServiceImplClass())) {
            builder.serviceBuilder().superServiceImplClass(serviceConfig.getSuperServiceImplClass());
        }
        if (StringUtils.isNotBlank(serviceConfig.getFormatServiceFileName())) {
            builder.serviceBuilder().formatServiceFileName(serviceConfig.getFormatServiceFileName());
        }
        if (StringUtils.isNotBlank(serviceConfig.getFormatServiceImplFileName())) {
            builder.serviceBuilder().formatServiceImplFileName(serviceConfig.getFormatServiceImplFileName());
        }
    }

    /**
     * Controller 策略配置
     *
     * @param builder 策略配置构建
     * @param controllerConfig 策略配置实体类
     */
    private void setControllerStrategy(StrategyConfig.Builder builder, ControllerConfig controllerConfig) {
        if (StringUtils.isNotBlank(controllerConfig.getSuperClassName())) {
            builder.controllerBuilder().superClass(controllerConfig.getSuperClassName());
        }
        if (controllerConfig.isEnableHyphen()) {
            builder.controllerBuilder().enableHyphenStyle();
        }
        if (controllerConfig.isEnableRest()) {
            builder.controllerBuilder().enableRestStyle();
        }
        if (StringUtils.isNotBlank(controllerConfig.getFormatFileName())) {
            builder.controllerBuilder().formatFileName(controllerConfig.getFormatFileName());
        }
    }

    public void settingClick(ActionEvent actionEvent) {
        AbstractJavaFxApplicationSupport.showView(SettingView.class, Modality.APPLICATION_MODAL);
    }

}
