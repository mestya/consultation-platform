package com.tju.consultationPlatform.util;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import org.apache.commons.lang.StringUtils;

/**
 * <p>Title: 获取资源</p>
 */
public class ResourceUtil {

    private static final Logger logger = LoggerFactory.getLogger(ResourceUtil.class);
    private static final SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
    private static final Random r = new Random();

    private ResourceUtil() {
    }


    public static String saveResource(MultipartFile files,String basePath,String uploadPath) throws IOException {
        //输入输出流
        FileOutputStream fileOutputStream = null;
        InputStream inputStream = null;

        try {
            if (files != null) {
                //获取文件原本名
                String fileName = files.getOriginalFilename();
                if (StringUtils.isNotBlank(fileName)) {
                    // 获取不重复的随机名
                    String realFileName = getRandomFileName();
                    // 获取文件的扩展名
                    String extension = fileName.substring(fileName.lastIndexOf("."));
                    //生成新的文件名
                    fileName = realFileName + extension;

                    //生成最终路径
                    String fileFinallyPath = basePath+uploadPath + fileName;
                    //生成相对路径，存储与数据库
                    uploadPath = uploadPath + fileName;
                    logger.info("最后路径："+fileFinallyPath);
                    File outputFile = new File(fileFinallyPath);

                    if (!outputFile.exists()) {
                        // 先得到文件的上级目录，并创建上级目录，在创建文件
                        outputFile.getParentFile().mkdirs();
                        outputFile.createNewFile();
                    }
                     logger.info(outputFile.getAbsolutePath());

                    fileOutputStream = new FileOutputStream(outputFile);
                    inputStream = files.getInputStream();
                    IOUtils.copy(inputStream, fileOutputStream);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //文件操作结束时关闭文件
            if (fileOutputStream != null) {
                fileOutputStream.flush();
                fileOutputStream.close();
            }
        }
        return uploadPath;
    }


    /**
     * 生成随机文件名，当前年月日小时分钟秒钟+五位随机数
     */
    public static String getRandomFileName() {
        // 获取随机的五位数
        int rannum = r.nextInt(89999) + 10000;
        String nowTimeStr = sDateFormat.format(new Date());
        return nowTimeStr + rannum;
    }


    public static File getResource(String resourceUrl) {
        if (resourceUrl == null) return(null);
        File file = null ;
        try {
            //通过类加载器加载声音资源，作为声音对象的参数
            file = ResourceUtils.getFile("classpath:static/"+resourceUrl);
        } catch ( Exception ex ) {
            logger.info("### Exception in getResource() : \r\n" + resourceUrl+ " " + ex.toString());
        }
        return file;
    }
}
