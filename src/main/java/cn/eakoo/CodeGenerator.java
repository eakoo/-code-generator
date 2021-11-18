package cn.eakoo;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.TemplateType;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.util.Collections;

/**
 * @author rui.zhou
 * @date 2021/11/17 11:33
 **/
public class CodeGenerator {

    private static final String USER_NAME = "ncm_uat";
    private static final String PASS_WORD = "ncm_uat123";
    private static final String URL = "jdbc:oracle:thin:@10.142.141.197:1521:dborgbk01";

    public static void main(String[] args) {

        FastAutoGenerator.create(URL, USER_NAME, PASS_WORD)
                .globalConfig(builder -> {
                    builder.author("rui.zhou") // 设置作者
                            .commentDate("yyyy-MM-dd")
                            .dateType(DateType.TIME_PACK)
                            .enableSwagger() // 开启 swagger 模式
                            .fileOverride() // 覆盖已生成文件
                            .outputDir("D://CodeGenerator"); // 指定输出目录
                })
                .packageConfig(builder -> {
                    builder.parent("cn.eakoo") // 设置父包名
                            .moduleName("system") // 设置父包模块名
                            .pathInfo(Collections.singletonMap(OutputFile.mapperXml, "D://CodeGenerator")); // 设置mapperXml生成路径
                })
                .strategyConfig(builder -> {
                    builder.addInclude("LCCONT") // 设置需要生成的表名
                            .addTablePrefix("t_", "c_"); // 设置过滤表前缀
                })
                .templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .execute();
    }

}
