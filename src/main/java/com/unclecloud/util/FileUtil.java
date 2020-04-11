package com.unclecloud.util;

import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Date;
import java.util.Map;

/**
 * 文件工具类
 */
public class FileUtil {

    /**
     * 文件上传
     *
     * @param file 上传的文件对象
     * @param uploadFolder 上传的文件夹
     * @return 返回上传后的文件名
     * @throws Exception
     */
    public static String uploadFile(MultipartFile file, String uploadFolder) throws Exception {
        // 上传的原始文件名
        String originalName = file.getOriginalFilename();

        // 生成的文件名
        StringBuffer fileName = new StringBuffer(String.format("%1$tY%1$tm%1$td%1$tH%1$tM%1$tS%1$tL" + originalName.substring(originalName.lastIndexOf(".")).toLowerCase(), new Date()));

        // 上传后的文件全路径
        String finalFilePath = new StringBuffer(uploadFolder).append(fileName).toString();

        // 转化成文件对象
        File finalFilePathFile = new File(finalFilePath);

        // 如果文件的父目录文件夹不存在，则创建文件夹
        if (!finalFilePathFile.getParentFile().exists()) {
            if (!finalFilePathFile.getParentFile().mkdirs()) {
                return "";
            }
        }

        // 上传
        file.transferTo(finalFilePathFile);

        return fileName.toString();
    }

    /**
     * 生成静态的html文件
     *
     * @param configuration
     * @param map 数据
     * @param templateFile 模版
     * @param targetHtml 生成后的html文件
     * @throws Exception
     */
    public static void generateHTML(Configuration configuration, Map<String, Object> map, String templateFile, String targetHtml) throws Exception {
        Template template = configuration.getTemplate(templateFile);
        File pathFile = new File(targetHtml.substring(0, targetHtml.lastIndexOf(File.separator)));
        if (!pathFile.exists()) {
            pathFile.mkdir();
        }
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(targetHtml));
        File htmlFile = new File(targetHtml);
        Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(htmlFile), "UTF-8"));
        template.process(map, out);
        bufferedWriter.close();
        out.flush();
        out.close();
    }

}
