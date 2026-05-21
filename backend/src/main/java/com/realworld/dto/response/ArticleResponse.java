package com.realworld.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ArticleResponse {
    private ArticleVO article;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class ArticleVO {
        private String slug;
        private String title;
        private String description;
        private String body;
        private List<String> tagList;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private boolean favorited;
        private int favoritesCount;
        private ProfileResponse.ProfileVO author;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MultipleArticles {
        private List<ArticleVO> articles;
        private long articlesCount;
    }
}
