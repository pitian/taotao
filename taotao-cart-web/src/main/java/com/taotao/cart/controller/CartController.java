package com.taotao.cart.controller;

import com.taotao.pojo.TbItem;
import com.taotao.service.ItemService;
import common.pojo.TaotaoResult;
import common.utils.CookieUtils;
import common.utils.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * 购物车管理Controller
 * <p>Title: CartController</p>
 * <p>Description: </p>
 * <p>Company: www.itcast.cn</p>
 * @version 1.0
 */
@Controller
public class CartController {
    @Autowired
    private ItemService itemService;
    @Value("${CART_KEY}")
    private String CART_KEY;
    @Value("${CART_EXPIER}")
    private Integer CART_EXPIER;

    @RequestMapping("/cart/add/itemId")
    public String addItemCart(@PathVariable Long itemId, @RequestParam(defaultValue="1")Integer num, HttpServletRequest request, HttpServletResponse response)throws Exception{
        //查询购物车列表'
        List<TbItem> cartItemList = getCartItemList(request);
        //判断商品在购物车中是否存在
        boolean flag = false;
        for (TbItem tbItem : cartItemList) {
            if(tbItem.getId()==itemId.longValue()){
                //如果存在数量相加
                tbItem.setNum(tbItem.getNum() + num);
                flag = true;
                break;
            }
        }
        //如果不存在，添加一个新的商品
        if(!flag){
            TbItem tbItem = itemService.getItemById(itemId);
            tbItem.setNum(num);
            String image = tbItem.getImage();
            if (StringUtils.isNotBlank(image)) {
                String[] images = image.split(",");
                tbItem.setImage(images[0]);
            }
            //把商品添加到购物车
            cartItemList.add(tbItem);
        }
        //把购物车列表写入cookie
        CookieUtils.setCookie(request, response, CART_KEY, JsonUtils.objectToJson(cartItemList),
                CART_EXPIER, true);
        return "cartSuccess";
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

    @RequestMapping("/cart/cart")
    public String showCartList(HttpServletRequest request) {
        //从cookie中取购物车列表
        List<TbItem> list =  getCartItemList(request);
        //把购物车列表传递给jsp
        request.setAttribute("cartList",list);
        //返回逻辑视图
        return "cart";
    }

    @RequestMapping("/cart/update/num/{itemId}/{num}")
    @ResponseBody
    public TaotaoResult updateItemNum(@PathVariable Long itemId,@PathVariable Integer num ,HttpServletRequest request,HttpServletResponse response)throws Exception{
        //从cookie中取购物车列表
        List<TbItem> list =  getCartItemList(request);
        //查询到对应的商品
        for (TbItem tbItem : list) {
            if(tbItem.getId()==itemId.longValue()){
                tbItem.setNum(num);
                break;
            }
        }
        //把购车列表写入 cookie
        CookieUtils.setCookie(request, response, CART_KEY, JsonUtils.objectToJson(list), CART_EXPIER, true);
        //返回成功
        return TaotaoResult.ok();
    }

    @RequestMapping("/cart/delete/{itemId}")
    public String deleteCartItem(@PathVariable Long itemId,HttpServletRequest request, HttpServletResponse response){
        //从cookie中取购物车列表
        List<TbItem> list =  getCartItemList(request);
        //找到对应的商品
        for (TbItem tbItem : list) {
            if (tbItem.getId() == itemId.longValue()) {
                //删除商品
                list.remove(tbItem);
                break;
            }
        }
        //把购车列表写入cookie
        CookieUtils.setCookie(request, response, CART_KEY, JsonUtils.objectToJson(list), CART_EXPIER, true);
        //重定向到购物车列表页面
        return "redirect:/cart/cart.html";
    }

}