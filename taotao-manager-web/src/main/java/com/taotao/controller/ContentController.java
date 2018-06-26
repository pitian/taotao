package com.taotao.controller;

import com.taotao.content.service.ContentService;
import com.taotao.pojo.TbContent;
import common.pojo.EasyUIDataGrideResult;
import common.pojo.EasyUITreeNode;
import common.pojo.TaotaoResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class ContentController {
    @Autowired
    private ContentService contentService;
    @RequestMapping("/content/query/list")
    @ResponseBody
    public EasyUIDataGrideResult getContentList(Long categoryId,int page,int rows)throws Exception{
        EasyUIDataGrideResult result = contentService.getContentList(categoryId,page,rows);
        return result;
    }
    @RequestMapping("/content/save")
    @ResponseBody
    public TaotaoResult addContent(TbContent tbContent) throws Exception{
        TaotaoResult result = contentService.addContent(tbContent);
        return result;
    }
}
