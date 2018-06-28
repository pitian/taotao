package com.taotao.portal.controller;

import com.taotao.content.service.ContentService;
import com.taotao.pojo.TbContent;
import com.taotao.portal.pojo.AD1Node;
import common.utils.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
@Controller
public class IndexController {
    @Value("${AD1_CATEGORY_ID}")
    private Long AD1_CATEGORY_ID;

    @Value("${AD1_WIDTH}")
    private Integer AD1_WIDTH;

    @Value("${AD1_HEIGHT}")
    private Integer AD1_HEIGHT;

    @Value("${AD1_WIDTH_B}")
    private Integer AD1_WIDTH_B;

    @Value("${AD1_HEIGHT_B}")
    private Integer AD1_HEIGHT_B;
    @Autowired
    private ContentService contentService;
    @RequestMapping("/index")
    public String showIndex(Model model)throws Exception{
        //1.根据cid查询轮播图内容列表
        List<TbContent> tbContentList = contentService.getContentByCid(AD1_CATEGORY_ID);
        //2.把列表转换为AD1node列表
        //判空
        List<AD1Node> ad1Nodes = new ArrayList<AD1Node>();
        for (TbContent tbContent:tbContentList) {
            AD1Node ad1Node = new AD1Node();
            ad1Node.setSrc(tbContent.getPic());
            ad1Node.setSrcB(tbContent.getPic2());
            ad1Node.setHeight(AD1_HEIGHT);
            ad1Node.setWidth(AD1_WIDTH);
            ad1Node.setHeightB(AD1_HEIGHT_B);
            ad1Node.setWidthB(AD1_WIDTH_B);
            ad1Node.setAlt(tbContent.getTitle());
            ad1Node.setHref(tbContent.getUrl());
            ad1Nodes.add(ad1Node);
        }
        //3.把列表转换为json数据
        String ad1Json = JsonUtils.objectToJson(ad1Nodes);
        //4.把json数据传递给页面
        model.addAttribute("ad1",ad1Json);
        return "index";
    }

}
