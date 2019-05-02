package com.tju.consultationPlatform.controller;


import com.tju.consultationPlatform.util.ResourceUtil;
import com.tju.consultationPlatform.util.SpeechUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;

@Controller
public class ChatController {

    @Value("${web.base.path}")
    private String basePath;

    @RequestMapping(value = "/textToMp3", method = RequestMethod.POST)
    @ResponseBody
    public String textToMp3(String context) {
        String path = "uploads/textToMp3/";
        String name = ResourceUtil.getRandomFileName()+".mp3";
        File dic = new File(basePath+path);
        if (!dic.exists()) {
            // 先得到文件的上级目录，并创建上级目录，在创建文件
            dic.mkdirs();
        }
        if(SpeechUtil.SpeechSynthesizer(context,basePath+path+name)){
            return path;
        }

        else return "false";
    }
}
