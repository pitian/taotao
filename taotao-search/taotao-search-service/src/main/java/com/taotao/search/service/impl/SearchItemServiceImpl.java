package com.taotao.search.service.impl;

import com.taotao.search.mapper.SearchItemMapper;
import com.taotao.search.pojo.SearchItem;
import com.taotao.search.service.SearchItemService;
import common.pojo.TaotaoResult;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 商品导入到索引库
 */
@Service
public class SearchItemServiceImpl implements SearchItemService {
    @Autowired
    private SearchItemMapper searchItemMapper;
    @Autowired
    private HttpSolrServer solrServer;
    @Override
    public TaotaoResult importItemToIndex(){
        try {
            //1.查询所有商品数据
            List<SearchItem> searchItemList= searchItemMapper.getItemList();
            //2.遍历商品数据添加到索引库
            for (SearchItem searchItem:searchItemList) {
                //创建文档对象
                SolrInputDocument document = new SolrInputDocument();
                //向文档添加域
                document.addField("id",searchItem.getId());
                document.addField("item_title",searchItem.getTitle());
                document.addField("item_sell_point",searchItem.getSell_point());
                document.addField("price",searchItem.getPrice());
                document.addField("item_image",searchItem.getImage());
                document.addField("item_category_name",searchItem.getCategory_name());
                document.addField("item_desc",searchItem.getItem_desc());
                solrServer.add(document);
                String s = "a";
            }
            solrServer.commit();
        }catch (Exception e){
            e.printStackTrace();
            return TaotaoResult.build(500,"出错啦！");
        }


        return TaotaoResult.ok();
    }
}
