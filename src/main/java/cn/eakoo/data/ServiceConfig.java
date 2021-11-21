package cn.eakoo.data;

import com.baomidou.mybatisplus.generator.function.ConverterFileName;
import lombok.Data;

/**
 * @author rui.zhou
 * @date 2021/11/21 20:16
 **/
@Data
public class ServiceConfig {
    
    /**
    * service 接口父类
    */
    private String superServiceClass;

    /**
     * service 实现类父类
     */
    private String superServiceImplClass;

    /**
     * service 接口文件名称
     */
    private ConverterFileName convertServiceFileName;

    /**
     * service 实现类文件名称
     */
    private ConverterFileName convertServiceImplFileName;

    /**
     * 格式化 service 接口文件名称
     */
    private String formatServiceFileName;

    /**
     * 格式化 service 实现类文件名称
     */
    private String formatServiceImplFileName;

}
