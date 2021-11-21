package cn.eakoo.data;

import com.baomidou.mybatisplus.generator.config.rules.DateType;
import lombok.Data;

/**
 * @author rui.zhou
 * @date 2021/11/19 12:11
 **/
@Data
public class GlobalConfig {

    /**
    * 作者
    */
    @Deprecated
    private String author;

    /**
     * 注释格式
     * 默认值: yyyy-MM-dd
     */
    private String commentDate;

    /**
     * 日期类型
     * DateType.ONLY_DATE 默认值: DateType.TIME_PACK
     */
    private DateType dateType;

    /**
     * 开启 kotlin 模式
     * 默认值:false
     */
    private boolean enableKotlin;

    /**
     * 开启 Swagger 模式
     * 默认值:false
     */
    private boolean enableSwagger;

    /**
     * 覆盖文件
     * 默认值:false
     */
    private boolean fileOverride;

    /**
     * 禁止打开输出目录
     * 默认值:true
     */
    private boolean disableOpenDir;

    /**
     * 代码输出目录
     */
    @Deprecated
    private String outputDir;

}
