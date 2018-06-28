package com.taotao.search.mapper;

import com.taotao.search.pojo.SearchItem;

import java.util.List;

public interface SearchItemMapper {
    public List<SearchItem> getItemList() throws Exception;
}

