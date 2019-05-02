package com.tju.consultationPlatform.service;


import com.tju.consultationPlatform.domain.Article;
import com.tju.consultationPlatform.repository.ArticleRepository;
import com.tju.consultationPlatform.util.ResourceUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class ArticleService {

    @Autowired
    private ArticleRepository articleRepository;
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
    @Value("${web.base.path}")
    private String basePath;

    public Article uploadArticle(String user,String headline,MultipartFile file)throws Exception{
        String uploadPath = "/uploads/articles/";
        String contentUrl = ResourceUtil.saveResource(file,basePath,uploadPath);

        Article article = new Article();
        article.setHeadline(headline);
        article.setAuthor(user);
        article.setContent_url(contentUrl);
        article.setCreateTime(df.format(new Date()));
        article = articleRepository.save(article);
        if(article==null){
            throw new Exception("上传文章失败");
        }else {
            return article;
        }
    }

    public List<Article> getArticle(int pageNum,int pageSize){
        PageRequest pageRequest = PageRequest.of(pageNum,pageSize);
        Page<Article> articles = articleRepository.getArticle(pageRequest);
        List<Article> articleList = articles.getContent();
        return articleList;
    }

    /**
     * 保存文章到本地
     */
    public void saveArticle(Article article) {
         articleRepository.save(article);
    }
}
