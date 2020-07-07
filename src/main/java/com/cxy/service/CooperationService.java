package com.cxy.service;

import com.alibaba.fastjson.JSONObject;
import com.cxy.model.entity.News;
import com.cxy.model.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author: Luo
 * @description:
 * @time: 2020/6/27 19:18
 * Modified By:
 */
@Service
public interface CooperationService {
    void createNews(JSONObject jsonObject, User user);

    List<News> findListNews(News news);

    News findNews(News news);
}
