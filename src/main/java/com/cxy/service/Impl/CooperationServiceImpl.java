package com.cxy.service.Impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cxy.dao.NewsMapper;
import com.cxy.model.entity.News;
import com.cxy.model.entity.User;
import com.cxy.service.CooperationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;


import java.util.List;

/**
 * @author: Luo
 * @description:
 * @time: 2020/6/27 19:18
 * Modified By:
 */
@Service
public class CooperationServiceImpl implements CooperationService {

    @Autowired
    NewsMapper newsMapper;

    @Override
    public void createNews(JSONObject jsonObject, User user) {
        News news = jsonObject.toJavaObject(News.class);
        if (news.getNeedId() != null)
        news.setNeedId(trimFirstAndLastChar(news.getNeedId()));
        if (news.getHope() != null)
        news.setHope(trimFirstAndLastChar(news.getHope()));
        if (news.getMode() != null)
            news.setMode(trimFirstAndLastChar(news.getMode()));

        news = setAttributeTONews(news,user);
        newsMapper.insert(news);
    }

    @Override
    public List<News> findListNews(News news) {
        QueryWrapper queryWrapper = new QueryWrapper<>(news).or(news != null).like(!StringUtils.isEmpty(news.getTitle()),"title",news.getTitle()).orderByAsc("status").orderByDesc("_time");
        return newsMapper.selectList(queryWrapper);
    }

    @Override
    public News findNews(News news) {
        QueryWrapper queryWrapper = new QueryWrapper<>(news);
        return newsMapper.selectOne(queryWrapper);
    }

    private String trimFirstAndLastChar(String source) {
        return source.substring(1,source.length() -1);
    }

    private News setAttributeTONews(News news,User user ) {
        news.setSysId("1");
        news.setTime(System.currentTimeMillis() / 1000);
        news.setUid(user.getUid());
        news.setUidname(user.getUserName());
        return news;
    }
}
