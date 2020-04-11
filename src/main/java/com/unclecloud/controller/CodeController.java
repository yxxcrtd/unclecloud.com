package com.unclecloud.controller;

import com.unclecloud.util.JsonResult;
import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
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

        // 2，生成表的 domain 类
        Map<String, Object> map = new HashMap<>();
        map.put("tableName", tableName);
        map.put("fields", tableFieldList);
        String className = getClassName(tableName);
        map.put("className", className);

        Template template = configuration.getTemplate("code/Domain.html");
        generateFile(configuration, map, "code/Domain.html", "code/" + className + ".java");

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
        } else if ("bigint".equals(type.toLowerCase()) || "int8".equals(type.toLowerCase()) || type.toLowerCase().startsWith("bigint") || type.toLowerCase().startsWith("int8")) {
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
     * @return
     */
    private static String getClassName(String tableName) {
        String result = "";
        String[] fields = tableName.toLowerCase().split("_");
        if (1 < fields.length) {
            for(int i = 0; i < fields.length; i++) {
                result += fields[i].substring(0, 1).toUpperCase() + fields[i].substring(1, fields[i].length());
            }
        }
        return result;
    }

}
