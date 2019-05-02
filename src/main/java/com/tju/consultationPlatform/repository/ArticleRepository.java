package com.tju.consultationPlatform.repository;

import com.tju.consultationPlatform.domain.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ArticleRepository extends PagingAndSortingRepository<Article, Integer>,
        JpaSpecificationExecutor<Article> {

    @Query("SELECT a FROM Article a ORDER BY a.createTime DESC")
    Page<Article> getArticle(Pageable pageable);

}