package com.taotao.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.jedis.JedisClient;
import com.taotao.jedis.JedisClientPool;
import com.taotao.mapper.TbItemDescMapper;
import com.taotao.mapper.TbItemMapper;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemDesc;
import com.taotao.pojo.TbItemExample;
import com.taotao.service.ItemService;
import common.pojo.EasyUIDataGrideResult;
import common.pojo.TaotaoResult;
import common.utils.IDUtils;
import common.utils.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;
import redis.clients.jedis.JedisPool;

import javax.annotation.Resource;
import javax.jms.*;
import java.util.Date;
import java.util.List;

/**
 * 商品管理Service
 */
@Service
public class ItemServiceImpl implements ItemService {
    @Autowired
    private TbItemMapper tbItemMapper;
    @Autowired
    private TbItemDescMapper tbItemDescMapper;
    @Autowired
    private JmsTemplate jmsTemplate;
    @Resource(name = "itemTopic")
    private Destination destination;
    @Autowired
    private JedisClient jedisClient;
    @Value("${ITEM_INFO}")
    private String ITEM_INFO;
    @Value("${TIME_EXPIRE}")
    private Integer TIME_EXPIRE;
    @Override
    public TbItem getItemById(long itemId) throws Exception {
        //先查询缓存中是否存在
        try{
            String json = jedisClient.get(ITEM_INFO+ ":" + itemId + ":BASE");
            if (StringUtils.isNotBlank(json)) {
                TbItem tbItem = JsonUtils.jsonToPojo(json,TbItem.class);
                return  tbItem;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        TbItem tbItem = tbItemMapper.selectByPrimaryKey(itemId);
        //查询出数据之后，加入到redis 缓存中
        try{
            jedisClient.set(ITEM_INFO+ ":" + itemId + ":BASE", JsonUtils.objectToJson(tbItem));
            //设置过期时间,默认为一天
            jedisClient.expire(ITEM_INFO+ ":" + itemId + ":BASE",TIME_EXPIRE);
        }catch (Exception e){
            e.printStackTrace();
        }
        return tbItem;
    }

    @Override
    public EasyUIDataGrideResult getItemList(int page, int rows) throws Exception {
        PageHelper.startPage(page,rows);
        TbItemExample tbItemExample = new TbItemExample();
        List<TbItem> tbItemList = tbItemMapper.selectByExample(tbItemExample);
        PageInfo<TbItem> pageInfo = new PageInfo(tbItemList);
        EasyUIDataGrideResult result = new EasyUIDataGrideResult();
        result.setTotal(pageInfo.getTotal());
        result.setRows(tbItemList);
        return result;
    }

    @Override
    public TaotaoResult addItem(TbItem tbItem, String desc) throws Exception {
        //1.生成id年月日时分秒
        final long itemId = IDUtils.genItemId();
        tbItem.setId(itemId);
        tbItem.setStatus((byte)1);
        tbItem.setCreated(new Date());
        tbItem.setUpdated(new Date());
        tbItemMapper.insert(tbItem);
        TbItemDesc tbItemDesc = new TbItemDesc();
        tbItemDesc.setItemId(itemId);
        tbItemDesc.setItemDesc(desc);
        tbItemDesc.setCreated(new Date());
        tbItemDesc.setUpdated(new Date());
        tbItemDescMapper.insert(tbItemDesc);
        //TODO发送消息
        jmsTemplate.send(destination, new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                TextMessage textMessage = session.createTextMessage(itemId+"");
                return textMessage;
            }
        });
        return TaotaoResult.ok();
    }

    @Override
    public TbItemDesc getItemDescById(long itemId) throws Exception {
        //先查询缓存中是否存在
        try{
            String json = jedisClient.get(ITEM_INFO+ ":" + itemId + ":DESC");
            if (StringUtils.isNotBlank(json)) {
                TbItemDesc TbItemDesc = JsonUtils.jsonToPojo(json,TbItemDesc.class);
                return  TbItemDesc;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        TbItemDesc tbItemDesc = tbItemDescMapper.selectByPrimaryKey(itemId);
        //查询出数据之后，加入到redis 缓存中
        try{
            jedisClient.set(ITEM_INFO+ ":" + itemId + ":DESC", JsonUtils.objectToJson(tbItemDesc));
            //设置过期时间,默认为一天
            jedisClient.expire(ITEM_INFO+ ":" + itemId + ":DESC",TIME_EXPIRE);
        }catch (Exception e){
            e.printStackTrace();
        }
        return tbItemDesc;
    }
}
