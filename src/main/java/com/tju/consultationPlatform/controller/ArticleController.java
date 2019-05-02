package com.tju.consultationPlatform.controller;

import com.tju.consultationPlatform.domain.Article;
import com.tju.consultationPlatform.domain.JsonResult;
import com.tju.consultationPlatform.service.ArticleService;
import com.tju.consultationPlatform.util.ArticalCrawler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@CrossOrigin
public class ArticleController {
    private static final Logger logger = LoggerFactory.getLogger(ArticleController.class);
    @Autowired
    private ArticleService articleService;
    @Value("${web.base.path}")
    private String basePath;
    @RequestMapping(value = "/uploadArticle",method = RequestMethod.POST)
    @ResponseBody
    JsonResult uploadArticle(MultipartFile file,String headline,String user){
        try {
            return new JsonResult(1,"success",articleService.uploadArticle(user,headline,file));
        }catch (Exception e){
            return new JsonResult(-1,e.getMessage(),null);
        }
    }

    @RequestMapping(value = "/crawlerArticle")
    @ResponseBody
    void crawlerArticle(String url){
       List<Article> articleList = ArticalCrawler.getArticleListFromUrl(url,basePath);
       for(Article article:articleList){
           articleService.saveArticle(article);
       }
    }

    @RequestMapping(value = "/getArticles",method = RequestMethod.GET)
    @ResponseBody
    List<Article> getArticles(int num){
        return articleService.getArticle(0,num);
    }

}
