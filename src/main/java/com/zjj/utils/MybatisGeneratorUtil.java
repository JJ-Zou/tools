package com.zjj.utils;

import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.exception.InvalidConfigurationException;
import org.mybatis.generator.exception.XMLParserException;
import org.mybatis.generator.internal.DefaultShellCallback;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MybatisGeneratorUtil {
    public static void main(String[] args) {
        File file = new File("src/main/resources/mybatis/generatorConfig.xml");
        List<String> warnings = new ArrayList<>();
        try {
            MyBatisGenerator myBatisGenerator = new MyBatisGenerator(
                    (new ConfigurationParser(warnings)).parseConfiguration(file),
                    new DefaultShellCallback(true),
                    warnings
            );
            myBatisGenerator.generate(null);
        } catch (SQLException | IOException | InterruptedException | InvalidConfigurationException | XMLParserException throwables) {
            throwables.printStackTrace();
        }
    }
}
