package com.taotao.order.service;

import com.taotao.order.pojo.OrderInfo;
import common.pojo.TaotaoResult;

public interface OrderService {
    TaotaoResult createOrder(OrderInfo orderInfo)throws Exception;
}
