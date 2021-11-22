package cn.eakoo.data;

import com.baomidou.mybatisplus.generator.config.po.LikeTable;
import lombok.Data;

/**
 * @author rui.zhou
 * @date 2021/11/21 20:54
 **/
@Data
public class StrategyConfig {
    
    /**
    * 开启大写命名
    * 默认值:false
    */
    private boolean enableCapitalMode;

    /**
     * 开启跳过视图
     * 默认值:false
     */
    private boolean enableSkipView;

    /**
     * 禁用 sql 过滤
     * 默认值:true，语法不能支持使用 sql 过滤表的话，可以考虑关闭此开关
     */
    private boolean disableSqlFilter;

    /**
     * 启用 schema
     * 默认值:false，多 schema 场景的时候打开
     */
    private boolean enableSchema;

    /**
     * 模糊表匹配(sql 过滤)
     * likeTable 与 notLikeTable 只能配置一项
     */
    private LikeTable likeTable;

    /**
     * 模糊表排除(sql 过滤)
     * likeTable 与 notLikeTable 只能配置一项
     */
    private LikeTable notLikeTable;

    /**
     * 增加表匹配(内存过滤)
     * include 与 exclude 只能配置一项
     */
    private String[] addInclude;

    /**
     * 增加表排除匹配(内存过滤)
     * include 与 exclude 只能配置一项
     */
    private String[] addExclude;

    /**
     * 增加过滤表前缀
     */
    private String[] addTablePrefix;

    /**
     * 增加过滤表后缀
     */
    private String[] addTableSuffix;

    /**
     * 增加过滤字段前缀
     */
    private String[] addFieldPrefix;

    /**
     * 增加过滤字段后缀
     */
    private String[] addFieldSuffix;

    /**
     * Entity策略配置
     */
    private EntityConfig entityConfig;

    /**
     * Mapper策略配置
     */
    private MapperConfig mapperConfig;

    /**
     * Service策略配置
     */
    private ServiceConfig serviceConfig;

    /**
     * Controller策略配置
     */
    private ControllerConfig controllerConfig;

}
