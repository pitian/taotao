package com.taotao.search.dao;

import com.taotao.search.pojo.SearchItem;
import com.taotao.search.pojo.SearchResult;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class SearchDao {
    @Autowired
    private SolrServer solrServer;
    public SearchResult search(SolrQuery query)throws Exception{
        QueryResponse response = solrServer.query(query);
        SolrDocumentList solrDocumentList = response.getResults();
        List<SearchItem> searchItemList = new ArrayList<>();
        for (SolrDocument solrDocument:solrDocumentList) {
            SearchItem searchItem = new SearchItem();
            searchItem.setCategory_name((String) solrDocument.get("item_category_name"));
            searchItem.setId((String) solrDocument.get("id"));
            searchItem.setImage((String) solrDocument.get("item_image"));
            searchItem.setSell_point((String) solrDocument.get("item_sell_point"));
            Map<String, Map<String, List<String>>> highlighting = response.getHighlighting();
            List<String> list = highlighting.get(solrDocument.get("id")).get("item_title");
            String item_title = "";
            if(null!=list&&list.size()>0){
                item_title=list.get(0);
            }else{
                item_title = (String) solrDocument.get("item_title");
            }
            searchItem.setTitle(item_title);
            searchItemList.add(searchItem);
        }
        SearchResult result = new SearchResult();
        result.setSearchItemList(searchItemList);
        result.setRecordCount(response.getResults().getNumFound());
        return result;
    }
}
