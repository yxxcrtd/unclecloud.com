package com.unclecloud.controller;

import com.unclecloud.util.JsonResult;
import freemarker.template.Configuration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.unclecloud.util.FileUtil.generateFile;
import static com.unclecloud.util.JsonResult.jsonResultSuccess;

/**
 * Code Generate Controller
 */
@Slf4j
@RestController
@RequestMapping("code")
public class CodeController {

    @Autowired
    Configuration configuration;

    private @Value("${spring.datasource.driver-class-name}") String driverClassName;
    private @Value("${spring.datasource.url}") String url;
    private @Value("${spring.datasource.username}") String username;
    private @Value("${spring.datasource.password}") String password;
    private @Value("${code.folder.java}") String javaFolder;
    private @Value("${code.folder.templates}") String templatesFolder;

    /**
     * Generate Code
     */
    @GetMapping("{tableName}")
    JsonResult generate(@PathVariable String tableName) throws Exception {

        // 1，获取表的字段属性
        List<Map<String, String>> tableFieldList = new ArrayList<>();
        Connection connection = null;
        ResultSet resultSet = null;
        try {
            Class.forName(driverClassName);
            connection = DriverManager.getConnection(url, username, password);
            DatabaseMetaData databaseMetaData = connection.getMetaData();
            resultSet = databaseMetaData.getColumns(null, null, tableName, null);

            while (resultSet.next()) {
                Map<String, String> map = new LinkedHashMap<>();
                map.put("fieldType", getFieldType(resultSet.getString("TYPE_NAME")));
                map.put("fieldName", getFieldName(resultSet.getString("COLUMN_NAME")));
                tableFieldList.add(map);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != connection) {
                    connection.close();
                }
                if (null != resultSet) {
                    resultSet.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // 2，准备模版数据
        Map<String, Object> map = new HashMap<>();
        map.put("tableName", tableName);
        map.put("fields", tableFieldList);
        String className = getClassName(tableName);
        map.put("className", className);

        // 生成 domain
        generateFile(configuration, map, "code/Domain.html", javaFolder + "domain" + File.separator + className + ".java");
        System.out.println("\nDomain: " + className + ".java 生成成功！");

        // 生成 controller
        generateFile(configuration, map, "code/Controller.html", javaFolder + "controller" + File.separator + className + "Controller.java");
        System.out.println("\nController: " + className + "Controller.java 生成成功！");

        // 生成 service
        generateFile(configuration, map, "code/Service.html", javaFolder + "service" + File.separator + className + "Service.java");
        System.out.println("\nService: " + className + "Service.java 生成成功！");

        // 生成 serviceImpl
        generateFile(configuration, map, "code/ServiceImpl.html", javaFolder + "service/impl" + File.separator + className + "ServiceImpl.java");
        System.out.println("\nServiceImpl: " + className + "ServiceImpl.java 生成成功！");

        // 生成 repository
        generateFile(configuration, map, "code/Repository.html", javaFolder + "repository" + File.separator + className + "Repository.java");
        System.out.println("\nRepository: " + className + "Repository.java 生成成功！");

        // 生成 Template List
        generateFile(configuration, map, "code/List.html", templatesFolder + StringUtils.uncapitalize(className) + File.separator + className + "List.html");
        System.out.println("\nTemplate List: " + className + "List.html 生成成功！");

        // 生成 Template Edit
        generateFile(configuration, map, "code/Edit.html", templatesFolder + StringUtils.uncapitalize(className) + File.separator + className + "Edit.html");
        System.out.println("\nTemplate Edit: " + className + "Edit.html 生成成功！");

        return jsonResultSuccess("SUCCESS", null);
    }

    /**
     * 根据表字段获取字段名称
     *
     * @param field 字段名称
     * @return
     */
    private static String getFieldName(String field) {
        String result = "";
        String[] fields = field.toLowerCase().split("_");
        result += fields[0];
        if (1 < fields.length) {
            for (int i = 1; i < fields.length; i++) {
                result += fields[i].substring(0, 1).toUpperCase() + fields[i].substring(1, fields[i].length());
            }
        }
        return result;
    }

    /**
     * 根据表字段类型生成对应的java的数据类型
     *
     * @param type 字段类型
     * @return
     */
    private static String getFieldType(String type) {
        String result = "String";
        if ("varchar".equals(type.toLowerCase()) || "text".equals(type.toLowerCase())) {
            result = "String";
        } else if ("int".equals(type.toLowerCase()) || "int2".equals(type.toLowerCase()) || "smallint".equals(type.toLowerCase()) || type.toLowerCase().startsWith("int")) {
            result = "Integer";
        } else if ("serial".equals(type.toLowerCase()) || "bigint".equals(type.toLowerCase()) || "int8".equals(type.toLowerCase()) || type.toLowerCase().startsWith("bigint") || type.toLowerCase().startsWith("int8")) {
            result = "Long";
        } else if ("timestamp with time zone".equals(type.toLowerCase()) || type.toLowerCase().startsWith("timestamp")) {
            result = "Date";
        } else if ("boolean".equals(type.toLowerCase())) {
            result = "Boolean";
        }
        return result;
    }

    /**
     * 根据表名生成类名
     *
     * @param tableName 表名
     */
    private static String getClassName(String tableName) {
        if (tableName.contains("_")) {
            int index = tableName.indexOf("_");
            return getClassName(tableName.substring(0, index) + StringUtils.capitalize(tableName.substring(index + 1, tableName.length())));
        }
        return StringUtils.capitalize(tableName.substring(1)); // 将首字母大写
    }

    // 测试首字母大小写
    public static void main(String[] args) {
        System.out.println(getClassName("t_instance"));
    }

}
