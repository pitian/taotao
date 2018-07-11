package com.taotao.item.controller;

import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemDesc;
import com.taotao.service.ItemService;
import common.utils.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ItemController {
    @Autowired
    private ItemService itemService;
    @RequestMapping("/item/${itemId}")
    public String showItem(@PathVariable Long itemId,Model model) throws Exception{
        TbItem tbItem = itemService.getItemById(itemId);
        TbItemDesc tbItemDesc = itemService.getItemDescById(itemId);
        model.addAttribute("item", JsonUtils.objectToJson(tbItem));
        model.addAttribute("itemDesc",JsonUtils.objectToJson(tbItemDesc));
        return "item";
    }
}
