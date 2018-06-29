package com.taotao.solrj;

import org.apache.solr.client.solrj.impl.CloudSolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;

public class TestSolrCloud {
    @Test
    public void testSolrCloudAddDocument()throws Exception{
        //
        CloudSolrServer solrServer  =new CloudSolrServer("192.168.25.128:2181,192.168.25.128:2182,192.168.25.128:2183");
        solrServer.setDefaultCollection("collection2");

        SolrInputDocument document = new SolrInputDocument();
        document.addField("item_title", "测试商品");
        document.addField("item_price", "100");
        document.addField("id", "test001");
        solrServer.add(document);
        solrServer.commit();
    }
}
