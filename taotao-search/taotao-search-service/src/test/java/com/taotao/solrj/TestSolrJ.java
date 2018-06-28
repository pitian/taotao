package com.taotao.solrj;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;

import java.util.List;
import java.util.Map;

public class TestSolrJ {
    @Test
    public void testAddDocument() throws Exception {
        //创建一个solr对象 HttpSolrServer  单机版
        //需要制定solr服务的url
        HttpSolrServer httpSolrServer = new HttpSolrServer("http://192.168.25.128:8081/solr/collection1");
        //创建一个文档对象 SolrInputDocument
        SolrInputDocument document = new SolrInputDocument();
        //向文档中添加域.必须有id域.域的名称必须在schema.xml中定义
        document.addField("id", "testField1");
        document.addField("item_title", "测试商品1");
        document.addField("item_price", 1000);
        //把文档对象写入索引库
        httpSolrServer.add(document);
        //提交
        httpSolrServer.commit();
    }

    @Test
    public void testDeleteDocumenById() throws Exception {
        HttpSolrServer solrServer = new HttpSolrServer("http://192.168.25.128:8081/solr/collection1");
        solrServer.deleteById("testField");
        solrServer.commit();
    }

    @Test
    public void testDeleteDocumenByQuery() throws Exception {
        HttpSolrServer solrServer = new HttpSolrServer("http://192.168.25.128:8081/solr/collection1");
        solrServer.deleteByQuery("id:testField1");
        solrServer.commit();
    }

    @Test
    public void searchDocument()throws Exception{
        //创建一个SolrServer 对象
        HttpSolrServer solrServer = new HttpSolrServer("http://192.168.25.128:8081/solr/collection1");
        //创建一个 SolrQuery 对象
        SolrQuery query = new SolrQuery();
        //设置查询条件,过滤条件,分页条件,排序条件,高亮
//        solrQuery.set("q","*:*");
        query.setQuery("手机");
        //分页
        query.setStart(0);
        query.setRows(10);
        //设置默认搜索域
        query.set("df","item_keywords");
        //设置高亮
        query.setHighlight(true);
        //高亮显示的域
        query.addHighlightField("item_title");
        query.setHighlightSimplePre("<em>");
        query.setHighlightSimplePost("</em>");
        //执行查询,得到一个Response 对象
        QueryResponse response =solrServer.query(query);
        //取查询结果
        SolrDocumentList solrDocumentList = response.getResults();
        //取查询结果总记录数
        System.out.println(response.getResults().getNumFound());
        for (SolrDocument solrDocument :solrDocumentList) {
            System.out.println(solrDocument.get("id"));
            Map<String, Map<String, List<String>>> responseHighlighting = response.getHighlighting();
            List<String> list = responseHighlighting.get(solrDocument.get("id")).get("item_title");
            String item_title = null;
            if(null!=list&&list.size()>0){
                item_title = list.get(0);
            }else{
                item_title = (String)solrDocument.get("item_title");
            }
            System.out.println(item_title);
            System.out.println(solrDocument.get("item_sell_point"));
            System.out.println(solrDocument.get("item_price"));
            System.out.println(solrDocument.get("item_image"));
            System.out.println(solrDocument.get("item_category_name"));

        }

    }
}
