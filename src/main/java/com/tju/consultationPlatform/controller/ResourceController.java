package com.tju.consultationPlatform.controller;

import com.tju.consultationPlatform.domain.JsonResult;
import com.tju.consultationPlatform.service.UserService;
import com.tju.consultationPlatform.util.ResourceUtil;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;

@Controller
@CrossOrigin
public class ResourceController {
    private static final Logger logger = LoggerFactory.getLogger(ResourceController.class);
    @Value("${web.base.path}")
    private String basePath;
    @Resource
    private UserService userService;

    @RequestMapping(value = "/uploadAvatar", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult uploadFaceUrl(String userId, @RequestParam("file") MultipartFile file )throws Exception{
        //图片存放根路径
        if(StringUtils.isBlank(userId)){
            return new JsonResult(-1,"error","用户Id不能为空...");
        }
        String uploadPathDB=null;
        String uploadPath ="/uploads/images/"+userId+"/avatar/";
        logger.info("上传地址："+uploadPath);
        try {
            uploadPathDB=ResourceUtil.saveResource(file,basePath,uploadPath);
        }catch (Exception e){
            return new JsonResult(-1,"error",e.getMessage());
        }

        logger.info(uploadPathDB);
        //更新用户中头像地址
        try {
            return new JsonResult(1,"success",userService.updateAvatar(userId, uploadPathDB));
        }catch (Exception e){
            return new JsonResult(-1,"error",e.getMessage());
        }

    }

    @RequestMapping(value = "/uploadAudio", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult uploadAudio(String from, String to,@RequestParam("file") MultipartFile file )throws Exception{

        if(StringUtils.isBlank(from)||StringUtils.isBlank(to)){
            return new JsonResult(-1,"error","用户Id不能为空...");
        }
        String uploadResultPath=null;
        String uploadPath ="/uploads/audio/"+from+"/";
        logger.info("上传地址："+uploadPath);
        try {
            uploadResultPath=ResourceUtil.saveResource(file,basePath,uploadPath);
        }catch (Exception e){
            return new JsonResult(-1,"error",e.getMessage());
        }

        logger.info(uploadResultPath);
        return new JsonResult(1,"success",uploadResultPath);

    }

    @RequestMapping(value = "/getSound", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult getSound(String soundUrl,HttpServletResponse response)throws Exception{

        logger.info("下载地址："+soundUrl);
        try {
            File clip = ResourceUtil.getResource(soundUrl);
            InputStream in = new FileInputStream(clip);
            byte[] bytes = IOUtils.toByteArray(in);
            response.setContentType("audio/mp3");
            OutputStream out = response.getOutputStream();
            out.write(bytes);
            out.flush();
            out.close();
            return null;
        }catch (Exception e){
            return new JsonResult(-1,"error",e.getMessage());
        }
    }

    @RequestMapping(value = "/getTxt", method = RequestMethod.GET)
    @ResponseBody
    public JsonResult getTxt(String soundUrl,HttpServletResponse response)throws Exception{

        logger.info("下载地址："+soundUrl);
        try {
            File clip = ResourceUtil.getResource(soundUrl);
            InputStream in = new FileInputStream(clip);
            byte[] bytes = IOUtils.toByteArray(in);
            response.setContentType("text/plain");
            response.setCharacterEncoding("utf-8");
            OutputStream out = response.getOutputStream();
            out.write(bytes);
            out.flush();
            out.close();
            return null;
        }catch (Exception e){
            return new JsonResult(-1,"error",e.getMessage());
        }
    }
}
