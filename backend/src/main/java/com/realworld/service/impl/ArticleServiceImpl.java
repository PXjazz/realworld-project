package com.realworld.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.realworld.dto.response.ArticleResponse;
import com.realworld.dto.response.ProfileResponse;
import com.realworld.entity.*;
import com.realworld.mapper.*;
import com.realworld.service.ArticleService;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ArticleServiceImpl implements ArticleService {

    private final ArticleMapper articleMapper;
    private final ArticleTagMapper articleTagMapper;
    private final TagMapper tagMapper;
    private final UserMapper userMapper;

    public ArticleServiceImpl(ArticleMapper articleMapper, ArticleTagMapper articleTagMapper,
                              TagMapper tagMapper, UserMapper userMapper) {
        this.articleMapper = articleMapper;
        this.articleTagMapper = articleTagMapper;
        this.tagMapper = tagMapper;
        this.userMapper = userMapper;
    }

    @Override
    public ArticleResponse.MultipleArticles findArticles(String tag, String author, int limit, int offset) {
        QueryWrapper<Article> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("created_at");

        if (author != null && !author.isEmpty()) {
            User authorUser = userMapper.selectOne(new QueryWrapper<User>().eq("username", author));
            if (authorUser == null) {
                return new ArticleResponse.MultipleArticles(List.of(), 0);
            }
            wrapper.eq("author_id", authorUser.getId());
        }

        if (tag != null && !tag.isEmpty()) {
            Tag tagEntity = tagMapper.selectOne(new QueryWrapper<Tag>().eq("name", tag));
            if (tagEntity == null) {
                return new ArticleResponse.MultipleArticles(List.of(), 0);
            }
            List<Long> articleIds = articleTagMapper.selectList(
                    new QueryWrapper<ArticleTag>().eq("tag_id", tagEntity.getId())
            ).stream().map(ArticleTag::getArticleId).toList();
            if (articleIds.isEmpty()) {
                return new ArticleResponse.MultipleArticles(List.of(), 0);
            }
            wrapper.in("id", articleIds);
        }

        long total = articleMapper.selectCount(wrapper);

        Page<Article> page = new Page<>(offset / limit + 1, limit);
        Page<Article> result = articleMapper.selectPage(page, wrapper);
        List<Article> articleList = result.getRecords();

        // Batch load tags
        Map<Long, List<String>> tagsByArticleId = new HashMap<>();
        if (!articleList.isEmpty()) {
            List<Long> articleIds = articleList.stream().map(Article::getId).toList();
            List<ArticleTag> articleTags = articleTagMapper.selectList(
                    new QueryWrapper<ArticleTag>().in("article_id", articleIds)
            );
            if (!articleTags.isEmpty()) {
                List<Long> tagIds = articleTags.stream().map(ArticleTag::getTagId).distinct().toList();
                Map<Long, String> tagNameMap = tagMapper.selectList(
                        new QueryWrapper<Tag>().in("id", tagIds)
                ).stream().collect(Collectors.toMap(Tag::getId, Tag::getName));

                for (ArticleTag at : articleTags) {
                    tagsByArticleId.computeIfAbsent(at.getArticleId(), k -> new ArrayList<>())
                            .add(tagNameMap.get(at.getTagId()));
                }
            }
        }

        // Batch load authors
        Map<Long, User> authorsById = new HashMap<>();
        if (!articleList.isEmpty()) {
            List<Long> authorIds = articleList.stream().map(Article::getAuthorId).distinct().toList();
            List<User> authors = userMapper.selectList(
                    new QueryWrapper<User>().in("id", authorIds)
            );
            authorsById = authors.stream().collect(Collectors.toMap(User::getId, u -> u));
        }

        List<ArticleResponse.ArticleVO> articles = new ArrayList<>();
        for (Article article : articleList) {
            List<String> tagList = tagsByArticleId.getOrDefault(article.getId(), List.of());
            User authorUser = authorsById.get(article.getAuthorId());

            ProfileResponse.ProfileVO authorProfile = new ProfileResponse.ProfileVO();
            if (authorUser != null) {
                authorProfile.setUsername(authorUser.getUsername());
                authorProfile.setBio(authorUser.getBio());
                authorProfile.setImage(authorUser.getImage());
            }
            authorProfile.setFollowing(false);

            ArticleResponse.ArticleVO vo = new ArticleResponse.ArticleVO();
            vo.setSlug(article.getSlug());
            vo.setTitle(article.getTitle());
            vo.setDescription(article.getDescription());
            vo.setBody(article.getBody());
            vo.setTagList(tagList);
            vo.setCreatedAt(article.getCreatedAt());
            vo.setUpdatedAt(article.getUpdatedAt());
            vo.setFavorited(false);
            vo.setFavoritesCount(0);
            vo.setAuthor(authorProfile);
            articles.add(vo);
        }

        return new ArticleResponse.MultipleArticles(articles, total);
    }
}
