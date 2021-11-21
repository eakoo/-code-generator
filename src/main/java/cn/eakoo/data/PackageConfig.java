package cn.eakoo.data;

import lombok.Data;
import java.util.Map;
import com.baomidou.mybatisplus.generator.config.OutputFile;

/**
 * @author rui.zhou
 * @date 2021/11/19 15:11
 **/
@Data
public class PackageConfig {
    
    /**
    * 父包名
    */
    @Deprecated
    private String parent;

    /**
     * 父包模块名
     */
    @Deprecated
    private String moduleName;

    /**
     * Entity 包名
     */
    private String entity;

    /**
     * Service 包名
     */
    private String service;

    /**
     * Service Impl 包名
     */
    private String serviceImpl;

    /**
     * Mapper 包名
     */
    private String mapper;

    /**
     * Mapper XML 包名
     */
    private String mapperXml;

    /**
     * Controller 包名
     */
    private String controller;

    /**
     * 自定义文件包名
     */
    private String other;

    /**
     * 路径配置信息
     */
    @Deprecated
    private Map<OutputFile, String> pathInfo;


}
