package com.taotao.controller;

import com.taotao.content.service.ContentCategoryService;
import common.pojo.EasyUITreeNode;
import common.pojo.TaotaoResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
@Controller
public class ContentCategoryController {

    @Autowired
    private ContentCategoryService contentCategoryService;
    @RequestMapping("/content/category/list")
    @ResponseBody
    public List<EasyUITreeNode> getContentCategoryList(
            @RequestParam(value="id", defaultValue="0")Long parentId) throws Exception{
        List<EasyUITreeNode> results = contentCategoryService.getContentCategoryList(parentId);
        return results;
    }

    @RequestMapping("/content/category/create")
    @ResponseBody
    public TaotaoResult addContentCategory(Long  parentId,String name)throws Exception{
        TaotaoResult taotaoResult = contentCategoryService.addContentCategory(parentId,name);
        return taotaoResult;
    }
    @RequestMapping("/content/category/update")
    @ResponseBody
    public TaotaoResult updateaContentCategory(Long id,String name)throws Exception{
        TaotaoResult result =  contentCategoryService.updateaContentCategory(id,name);
        return result;
    }
    @RequestMapping("/content/category/delete/")
    @ResponseBody
    public TaotaoResult deleteContentCategory(Long id) throws Exception{
        return null;
    }

}
