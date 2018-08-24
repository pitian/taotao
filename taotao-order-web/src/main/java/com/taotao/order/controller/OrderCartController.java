package com.taotao.order.controller;

import com.taotao.order.pojo.OrderInfo;
import com.taotao.order.service.OrderService;
import com.taotao.pojo.TbItem;
import common.pojo.TaotaoResult;
import common.utils.CookieUtils;
import common.utils.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Controller
public class OrderCartController {
    @Value("{CART_KEY}")
    private String CART_KEY;
    @Autowired
    private OrderService orderService;
    @RequestMapping("/order/order-cart")
    public String showOrderCart(HttpServletRequest request){
        //用户必须是登录状态
        //取用户id
        //根据用户信息取收货地址列表，使用静态数据。
        //把收货地址列表取出传递给页面
        //从cookie中取购物车商品列表展示到页面
        List<TbItem> cartItemList = getCartItemList(request);
        request.setAttribute("cartList",cartItemList);
        //返回逻辑视图
        return "order-cart";
    }

    public List<TbItem> getCartItemList(HttpServletRequest request){
        //从cookie中取购物车商品列表
        String json = CookieUtils.getCookieValue(request,CART_KEY,"utf-8");
        if(StringUtils.isBlank(json)){
            //如果没有内容，返回一个空的列表
            return new ArrayList();
        }
        List<TbItem> list =  JsonUtils.jsonToList(json,TbItem.class);
        return list;
    }

    /**
     * 生成订单处理
     */
    @RequestMapping(value = "/order/create",method = RequestMethod.POST)
    public String createOrder(OrderInfo orderInfo,Model model)throws Exception{
        TaotaoResult result = orderService.createOrder(orderInfo);
        model.addAttribute("orderId",result.getData().toString());
        model.addAttribute("payment",orderInfo.getPayment());
        //预计三天后到达
        DateTime dateTime = new DateTime();
        dateTime.plusDays(3);
        model.addAttribute("data",dateTime.toString("yyyy-MM-dd"));
        return "success";
    }


}
