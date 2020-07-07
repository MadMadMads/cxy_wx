package com.cxy.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cxy.model.entity.News;

import java.util.List;

/**
 *
 *@author: Luo
 *@description: 
 *@time: 2020/7/2 22:10
 *Modified By:
 * 
 */
public interface NewsMapper extends BaseMapper<News> {
    public List<News> listNews(News news);
}