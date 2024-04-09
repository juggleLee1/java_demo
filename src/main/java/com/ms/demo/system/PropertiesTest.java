package com.ms.demo.system;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * ClassName: PropertiesTest
 * Package: com.ms.demo.system
 * Description:
 *
 * @author Lee
 * @version 1.0
 * @create 2024/4/9 18:56
 */
public class PropertiesTest {
    public static void main(String[] args) throws IOException {
        Properties properties = new Properties();
        // maven项目会将resources目录下的配置文件编译到classes目录下，getResourceAsStream默认的根路径就是classes
        InputStream resourceAsStream = PropertiesTest.class.getClassLoader().getResourceAsStream("jdbc.properties");
        properties.load(resourceAsStream);

        System.out.println(properties.get("user"));
    }
}
