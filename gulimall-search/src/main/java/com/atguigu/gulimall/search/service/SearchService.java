package com.atguigu.gulimall.search.service;

import com.atguigu.gulimall.search.vo.SearchParam;
import com.atguigu.gulimall.search.vo.SearchResponse;

/**
 * @author heyijieyou
 * @date 2019-08-13 23:12
 */
public interface SearchService {

    SearchResponse search(SearchParam param);
}
