package com.tju.consultationPlatform.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.tju.consultationPlatform.domain.Article;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Whitelist;
import org.jsoup.select.Elements;

public class ArticalCrawler {
    //private static String url = "https://new.qq.com/ch2/yangsheng";

    // 获取文章列表
    public static List<Article> getArticleListFromUrl(String crawlerUrl, String storeBaseUrl) {
     Document doc = null;
     List<Article> articleList = new ArrayList<>();
     try {
        doc = Jsoup.connect(crawlerUrl)
                 .header("Accept-Language","zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3")
                 .header("Content-Type", "text/html;charset=UTF-8")
                 .header("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.103 Safari/537.36")
                 .maxBodySize(0)
                 .timeout(10000).get();

     } catch (IOException e) {
         e.printStackTrace();
     }
    Elements elements = doc.getElementById("content").getElementsByTag("a");//找到所有a标签
        if(elements==null||elements.size()==0){
            System.out.println("根本没获取到啊，笨蛋");
        }
        for (Element element : elements) {
            final String relHref = element.attr("href"); // == "/"这个是href的属性值，一般都是链接。这里放的是文章的连接
            System.out.println("http://www.laonian100.com"+relHref);
         //用if语句过滤掉不是文章链接的内容。因为文章的链接有两个，但评论的链接只有一个，反正指向相同的页面就拿评论的链接来用吧
                articleList.add(getArticleFromUrl("http://www.laonian100.com"+relHref,storeBaseUrl));//可以通过这个url获取文章了
        }
        return articleList;
    }
    //获取文章内容
    public static Article getArticleFromUrl(String detailurl,String basePath) {
        try {
            Document document = Jsoup.connect(detailurl).userAgent("Mozilla/5.0").timeout(3000).post();
            Element elementTitle = document.getElementsByTag("title").first();//标题。
            System.out.println(elementTitle.text());
            String filename = elementTitle.text();
            Element authorElement = document.getElementById("webcopy").getElementsByTag("li").first();
            String author = authorElement.text();
            Element elementContent = document.getElementById("content");//内容。
            System.out.println(elementContent.text());
            System.out.println(br2nl(elementContent.html()));
            return saveArticle(filename,author,br2nl(elementContent.html()),basePath);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }
    public static String br2nl(String html){
        if(html==null) return html;
        Document document = Jsoup.parse(html);
        document.outputSettings(new Document.OutputSettings().prettyPrint(false));//makes html() preservelinebreaks and spacing
        document.select("br").append("\\n");
        document.select("p").prepend("\\n");
        String s = document.html().replaceAll("\\\\n", "\n");
        return Jsoup.clean(s, "", Whitelist.none(), new Document.OutputSettings().prettyPrint(false));
    }
    //保存文章到本地
    public static Article saveArticle(String headline, String author,String content, String basePath) {
        String uploadPath = "/uploads/articles/"+ResourceUtil.getRandomFileName()+".txt";
        //String lujing = "d:\\MyLoadArticle\\" + blogName + "\\" + titile + ".txt";//保存到本地的路径和文件名
        File file = new File(basePath+uploadPath);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
         //把文本读入文件
        try {
            FileWriter fw = new FileWriter(file, true);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(content);
            bw.flush();
            bw.close();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        Article article = new Article();
        article.setAuthor(author);
        article.setHeadline(headline);
        article.setContent_url(uploadPath);
        article.setCreateTime(df.format(new Date()));
        return article;
    }
}
