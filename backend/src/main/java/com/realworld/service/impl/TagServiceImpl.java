package com.realworld.service.impl;

import com.realworld.entity.Tag;
import com.realworld.mapper.TagMapper;
import com.realworld.service.TagService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagServiceImpl implements TagService {

    private final TagMapper tagMapper;

    public TagServiceImpl(TagMapper tagMapper) {
        this.tagMapper = tagMapper;
    }

    @Override
    public List<String> getAllTags() {
        return tagMapper.selectList(null).stream()
                .map(Tag::getName)
                .toList();
    }
}
