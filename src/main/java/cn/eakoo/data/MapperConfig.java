package cn.eakoo.data;

import com.baomidou.mybatisplus.generator.function.ConverterFileName;
import lombok.Data;
import org.apache.ibatis.cache.Cache;

/**
 * @author rui.zhou
 * @date 2021/11/21 20:29
 **/
@Data
public class MapperConfig {

    /**
    * 父类
    */
    private String superClassName;

    /**
     * 开启 @Mapper 注解
     */
    private boolean enableMapperAnnotation;

    /**
     * 启用 BaseResultMap 生成
     */
    private boolean enableBaseResultMap;

    /**
     * 启用 BaseColumnList
     */
    private boolean enableBaseColumnList;

    /**
     * 设置缓存实现类
     */
    private Class<? extends Cache> cache;

    /**
     * 转换 mapper 类文件名称
     */
    private ConverterFileName convertMapperFileName;

    /**
     * 转换 xml 文件名称
     */
    private ConverterFileName convertXmlFileName;

    /**
     * 格式化 mapper 文件名称
     */
    private String formatMapperFileName;

    /**
     * 格式化 xml 实现类文件名称
     */
    private String formatXmlFileName;


}
