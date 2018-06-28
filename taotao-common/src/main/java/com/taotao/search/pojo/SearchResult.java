package com.taotao.search.pojo;

import java.util.List;

public class SearchResult {
    public int totalPage;
    public List<SearchItem> searchItemList;

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public List<SearchItem> getSearchItemList() {
        return searchItemList;
    }

    public void setSearchItemList(List<SearchItem> searchItemList) {
        this.searchItemList = searchItemList;
    }
}
