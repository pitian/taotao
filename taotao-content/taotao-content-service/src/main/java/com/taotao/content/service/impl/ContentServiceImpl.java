package com.taotao.content.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.content.service.ContentService;
import com.taotao.jedis.JedisClient;
import com.taotao.mapper.TbContentMapper;
import com.taotao.pojo.TbContent;
import com.taotao.pojo.TbContentExample;
import com.taotao.pojo.TbItem;
import common.pojo.EasyUIDataGrideResult;
import common.pojo.EasyUITreeNode;
import common.pojo.TaotaoResult;
import common.utils.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ContentServiceImpl implements ContentService{
    @Autowired
    private TbContentMapper tbContentMapper;

    @Autowired
    private JedisClient jedisClient;

    @Value("${INDEX_CONTENT}")
    private String INDEX_CONTENT;

    @Override
    public EasyUIDataGrideResult getContentList(Long categoryId,int page, int rows) throws Exception {
        PageHelper.startPage(page,rows);
        TbContentExample tbContentExample = new TbContentExample();
        TbContentExample.Criteria criteria = tbContentExample.createCriteria();
        criteria.andCategoryIdEqualTo(categoryId);
        List<TbContent> tbContents = tbContentMapper.selectByExample(tbContentExample);
        PageInfo<TbItem> pageInfo = new PageInfo(tbContents);
        EasyUIDataGrideResult result = new EasyUIDataGrideResult();
        result.setRows(tbContents);
        result.setTotal(pageInfo.getTotal());
        return result;
    }

    @Override
    public TaotaoResult addContent(TbContent tbContent) throws Exception {
        tbContent.setCreated(new Date());
        tbContent.setUpdated(new Date());
        tbContentMapper.insert(tbContent);
        //同步缓存
        //删除对应的缓存信息
        jedisClient.hdel(INDEX_CONTENT,tbContent.getCategoryId().toString());
        return TaotaoResult.ok();
    }

    @Override
    public List<TbContent> getContentByCid(long cid) throws Exception {

        try{
            //缓存中是否存在
            String json = jedisClient.hget(INDEX_CONTENT,String.valueOf(cid));
            if(StringUtils.isNotBlank(json)){
                List<TbContent> list = JsonUtils.jsonToList(json,TbContent.class);
                return list;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        TbContentExample exampl = new TbContentExample();
        TbContentExample.Criteria criteria = exampl.createCriteria();
        criteria.andCategoryIdEqualTo(cid);
        List<TbContent> tbContents = tbContentMapper.selectByExample(exampl);
        try{
            //将查询出来的数据放到缓存中
            String json = JsonUtils.objectToJson(tbContents);
            jedisClient.hset(INDEX_CONTENT,String.valueOf(cid),json);
        }catch (Exception e){
            e.printStackTrace();
        }
        return tbContents;
    }
}
