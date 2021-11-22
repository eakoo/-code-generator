package cn.eakoo.data;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.IFill;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.function.ConverterFileName;
import lombok.Data;
import java.util.List;

/**
 * @author rui.zhou
 * @date 2021/11/21 20:43
 **/
@Data
public class EntityConfig {
    
    /**
    * 父类
    */
    private String superClassName;

    /**
     * 禁用生成 serialVersionUID
     * 默认值:true
     */
    private boolean disableSerialVersionUID;

    /**
     * 开启生成字段常量
     * 默认值:false
     */
    private boolean enableColumnConstant;

    /**
     * 开启链式模型
     * 默认值:false
     */
    private boolean enableChainModel;

    /**
     * 开启 lombok 模型
     * 默认值:false
     */
    private boolean enableLombok;

    /**
     * 开启 Boolean 类型字段移除 is 前缀
     * 默认值:false
     */
    private boolean enableRemoveIsPrefix;

    /**
     * 开启生成实体时生成字段注解
     * 默认值:false
     */
    private boolean enableTableFieldAnnotationEnable;

    /**
     * 开启 ActiveRecord 模型
     * 默认值:false
     */
    private boolean enableActiveRecord;

    /**
     * 乐观锁字段名(数据库)
     */
    private String versionColumnName;

    /**
     * 乐观锁属性名(实体)
     */
    private String versionPropertyName;

    /**
     * 逻辑删除字段名(数据库)
     */
    private String logicDeleteColumnName;

    /**
     * 逻辑删除属性名(实体)
     */
    private String logicDeletePropertyName;

    /**
     * 数据库表映射到实体的命名策略
     * 默认下划线转驼峰命名:NamingStrategy.underline_to_camel
     */
    private NamingStrategy naming;

    /**
     * 数据库表字段映射到实体的命名策略
     * 默认为 null，未指定按照 naming 执行
     */
    private NamingStrategy columnNaming;

    /**
     * 添加父类公共字段
     */
    private String[] addSuperEntityColumns;

    /**
     * 添加忽略字段
     */
    private String[] addIgnoreColumns;

    /**
     * 添加表字段填充
     */
    private List<IFill> addTableFills;

    /**
     * 全局主键类型
     */
    private IdType idType;

    /**
     * 转换文件名称
     */
    private ConverterFileName convertFileName;

    /**
     * 格式化文件名称
     */
    private String formatFileName;

}
