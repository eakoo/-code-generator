package cn.eakoo.data;

import com.baomidou.mybatisplus.generator.function.ConverterFileName;
import lombok.Data;

/**
 * @author rui.zhou
 * @date 2021/11/21 20:04
 **/
@Data
public class ControllerConfig {

    /**
    * 父类
    */
    private String superClassName;

    /**
     * 开启驼峰转连字符
     * 默认值:false
     */
    private boolean enableHyphen;

    /**
     * 开启生成@RestController
     * 默认值:false
     */
    private boolean enableRest;

    /**
     * 转换文件名称
     */
    private ConverterFileName convertFileName;

    /**
     * 格式化文件名称
     */
    private String formatFileName;

}
