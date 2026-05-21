package com.realworld.service;

import com.realworld.dto.response.ArticleResponse;

public interface ArticleService {
    ArticleResponse.MultipleArticles findArticles(String tag, String author, int limit, int offset);
}
