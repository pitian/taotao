package com.taotao.order.service.impl;

import com.taotao.jedis.JedisClient;
import com.taotao.mapper.TbItemMapper;
import com.taotao.mapper.TbOrderItemMapper;
import com.taotao.mapper.TbOrderMapper;
import com.taotao.mapper.TbOrderShippingMapper;
import com.taotao.order.pojo.OrderInfo;
import com.taotao.order.service.OrderService;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbOrder;
import com.taotao.pojo.TbOrderItem;
import com.taotao.pojo.TbOrderShipping;
import common.pojo.TaotaoResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.Date;
import java.util.List;

public class OrderServiceImpl implements OrderService {
    @Autowired
    private TbOrderMapper tbOrderMapper;
    @Autowired
    private TbOrderItemMapper tbOrderItemMapper;

    @Autowired
    private TbOrderShippingMapper tbOrderShippingMapper;
    @Autowired
    private JedisClient jedisClient;

    @Value("${ORDER_ID_GEN_KEY}")
    private String ORDER_ID_GEN_KEY;
    @Value("${ORDER_ID_BEGIN_VALUE}")
    private String ORDER_ID_BEGIN_VALUE;

    @Value("${ORDER_ITEM_ID_GEN_KEY}")
    private String ORDER_ITEM_ID_GEN_KEY;
    @Override
    public TaotaoResult createOrder(OrderInfo orderInfo) throws Exception {
        //生成订单号,可以使用redis的incr生成
        if(!jedisClient.exists(ORDER_ID_GEN_KEY)){
            jedisClient.set(ORDER_ID_GEN_KEY,ORDER_ID_BEGIN_VALUE);
        }
       String orderId  =  jedisClient.incr(ORDER_ID_GEN_KEY).toString();
        //向订单表插入数据,需要不全POJO
        orderInfo.setOrderId(orderId);
        //免邮费
        orderInfo.setPostFee("0");
        orderInfo.setStatus(1);
        //订单创建时间
        orderInfo.setCreateTime(new Date());
        orderInfo.setUpdateTime(new Date());

        tbOrderMapper.insert(orderInfo);
        //订单明细表插入数据
        List<TbOrderItem> tbOrderItems = orderInfo.getOrderItems();
        for (TbOrderItem tbOrderItem:tbOrderItems) {
            //获取明细主键
            String oid = jedisClient.incr(ORDER_ITEM_ID_GEN_KEY).toString();
            tbOrderItem.setId(oid);
            tbOrderItem.setOrderId(orderId);
            //插入明细数据
            tbOrderItemMapper.insert(tbOrderItem);
        }
        //向订单物流表插入数据
        TbOrderShipping tbOrderShipping = orderInfo.getOrderShipping();
        tbOrderShipping.setOrderId(orderId);
        tbOrderShipping.setCreated(new Date());
        tbOrderShipping.setUpdated(new Date());
        tbOrderShippingMapper.insert(tbOrderShipping);
        //返回订单号
        return TaotaoResult.ok(orderId);
    }
}
